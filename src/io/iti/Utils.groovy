#!/usr/bin/env groovy
package io.iti

def getProjectConfig() {
    def config = [
            'name': System.getenv('PROJECT_NAME'),
            'docker_registry': System.getenv('PROJECT_DOCKER_REGISTRY')
    ]
    return config
}

def dockerLogin(String registry, String credentialId) {
    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: credentialId,usernameVariable: 'USERNAME',passwordVariable: 'PASSWORD']]) {
        sh "docker login -u $USERNAME -p $PASSWORD ${registry}"
    }
}

return this