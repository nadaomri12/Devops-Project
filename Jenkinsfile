pipeline {     
    agent any
      
    environment {         
        // Crée des tags uniques basés sur le numéro de build Jenkins         
        FRONTEND_TAG = "frontend-${env.BUILD_NUMBER}"         
        BACKEND_TAG = "backend-${env.BUILD_NUMBER}"         
        NEXUS_HOST = 'localhost'   // Nexus host is localhost
        NEXUS_PORT = '8082'        // Nexus Docker registry port
    }      
    
    stages {         
        stage('Build and Push Frontend Image') {             
            steps {                 
                script {                     
                    docker.withRegistry("http://${NEXUS_HOST}:${NEXUS_PORT}", 'NexusCredentials') {                         
                        def frontendImage = docker.build("coaudit-frontend:${FRONTEND_TAG}", "-f coAudit-frontend/Dockerfile coAudit-frontend")
                        frontendImage.push() // Push with the unique build tag
                        frontendImage.push('latest') // Optionally push the 'latest' tag
                    }                 
                }             
            }         
        }          
        
        stage('Build and Push Backend Image') {             
            steps {                 
                script {                     
                    docker.withRegistry("http://${NEXUS_HOST}:${NEXUS_PORT}", 'NexusCredentials') {                         
                        def backendImage = docker.build("coaudit-backend:${BACKEND_TAG}", "-f coAudit-backend/Dockerfile coAudit-backend")
                        backendImage.push() // Push with the unique build tag
                        backendImage.push('latest') // Optionally push the 'latest' tag
                    }                 
                }             
            }         
        }     
    }
}
