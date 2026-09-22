import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import java.text.SimpleDateFormat
import java.util.Properties
import java.util.Date

plugins {
	kotlin("jvm")
	id("net.fabricmc.fabric-loom")
	`maven-publish`
}

tasks.wrapper {
	gradleVersion = "9.5.1"
	distributionType = Wrapper.DistributionType.ALL
}

val secrets = Properties()
val secretsFile = file("secrets.properties")

if (secretsFile.exists()) {
	secretsFile.inputStream().use { stream ->
		secrets.load(stream)
	}
	fileTree("secrets").matching {
		include("**/*.properties")
	}.forEach { file ->
		file.inputStream().use { stream ->
			secrets.load(stream)
		}
	}
}

version = if (project.hasProperty("CIRevision")) {
	project.property("CIRevision").toString()
} else {
	project.property("mod_version").toString()
}

group = project.property("group_name").toString()

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(25))
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.compilerArgs.addAll(listOf("-Xlint:all,-classfile,-processing,-deprecation,-this-escape", "-Werror"))
	options.encoding = "UTF-8"
}

base {
	archivesName.set("${project.property("mod_id")}-${project.property("minecraft_version")}")
}

subprojects {
	version = rootProject.version
}

loom {
	accessWidenerPath.set(rootProject.file("src/main/resources/twilightforest.classtweaker"))
	enableTransitiveAccessWideners = true

	interfaceInjection {
		enableDependencyInterfaceInjection = true
	}
}

fabricApi {
	configureDataGeneration {
		client = true
		createSourceSet = true
		outputDirectory = file("src/main/generated/resources")
	}
}

repositories {
	mavenLocal()
	maven {
		name = "Fuzs Mod Resources"
		url = uri("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
	}
	maven {
		name = "Terraformers"
		url = uri("https://maven.terraformersmc.com/")
	}
	maven {
		name = "Jared's maven"
		url = uri("https://maven.blamejared.com/")
	}
	maven {
		url = uri("https://api.modrinth.com/maven")
	}
}

dependencies {
	// Minecraft & Fabric
	minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
	implementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
	implementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_api_version")}")
	implementation("net.fabricmc:fabric-language-kotlin:" + "${project.property("fabric_language_kotlin_version")}+" + "kotlin.${project.property("kotlin_version")}")

	// NeoForge Config API
	include(implementation("fuzs.forgeconfigapiport:forgeconfigapiport-fabric:" + project.property("forge_config_api_port_version"))!!)

	// Built-in library
	implementation(include(project(":carminite-lib"))!!)

	// Compatibility
	implementation("com.terraformersmc:modmenu:${project.property("mod_menu_version")}")
	implementation("maven.modrinth:jade:${project.property("jade_version")}")
	implementation("mezz.jei:jei-" + "${project.property("minecraft_version")}-fabric:" + project.property("jei_version"))
}

val minimumLoaderVersion = project.property("minimum_loader_version").toString()
val baseMinecraftVersion = project.property("base_minecraft_version").toString()
val minimumFabricApiVersion = project.property("minimum_fabric_api_version").toString()
val fabricLanguageKotlinVersion = project.property("fabric_language_kotlin_version").toString()
val javaVersion = java.toolchain.languageVersion.get().asInt()

tasks.processResources {
	val ver = project.version
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	inputs.property("version", ver)
	filesMatching("fabric.mod.json") {
		expand(
			mapOf(
				"version" to ver,
				"loader_version" to minimumLoaderVersion,
				"minecraft_version" to baseMinecraftVersion,
				"fabric_api_version" to minimumFabricApiVersion,
				"fabric_language_kotlin_version" to fabricLanguageKotlinVersion,
				"java_version" to javaVersion
			)
		)
	}

	exclude(".cache")

	val lazyTree = providers.provider {
		project.fileTree(outputs.files.asPath) {
			include("**/*.json", "**/*.mcmeta")
		}
	}

	doLast {
		lazyTree.get().forEach { file ->
			file.writeText(JsonOutput.toJson(JsonSlurper().parse(file)))
		}
	}
}

tasks.jar {
	exclude("data/twilightforest/function/**")
	manifest {
		attributes(
			mapOf(
				"Specification-Title" to project.property("mod_id"),
				"Specification-Vendor" to "TeamTwilight",
				"Specification-Version" to "1",
				"Implementation-Title" to project.name,
				"Implementation-Version" to project.version,
				"Implementation-Vendor" to "TeamTwilight",
				"Implementation-Timestamp" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date())
			)
		)
	}
}

val sourceJar by tasks.registering(Jar::class) {
	dependsOn(tasks.classes)
	from(sourceSets.main.get().allSource)
	archiveClassifier.set("sources")
}

val mavenUser = System.getenv("MAVEN_USER") ?: secrets.getProperty("maven_username")
val mavenToken = System.getenv("MAVEN_PASS") ?: secrets.getProperty("maven_password")

publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			artifact(tasks.jar)
			artifact(sourceJar)
			groupId = "team-twilight"
			artifactId = project.property("mod_id").toString()
		}
	}

	repositories {
		maven {
			url = uri("https://maven.tamaized.com/releases")
			credentials {
				username = mavenUser
				password = mavenToken
			}
		}
	}
}