plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.compose")

}

android {
    namespace = "com.example.inventariadosapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.inventariadosapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    // 👇 Este bloque debe ir aquí, dentro de android
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
}



dependencies {    // AndroidX + Compose core
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.2")

    // --- FIREBASE ---
    // Importa la BoM (Bill of Materials) de Firebase.
    // Esto gestiona las versiones de las librerías de Firebase para que sean compatibles.
    // Solo necesitas declararla una vez y con la versión más reciente.
    implementation(platform("com.google.firebase:firebase-bom:33.2.0"))

    // Librerías de Firebase que necesitas (sin especificar versiones, la BoM se encarga)
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-storage") // Esta es la librería correcta para Cloud Storage

    // --- COMPOSE ---
    // BOM de Compose para asegurar compatibilidad entre sus librerías
    implementation(platform("androidx.compose:compose-bom:2025.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.4.0") // O considera usar la de la BoM
    implementation("androidx.navigation:navigation-compose:2.9.5")

    // Iconos de Material
    implementation("androidx.compose.material:material-icons-core:1.7.0")
    implementation("androidx.compose.material:material-icons-extended:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation(libs.androidx.compose.ui.text)

    // --- TESTS ---
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.10.01")) // Usa la misma BoM de Compose
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

