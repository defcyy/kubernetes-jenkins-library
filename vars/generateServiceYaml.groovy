#!/usr/bin/env groovy

def call(String name, int containerPort, int servicePort) {
    def service="""
apiVersion: v1
kind: Service
metadata:
  labels:
    project: demo
    app: ${name}
  name: ${name}
  namespace: demo
spec:
  ports:
  - name: http
    port: ${servicePort}
    targetPort: ${containerPort}
    """
    return service
}

return this;