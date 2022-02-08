object Testing {
    // JUnit
    private const val jUnitVersion = "4.13.2"
    const val jUnit = "junit:junit:$jUnitVersion"

    private const val jUnitAndroidVersion = "1.1.3"
    const val jUnitAndroid = "androidx.test.ext:junit:$jUnitAndroidVersion"

    // Google Truth
    private const val truthVersion = "1.1.3"
    const val truth = "com.google.truth:truth:$truthVersion"

    // Test Runner
    private const val testRunnerVersion = "1.4.0"
    const val testRunner = "androidx.test:runner:$testRunnerVersion"

    // Coroutines
    private const val coroutinesVersion = "1.6.0"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion"

    // Room
    const val roomAndroid = "androidx.room:room-testing:${Room.version}"

    // Dagger Hilt
    const val hiltAndroid = "com.google.dagger:hilt-android-testing:${DaggerHilt.version}"
    const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:${DaggerHilt.version}"

    // WorkManager
    const val workManagerAndroid = "androidx.work:work-testing:${WorkManager.version}"

    // Mock
    private const val mockkVersion = "1.12.2"
    const val mockk = "io.mockk:mockk:$mockkVersion"
    const val mockkAndroid = "io.mockk:mockk-android:$mockkVersion"

    private const val mockWebServerVersion = "4.9.3"
    const val mockWebServer = "com.squareup.okhttp3:mockwebserver:$mockWebServerVersion"
}