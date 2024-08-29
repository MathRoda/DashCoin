pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    }
}
rootProject.name = "Dash Coin"

include(":androidApp")
include(":core")
include(":core-datasource")
include(":core-domain")
include(":core-network")
include(":core-infrastructure")
include(":core-cache")

include(":features")
include(":features:coin-details")
include(":features:common")
include(":features:coins")
include(":features:profile")
include(":features:favorite-coins")
include(":features:news")
include(":features:onboarding")

include(":auth")
include(":auth:signin")
include(":auth:signup")
include(":auth:forgot-password")
include(":shared")
