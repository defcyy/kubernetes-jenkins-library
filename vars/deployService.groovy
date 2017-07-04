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
    common.kubernetesApply(deployment)

    def service = common.serviceDefination(config.environment, config.service, config.servicePort, config.containerPort)
    common.kubernetesApply(service)

    def namespace = common.getNamespace(config.environment)
    sh "kubectl rollout status deployment/${config.service} --namespace ${namespace}"

    def image = common.getServiceImage(config.environment, config.service)
    echo "Deploy done! current service image ${image}"
}

return this