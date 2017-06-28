#!/usr/bin/env groovy
package io.iti

@Grab('org.yaml:snakeyaml:1.18')
import org.yaml.snakeyaml.Yaml

def getProjectConfig() {
    def configFile = "/etc/project/config.yaml"
    Yaml parser = new Yaml()
    Map map = (Map) parser.load((configFile as File).text)
    return map
}

def dockerLogin(String registry, String credentialId) {
    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: credentialId,usernameVariable: 'USERNAME',passwordVariable: 'PASSWORD']]) {
        sh "docker login -u $USERNAME -p $PASSWORD ${registry}"
    }
}

return this