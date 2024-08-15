import com.mathroda.buildsrc.Deps

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    //Voyager
    with(Deps.Voyager) {
        implementation(screenModel)
    }
}
