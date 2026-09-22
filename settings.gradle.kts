pluginManagement {
	val kotlin_version = providers.gradleProperty("kotlin_version")
	val loom_version = providers.gradleProperty("loom_version")

	repositories {
		maven {
			name = "Fabric"
			url = uri("https://maven.fabricmc.net/")
		}
		mavenCentral()
		gradlePluginPortal()
	}

	plugins {
		kotlin("jvm") version kotlin_version.get()
		id("net.fabricmc.fabric-loom") version loom_version.get()
	}
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":carminite-lib")

rootProject.name = "twilightforest-fabric"