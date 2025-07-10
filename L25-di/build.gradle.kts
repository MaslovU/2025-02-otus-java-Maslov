dependencies {
    implementation("org.reflections:reflections:0.10.2")

    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.mockito:mockito-junit-jupiter")

    implementation ("org.projectlombok:lombok")
    implementation("ch.qos.logback:logback-classic")

    annotationProcessor ("org.projectlombok:lombok")
}
tasks.test {
    useJUnitPlatform()
}