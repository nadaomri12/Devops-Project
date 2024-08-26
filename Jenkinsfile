pipeline {
    agent any

    environment {
        // Crée des tags uniques basés sur le numéro de build Jenkins
        FRONTEND_TAG = "frontend-${env.BUILD_NUMBER}"
        BACKEND_TAG = "backend-${env.BUILD_NUMBER}"
        AZURE_VM_IP = '13.91.127.73'
    }

    stages {
        stage('Install Azure CLI') {
            steps {
                script {
                    sh '''
                        echo "Installing Azure CLI..."
                        curl -sL https://aka.ms/InstallAzureCLIDeb | bash
                    '''
                }
            }
        }

        stage('Azure CLI Authentication') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'azureCredentials', usernameVariable: 'AZURE_USERNAME', passwordVariable: 'AZURE_PASSWORD')]) {
                        sh '''
                            echo "Authenticating with Azure CLI..."
                            az login -u $AZURE_USERNAME -p $AZURE_PASSWORD
                        '''
                    }
                }
            }
        }

        stage('Login to Nexus') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'NexusCredentials', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                        sh '''
                            echo "Logging into Nexus..."
                            echo $NEXUS_PASSWORD | docker login -u $NEXUS_USERNAME --password-stdin 13.91.127.73:8082
                        '''
                    }
                }
            }
        }

        stage('Build and Push Frontend Image') {
            steps {
                script {
                    sh """
                        echo "Building frontend Docker image with tag ${FRONTEND_TAG}..."
                        docker build -t 13.91.127.73:8082/coaudit-frontend: ${FRONTEND_TAG} -f coAudit-frontend/Dockerfile coAudit-frontend
                        echo "Pushing frontend Docker image with tag ${FRONTEND_TAG}..."
                        docker push 13.91.127.73:8082/coaudit-frontend: ${FRONTEND_TAG}
                    """
                }
            }
        }

        stage('Build and Push Backend Image') {
            steps {
                script {
                    sh """
                        echo "Building backend Docker image with tag ${BACKEND_TAG}..."
                        docker build -t 13.91.127.73:8082/coaudit-backend:${BACKEND_TAG} -f coAudit-backend/Dockerfile coAudit-backend
                        echo "Pushing backend Docker image with tag ${BACKEND_TAG}..."
                        docker push 13.91.127.73:8082/coaudit-backend:${BACKEND_TAG}
                    """
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        success {
            echo "Pipeline completed successfully."
        }
        failure {
            echo "Pipeline failed."
        }
    }
}
