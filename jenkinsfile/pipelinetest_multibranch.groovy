pipeline {
  agent any

  options {
    buildDiscarder(logRotator(numToKeepStr: '10', daysToKeepStr: '3'))
    retry(3)
    timeout(time: 10, unit: 'HOURS')
  }

  stages {
    stage('environment_orig') {
      steps {
        echo "${env.BUILD_NUMBER}" 
        echo "${env.BRANCH_NAME}"
        echo "${env.BUILD_URL}"
        echo "${env.GIT_BRANCH}"
        echo "test jenkins github trigger"
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
