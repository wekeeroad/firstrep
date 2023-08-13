pipeline {
  agent any

  //triggers {
  //  cron('*/2 * * * *')
  //}
  options {
    buildDiscarder(logRotator(numToKeepStr: '10', daysToKeepStr: '3'))
    retry(3)
    timeout(time: 10, unit: 'HOURS')
  }

  environment {
    EE_TEST = 'This is a env test at pipeline'
    GOPATH = '/home/ubuntu/go_test'
  }

  parameters {
    string(name: "branch", defaultValue: "master", description: "The branch of target repo")
    booleanParam(name: "ifBuild", defaultValue: true, description: "Ensure if build")
    text(name: "textOpenSource", defaultValue: "", description: "Type in information about open source")
    choice(name: "choiceBuildType", choices: "gate\nrelease", description: "Select the type of build")
    password(name: "passwordOfRepo", defaultValue: "secret", description: "Type in the password to download repo")
  }
  
  tools {
    go 'go1.21.0'
  }
  
  stages {
    stage('environment_orig') {
      steps {
        echo "${env.BUILD_NUMBER}" 
        echo "${env.BRANCH_NAME}"
        echo "${env.BUILD_URL}"
        echo "${env.GIT_BRANCH}"
      }
    }
    stage('environment_test') {
      environment {
        SS_TEST = 'This is a env test at stage'
      }
      steps {
        echo "${env.ENVTEST}"
        echo "${env.EE_TEST}"
        echo "${env.SS_TEST}"
      }
    }
    stage('params') {
      steps {
        echo "${params.branch}"
        echo "${params.ifBuild}"
        echo "${params.textOpenSource}"
        echo "${params.choiceBuildType}"
        echo "${params.passwordOfRepo}"
      }
    }
    stage('Build') {
      steps {
        echo 'Hello world'
        echo "${env.ENVTEST}"
        echo "${ENVTEST}"
        dir('/home/ubuntu/go_test') {
          sh "go run hello_world.go"
        }
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
