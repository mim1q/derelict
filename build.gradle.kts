import com.matthewprenger.cursegradle.*
import net.fabricmc.loom.task.RemapJarTask
import java.io.FileNotFoundException

plugins {
  id("fabric-loom") version Versions.loom
  id("com.modrinth.minotaur") version Versions.minotaur
  id("com.matthewprenger.cursegradle") version Versions.cursegradle
  id("io.github.p03w.machete") version "2.0.1"
  kotlin("jvm") version Versions.kotlin
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
}

dependencies {
  minecraft("com.mojang:minecraft:${Versions.minecraft}")
  mappings("net.fabricmc:yarn:${Versions.yarn}:v2")
  modImplementation("net.fabricmc:fabric-loader:${Versions.fabricLoader}")
  modImplementation("net.fabricmc.fabric-api:fabric-api:${Versions.fabricApi}")
  modImplementation("net.fabricmc:fabric-language-kotlin:${Versions.fabricLanguageKotlin}")
  implementation(kotlin("stdlib-jdk8"))
  // owo-lib
  annotationProcessor(modImplementation("io.wispforest:owo-lib:${Versions.owoLib}")!!)
  include("io.wispforest:owo-sentinel:${Versions.owoLib}")
  // Jankson for owo-lib config comments
  implementation("blue.endless:jankson:${Versions.jankson}")
}

@Suppress("UnstableApiUsage")
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