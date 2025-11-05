const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();
const db = admin.firestore();

// 🔔 1. Notificar cuando cambia de Disponible → Asignado
exports.notificarCambioEstado = functions.firestore
  .document("equipos/{equipoId}")
  .onUpdate(async (change, context) => {
    const antes = change.before.data();
    const despues = change.after.data();

    if (antes.estado === "Disponible" && despues.estado === "Asignado") {
      if (!despues.tokenNotificacion) return null;

      await admin.messaging().send({
        token: despues.tokenNotificacion,
        notification: {
          title: "Equipo asignado",
          body: `El equipo ${despues.referencia} ha sido asignado.`,
        },
      });
    }
    return null;
  });

// ⏰ 2. Notificar certificados por vencer
exports.notificarCertificadosVencimiento = functions.pubsub
  .schedule("every 24 hours") // se ejecuta cada día
  .onRun(async () => {
    const hoy = new Date();
    const equiposSnap = await db.collection("equipos").get();

    for (const doc of equiposSnap.docs) {
      const equipo = doc.data();
      const fechaCert = new Date(equipo.fechaCertificacion);
      const fechaVencimiento = new Date(fechaCert);
      fechaVencimiento.setFullYear(fechaCert.getFullYear() + 1);

      const diasRestantes = Math.ceil((fechaVencimiento - hoy) / (1000 * 60 * 60 * 24));

      if (diasRestantes <= 15 && equipo.tokenNotificacion) {
        await admin.messaging().send({
          token: equipo.tokenNotificacion,
          notification: {
            title: "Certificado por vencer",
            body: `El certificado del equipo ${equipo.referencia} vence en ${diasRestantes} días.`,
          },
        });
      }
    }
    return null;
  });
