/* Copyright 2004-2005 Graeme Rocher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.grails.struts.action;

import grails.util.BuildSettings;
import grails.util.BuildSettingsHolder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;

import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Allows Struts to read the web.xml file from Grails' generated one.
 *
 * @author Graeme Rocher
 * @since 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ServletContextWrapper implements ServletContext {

	private ServletContext adaptee;
	private GrailsApplication application;

	public ServletContextWrapper(ServletContext adaptee) {
		Assert.notNull(adaptee);
		this.adaptee = adaptee;

		WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(adaptee);
		application = (GrailsApplication)applicationContext.getBean("grailsApplication");
	}

	public String getContextPath() {
		return adaptee.getContextPath();
	}

	public ServletContext getContext(String s) {
		return adaptee.getContext(s);
	}

	public int getMajorVersion() {
		return adaptee.getMajorVersion();
	}

	public int getMinorVersion() {
		return adaptee.getMinorVersion();
	}

	public String getMimeType(String s) {
		return adaptee.getMimeType(s);
	}

	public Set getResourcePaths(String s) {
		return adaptee.getResourcePaths(s);
	}

	public URL getResource(String s) throws MalformedURLException {
		return adaptee.getResource(s);
	}

	public InputStream getResourceAsStream(String name) {
		if (application.isWarDeployed() || !name.equals("/WEB-INF/web.xml")) {
			return adaptee.getResourceAsStream(name);
		}

		BuildSettings settings = BuildSettingsHolder.getSettings();
		try {
			Resource resource = new FileSystemResource(settings.getResourcesDir().getAbsolutePath() + "/web.xml");
			return resource.exists() ? resource.getInputStream() : null;
		}
		catch (IOException e) {
			adaptee.log(e.getMessage(), e);
			return null;
		}
	}

	public RequestDispatcher getRequestDispatcher(String s) {
		return adaptee.getRequestDispatcher(s);
	}

	public RequestDispatcher getNamedDispatcher(String s) {
		return adaptee.getNamedDispatcher(s);
	}

	@SuppressWarnings("deprecation")
	public Servlet getServlet(String s) throws ServletException {
		return adaptee.getServlet(s);
	}

	@SuppressWarnings("deprecation")
	public Enumeration getServlets() {
		return adaptee.getServlets();
	}

	@SuppressWarnings("deprecation")
	public Enumeration getServletNames() {
		return adaptee.getServletNames();
	}

	public void log(String s) {
		adaptee.log(s);
	}

	@SuppressWarnings("deprecation")
	public void log(Exception e, String s) {
		adaptee.log(e, s);
	}

	public void log(String s, Throwable throwable) {
		adaptee.log(s, throwable);
	}

	public String getRealPath(String s) {
		return adaptee.getRealPath(s);
	}

	public String getServerInfo() {
		return adaptee.getServerInfo();
	}

	public String getInitParameter(String s) {
		return adaptee.getInitParameter(s);
	}

	public Enumeration getInitParameterNames() {
		return adaptee.getInitParameterNames();
	}

	public Object getAttribute(String s) {
		return adaptee.getAttribute(s);
	}

	public Enumeration getAttributeNames() {
		return adaptee.getAttributeNames();
	}

	public void setAttribute(String s, Object o) {
		adaptee.setAttribute(s, o);
	}

	public void removeAttribute(String s) {
		adaptee.removeAttribute(s);
	}

	public String getServletContextName() {
		return adaptee.getServletContextName();
	}

	@Override
	public JspConfigDescriptor getJspConfigDescriptor() {
		return adaptee.getJspConfigDescriptor();
	}

	@Override
	public ClassLoader getClassLoader() {
		return adaptee.getClassLoader();
	}

	@Override
	public void declareRoles(String... strings) {
		adaptee.declareRoles(strings);
	}

	@Override
	public <T extends EventListener> T createListener(Class<T> tClass) throws ServletException {
		return adaptee.createListener(tClass);
	}

	@Override
	public <T extends EventListener> void addListener(T t) {
		adaptee.addListener(t);
	}

	@Override
	public void addListener(String s) {
		adaptee.addListener(s);
	}

	@Override
	public void addListener(Class<? extends EventListener> aClass) {
		adaptee.addListener(aClass);
	}

	@Override
	public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
		return adaptee.getEffectiveSessionTrackingModes();
	}

	@Override
	public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
		return adaptee.getDefaultSessionTrackingModes();
	}

	@Override
	public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) throws IllegalStateException, IllegalArgumentException {
		adaptee.setSessionTrackingModes(sessionTrackingModes);
	}

	@Override
	public SessionCookieConfig getSessionCookieConfig() {
		return adaptee.getSessionCookieConfig();
	}

	@Override
	public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
		return adaptee.getFilterRegistrations();
	}

	@Override
	public FilterRegistration getFilterRegistration(String s) {
		return adaptee.getFilterRegistration(s);
	}

	@Override
	public <T extends Filter> T createFilter(Class<T> tClass) throws ServletException {
		return adaptee.createFilter(tClass);
	}

	@Override
	public FilterRegistration.Dynamic addFilter(String s, Class<? extends Filter> aClass) {
		return adaptee.addFilter(s, aClass);
	}

	@Override
	public FilterRegistration.Dynamic addFilter(String s, Filter filter) {
		return adaptee.addFilter(s, filter);
	}

	@Override
	public FilterRegistration.Dynamic addFilter(String s, String s1) {
		return adaptee.addFilter(s, s1);
	}

	@Override
	public Map<String, ? extends ServletRegistration> getServletRegistrations() {
		return adaptee.getServletRegistrations();
	}

	@Override
	public ServletRegistration getServletRegistration(String s) {
		return adaptee.getServletRegistration(s);
	}

	@Override
	public <T extends Servlet> T createServlet(Class<T> tClass) throws ServletException {
		return adaptee.createServlet(tClass);
	}

	@Override
	public ServletRegistration.Dynamic addServlet(String s, Class<? extends Servlet> aClass) {
		return adaptee.addServlet(s, aClass);
	}

	@Override
	public ServletRegistration.Dynamic addServlet(String s, Servlet servlet) {
		return adaptee.addServlet(s, servlet);
	}

	@Override
	public ServletRegistration.Dynamic addServlet(String s, String s1) {
		return adaptee.addServlet(s, s1);
	}

	@Override
	public boolean setInitParameter(String s, String s1) {
		return adaptee.setInitParameter(s, s1);
	}

	@Override
	public int getEffectiveMinorVersion() {
		return adaptee.getEffectiveMinorVersion();
	}

	@Override
	public int getEffectiveMajorVersion() {
		return adaptee.getEffectiveMajorVersion();
	}
}
