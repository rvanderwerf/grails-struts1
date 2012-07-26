package org.codehaus.grails.struts.action

import org.springframework.web.struts.DelegatingActionProxy
import org.apache.struts.action.ActionMapping
import org.springframework.web.context.support.WebApplicationContextUtils
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.ControllerArtefactHandler
import org.apache.struts.action.ActionForward
import org.apache.struts.action.ActionForm
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.web.context.request.RequestContextHolder
//import org.codehaus.groovy.grails.web.servlet.mvc.SimpleGrailsControllerHelper
import org.springframework.web.servlet.ModelAndView
import org.codehaus.groovy.grails.web.servlet.mvc.MethodGrailsControllerHelper

/**
 * A Proxy that can be configured in struts-config.xml that allows Grails controllers to act like Struts actions. 
 *
 * @author Graeme Rocher
 * @since 1.0
 *
 * Created: Mar 3, 2008
*/
class ControllerActionProxy extends DelegatingActionProxy {

    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        def applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext())
        def application = ApplicationHolder.getApplication()

        String path = actionMapping.getPath()
        def controllerClass = application.getArtefactForFeature(ControllerArtefactHandler.TYPE, path)

        if(controllerClass) {
            def controller = applicationContext.getBean(controllerClass.fullName)
            def webRequest = RequestContextHolder.currentRequestAttributes()

            def actionName = controllerClass.getClosurePropertyName(path)
            def viewName = controllerClass.getViewByURI(path)
            
            webRequest.actionName = actionName

            def request = webRequest.request

            request.setAttribute('org.codehaus.grails.struts.ACTION_MAPPING',actionMapping)
            request.setAttribute('org.codehaus.grails.struts.ACTION_FORM', actionForm)

            def result = controller[actionName].call()

            def helper = new MethodGrailsControllerHelper(application, applicationContext, getServlet().getServletContext())
            //helper.configureStateForWebRequest(webRequest, webRequest.getRequest())
            // helper.set
            helper.setApplicationContext()
            ModelAndView mv = helper.handleActionResponse(controller, result, actionName, viewName)
            if(mv) {
                mv.getModel()?.each { k, v ->
                    request.setAttribute(k,v)                    
                }
                if(mv.viewName)
                    return new ActionForward("${mv.viewName}.gsp")
            }
        }
        else {
            return super.execute(actionMapping, actionForm, httpServletRequest, httpServletResponse);
        }

    }
}