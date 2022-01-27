def call() {
  stage('Compile Code') {
    sh 'mvn clean compile -e'
  }
  stage('Test Code') {
    sh 'mvn clean test -e'
  }
  stage('Jar Code') {
    sh 'mvn clean package -e'
  }
  stage('Sonar') {
    def scannerHome = tool 'sonar-scanner';
    withSonarQubeEnv('sonar-server') { 
      sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.projectBaseDir=/Users/jaruizf/repos/ejemplo-gradle -Dsonar.sources=src -Dsonar.java.binaries=build"
    }
  }
  stage('UploadNexus') {
    nexusPublisher nexusInstanceId: 'nexus-local', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: '/Users/jaruizf/.jenkins/workspace/ejemplo-gradle_maven-gradle/build/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
  }
}

return this;
