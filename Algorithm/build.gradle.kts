plugins {
    kotlin("jvm")
}

group = "khelp.algorithm"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    api(project(":Utilities"))
    api(project(":Math"))
    api(project(":Image"))
    implementation(project(":Thread"))
    implementation(project(":UI"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}