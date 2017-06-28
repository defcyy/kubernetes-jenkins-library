#!/usr/bin/env groovy

def call(String service, String version, String path, String credentialId) {
    def common = new org.iti.Common()

    def image = common.dockerImage(service, version)

    sh "docker build -t ${image} ${path}"
    utils.dockerLogin(common.projectConfig.docker_registry, credentialId)
    sh "docker push ${image}"
}

return this
