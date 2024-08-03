import com.matthewprenger.cursegradle.*
import net.fabricmc.loom.task.RemapJarTask
import java.io.FileNotFoundException

plugins {
    id("fabric-loom") version Versions.LOOM
    id("com.modrinth.minotaur") version Versions.MINOTAUR
    id("com.matthewprenger.cursegradle") version Versions.CURSEGRADLE
    id("io.github.p03w.machete") version "2.0.1"
    kotlin("jvm") version Versions.KOTLIN
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

base {
    archivesName.set(ModData.id)
}

group = ModData.group
version = ModData.version

repositories {
    mavenCentral()
    maven("https://maven.wispforest.io")
    maven("https://maven.shedaniel.me")
    maven("https://maven.terraformersmc.com")
    maven("https://api.modrinth.com/maven")
    maven {
        name = "Mim1q's Maven"
        url = uri("https://maven.mim1q.dev")
    }
    maven {
        name = "Ladysnake Maven"
        url = uri("https://maven.ladysnake.org/releases")
    }
    mavenLocal()
}

dependencies {
    minecraft("com.mojang:minecraft:${Versions.MINECRAFT}")
    mappings("net.fabricmc:yarn:${Versions.YARN}:v2")
    modImplementation("net.fabricmc:fabric-loader:${Versions.FABRIC_LOADER}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${Versions.FABRIC_API}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${Versions.FABRIC_LANUGAGE_KOTLIN}")
    implementation(kotlin("stdlib-jdk8"))
    // owo-lib
    annotationProcessor(modImplementation("io.wispforest:owo-lib:${Versions.OWO_LIB}")!!)
    include("io.wispforest:owo-sentinel:${Versions.OWO_LIB}")
    // Jankson for owo-lib config comments
    implementation("blue.endless:jankson:${Versions.JANKSON}")
    // REI for recipe display integration
    modRuntimeOnly("me.shedaniel:RoughlyEnoughItems-fabric:${Versions.REI}")
    modCompileOnly("me.shedaniel:RoughlyEnoughItems-api-fabric:${Versions.REI}")
    modCompileOnly("me.shedaniel:RoughlyEnoughItems-default-plugin-fabric:${Versions.REI}")
    // EMI, same as above
    modCompileOnly("dev.emi:emi-fabric:${Versions.EMI}:api")
    modLocalRuntime("dev.emi:emi-fabric:${Versions.EMI}")
    // Gimm1q for common Mim1q's mods code
    include(modImplementation("dev.mim1q:gimm1q:${Versions.GIMM1Q}")!!)
    // Cardinal Components API for synced entity data
    include(modImplementation("dev.onyxstudios.cardinal-components-api:cardinal-components-base:${Versions.CARDINAL_COMPONENTS}")!!)
    include(modImplementation("dev.onyxstudios.cardinal-components-api:cardinal-components-entity:${Versions.CARDINAL_COMPONENTS}")!!)
    // Biolith for biome generation
    include(modImplementation("maven.modrinth:biolith:${Versions.BIOLITH}")!!)
    include(modImplementation("com.terraformersmc.terraform-api:terraform-surfaces-api-v1:10.0.0")!!)
}

tasks {
    withType<ProcessResources> {
        inputs.property("version", ModData.version)
        filesMatching("fabric.mod.json") {
            expand("version" to ModData.version)
        }
    }
    withType<JavaCompile> {
        configureEach {
            options.release.set(17)
        }
    }
    register("runDatagenScript") {
        dependsOn(":datagen:run")
        group = "fabric"
    }
}

sourceSets {
    main {
        resources {
            srcDir("src/main/generated")
        }
        java {
            srcDir("$buildDir/generated/sources/annotationProcessor/java")
        }
    }
}

// Publishing
val secretsFile = rootProject.file("publishing.properties")
val secrets = Secrets(secretsFile)

val remapJar = tasks.getByName("remapJar") as RemapJarTask
val newVersionName = "${ModData.id}-${ModData.mcVersions[0]}-${ModData.version}"
val newChangelog = try {
    rootProject.file("changelogs/${ModData.version}.md").readText()
} catch (_: FileNotFoundException) {
    println("No changelog found")
    ""
}

if (secrets.isModrinthReady()) {
    println("Setting up Minotaur")
    modrinth {
        token.set(secrets.modrinthToken)
        projectId.set(secrets.modrinthId)
        uploadFile.set(remapJar)
        versionName.set(newVersionName)
        versionType.set(ModData.versionType)
        changelog.set(newChangelog)
        syncBodyFrom.set(rootProject.file("project/PROJECT.md").readText())
        gameVersions.set(ModData.mcVersions)
        loaders.set(listOf("fabric"))
        dependencies {
            ModData.dependencies.forEach(required::project)
        }
    }
}

if (secrets.isCurseforgeReady()) {
    println("Setting up Cursegradle")
    curseforge {
        apiKey = secrets.curseforgeToken
        project(closureOf<CurseProject> {
            id = secrets.curseforgeId
            releaseType = ModData.versionType
            ModData.mcVersions.forEach(::addGameVersion)
            addGameVersion("Fabric")
            changelog = newChangelog
            changelogType = "markdown"
            relations(closureOf<CurseRelation> {
                ModData.dependencies.forEach(::requiredDependency)
            })
            mainArtifact(remapJar, closureOf<CurseArtifact> {
                displayName = newVersionName
            })
        })
        options(closureOf<Options> {
            forgeGradleIntegration = false
        })
    }
    project.afterEvaluate {
        tasks.getByName<CurseUploadTask>("curseforge${secrets.curseforgeId}") {
            dependsOn(remapJar)
        }
    }
}

tasks.getByName("sourcesJar").dependsOn("compileJava")
tasks.getByName("modrinth").dependsOn("optimizeOutputsOfRemapJar")

kotlin {
    jvmToolchain(17)
}