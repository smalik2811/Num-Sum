plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.daggerHilt)
//    alias(libs.plugins.googleServices) // Firebase Connection
}

android {
    namespace = "com.yangian.numsum.core.firebase"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
}

dependencies {

    // androidx
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // Dagger-Hilt
    implementation(libs.dagger.hilt.android)
    implementation(project(":core:model"))
    implementation(project(":core:datastore"))
    ksp(libs.dagger.hilt.compiler)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.cloud.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.config)

    // test
    testImplementation(libs.junit)

    // android-test
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Project Modules
    implementation(project(":core:constant"))
    implementation(project(":core:data"))
}