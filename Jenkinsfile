pipeline {
    agent any

    environment {
        // Create unique tags based on Jenkins build number
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

       /* stage('Deploy to VM') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'AzureVMCredentials', usernameVariable: 'VM_USERNAME', passwordVariable: 'VM_PASSWORD')]) {
                        sh '''
                            echo "Deploying to Azure VM..."
                            # Copy Docker Compose files to the VM
                            sshpass -p $VM_PASSWORD scp -o StrictHostKeyChecking=no docker-compose.yml .env $VM_USERNAME@$AZURE_VM_IP:/home/$VM_USERNAME/
                            
                            # SSH into the VM and run Docker Compose commands
                            sshpass -p $VM_PASSWORD ssh -o StrictHostKeyChecking=no $VM_USERNAME@$AZURE_VM_IP '
                                echo "Navigating to Docker Compose directory..."
                                cd /home/$VM_USERNAME
                                echo "Pulling latest images and starting containers..."
                                docker-compose pull
                                docker-compose up -d
                            '
                        '''
                    }
                }
            }
        }
    }
*/
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
