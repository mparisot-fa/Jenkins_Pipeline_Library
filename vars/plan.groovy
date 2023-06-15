// https://www.lambdatest.com/blog/use-jenkins-shared-libraries-in-a-jenkins-pipeline/
def call(Map config) {
    set_parameters(config)
    pipeline {
        agent any
        stages {
            stage("plan") {
                steps {
                    script {
                        withEnv(['VAR1=value1']) {
                            script = libraryResource('com/test/scripts/plan.sh')
                            sh(script)
                        }
                    }
                }
            }

        }
    }
}
