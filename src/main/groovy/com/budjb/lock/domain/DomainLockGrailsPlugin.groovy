package com.budjb.lock.domain

import grails.plugins.*

class DomainLockGrailsPlugin extends Plugin {
    /**
     * Grails version requirement.
     */
    def grailsVersion = "3.1.0 > *"

    /**
     * Plugin title.
     */
    def title = "Domain Lock"

    /**
     * Plugin author.
     */
    def author = "Bud Byrd"

    /**
     * Author email address.
     */
    def authorEmail = "bud.byrd@gmail.com"

    /**
     * Plugin description.
     */
    def description = 'Provides a domain-backed concurrency Lock implementation.'

    /**
     * URL to the plugin's documentation
     */
    def documentation = "http://budjb.github.io/grails-domain-lock"

    /**
     * Plugin license.
     */
    def license = "APACHE"

    /**
     * Issue tracker.
     */
    def issueManagement = [ system: "github", url: "https://github.com/budjb/grails-domain-lock/issues" ]

    /**
     * SCM.
     */
    def scm = [ url: "https://github.com/budjb/grails-domain-lock" ]
}
