package org.codehaus.grails.struts.action

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.struts.action.ActionForm
import org.apache.struts.action.ActionForward
import org.apache.struts.action.ActionMapping
import org.codehaus.groovy.grails.commons.ControllerArtefactHandler
import org.codehaus.groovy.grails.web.servlet.mvc.MethodGrailsControllerHelper
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.support.WebApplicationContextUtils
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.struts.DelegatingActionProxy

/**
 * A Proxy that can be configured in struts-config.xml that allows Grails controllers to act like Struts actions.
 *
 * @author Graeme Rocher
 * @since 1.0
*/
class ControllerActionProxy extends DelegatingActionProxy {

	ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		def applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext())
		def application = applicationContext.grailsApplication

		String path = actionMapping.path
		def controllerClass = application.getArtefactForFeature(ControllerArtefactHandler.TYPE, path)
		if (!controllerClass) {
			return super.execute(actionMapping, actionForm, httpServletRequest, httpServletResponse)
		}

		def controller = applicationContext.getBean(controllerClass.fullName)
		def webRequest = RequestContextHolder.currentRequestAttributes()

		String actionName = controllerClass.getClosurePropertyName(path)
		String viewName = controllerClass.getViewByURI(path)

		webRequest.actionName = actionName

		def request = webRequest.request

		request.setAttribute('org.codehaus.grails.struts.ACTION_MAPPING', actionMapping)
		request.setAttribute('org.codehaus.grails.struts.ACTION_FORM', actionForm)

		def result = controller[actionName].call()

		def helper = new MethodGrailsControllerHelper(application, applicationContext, getServlet().getServletContext())
		helper.setApplicationContext()
		ModelAndView mv = helper.handleActionResponse(controller, result, actionName, viewName)
		if (mv) {
			mv.model.each { name, value -> request.setAttribute(name, value) }
			if (mv.viewName) {
				return new ActionForward("${mv.viewName}.gsp")
			}
		}
	}
}
