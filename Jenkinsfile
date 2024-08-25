pipeline {
    agent any

    environment {
        // Créez un tag unique basé sur le numéro de build Jenkins
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
                        docker build -t 13.91.127.73:8082/coaudit-frontend:latest -f coAudit-frontend/Dockerfile coAudit-frontend
                        echo "Pushing frontend Docker image with tag ${FRONTEND_TAG}..."
                        docker push 13.91.127.73:8082/coaudit-frontend:latest
                    """
                }
            }
        }

        stage('Build and Push Backend Image') {
            steps {
                script {
                    sh """
                        echo "Building backend Docker image with tag ${BACKEND_TAG}..."
                        docker build -t 13.91.127.73:8082/coaudit-backend:latest -f coAudit-backend/Dockerfile coAudit-backend
                        echo "Pushing backend Docker image with tag ${BACKEND_TAG}..."
                        docker push 13.91.127.73:8082/coaudit-backend:latest
                    """
                }
            }
        }

        stage('Deploy to Azure VM with Docker Compose') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'AzureVMCredentials', usernameVariable: 'VM_USERNAME', passwordVariable: 'VM_PASSWORD')]) {
                        sh """
                            echo "Deploying to Azure VM..."

                            # Copier le fichier docker-compose.yml vers la VM
                            sshpass -p ${VM_PASSWORD} scp -o StrictHostKeyChecking=no docker-compose.yml ${VM_USERNAME}@${AZURE_VM_IP}:/projectdevops

                            # Exécuter les commandes Docker Compose sur la VM
                            sshpass -p ${VM_PASSWORD} ssh -o StrictHostKeyChecking=no ${VM_USERNAME}@${AZURE_VM_IP} << 'EOF'
                            cd /projectdevops
                            docker-compose down || true
                            docker-compose up -d
                            EOF
                        """
                    }
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
