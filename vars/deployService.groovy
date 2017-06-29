#!/usr/bin/env groovy

def call(String environment, String serviceName, int replicas, int containerPort, int servicePort, String version) {
    def common = new org.iti.Common()

    def deployment = common.deploymentDefination(environment, serviceName, replicas, containerPort, version)
    kubernetesApply(deployment)

    def service = common.serviceDefination(environment, serviceName, servicePort, containerPort)
    kubernetesApply(service)
}

return this