// vars/docker_push.groovy
def call(Map config = [:]) {
    def imageName   = config.imageName.split('/').last()   // "backend" from "rahulrajprd/backend"
    def fullImage   = config.imageName                     // "rahulrajprd/backend"
    def imageTag    = config.imageTag    ?: 'latest'
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
            
            docker rmi ${imageName}:${imageTag} ${fullImage}:${imageTag} ${fullImage}:latest
        """
    }
}
