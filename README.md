grails-struts1
==============

Grails Struts1 Plugin

This version of the plugin has been updated to work with Grails 2.+
A plug-in that makes Struts 1 (http://struts.apache.org/) the default controller/view rendering framework for Grails. Struts
is an older first generation framework and this plug-in facilitates migration away from Struts to a modern stack like Grails.
If you want to use this with Grails 1.3.x, you must use v1.3.10 of this plugin as Grails 2 introduced breaking changes.

This plugin is useful for others who want to introduce Grails into their old legacy apps to breathe new life into then, and give
them a path to move forward without having to start over all at once.

See a demo app I made to try things out at  https://github.com/rvanderwerf/struts-demo

Original author: Graeme Rocher

TODOS
===========
The recommended path to use this with Grails 2 is to use existing Struts actions in Java or Groovy, and gradually
port them to real Grails controllers.
There may be bugs in the ControllerActionProxy, please report issues at http://jira.grails.org/browse/GPSTRUTS1
or contact Ryan Vanderwerf @ rvanderwerf@gmail.com

History
===================
Originally written for Grails 1.3.x by Graeme Rocher
2012 - updated v 1.3.11 to work with Grails 2.x  by Ryan Vanderwerf, source ported to new grails repository



Ryan Vanderwerf
rvanderwerf@gmail.com
