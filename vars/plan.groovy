def call() {
    withEnv(['VAR1=value1']) {
        script = libraryResource('com/test/scripts/test.sh')
        sh(script)
    }
}
