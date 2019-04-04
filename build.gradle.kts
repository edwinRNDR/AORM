import tanvd.kosogor.proxy.publishJar

group = "tanvd.aorm"
version = "1.1.3"

plugins {
    kotlin("jvm") version "1.3.21" apply true
    id("tanvd.kosogor") version "1.0.4"
}

repositories {
    jcenter()
    maven("https://dl.bintray.com/jfrog/jfrog-jars")
}

dependencies {
    compile("org.jetbrains.kotlin", "kotlin-stdlib", "1.3.21")
    compile("org.jetbrains.kotlin", "kotlin-reflect", "1.3.21")
    compile("ru.yandex.clickhouse", "clickhouse-jdbc", "0.1.52")
    compile("joda-time", "joda-time", "2.9.9")
    compile("org.slf4j", "slf4j-api", "1.7.25")

    testCompile("org.testng", "testng", "6.11")
    testCompile("org.mockito", "mockito-all", "1.10.19")
    testCompile("org.powermock", "powermock-mockito-release-full", "1.6.4")
}

(tasks["test"] as Test).apply {
    systemProperty("clickhouseUrl", System.getenv("clickhouseUrl"))
    systemProperty("clickhouseUser", System.getenv("clickhouseUser"))
    systemProperty("clickhousePassword", System.getenv("clickhousePassword"))

    useTestNG()
}

publishJar {
    publication {
        artifactId = "aorm"
    }

    artifactory {
        serverUrl = "https://oss.jfrog.org/artifactory"
        repository = "oss-snapshot-local"
        username = "tanvd"
        secretKey = System.getenv("artifactory_api_key")
    }

    bintray {
        username = "tanvd"
        secretKey = System.getenv("bintray_api_key")
        repository = "aorm"
        info {
            githubRepo = "tanvd/aorm"
            vcsUrl = "https://github.com/tanvd/aorm"
            labels.addAll(listOf("kotlin", "clickhouse"))
            license = "MIT"
            description = "Kotlin SQL Framework for Clickhouse"
        }
    }
}
