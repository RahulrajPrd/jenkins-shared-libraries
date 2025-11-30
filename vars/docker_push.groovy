def call(Map config = [:]) {

    def imageName   = config.imageName               // local image name: wanderlust-backend
    def fullImage   = config.fullImage               // repo image: rahulrajparida/wanderlust-backend
    def imageTag    = config.imageTag ?: 'latest'
    def credentials = config.credentials ?: 'dockerHubLogin'

    echo "Pushing Docker image: ${fullImage}:${imageTag}"

    withCredentials([usernamePassword(
        credentialsId: credentials,
        usernameVariable: 'DOCKER_USER',
        passwordVariable: 'DOCKER_PASS'
    )]) {

        bat """
            echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin

            docker tag ${imageName}:${imageTag} ${fullImage}:${imageTag}
            docker tag ${imageName}:${imageTag} ${fullImage}:latest

            docker push ${fullImage}:${imageTag}
            docker push ${fullImage}:latest
        """
    }
}
