#!/usr/bin/env groovy

def call(String name, String image, int port, int replicas) {
    def yaml="""
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: ${name}
  namespace: demo
spec:
  replicas: ${replicas}
  strategy:
    type: RollingUpdate
  template:
    metadata:
      labels:
        project: demo
        app: ${name}
    spec:
      nodeSelector:
        cluster.tier: app
        project: demo
      containers:
      - name: ${name}
        image: ${image}
        imagePullPolicy: IfNotPresent
        ports:
          - containerPort: ${port}
        readinessProbe:
          tcpSocket:
            port: ${port}
          initialDelaySeconds: 150
          periodSeconds: 15
        livenessProbe:
          tcpSocket:
            port: ${port}
          initialDelaySeconds: 150
          periodSeconds: 15
"""

    return yaml
}

return this