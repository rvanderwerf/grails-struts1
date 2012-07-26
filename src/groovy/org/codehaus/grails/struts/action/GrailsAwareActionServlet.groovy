package org.codehaus.grails.struts.action

import org.apache.struts.action.ActionServlet
import javax.servlet.ServletContext
import javax.servlet.ServletConfig
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.apache.struts.action.RequestProcessor
import org.apache.struts.config.ModuleConfig
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.GrailsApplication

/**
* @author Graeme Rocher
* @since 1.0
*
* Created: Mar 4, 2008
*/
class GrailsAwareActionServlet extends ActionServlet {

    public ServletContext getServletContext() {
        return new ServletContextWrapper(super.getServletContext()); 
    }

    protected synchronized RequestProcessor getRequestProcessor(ModuleConfig config) {
        GrailsApplication application = ApplicationHolder.getApplication()
        if(!application.isWarDeployed()) {

            Thread t = Thread.currentThread()
            ClassLoader cl = t.getContextClassLoader();
            try {
                t.setContextClassLoader application.getClassLoader()
                return super.getRequestProcessor(config)
            } finally {
                t.setContextClassLoader cl
            };
        }
        else {
            return super.getRequestProcessor(config)
        }
    }



}