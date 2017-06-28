#!/usr/bin/env groovy

def call(String service, String version, String path, String credentialId) {
    def utils = new io.iti.Utils()

    def config = utils.getProjectConfig()
    def image = "${config.registry}/${config.name}/${service}:${version}"
    sh "docker built -t ${image} ${path}"

    utils.dockerLogin(config.registry, credentialId)
    sh "docker push ${image}"
}

return this
