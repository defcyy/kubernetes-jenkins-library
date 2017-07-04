#!/usr/bin/env groovy

/*
runCommandInDocker {
    image = 'image'
    version = 'service'
    command = 'command'
    volume = 'v1:v1'
}
 */
def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    def common = new org.iti.Common()
    def projectConfig = new org.iti.Config().getProjectConfig()
    def image = "${projectConfig.docker_registry}/${image}:${version}"

    def volumeFlag = ""
    if (config.workspacePath != null & config.containerPath != null) {
        def path = common.workspacePath(config.workspacePath)
        volumeFlag = "-v ${path}:${config.containerPath}"
    }

    sh "docker run --rm ${volumeFlag} ${image} sh -c '${config.command}'"
}

return this