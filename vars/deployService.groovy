#!/usr/bin/env groovy

/*
deployService {
    environment = 'test'
    service = 'service_name'
    replicas = container_count
    servicePort = service_port
    containerPort = container_port
    version = image_version
}
*/
def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    def common = new org.iti.Common()
    def version = config.get('version', env.BUILD_NUMBER)
    def deployment = common.deploymentDefination(config.environment, config.service, config.replicas, config.containerPort, version)
    kubernetesApply(deployment)

    def service = common.serviceDefination(config.environment, config.service, config.servicePort, config.containerPort)
    kubernetesApply(service)
}

return this