#!/usr/bin/env groovy

/*
buildDockerImage {
    service: 'service_name',
    version: 'image_version',
    path: 'dockerfile_path'
}
 */
def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    def common = new org.iti.Common()

    def image = common.dockerImage(config.service, config.version)
    def path = config.get('path', '.')

    def hasDockerfile = fileExists common.dockerfilePath(path)
    if (!hasDockerfile) {
        throw new RuntimeException('dockerfile is not exists!')
    }

    sh "docker build -t ${image} ${path}"
    sh "docker push ${image}"
    sh "docker rmi ${image}"
}

return this
