grails.project.dependency.resolution = {
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve

    repositories {
        inherits true // Whether to inherit repository definitions from plugins
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()

        // uncomment these to enable remote dependency resolution from public Maven repositories
        //mavenCentral()
        //mavenLocal()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.16'
        compile "taglibs:standard:1.1.2"

    }
    plugins {
        runtime ":release:2.0.4"
    }
    grails.project.repos.PluginSnapShots.url = "http://127.0.0.1:8083/artifactory/plugins-snapshot-local"
    grails.project.repos.PluginSnapShots.type = "maven"
    grails.project.repos.PluginSnapShots.username = "admin"
    grails.project.repos.PluginSnapShots.password = "password"

    grails.project.repos.default = "PluginSnapShots"

}