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

    return "${projectConfig.docker_registry}/${projectConfig.name}/${serviceName}:${version}"
}

def getNamespace(String environment) {
    def projectConfig = new Config().getProjectConfig()

    return "${projectConfig.name}-${environment}"
}

def deploymentDefination(String environment, String serviceName, int replicas, int containerPort, String version, Map... environments) {
    def projectConfig = new Config().getProjectConfig()

    def project = [name: projectConfig.name, env: environment]
    def service = [name: serviceName, replicas: replicas]
    def container = [image: dockerImage(serviceName, version), port: containerPort]

    def binding = [project: project, service: service, container: container, environments: environments]

    def defaultDeploymentTpl = Paths.get(projectConfig.template_path, 'deployment-tpl.yaml').toString()
    def content = readFile defaultDeploymentTpl
    def template = new StreamingTemplateEngine().createTemplate(content)

    return template.make(binding).toString()
}

def serviceDefination(String environment, String serviceName, int servicePort, int containerPort) {
    def projectConfig = new Config().getProjectConfig()

    def project = [name: projectConfig.name, env: environment]
    def service = [name: serviceName, port: servicePort]
    def container = [port: containerPort]
    def binding = [project: project, service: service, container: container]

    def defaultServiceTpl = Paths.get(projectConfig.template_path, 'service-tpl.yaml').toString()
    def content = readFile defaultServiceTpl
    def template = new StreamingTemplateEngine().createTemplate(content)

    return template.make(binding).toString()
}

def getDockerfileBaseImage(String dockerfile) {
    def content = readFile dockerfile
    def baseImage = ""
    content.readLines().each { line ->
        def cmd = line.trim().replaceAll("( )+", " ").tokenize()
        if (cmd[0] != null && cmd[0].toUpperCase() == "FROM") {
            baseImage = cmd[1]
        }
    }

    return baseImage
}

def dockerfilePath(String path) {
    return Paths.get(path, 'Dockerfile').toString()
}

def workspacePath(String path) {
    return Paths.get(env.WORKSPACE, path).normalize().toString()
}

def workspaceHostPath(String path) {

    def getContainerCmd = "kubectl get pod \${HOSTNAME} -o jsonpath='{.status.containerStatuses[0].containerID}'"
    def container = sh script: "${getContainerCmd}", returnStdout: true
    def containerId = container.trim().replace('docker://', '')

    def hostPathCmd = "docker inspect ${containerId} -f '{{ range .Mounts }}{{ if eq .Destination \"/workspace\" }}{{ .Source }}{{ end }}{{ end }}'"
    def hostPath = sh script: "${hostPathCmd}", returnStdout: true

    return Paths.get(hostPath.trim(), env.JOB_NAME, path).normalize().toString()
}

def kubernetesApply(String content) {
    def cmd = """cat <<EOF | kubectl apply -f -
${content}
EOF

"""
    sh "${cmd}"
}

def getServiceImage(String environment, String service) {
    def namespace = getNamespace(environment)
    def imageCmd = "kubectl get deployment ${service}  --namespace ${namespace} -o jsonpath='{.spec.template.spec.containers[0].image}'"
    def image = sh script: "${imageCmd}", returnStdout: true
    return image.trim()
}

return this

return this