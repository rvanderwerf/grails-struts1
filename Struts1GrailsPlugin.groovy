import org.codehaus.grails.struts.StrutsAwareMultipartResolver
import org.codehaus.grails.struts.action.GrailsAwareActionServlet

class Struts1GrailsPlugin {
	def version = "1.3.10"
	def author = "Graeme Rocher"
	def title = "Struts 1 Plugin"
	def grailsVersion = "2.0 > *"
	def description = """
Makes Struts 1 (http://struts.apache.org/) the default controller/view rendering framework for Grails. Struts
is an older first generation framework and this plug-in facilitates migration away from Struts to a modern stack like Grails.
"""
	def documentation = 'TODO'

	def watchedResources = ["file:./web-app/WEB-INF/struts-config.xml","file:./web-app/WEB-INF/validation.xml"]

	String license = 'APACHE'
	def scm = [url: 'https://github.com/rvanderwerf/grails-struts1']
	def issueManagement = [system: 'JIRA', url: 'http://jira.grails.org/browse/GPSTRUTS1']

	def doWithDynamicMethods = {
		application.controllerClasses*.metaClass.each {
			metaClass.getActionMapping = {-> request['org.codehaus.grails.struts.ACTION_MAPPING'] }
			metaClass.getActionForm = {-> request['org.codehaus.grails.struts.ACTION_FORM']}
		}
	}

	def doWithSpring = {
		multipartResolver(StrutsAwareMultipartResolver) {
			strutsActionExtension = ".do"
		}
	}

	def doWithWebDescriptor = { xml ->
		def strutsServlet = xml.servlet.find { it.'servlet-name' == 'struts' }

		if (!strutsServlet || strutsServlet.size() == 0) {
			def servlets = xml.servlet[0]

			servlets += {
				servlet {
					'servlet-name'('struts')
					'servlet-class'(GrailsAwareActionServlet.name)
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
