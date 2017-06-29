#!/usr/bin/env groovy

def call(String service, String version, String command) {
    def common = new org.iti.Common()
    def image = common.dockerImage(service, version)

    sh "docker run --rm ${image} sh -c '${command}'"
}

return this