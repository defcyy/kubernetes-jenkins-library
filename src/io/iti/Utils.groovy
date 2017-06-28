#!/usr/bin/env groovy
package io.iti

def getProjectConfig() {
    def config = [
            'name': env.PROJECT_NAME,
            'docker_registry': env.PROJECT_DOCKER_REGISTRY
    ]
    return config
}

def dockerLogin(String registry, String credentialId) {
    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: credentialId,usernameVariable: 'USERNAME',passwordVariable: 'PASSWORD']]) {
        sh "docker login -u $USERNAME -p $PASSWORD ${registry}"
    }
}

return this