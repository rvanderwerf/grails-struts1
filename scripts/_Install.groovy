includeTargets << grailsScript( "_GrailsInit" )
checkVersion()

if(!new File("./web-app/WEB-INF/struts-config.xml").exists()) {
   ant.copy(file:"$struts1PluginDir/src/templates/struts-config.xml", todir: "./web-app/WEB-INF")
}
if(!new File("./web-app/WEB-INF/validation.xml").exists()) {
   ant.copy(file:"$struts1PluginDir/src/templates/validation.xml", todir: "./web-app/WEB-INF")
}