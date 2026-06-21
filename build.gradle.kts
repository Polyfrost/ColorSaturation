import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("dev.kikugie.loom-back-compat")
    id("org.jetbrains.kotlin.jvm") version "2.4.0"
    id("dev.deftu.gradle.bloom") version "0.2.0"
    id("me.modmuss50.mod-publish-plugin") version "1.1.0"
}

val modid = property("mod.id") as String
val modname = property("mod.name") as String
val modversion = property("mod.version") as String
val mcversion = property("minecraft_version") as String
val versionrange = property("minecraft_version_range")
val loaderversion = property("loader_version")
val oneconfigVersion = property("oneconfig_version") as String

base {
    archivesName.set("$modid-$modversion+$mcversion")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    google()

    maven("https://maven.parchmentmc.org")
    maven("https://repo.polyfrost.org/releases")
    maven("https://repo.polyfrost.org/snapshots")
    maven("https://maven.gegy.dev/releases")
    maven("https://central.sonatype.com/repository/maven-snapshots")
    maven("https://maven.logix.dev/snapshots") {
        content { excludeGroup("net.kyori") }
    }
    maven("https://nexus.prsm.wtf/repository/maven-public/maven-repo/releases/")
    maven("https://repo.hypixel.net/repository/Hypixel/")
    maven("https://maven.deftu.dev/releases")
    maven("https://maven.fabricmc.net/releases")
    maven("https://jitpack.io") {
        content { includeGroupAndSubgroups("com.github") }
    }
    maven("https://maven.bawnorton.com/releases") {
        content { includeGroup("com.github.bawnorton.mixinsquared") }
    }
    maven("https://maven.azureaaron.net/releases") {
        content { includeGroup("net.azureaaron") }
    }
    maven("https://redirector.kotlinlang.org/maven/compose-dev")
}

loom {
    runConfigs.all {
        ideConfigGenerated(stonecutter.current.isActive)
        runDir = "../../run"
    }

    runConfigs.remove(runConfigs["server"])
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")

    val hasOfficialMappings = findProperty("has_official_mappings")?.toString()?.toBoolean() ?: true
    if (hasOfficialMappings) {
        @Suppress("UnstableApiUsage")
        mappings(loom.layered {
            officialMojangMappings()
            optionalProp("${property("parchment_version")}") {
                parchment("org.parchmentmc.data:parchment-${property("minecraft_version")}:$it@zip")
            }
            optionalProp("${property("yalmm_version")}") {
                mappings("dev.lambdaurora:yalmm-mojbackward:${property("minecraft_version")}+build.$it")
            }
        })
    } else {
        findProperty("mappings_version")?.toString()?.takeUnless { it.isBlank() }?.let {
            mappings(it)
        }
    }

    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")

    modImplementation("org.polyfrost.oneconfig:${property("minecraft_version")}-fabric:$oneconfigVersion")
    implementation("org.polyfrost.oneconfig:config:$oneconfigVersion")
    implementation("org.polyfrost.oneconfig:config-impl:$oneconfigVersion")
    implementation("org.polyfrost.oneconfig:events:$oneconfigVersion")
    implementation("org.polyfrost.oneconfig:internal:$oneconfigVersion")
    implementation("org.polyfrost.oneconfig:ui:$oneconfigVersion")
    implementation("org.polyfrost.oneconfig:utils:$oneconfigVersion")
}

bloom {
    replacement("@MOD_ID@", modid)
    replacement("@MOD_NAME@", modname)
    replacement("@MOD_VERSION@", modversion)
}

tasks.processResources {
    val postEffectJson = when {
        mcversion == "1.21.4" -> """
            {
                "targets": {
                    "swap": {}
                },
                "passes": [
                    {
                        "program": "colorsaturation:post/color_saturation_legacy",
                        "inputs": [
                            { "sampler_name": "DiffuseSampler", "target": "minecraft:main" }
                        ],
                        "uniforms": [
                            { "name": "Saturation", "values": [ 1.0 ] }
                        ],
                        "output": "swap"
                    },
                    {
                        "program": "minecraft:post/blit",
                        "inputs": [
                            { "sampler_name": "In", "target": "swap" }
                        ],
                        "output": "minecraft:main"
                    }
                ]
            }
        """.trimIndent()
        mcversion == "1.21.5" -> """
            {
                "targets": {
                    "swap": {}
                },
                "passes": [
                    {
                        "vertex_shader": "minecraft:post/blit",
                        "fragment_shader": "colorsaturation:post/color_saturation_legacy",
                        "inputs": [
                            { "sampler_name": "DiffuseSampler", "target": "minecraft:main" }
                        ],
                        "uniforms": [
                            { "name": "Saturation", "type": "float", "values": [ 1.0 ] }
                        ],
                        "output": "swap"
                    },
                    {
                        "vertex_shader": "minecraft:post/blit",
                        "fragment_shader": "minecraft:post/blit",
                        "inputs": [
                            { "sampler_name": "In", "target": "swap" }
                        ],
                        "uniforms": [
                            { "name": "ColorModulate", "type": "vec4", "values": [ 1.0, 1.0, 1.0, 1.0 ] }
                        ],
                        "output": "minecraft:main"
                    }
                ]
            }
        """.trimIndent()
        mcversion == "1.21.8" -> """
            {
                "targets": {
                    "swap": {}
                },
                "passes": [
                    {
                        "vertex_shader": "minecraft:post/blit",
                        "fragment_shader": "colorsaturation:post/color_saturation",
                        "inputs": [
                            { "sampler_name": "DiffuseSampler", "target": "minecraft:main" }
                        ],
                        "uniforms": {
                            "SaturationConfig": [
                                {
                                    "name": "Saturation",
                                    "type": "float",
                                    "value": 1.0
                                }
                            ]
                        },
                        "output": "swap"
                    },
                    {
                        "vertex_shader": "minecraft:post/blit",
                        "fragment_shader": "minecraft:post/blit",
                        "inputs": [
                            { "sampler_name": "In", "target": "swap" }
                        ],
                        "output": "minecraft:main"
                    }
                ]
            }
        """.trimIndent()
        else -> """
            {
                "targets": {
                    "swap": {}
                },
                "passes": [
                    {
                        "vertex_shader": "minecraft:core/screenquad",
                        "fragment_shader": "colorsaturation:post/color_saturation",
                        "inputs": [
                            { "sampler_name": "DiffuseSampler", "target": "minecraft:main" }
                        ],
                        "uniforms": {
                            "SaturationConfig": [
                                {
                                    "name": "Saturation",
                                    "type": "float",
                                    "value": 1.0
                                }
                            ]
                        },
                        "output": "swap"
                    },
                    {
                        "vertex_shader": "minecraft:core/screenquad",
                        "fragment_shader": "minecraft:post/blit",
                        "inputs": [
                            { "sampler_name": "In", "target": "swap" }
                        ],
                        "output": "minecraft:main"
                    }
                ]
            }
        """.trimIndent()
    }

    val props = mapOf(
        "mod_id" to modid,
        "mod_name" to modname,
        "mod_version" to modversion,
        "minecraft_version_range" to versionrange,
        "loader_version" to loaderversion,
        "java_version" to "JAVA_${findProperty("java_version")?.toString() ?: "21"}",
    )

    inputs.properties(props)
    inputs.property("postEffectJson", postEffectJson)

    filesMatching(listOf("fabric.mod.json", "mixins.$modid.json")) {
        expand(props)
    }

    exclude("assets/colorsaturation/post_effect/color_saturation.json")

    if (mcversion != "1.21.1") {
        exclude(
            "assets/minecraft/shaders/post/color_saturation.json",
            "assets/minecraft/shaders/program/color_saturation.json",
            "assets/minecraft/shaders/program/color_saturation.fsh"
        )
    }

    if (mcversion != "1.21.4") {
        exclude("assets/colorsaturation/shaders/post/color_saturation_legacy.json")
    }

    doLast {
        val output = destinationDir.resolve("assets/colorsaturation/post_effect/color_saturation.json")
        output.parentFile.mkdirs()
        output.writeText("$postEffectJson\n")
    }
}

val javaVersionStr = findProperty("java_version")?.toString() ?: "21"
val javaVersionInt = javaVersionStr.toInt()

val kotlinJvmTarget = when (javaVersionInt) {
    21 -> JvmTarget.JVM_21
    22 -> JvmTarget.JVM_22
    23 -> JvmTarget.JVM_23
    24 -> JvmTarget.JVM_24
    25 -> JvmTarget.JVM_25
    else -> JvmTarget.JVM_21
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(javaVersionInt)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(kotlinJvmTarget)
}

java {
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersionInt))
    }
}

tasks.jar {
    inputs.property("archivesName", base.archivesName)

    from("LICENSE") {
        rename { "${it}_${inputs.properties["archivesName"]}" }
    }
}

fun <T> optionalProp(property: String, block: (String) -> T?): T? =
    findProperty(property)?.toString()?.takeUnless { it.isBlank() }?.let(block)

val modrinthMinecraftVersionOverride = mapOf(
    "1.21.8" to listOf("1.21.7", "1.21.8"),
    "1.21.10" to listOf("1.21.9", "1.21.10"),
    "26.1" to listOf("26.1", "26.1.1", "26.1.2"),
    "26.1.1" to listOf("26.1", "26.1.1", "26.1.2"),
    "26.1.2" to listOf("26.1", "26.1.1", "26.1.2"),
)

val modrinthId = listOf("oneconfig.publish.modrinth", "publish.modrinth").firstNotNullOfOrNull { findProperty(it) }?.toString()?.takeIf { it.isNotBlank() }
val modrinthToken = listOf("oneconfig.publish.modrinth.token", "publish.modrinth.token", "modrinth.token").firstNotNullOfOrNull { findProperty(it) }?.toString()?.takeIf { it.isNotBlank() }
val minecraftVersion = modrinthMinecraftVersionOverride[mcversion] ?: listOf(mcversion)
val publishJarTaskName = if ("remapJar" in tasks.names) "remapJar" else "jar"
val changelogs = rootProject.file("CHANGELOG.md").takeIf { it.exists() }?.readText() ?: "No changelog provided."

val validateChangelog by tasks.registering {
    description = "Validates that the changelog is written for the current version."
    if (!changelogs.contains(modversion)) {
        throw GradleException("Changelog for version $modversion not found.")
    }
}

tasks.matching { it.name == "publishMods" || it.name == "publishModrinth" }.configureEach {
    dependsOn(validateChangelog)
}

publishMods {
    file = tasks.named<AbstractArchiveTask>(publishJarTaskName).flatMap { it.archiveFile }
    displayName = modversion
    version = "v$modversion"
    changelog = changelogs
    type = BETA
    modLoaders.add("fabric")
    dryRun = modrinthId == null || modrinthToken == null

    if (modrinthId != null) {
        modrinth {
            projectId = modrinthId
            accessToken = modrinthToken.orEmpty()
            minecraftVersions.addAll(minecraftVersion)
            requires("oneconfig")
            requires("fabric-language-kotlin")
        }
    }
}
