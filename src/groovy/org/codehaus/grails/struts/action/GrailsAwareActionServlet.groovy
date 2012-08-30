package org.codehaus.grails.struts.action

import javax.servlet.ServletConfig
import javax.servlet.ServletContext
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.struts.action.ActionServlet
import org.apache.struts.action.RequestProcessor
import org.apache.struts.config.ModuleConfig
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.web.context.support.WebApplicationContextUtils

/**
 * @author Graeme Rocher
 * @since 1.0
 */
class GrailsAwareActionServlet extends ActionServlet {

	ServletContext getServletContext() {
		new ServletContextWrapper(super.getServletContext())
	}

	@Override
	protected synchronized RequestProcessor getRequestProcessor(ModuleConfig config) {
		def applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(super.getServletContext())
		GrailsApplication application = applicationContext.grailsApplication
		if (application.isWarDeployed()) {
			return super.getRequestProcessor(config)
		}

		Thread thread = Thread.currentThread()
		ClassLoader cl = thread.getContextClassLoader()
		try {
			thread.setContextClassLoader application.getClassLoader()
			return super.getRequestProcessor(config)
		}
		finally {
			thread.setContextClassLoader cl
		}
	}
}
