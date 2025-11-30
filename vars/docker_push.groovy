def call(Map config = [:]) {
    def imageName   = config.imageName                    // e.g. "myuser/backend"
    def imageTag    = config.imageTag    ?: 'latest'
    def credentials = config.credentials ?: 'dockerHubLogin'   // ← match your Jenkins credential ID

    echo "Pushing Docker image: ${imageName}:${imageTag}"

    withCredentials([usernamePassword(
        credentialsId: credentials,
        usernameVariable: 'DOCKER_USER',
        passwordVariable: 'DOCKER_PASS'
    )]) {
        bat """
            echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin
            docker push ${imageName}:${imageTag}
            docker push ${imageName}:latest
        """
    }
}
