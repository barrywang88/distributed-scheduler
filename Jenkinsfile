pipeline {
  agent {
    node {
      label 'master'
    }

  }
  stages {
    stage('build') {
      steps {
        sh 'sbt clean api/compile api/package api/publishLocal api/publishM2 api/publish service/compile service/package docker'
      }
    }



    stage('pushImage') {
      steps {
      
        sh 'docker login --username readonly --password readonly harbor.today36524.td'
        sh 'docker push  harbor.today36524.td/biz/finance-task_service:`echo $GIT_COMMIT|cut -c1-7`'
        sh "sed -i 's#tag: .*#tag: '`echo $GIT_COMMIT|cut -c1-7`'#g' charts/values.yaml"
      }
    }
    
     stage('sonar') {
          steps {
          sh 'echo $GIT_COMMIT|cut -c1-7 '
           // sh 'sonar-scanner \
//-Dsonar.projectKey=finance-task \
//-Dsonar.sources=. \
//-Dsonar.host.url=http://10.100.110.180:9000 \
//-Dsonar.login=ce2be7592e491c6590a1a8cee1f8d253b3214fe8'
          }
        }



    stage('deploy') {
      steps {
      sh 'echo $GIT_COMMIT|cut -c1-7 '
      sh 'helm  upgrade finance-task ./charts'
      
      }
    }
  }
}
