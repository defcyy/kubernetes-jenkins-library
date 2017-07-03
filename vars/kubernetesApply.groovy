#!/usr/bin/env groovy

/*
kubernetesApply {
    file: content
}
 */
def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    echo "apply kubernetes resource:\n" + config.file

    def cmd = """cat <<EOF | kubectl apply -f -
${config.file}
EOF

"""
    sh "${cmd}"
}

return this