pipeline {
    agent any
    tools {
        maven 'Maven' // Nom de l'installation Maven configurée dans Jenkins
    }
    environment {         
        // Crée des tags uniques basés sur le numéro de build Jenkins         
        FRONTEND_TAG = "frontend-${env.BUILD_NUMBER}"         
        BACKEND_TAG = "backend-${env.BUILD_NUMBER}"         
    }  
    stages {
        stage('Git Checkout') {
            steps {
                checkout([$class: 'GitSCM', 
                          branches: [[name: '*/dev']], 
                          extensions: [], 
                          userRemoteConfigs: [[
                              url: 'https://github.com/nadaomri12/Devops-Project.git', 
                              credentialsId: 'git-credentials'
                          ]]
                ])
                echo 'Git Checkout Completed'
            }
        }

        stage('Login to Nexus') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'NexusCredentials', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                        sh '''
                            echo "Logging into Nexus..."
                            echo "$NEXUS_PASSWORD" | docker login -u "$NEXUS_USERNAME" --password-stdin http://localhost:8082
                        '''
                    }
                }
            }
        }
   
        stage('Build and Push Frontend Image') {             
            steps {                 
                script {                     
                    docker.withRegistry("http://localhost:8082", 'NexusCredentials') {                         
                        def frontendImage = docker.build("localhost:8082/coaudit-frontend:${FRONTEND_TAG}", "-f coAudit-frontend/Dockerfile coAudit-frontend")
                        frontendImage.push()
                        frontendImage.push('latest')                     
                    }                 
                }             
            }         
        }          
        
        stage('Build and Push Backend Image') {             
            steps {                 
                script {                     
                    docker.withRegistry("http://localhost:8082", 'NexusCredentials') {                         
                        def backendImage = docker.build("localhost:8082/coaudit-backend:${BACKEND_TAG}", "-f coAudit-backend/Dockerfile coAudit-backend")
                        backendImage.push()  
                        backendImage.push('latest')                      
                    }                 
                }             
            }         
        } 
    }
}
