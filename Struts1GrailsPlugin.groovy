import org.codehaus.grails.struts.action.GrailsAwareActionServlet

class Struts1GrailsPlugin {
    def version = "1.3.11"
	def author = "Graeme Rocher"
	def title = "Provides integration between Grails and the Struts 1 framework"
	def grailsVersion = "2.0 > *"
    def documentation = "https://github.com/rvanderwerf/grails-struts1"
    def license = 'APACHE'
    def developers = [
            [name:  'Graeme Rocher', email: 'grocher@vmware.com'],
            [name: 'Ryan Vanderwerf', email: 'rvanderwerf@gmail.com']]
	def issueManagement = [system: 'JIRA', url: 'http://jira.grails.org/browse/GPSTRUTS1']
    def scm = [url: 'https://github.com/rvanderwerf/grails-struts1']
	def description = """
A plug-in that makes Struts 1 (http://struts.apache.org/) the default controllfer/view rendering framework for Grails. Struts
is an older first generation framework and this plug-in facilitates migration away from Struts to a modern stack like Grails.
If you want to use this with Grails 1.3.x, you must use v1.3.10 of this plugin as Grails 2 introduced breaking changes.
	"""

    def watchedResources = ["file:./web-app/WEB-INF/struts-config.xml","file:./web-app/WEB-INF/validation.xml"]

    def doWithDynamicMethods = {
        application.controllerClasses*.metaClass.each {
            metaClass.getActionMapping = {-> request['org.codehaus.grails.struts.ACTION_MAPPING'] }
            metaClass.getActionForm = {-> request['org.codehaus.grails.struts.ACTION_FORM']}
        }
    }

    /**
     * this resolver tells grails not to mess with any struts stuff ending in .do, otherwise
     * it will break any kind of mime-multipart file uploads
     */
    def doWithSpring = {
        multipartResolver(org.codehaus.grails.struts.StrutsAwareMultipartResolver) {
                    strutsActionExtension = ".do"

        }
    }
    /**
     * add struts servlets to web.xml for you
     */
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
