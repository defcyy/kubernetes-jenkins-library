#!/usr/bin/env groovy

/*
buildDockerImage {
    service = 'service_name'
    version = 'image_version'
    path = 'dockerfile_path'
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
    def path = config.get('path', '.')

    def dockerfile = common.dockerfilePath(path)
    def hasDockerfile = fileExists dockerfile
    if (!hasDockerfile) {
        throw new RuntimeException('dockerfile is not exists!')
    }

    def projectConfig = new org.iti.Config().getProjectConfig()
    def baseImage = common.getDockerfileBaseImage(dockerfile)
    if (!baseImage.startsWith(projectConfig.docker_registry)) {
        throw new RuntimeException('base image is not forbidden')
    }

    sh "docker build -t ${image} ${path}"
    sh "docker push ${image}"
    sh "docker rmi ${image}"
}

return this
