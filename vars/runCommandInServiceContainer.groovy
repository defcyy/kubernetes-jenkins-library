#!/usr/bin/env groovy

/*
runCommandInServiceContainer {
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
    def version = config.get('version', env.BUILD_NUMBER)
    def image = common.dockerImage(config.service, version)

    def volumeFlag = ""
    if (config.workspacePath != null & config.containerPath != null) {
        def path = common.workspacePath(config.workspacePath)
        volumeFlag = "-v ${path}:${config.containerPath}"
    }

    sh "docker run --rm ${volumeFlag} ${image} sh -c '${config.command}'"
}

return this