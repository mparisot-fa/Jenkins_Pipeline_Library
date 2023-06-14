def call() {
    withEnv(['VAR1=value1']) {
        sh(libraryResource('com/test/scripts/test.sh'))
    }
}
