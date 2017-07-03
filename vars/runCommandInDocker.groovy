#!/usr/bin/env groovy

/*
runCommandInDocker {
    service = 'service_name'
    version = 'service'
    command = 'command'
}
 */
def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    def common = new org.iti.Common()
    def image = common.dockerImage(config.service, config.version)

    sh "docker run --rm ${image} sh -c '${config.command}'"
}

return this