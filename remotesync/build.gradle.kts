plugins {
    id("com.android.library")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.incremental"] = "true"
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":core"))

    // Dagger Hilt
    implementation(DaggerHilt.hiltAndroid)
    implementation(DaggerHilt.lifecycleViewModel)
    implementation(DaggerHilt.navFragmentAndroid)
    kapt(DaggerHilt.compiler)
    kapt(DaggerHilt.compilerAndroid)

    // Coroutines
    implementation(Coroutines.coroutines)

    // Room
    implementation(Room.roomKtx)
    implementation(Room.runtime)
    kapt(Room.compiler)

    // DataStore Preferences
    implementation(DataStore.preferences)

    // WorkManager
    implementation(WorkManager.runtime)
    implementation(WorkManager.multiprocess)

    // Retrofit
    implementation(Retrofit.retrofit)
    implementation(Retrofit.okHttp)
    implementation(Retrofit.okHttpLoggingInterceptor)
    implementation(Retrofit.moshiConverter)

    // Testing
    testImplementation(Testing.jUnit)
    testImplementation(Testing.truth)
    testImplementation(Testing.coroutines)
    testImplementation(Testing.roomAndroid)
    testImplementation(Testing.workManagerAndroid)
    testImplementation(Testing.mockk)
    testImplementation(Testing.hiltAndroid)
    kaptTest(Testing.hiltAndroidCompiler)

    androidTestImplementation(Testing.jUnitAndroid)
    androidTestImplementation(Testing.truth)
    androidTestImplementation(Testing.testRunner)
    androidTestImplementation(Testing.coroutines)
    androidTestImplementation(Testing.roomAndroid)
    androidTestImplementation(Testing.workManagerAndroid)
    androidTestImplementation(Testing.mockk)
    androidTestImplementation(Testing.mockkAndroid)
    androidTestImplementation(Testing.mockWebServer)
    androidTestImplementation(Testing.hiltAndroid)
    kaptAndroidTest(Testing.hiltAndroidCompiler)
}