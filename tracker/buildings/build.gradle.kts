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
    buildFeatures {
        viewBinding = true
        dataBinding = true
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
    testImplementation(project(":core-test"))

    // Coroutines
    implementation(Coroutines.coroutines)

    // Lifecycle
    implementation(LifeCycle.runtime)
    implementation(LifeCycle.liveData)
    implementation(LifeCycle.viewModel)

    // Navigation
    implementation(Navigation.runtime)
    implementation(Navigation.navFragment)
    implementation(Navigation.navUi)

    // Room
    implementation(Room.roomKtx)
    implementation(Room.runtime)
    kapt(Room.compiler)

    // DataStore Preferences
    implementation(DataStore.preferences)

    // Dagger Hilt
    implementation(DaggerHilt.hiltAndroid)
    implementation(DaggerHilt.lifecycleViewModel)
    implementation(DaggerHilt.navFragmentAndroid)
    kapt(DaggerHilt.compiler)
    kapt(DaggerHilt.compilerAndroid)

    // Moshi
    implementation(Moshi.moshi)
    kapt(Moshi.moshiCodeGen)

    // Retrofit
    implementation(Retrofit.retrofit)
    implementation(Retrofit.okHttp)
    implementation(Retrofit.okHttpLoggingInterceptor)

    // Timber Logging
    implementation(Timber.timber)

    // Testing
    testImplementation(Testing.jUnit)
    testImplementation(Testing.truth)
    testImplementation(Testing.coroutines)

    androidTestImplementation(Testing.jUnitAndroid)
    androidTestImplementation(Testing.truth)
    androidTestImplementation(Testing.testRunner)
    androidTestImplementation(Testing.testRules)
    androidTestImplementation(Testing.coroutines)
    androidTestImplementation(Testing.roomAndroid)
    androidTestImplementation(Testing.mockk)
    androidTestImplementation(Testing.mockkAndroid)
    androidTestImplementation(Testing.hiltAndroid)
    androidTestImplementation(Testing.espressoCore)
    kaptAndroidTest(Testing.hiltAndroidCompiler)
}