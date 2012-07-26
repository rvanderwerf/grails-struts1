import org.codehaus.grails.struts.action.GrailsAwareActionServlet

class Struts1GrailsPlugin {
    def version = "1.3.10"
	def author = "Graeme Rocher"
	def title = "Provides integration between Grails and the Struts 1 framework"
	def grailsVersion = "1.1 > *"
	def description = """
A plug-in that makes Struts 1 (http://struts.apache.org/) the default controller/view rendering framework for Grails. Struts
is an older first generation framework and this plug-in facilitates migration away from Struts to a modern stack like Grails.
	"""

    def watchedResources = ["file:./web-app/WEB-INF/struts-config.xml","file:./web-app/WEB-INF/validation.xml"]

    def doWithDynamicMethods = {
        application.controllerClasses*.metaClass.each {
            metaClass.getActionMapping = {-> request['org.codehaus.grails.struts.ACTION_MAPPING'] }
            metaClass.getActionForm = {-> request['org.codehaus.grails.struts.ACTION_FORM']}
        }
    }

    def doWithWebDescriptor = { xml ->
        def strutsServlet = xml.servlet.find { it.'servlet-name' == 'struts' }

        if(!strutsServlet || strutsServlet.size() == 0) {
            def servlets = xml.servlet[0]

            servlets += {
                servlet {
                    'servlet-name'('struts')
                    'servlet-class'(GrailsAwareActionServlet.getName())
                    'load-on-startup'(2)
                }
            }

            def mappings = xml.'servlet-mapping'[0]

            mappings += {
                'servlet-mapping' {
                    'servlet-name'('struts')
                    'url-pattern'('*.do')
                }
            }
        }
    }


    def onChange = {
        restartContainer()
    }
	                                      
}
