#!/usr/bin/env groovy
package org.iti

import groovy.text.StreamingTemplateEngine

import java.nio.file.Paths

def dockerLogin(String credentialId) {
    def projectConfig = new Config().getProjectConfig()
    def registry = projectConfig.docker_registry
    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: credentialId,usernameVariable: 'USERNAME',passwordVariable: 'PASSWORD']]) {
        sh "docker login -u $USERNAME -p $PASSWORD ${registry}"
    }
}

def dockerImage(String serviceName, String version) {
    def projectConfig = new Config().getProjectConfig()

    return "${projectConfig.docker}/${projectConfig.name}/${serviceName}:${version}"
}

def deploymentDefination(String serviceName, int replicas, int containerPort, String version) {
    def projectConfig = new Config().getProjectConfig()

    def project = [name: projectConfig.name]
    def service = [name: serviceName, replicas: replicas]
    def container = [image: dockerImage(serviceName, version), port: containerPort]

    def binding = [project: project, service: service, container: container]

    def defaultDeploymentTpl = Paths.get(projectConfig.template_path, 'deployment-tpl.yaml').toString()
    def content = readFile defaultDeploymentTpl
    def template = new StreamingTemplateEngine().createTemplate(content)

    return template.make(binding)
}


def serviceDefination(String serviceName, int servicePort, int containerPort) {
    def projectConfig = new Config().getProjectConfig()

    def project = [name: projectConfig.name]
    def service = [name: serviceName, port: servicePort]
    def container = [port: containerPort]
    def binding = [project: project, service: service, container: container]

    def defaultServiceTpl = Paths.get(projectConfig.template_path, 'service-tpl.yaml').toString()
    def content = readFile defaultServiceTpl
    def template = new StreamingTemplateEngine().createTemplate(content)

    return template.make(binding)
}

return this