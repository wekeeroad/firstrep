pipeline {
  agent any

  options {
    buildDiscarder(logRotator(numToKeepStr: '10', daysToKeepStr: '3'))
  }

  parameters {
    choice(name: "choiceBuildType", choices: "gate\nrelease", description: "Select the type of build")
  }

  stages {
    stage('build gate') {
      when {
        expression { return params.choiceBuildType == "gate" }
      }
      steps {
        echo "build for gate"
        build(
          job: "release_test",
          parameters: [
            string(name: "choiceBuildType", value: "${params.choiceBuildType}")
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
        build(
          job: "release_test",
          parameters: [
            string(name: "choiceBuildType", value: "${params.choiceBuildType}")
          ]
        )
      }
    }
  }
}
