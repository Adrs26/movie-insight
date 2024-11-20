plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")
}

android {
    namespace = "com.example.movieinsight"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.movieinsight"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField(
                "String",
                "BASE_URL",
                "\"https://api.themoviedb.org/3/\""
            )
            buildConfigField(
                "String",
                "AUTH_TOKEN",
                "\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5OTM0N2E2OWRlZTQ2NjUxNjgzYWMzZmM3MDIyZjFiYyIsIm5iZiI6MTczMDIwOTcyMi41OTUzMDEsInN1YiI6IjY3MjBlNWI2NzY5MTA3ZDc3YjQ4ODUwOSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.7xcU49_uwevdgJfEYXXOQ5vAMXcW9HQvAaG5aX6r9Q4\""
            )
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField(
                "String",
                "BASE_URL",
                "\"https://api.themoviedb.org/3/\""
            )
            buildConfigField(
                "String",
                "AUTH_TOKEN",
                "\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5OTM0N2E2OWRlZTQ2NjUxNjgzYWMzZmM3MDIyZjFiYyIsIm5iZiI6MTczMDIwOTcyMi41OTUzMDEsInN1YiI6IjY3MjBlNWI2NzY5MTA3ZDc3YjQ4ODUwOSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.7xcU49_uwevdgJfEYXXOQ5vAMXcW9HQvAaG5aX6r9Q4\""
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
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.viewbindingpropertydelegate.noreflection)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.glide)
    implementation (libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    kapt(libs.androidx.room.compiler)
}