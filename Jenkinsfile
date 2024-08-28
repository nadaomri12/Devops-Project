pipeline {
    agent any

    environment {
        // Crée des tags uniques basés sur le numéro de build Jenkins
        FRONTEND_TAG = "frontend-${env.BUILD_NUMBER}"
        BACKEND_TAG = "backend-${env.BUILD_NUMBER}"
        AZURE_VM_IP = '13.64.76.130'
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
                            echo $NEXUS_PASSWORD | docker login -u $NEXUS_USERNAME --password-stdin ${AZURE_VM_IP}:8082
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
                        docker build -t ${AZURE_VM_IP}:8082/coaudit-frontend:${FRONTEND_TAG} -f coAudit-frontend/Dockerfile coAudit-frontend
                        echo "Pushing frontend Docker image with tag ${FRONTEND_TAG}..."
                        docker push ${AZURE_VM_IP}:8082/coaudit-frontend:${FRONTEND_TAG}
                    """
                }
            }
        }

        stage('Build and Push Backend Image') {
            steps {
                script {
                    sh """
                        echo "Building backend Docker image with tag ${BACKEND_TAG}..."
                        docker build -t ${AZURE_VM_IP}:8082/coaudit-backend:${BACKEND_TAG} -f coAudit-backend/Dockerfile coAudit-backend
                        echo "Pushing backend Docker image with tag ${BACKEND_TAG}..."
                        docker push ${AZURE_VM_IP}:8082/coaudit-backend:${BACKEND_TAG}
                    """
                }
            }
        }

        stage('Deploy to Azure VM') {
            steps {
                script {
                    sshagent(['AzureVMSSH']) {
                        sh '''
                            echo "Deploying application to Azure VM..."
                            scp -o StrictHostKeyChecking=no docker-compose.yml .env nadaomri@${AZURE_VM_IP}:/home/nadaomri/
                            ssh -o StrictHostKeyChecking=no nadaomri@${AZURE_VM_IP} << EOF
                                echo "Running Docker Compose on Azure VM..."
                                cd /home/nadaomri/
                                docker-compose down || true
                                docker-compose pull
                                docker-compose up -d
                            EOF
                        '''
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
