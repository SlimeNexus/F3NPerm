import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    java
    idea
}

group = "nexus.slime"
version = "3.4"

val targetJavaVersion = 8

// Dependencies
repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.9.4-R0.1-SNAPSHOT")
    compileOnly("io.netty:netty-all:4.1.90.Final")

    compileOnly("com.comphenix.protocol:ProtocolLib:5.0.0")
    compileOnly("net.luckperms:api:5.4")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

// Set java version

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)

    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion

    if (JavaVersion.current() < javaVersion) {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
        }
    }
}

tasks.withType(JavaCompile::class) {
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersion)
    }
}

// Set the right test framework

tasks.test {
    useJUnitPlatform()
}

// Add version to plugin.yml

tasks.processResources {
    val props = mapOf("version" to version)

    inputs.properties(props)
    filteringCharset = "UTF-8"

    filesMatching("plugin.yml") {
        expand(props)
    }

    filesMatching("config.yml") {
        filter(ReplaceTokens::class, mapOf("tokens" to props))
    }
}
