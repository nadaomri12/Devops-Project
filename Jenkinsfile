pipeline {     
    agent any
      
    environment {         
        // Crée des tags uniques basés sur le numéro de build Jenkins         
        FRONTEND_TAG = "frontend-${env.BUILD_NUMBER}"         
        BACKEND_TAG = "backend-${env.BUILD_NUMBER}"         
        AZURE_VM_IP = '13.64.76.130'     
    }      
    
    stages {         
        stage('Build Frontend Image') {             
            steps {                 
                script {                     
                    docker.build("coaudit-frontend:${FRONTEND_TAG}", "-f coAudit-frontend/Dockerfile coAudit-frontend")                 
                }             
            }         
        }          
        
        stage('Build Backend Image') {             
            steps {                 
                script {                     
                    docker.build("coaudit-backend:${BACKEND_TAG}", "-f coAudit-backend/Dockerfile coAudit-backend")                 
                }             
            }         
        }     
    }
}
