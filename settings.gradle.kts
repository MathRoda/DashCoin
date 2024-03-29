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

include(":app")
include(":core")
include(":core-datasource")
include(":core-domain")
include(":core-network")
include(":core-infrastructure")
include(":feat-coin-detail")
include(":feat-common")
include(":feat-coins")
include(":feat-profile")
include(":feat-favorite-coins")
include(":feat-news")
include(":feat-onboarding")
include(":feat-auth-signin")
include(":feat-auth-signup")
include(":feat-auth-forgot-password")
include(":core-cache")