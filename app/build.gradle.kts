plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = ProjectConfig.appId
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

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
    packagingOptions {
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
        resources.excludes.add("META-INF/licenses/ASM")
    }
}

dependencies {
    // Core
    implementation(AndroidX.coreKtx)
    implementation(AndroidX.appCompat)
    implementation(Material.material)

    // Modules
    implementation(project(Modules.core))
    implementation(project(Modules.trackerBuildings))
    // implementation(project(Modules.trackerResearch))
    // implementation(project(Modules.statistics))
    testImplementation(project(Modules.coreTest))

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

    // WorkManager
    implementation(WorkManager.runtime)
    implementation(WorkManager.multiprocess)

    // Retrofit
    implementation(Retrofit.retrofit)
    implementation(Retrofit.okHttp)
    implementation(Retrofit.okHttpLoggingInterceptor)
    implementation(Retrofit.moshiConverter)

    // Dagger Hilt
    implementation(DaggerHilt.hiltAndroid)
    implementation(DaggerHilt.lifecycleViewModel)
    implementation(DaggerHilt.navFragmentAndroid)
    implementation(DaggerHilt.workManagerHilt)
    kapt(DaggerHilt.compiler)
    kapt(DaggerHilt.compilerAndroid)

    // Glide
    implementation(Glide.glide)
    kapt(Glide.compiler)

    // Moshi
    implementation(Moshi.moshi)
    kapt(Moshi.moshiCodeGen)

    // Timber Logging
    implementation(Timber.timber)

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
    androidTestImplementation(Testing.testRules)
    androidTestImplementation(Testing.coroutines)
    androidTestImplementation(Testing.roomAndroid)
    androidTestImplementation(Testing.workManagerAndroid)
    androidTestImplementation(Testing.mockk)
    androidTestImplementation(Testing.mockkAndroid)
    androidTestImplementation(Testing.mockWebServer)
    androidTestImplementation(Testing.hiltAndroid)
    androidTestImplementation(Testing.espressoCore)
    kaptAndroidTest(Testing.hiltAndroidCompiler)
}