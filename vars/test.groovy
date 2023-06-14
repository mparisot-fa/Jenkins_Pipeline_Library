// https://www.lambdatest.com/blog/use-jenkins-shared-libraries-in-a-jenkins-pipeline/
def call() {
    set_parameters()
    pipeline {
        agent any
        stages {
            stage("plan") {
                steps {
                    script {
                        withEnv(['VAR1=value1']) {
                            script = libraryResource('com/test/scripts/test.sh')
                            sh(script)
                        }
                    }
                }
            }
        }
    }
}
