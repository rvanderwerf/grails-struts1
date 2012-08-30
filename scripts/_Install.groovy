includeTargets << grailsScript( "_GrailsInit" )
checkVersion()

['struts-config', 'validation'].each { String name ->
	if (!new File("./web-app/WEB-INF/${name}.xml").exists()) {
		ant.copy(file:"$struts1PluginDir/src/templates/${name}.xml", todir: "./web-app/WEB-INF")
	}
}
