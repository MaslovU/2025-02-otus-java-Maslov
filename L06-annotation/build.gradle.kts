dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation ("ch.qos.logback:logback-classic")
}

tasks.test {
    useJUnitPlatform()
}

group = "ru.otus"

