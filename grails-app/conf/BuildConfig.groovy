grails.project.work.dir = 'target'
grails.project.source.level = 1.6

grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
		grailsCentral()

		mavenLocal()
		mavenCentral()
	}

	dependencies {
		compile('taglibs:standard:1.1.2', 'org.apache.struts:struts-core:1.3.10', 'org.apache.struts:struts-extras:1.3.10',
		        'org.apache.struts:struts-taglib:1.3.10',
				  //'org.apache.struts:mailreader-dao:1.3.10',
		        'commons-chain:commons-chain:1.2', 'commons-digester:commons-digester:1.8',
				  'org.springframework:spring-struts:3.1.0.RELEASE') {
			transitive = false
		}
	}

	plugins {
		build(':release:2.0.4', ':rest-client-builder:1.0.2') {
			export = false
		}
	}
}
