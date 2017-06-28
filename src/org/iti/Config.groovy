#!/usr/bin/env groovy
package org.iti

@Grab('org.yaml:snakeyaml:1.18')
import org.yaml.snakeyaml.Yaml

def getProjectConfig() {
    def CONFIG_PATH = "/kubernetes/project/config.yaml"
    def config = [
            template_path: "/kubernetes/template",
            docker_registry: "nexus-registry.cn133.azure.net"
    ]

    String content = readFile CONFIG_PATH
    Yaml yaml = new Yaml()
    def readConfig = (Map) yaml.load(content)
    readConfig.each {
        config.put(it.key, it.value)
    }

    return config
}

return this