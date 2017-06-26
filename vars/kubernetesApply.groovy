#!/usr/bin/env groovy

def call(String yaml) {

    echo "apply kubernetes resource:\n" + yaml

    def cmd = """cat <<EOF | kubectl apply -f -
${yaml}
EOF

"""
    sh "${cmd}"
}

return this