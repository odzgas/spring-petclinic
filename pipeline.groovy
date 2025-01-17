pipeline {
    agent any
//    triggers { pollSCM('* * * * *') }
    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "MAVEN3"
    }

    stages {
        stage('Checkout') {
            steps {
                // Get some code from a GitHub repository
                git branch: 'main', url: 'https://github.com/odzgas/spring-petclinic.git'
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean package'
            }

//            post {
//                // If Maven was able to run the tests, even if some of the test
//                // failed, record the test results and archive the jar file.
//                always {
//                    junit '**/target/surefire-reports/TEST-*.xml'
//                    archiveArtifacts 'target/*.jar'
//                }
//
//            }
        }
        stage('Email') {
            steps {
                emailext subject: 'Job \'${JOB_NAME}\'(${BUILD_NUMBER}) is waiting for input\n',
                        body: 'Please go to $(BUILD_URL} and verify the build',
                        attachLog: true,
                        compressLog: true,
                        to: 'test@jenkins',
                        recipientProviders: [upstreamDevelopers(), requestor()]
            }
        }

    }
}
