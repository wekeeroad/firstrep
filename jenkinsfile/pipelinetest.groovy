pipeline {
  agent any
  options {
    buildDiscarder(logRotator(numToKeepStr: '10', daysToKeepStr: '3'))
    timeout(time: 10, unit: 'HOURS')
  }
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
