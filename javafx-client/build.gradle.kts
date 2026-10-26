plugins {
    id("java")
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "ru.rtumirea.meetly"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("ru.rtumirea.meetly.Main")
}

javafx {
    version = "25.0.1"
    modules = listOf("javafx.controls")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.postgresql:postgresql:42.7.13")
    implementation("io.github.cdimascio:dotenv-java:3.2.0")
    implementation("com.google.zxing:core:3.5.3")
    implementation("org.postgresql:postgresql:42.7.13")
    implementation("io.github.cdimascio:dotenv-java:3.2.0")

    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
