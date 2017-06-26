#!/usr/bin/env groovy

def call(String name, int containerPort, int servicePort) {
    def yaml="""
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

    return yaml
}

return this