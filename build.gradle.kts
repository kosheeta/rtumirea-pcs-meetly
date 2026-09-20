plugins {
    id("java")
    application
}

group = "ru.rtumirea.meetly"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("ru.rtumirea.meetly.Main")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.postgresql:postgresql:42.7.13")
    implementation("io.github.cdimascio:dotenv-java:3.2.0")
    implementation("org.apache.poi:poi-ooxml:5.3.0")

    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}