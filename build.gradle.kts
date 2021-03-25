import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    id("io.gitlab.arturbosch.detekt") apply false
    id("io.spring.dependency-management")
    id("org.springframework.boot") apply false
    kotlin("jvm") apply false
    kotlin("plugin.spring") apply false
    idea
    java
}

subprojects {
    if ( name == "cust-prof-web-ui" ) {
        return@subprojects
    }

    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "kotlin")
    apply(plugin = "io.spring.dependency-management")
    group = "com.telus.custprof.web"

    tasks.withType<Detekt> {
        failFast = false
        jvmTarget = "11"
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/capsa-digital/*")
            credentials {
                username = "michaellif"
                password = String(
                    charArrayOf(
                        '0', 'd', '0', 'a', '4', 'b', 'c', 'e', 'a', '2', 'b', '1', '1', 'e', '0', 'e', 'b', '4', '5', '8',
                        '8', '8', '9', 'b', 'f', '0', 'e', 'e', 'd', '0', 'a', '2', 'd', 'b', '7', 'c', 'a', 'e', 'b', 'a'
                    )
                )
            }
        }
        maven { setUrl("https://repo1.maven.org/maven2") }
        mavenCentral()
        google()
        jcenter()
        maven { setUrl("https://mvnrepository.com/artifact") }
        maven { setUrl("https://dl.bintray.com/kotlin/kotlinx.html") }
    }

    dependencyManagement {
        imports {
            mavenBom("io.projectreactor:reactor-bom:${CoreVersion.REACTOR_BOM}")
            mavenBom("org.springframework.boot:spring-boot-starter-parent:${CoreVersion.SPRING_BOOT}")
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${CoreVersion.SPRING_CLOUD}")
        }
        dependencies {
            dependency("ch.qos.logback.contrib:logback-jackson:${CoreVersion.LOGBACK_CONTRIB}")
            dependency("ch.qos.logback.contrib:logback-json-classic:${CoreVersion.LOGBACK_CONTRIB}")
            dependency("com.github.tomakehurst:wiremock-jre8:${CoreVersion.WIREMOCK_JRE8}")
            dependency("com.google.guava:guava:${CoreVersion.GUAVA}")
            dependency("com.sun.xml.bind:jaxb-core:${CoreVersion.JAXB_IMPL}")
            dependency("com.sun.xml.bind:jaxb-impl:${CoreVersion.JAXB_IMPL}")
            dependency("com.willowtreeapps.assertk:assertk-jvm:${CoreVersion.ASSERTK_JVM}")
            dependency("digital.capsa:capsa-core:${CoreVersion.CAPSA}")
            dependency("digital.capsa:capsa-it:${CoreVersion.CAPSA}")
            dependency("javax.validation:validation-api:${CoreVersion.JAVAX_VALIDATION}")
            dependency("javax.xml.bind:jaxb-api:${CoreVersion.JAXB_API}")
            dependency("org.hamcrest:java-hamcrest:${CoreVersion.JAVA_HAMCREST}")
            dependency("org.seleniumhq.selenium:selenium-chrome-driver:${CoreVersion.SELENIUM}")
            dependency("org.seleniumhq.selenium:selenium-firefox-driver:${CoreVersion.SELENIUM}")
            dependency("org.seleniumhq.selenium:selenium-java:${CoreVersion.SELENIUM}")
            dependency("org.springdoc:springdoc-openapi-ui:${CoreVersion.OPENAPI}")
        }
    }

    tasks {
        test {
            useJUnitPlatform {
                includeEngines("junit-jupiter")
                excludeEngines("junit-vintage")
            }
            //testLogging.showStandardStreams = false
            testLogging {
                showCauses = true
                showExceptions = true
                showStackTraces = true
                showStandardStreams = true
                exceptionFormat = TestExceptionFormat.FULL
                afterSuite(KotlinClosure2<TestDescriptor, TestResult, Any>({ desc, result ->
                    if (desc.parent == null) { // will match the outermost suite
                        val output =
                            "Results: ${result.resultType} (${result.testCount} tests, ${result.successfulTestCount} passed, ${result.failedTestCount} failed, ${result.skippedTestCount} skipped)"
                        println(output)
                    }
                }))
            }
            jvmArgs("-Dspring.profiles.active=test")
        }
    }
}

