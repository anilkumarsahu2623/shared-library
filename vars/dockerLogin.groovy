def call() {
    bat '''
        # Remove existing Docker credentials
        rm -rf ~/.docker/config.json || true
        security delete-generic-password -s "Docker Credentials" || true
        
        # Verify Docker is running
        docker info
        
        # Login to Docker Hub
         stage('Docker Login') {
            steps {
                script {
                    withDockerRegistry(credentialsId: 'docker', toolName: 'docker')  {
                        echo 'Successfully logged in to Docker registry'
                   }
                    
                }
            }
        }
    '''
}
