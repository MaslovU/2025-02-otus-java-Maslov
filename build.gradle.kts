import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel

plugins {
    idea
    id("io.spring.dependency-management")
    id("org.springframework.boot") apply false
}

idea {
    project {
        languageLevel = IdeaLanguageLevel(21)
    }
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}


allprojects {
    group = "ru.otus"

    repositories {
        mavenLocal()
        mavenCentral()
    }

    val testcontainersBom: String by project
    val protobufBom: String by project
    val guava: String by project

    apply(plugin = "io.spring.dependency-management")
    dependencyManagement {
        dependencies {
            imports {
                mavenBom(BOM_COORDINATES)
                mavenBom("org.testcontainers:testcontainers-bom:$testcontainersBom")
                mavenBom("com.google.protobuf:protobuf-bom:$protobufBom")
            }
            dependency("com.google.guava:guava:$guava")
        }
    }

//    configurations.all {
//        resolutionStrategy {
//            failOnVersionConflict()
//
//
//            force("javax.servlet:servlet-api:2.5")
//            force("commons-logging:commons-logging:1.1.1")
//            force("commons-lang:commons-lang:2.5")
//            force("org.codehaus.jackson:jackson-core-asl:1.8.8")
//            force("org.codehaus.jackson:jackson-mapper-asl:1.8.8")
//            force("org.sonarsource.analyzer-commons:sonar-analyzer-commons:2.8.0.2699")
//            force("org.sonarsource.analyzer-commons:sonar-xml-parsing:2.8.0.2699")
//            force("org.sonarsource.sslr:sslr-core:1.24.0.633")
//            force("org.sonarsource.analyzer-commons:sonar-analyzer-recognizers:2.8.0.2699")
//            force("com.google.code.findbugs:jsr305:3.0.2")
//            force("commons-io:commons-io:2.15.1")
//            force("com.google.errorprone:error_prone_annotations:2.26.1")
//            force("com.google.j2objc:j2objc-annotations:3.0.0")
//        }
//    }
}

subprojects {
    plugins.apply(JavaPlugin::class.java)
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-Xlint:all,-serial,-processing"))
    }
}

tasks {
    val managedVersions by registering {
        doLast {
            project.extensions.getByType<DependencyManagementExtension>()
                .managedVersions
                .toSortedMap()
                .map { "${it.key}:${it.value}" }
                .forEach(::println)
        }
    }
}

