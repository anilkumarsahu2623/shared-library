def call(Map config = [:]) {
    def dockerImage = config.dockerImage ?: ''
    def dockerCredentialsId = config.dockerCredentialsId ?: ''
    def gitUrl = config.gitUrl ?: ''
    def gitBranch = config.gitBranch ?: 'main'

    pipeline {
        agent any

        environment {
            DOCKER_IMAGE = "${dockerImage}"
            DOCKER_REGISTRY_CREDENTIALS = credentials("${dockerCredentialsId}")
        }

        stages {
            stage('Checkout') {
                steps {
                    script {
                        // Checkout repository for Windows compatibility
                        bat 'rmdir /S /Q Ecom_project'
                        bat "git clone -b ${gitBranch} ${gitUrl}"
                    }
                }
            }

            stage('Docker Login') {
                steps {
                    script {
                        // Docker login command compatible with Windows
                        bat "docker login -u ${DOCKER_REGISTRY_CREDENTIALS_USR} -p ${DOCKER_REGISTRY_CREDENTIALS_PSW}"
                    }
                }
            }

            stage('Docker Build') {
                steps {
                    script {
                        // Docker build command compatible with Windows
                        //bat "docker build -t ${DOCKER_IMAGE} ."
                        bat 'docker build -t anilkumarsahu2623/ecom_project -f Ecom_project/Dockerfile .'
                    }
                }
            }

            stage('Docker Push') {
                steps {
                    script {
                        // Docker push command compatible with Windows
                        bat "docker push ${DOCKER_IMAGE}"
                    }
                }
            }
        }

        post {
            success {
                echo "Pipeline executed successfully!"
            }
            failure {
                echo "Pipeline failed! Check the logs for details."
            }
            always {
                script {
                    // Cleanup Docker containers or images if necessary
                    bat "docker system prune -f"
                }
            }
        }
    }
}
