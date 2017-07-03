#!/usr/bin/env groovy

/*
rollbackService {
    environment = 'test'
    service = 'service_name'
}
*/
def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    def common = new org.iti.Common()
    def namespace = common.getNamespace(config.environment)

    sh "kubectl rollout undo deployment/${config.service} --namespace ${namespace}"
}

return this