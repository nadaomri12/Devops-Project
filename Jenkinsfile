pipeline {
    agent any

    environment {
        // Créez un tag unique basé sur le numéro de build Jenkins
        FRONTEND_TAG = "frontend-${env.BUILD_NUMBER}"
        BACKEND_TAG = "backend-${env.BUILD_NUMBER}"
        REGISTRY_URL = "13.91.127.73:8082"
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
                            echo $NEXUS_PASSWORD | docker login -u $NEXUS_USERNAME --password-stdin $REGISTRY_URL
                        '''
                    }
                }
            }
        }

        stage('Build, Tag and Push Frontend Image') {
            steps {
                script {
                    sh """
                        echo "Building frontend Docker image with tag ${FRONTEND_TAG}..."
                        docker build -t $REGISTRY_URL/coaudit-frontend:${FRONTEND_TAG} -f coAudit-frontend/Dockerfile coAudit-frontend
                        echo "Tagging frontend Docker image with 'latest'..."
                        docker tag $REGISTRY_URL/coaudit-frontend:${FRONTEND_TAG} $REGISTRY_URL/coaudit-frontend:latest
                        echo "Removing old 'latest' tag if it exists..."
                        docker rmi $REGISTRY_URL/coaudit-frontend:latest || true
                        echo "Pushing frontend Docker image with tag ${FRONTEND_TAG}..."
                        docker push $REGISTRY_URL/coaudit-frontend:${FRONTEND_TAG}
                        echo "Pushing frontend Docker image with tag 'latest'..."
                        docker push $REGISTRY_URL/coaudit-frontend:latest
                    """
                }
            }
        }

        stage('Build, Tag and Push Backend Image') {
            steps {
                script {
                    sh """
                        echo "Building backend Docker image with tag ${BACKEND_TAG}..."
                        docker build -t $REGISTRY_URL/coaudit-backend:${BACKEND_TAG} -f coAudit-backend/Dockerfile coAudit-backend
                        echo "Tagging backend Docker image with 'latest'..."
                        docker tag $REGISTRY_URL/coaudit-backend:${BACKEND_TAG} $REGISTRY_URL/coaudit-backend:latest
                        echo "Removing old 'latest' tag if it exists..."
                        docker rmi $REGISTRY_URL/coaudit-backend:latest || true
                        echo "Pushing backend Docker image with tag ${BACKEND_TAG}..."
                        docker push $REGISTRY_URL/coaudit-backend:${BACKEND_TAG}
                        echo "Pushing backend Docker image with tag 'latest'..."
                        docker push $REGISTRY_URL/coaudit-backend:latest
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
