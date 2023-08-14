pipeline {
  agent any

  options {
    buildDiscarder(logRotator(numToKeepStr: '10', daysToKeepStr: '3'))
  }

  environment {
    approvalMap = ''
  }

  parameters {
    choice(name: "choiceBuildType", choices: "gate\nrelease", description: "Select the type of build")
  }
  
  stages {
    stage('input parameters') {
      steps {
        timeout(time: 1, unit: 'MINUTES') {
          script {
            approvalMap = input(
              message: 'Type in some parameters',
              ok: 'Confirm',
              parameters: [
                string(name: "branch", defaultValue: "master", description: "The branch of target repo"),
                booleanParam(name: "ifBuild", defaultValue: true, description: "Ensure if build")
              ],
              submitter: 'weikelu',
              submitterParameter: 'APPROVER'
            )
          }
        }
      }
    }
    stage('build gate') {
      when {
        expression { return params.choiceBuildType == "gate" }
      }
      steps {
        echo "build for gate"
        echo "approval by ${approvalMap['APPROVER']}"
        build(
          job: "release_test",
          parameters: [
            string(name: "choiceBuildType", value: "${params.choiceBuildType}"),
            string(name: "branch", value: "${approvalMap['branch']}"),
            booleanParam(name: "ifBuild", value: ${approvalMap['ifBuild']})
          ]
        )
      }
    }
    stage('build release') {
      when {
        expression { return params.choiceBuildType == "release" }
      }
      steps {
        echo "build for release"
        echo "approval by ${approvalMap['APPROVER']}"
        build(
          job: "release_test",
          parameters: [
            string(name: "choiceBuildType", value: "${params.choiceBuildType}"),
            string(name: "branch", value: ${approvalMap['branch']}),
            booleanParam(name: "ifBuild", value: ${approvalMap['ifBuild']})
          ]
        )
      }
    }
  }
}
