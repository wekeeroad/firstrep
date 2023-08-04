pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        echo 'Hello world'
      }
      post {
        always {
          sh "echo 'stage Build finish!'"
        }
      }
    } 
  }
  post {
    changed {
      echo "pipeline post changed!"
    }
    always {
      echo "pipeline post always!"
    }
    success {
      echo "pipeline post success!"
    }
  }
}
