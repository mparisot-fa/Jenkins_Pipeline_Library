def call() {
    withEnv(['VAR1=value1']) {
        sh(libraryResource('test.sh'))
    }
}
