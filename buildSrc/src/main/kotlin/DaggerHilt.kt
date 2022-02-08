object DaggerHilt {
    const val version = "2.40.5"
    const val hiltAndroid = "com.google.dagger:hilt-android:$version"
    const val compiler = "com.google.dagger:hilt-android-compiler:$version"

    private const val androidLifecycleVersion = "1.0.0-alpha03"
    const val lifecycleViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:$androidLifecycleVersion"

    private const val androidNavFragmentVersion = "1.0.0"
    const val navFragmentAndroid = "androidx.hilt:hilt-navigation-fragment:$androidNavFragmentVersion"

    private const val androidCompilerVersion = "1.0.0"
    const val compilerAndroid = "androidx.hilt:hilt-compiler:$androidCompilerVersion"
}