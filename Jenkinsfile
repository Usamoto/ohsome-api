pipeline {
  agent {label 'master'}

  environment {
    RELEASE_REGEX = /^([0-9]+(\.[0-9]+)*)(-(RC|beta-|alpha-)[0-9]+)?$/
    RELEASE_DEPLOY = false
    SNAPSHOT_DEPLOY = false

    VERSION = sh(returnStdout: true, script: 'mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version | grep -Ev "(^\\[|Download\\w+)"').trim()
    MAVEN_TEST_OPTIONS = '-Dport_get=8081 -Dport_post=8082 -Dport_data=8083 -DdbFilePathProperty="--database.db=/opt/data/heidelberg.oshdb"'
  }

  stages {
    stage ('Build and Test') {
      steps {
        script {
          env.MAVEN_HOME = '/usr/share/maven'

          author = sh(returnStdout: true, script: 'git show -s --pretty=%an')
          echo author

          commiti= sh(returnStdout: true, script: 'git log -1')
          echo commiti

          reponame=sh(returnStdout: true, script: 'basename `git remote get-url origin` .git').trim()
          echo reponame

          gittiid=sh(returnStdout: true, script: 'git describe --tags --long  --always').trim()
          echo gittiid

          echo env.BRANCH_NAME
          echo env.BUILD_NUMBER
          echo env.TAG_NAME

          if(!(VERSION ==~ RELEASE_REGEX || VERSION ==~ /.*-SNAPSHOT$/)) {
            echo 'Version:'
            echo VERSION
            error 'The version declaration is invalid. It is neither a release nor a snapshot. Mabe some error while fetching it using maven.'
          }
        }
        script {
          server = Artifactory.server 'HeiGIT Repo'
          rtMaven = Artifactory.newMavenBuild()

          rtMaven.resolver server: server, releaseRepo: 'main', snapshotRepo: 'main'
          rtMaven.deployer server: server, releaseRepo: 'libs-release-local', snapshotRepo: 'libs-snapshot-local'
          rtMaven.deployer.deployArtifacts = false

          withCredentials([string(credentialsId: 'gpg-signing-key-passphrase', variable: 'PASSPHRASE')]) {
            buildInfo = rtMaven.run pom: 'pom.xml', goals: 'clean compile javadoc:jar source:jar install -P sign,git,withDep -Dmaven.repo.local=.m2 $MAVEN_TEST_OPTIONS -Dgpg.passphrase=$PASSPHRASE'
          }
        }
      }
      post {
        failure {
          rocketSend channel: 'jenkinsohsome', emoji: ':sob:' , message: "$reponame-build nr. ${env.BUILD_NUMBER} *failed* on Branch - ${env.BRANCH_NAME}  (<${env.BUILD_URL}|Open Build in Jenkins>). Latest commit from  ${author}. Review the code!" , rawMessage: true
        }
      }
    }

    stage ('Deploy Snapshot') {
      when {
        expression {
          return env.BRANCH_NAME ==~ /(^master$)/ && VERSION ==~ /.*-SNAPSHOT$/
        }
      }
      steps {
        script {
          rtMaven.deployer.deployArtifacts buildInfo
          server.publishBuildInfo buildInfo
          SNAPSHOT_DEPLOY = true
        }
      }
      post {
        failure {
          rocketSend channel: 'jenkinsohsome', message: "Deployment of $reponame-build nr. ${env.BUILD_NUMBER} *failed* on Branch - ${env.BRANCH_NAME}  (<${env.BUILD_URL}|Open Build in Jenkins>). Latest commit from  ${author}. Is Artifactory running?" , rawMessage: true
        }
      }
    }

    stage ('Deploy Release') {
      when {
        expression {
          return VERSION ==~ RELEASE_REGEX && env.TAG_NAME ==~ RELEASE_REGEX
        }
      }
      steps {
        script {
          rtMaven.deployer.deployArtifacts buildInfo
          server.publishBuildInfo buildInfo
          RELEASE_DEPLOY = true
        }
        withCredentials([
            file(credentialsId: 'ossrh-settings', variable: 'settingsFile'),
            string(credentialsId: 'gpg-signing-key-passphrase', variable: 'PASSPHRASE')
        ]) {
          // copy of the above build, since "deploy" does rebuild the packages, without withDep profile
          sh 'mvn -s $settingsFile javadoc:jar source:jar deploy -P sign,git,deploy-central -Dmaven.repo.local=.m2 -Dgpg.passphrase=$PASSPHRASE -DskipTests=true'
        }
      }
      post {
        failure {
          rocketSend channel: 'jenkinsohsome', message: "Deployment of $reponame-build nr. ${env.BUILD_NUMBER} *failed* on Branch - ${env.BRANCH_NAME}  (<${env.BUILD_URL}|Open Build in Jenkins>). Latest commit from  ${author}. Is Artifactory running?" , rawMessage: true
        }
      }
    }

    stage ('Publish Javadoc') {
      when {
        anyOf {
          equals expected: true, actual: RELEASE_DEPLOY
          equals expected: true, actual: SNAPSHOT_DEPLOY
        }
      }
      steps {
        script {
          // load dependencies to artifactory
          rtMaven.run pom: 'pom.xml', goals: 'org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version -Dmaven.repo.local=.m2'

          javadc_dir="/srv/javadoc/java/" + reponame + "/" + VERSION + "/"
          echo javadc_dir

          rtMaven.run pom: 'pom.xml', goals: 'clean javadoc:javadoc -Dadditionalparam=-Xdoclint:none -Dmaven.repo.local=.m2'
          sh "echo $javadc_dir"
          // make sure jenkins uses bash not dash!
          sh "mkdir -p $javadc_dir && rm -Rf $javadc_dir* && find . -path '*/target/site/apidocs' -exec cp -R --parents {} $javadc_dir \\; && find $javadc_dir -path '*/target/site/apidocs' | while read line; do echo \$line; neu=\${line/target\\/site\\/apidocs/} ;  mv \$line/* \$neu ; done && find $javadc_dir -type d -empty -delete"
        }
      }
      post {
        failure {
          rocketSend channel: 'jenkinsohsome', message: "Deployment of javadoc $reponame-build nr. ${env.BUILD_NUMBER} *failed* on Branch - ${env.BRANCH_NAME}  (<${env.BUILD_URL}|Open Build in Jenkins>). Latest commit from  ${author}." , rawMessage: true
        }
      }
    }

    // START CUSTOM ohsome API
    stage ('Publish API Docs') {
      when {
        expression {
          return env.BRANCH_NAME ==~ /(^[0-9]+$)|(^(([0-9]+)(\.))+([0-9]+)?$)|(^master$)/
        }
      }
      steps {
        script {
          DOC_RELEASE_REGEX = /^([0-9]+(\.[0-9]+)*)$/
          DOCS_DEPLOYMENT = "development"
          API_DOCS_PATH = "development"
          if(VERSION ==~ DOC_RELEASE_REGEX) {
            DOCS_DEPLOYMENT = "release"
            API_DOCS_PATH = sh(returnStdout: true, script: 'cd docs && python3 get_pom_metadata.py | awk \'/^Path:/{ print $2 }\'').trim()
          }

          publish_dir = "/srv/javadoc/" + reponame + "/" + API_DOCS_PATH + "/"
          venv_dir = sh(returnStdout: true, script: 'mktemp -d --suffix .sphinx-docs').trim() + "/venv"

          if (!fileExists("$venv_dir")) {
            sh "python3 -m venv $venv_dir"
          }

          sh """
            source $venv_dir/bin/activate
            cd docs
            python3 -m pip install -U pip
            python3 -m pip install -r requirements.txt
            DOCS_DEPLOYMENT=${DOCS_DEPLOYMENT} make clean html
          """
          sh "mkdir -p $publish_dir && rm -rf $publish_dir* && cp -r docs/_build/html/* $publish_dir"
          sh "rm -rf $venv_dir"
        }
      }
      post {
        failure {
          sh "rm -rf $venv_dir"
          rocketSend channel: 'jenkinsohsome', message: "Deployment of api docs $reponame-build nr. ${env.BUILD_NUMBER} *failed* on Branch - ${env.BRANCH_NAME}  (<${env.BUILD_URL}|Open Build in Jenkins>). Latest commit from  ${author}." , rawMessage: true
        }
      }
    }
    // END CUSTOM ohsome API

    stage ('Reports and Statistics') {
      steps {
        script {
          // jacoco
          report_dir="/srv/reports/" + reponame + "/" + VERSION + "_"  + env.BRANCH_NAME + "/" +  env.BUILD_NUMBER + "_" +gittiid+"/jacoco/"

          rtMaven.run pom: 'pom.xml', goals: 'clean verify -Pjacoco -Dmaven.repo.local=.m2 $MAVEN_TEST_OPTIONS'
          jacoco(
              execPattern      : '**/target/jacoco.exec',
              classPattern     : '**/target/classes',
              sourcePattern    : '**/src/main/java',
              inclusionPattern : '/org/heigit/**'
          )
          sh "mkdir -p $report_dir && rm -Rf $report_dir* && find . -path '*/target/site/jacoco' -exec cp -R --parents {} $report_dir \\; && find $report_dir -path '*/target/site/jacoco' | while read line; do echo \$line; neu=\${line/target\\/site\\/jacoco/} ;  mv \$line/* \$neu ; done && find $report_dir -type d -empty -delete"

          // infer
          if(env.BRANCH_NAME ==~ /(^master$)/) {
            report_dir="/srv/reports/" + reponame + "/" + VERSION + "_"  + env.BRANCH_NAME + "/" +  env.BUILD_NUMBER + "_" +gittiid+"/infer/"
            sh "mvn clean"
            sh "infer run --pmd-xml -r -- mvn compile"
            sh "mkdir -p $report_dir && rm -Rf $report_dir* && cp -R ./infer-out/* $report_dir"
          }

          // warnings plugin
          rtMaven.run pom: 'pom.xml', goals: '--batch-mode -V -e compile checkstyle:checkstyle pmd:pmd pmd:cpd com.github.spotbugs:spotbugs-maven-plugin:3.1.7:spotbugs -Dmaven.repo.local=.m2'

          recordIssues enabledForFailure: true, tools: [mavenConsole(),  java(), javaDoc()]
          recordIssues enabledForFailure: true, tool: checkStyle()
          recordIssues enabledForFailure: true, tool: spotBugs()
          recordIssues enabledForFailure: true, tool: cpd(pattern: '**/target/cpd.xml')
          recordIssues enabledForFailure: true, tool: pmdParser(pattern: '**/target/pmd.xml')
          recordIssues enabledForFailure: true, tool: pmdParser(pattern: '**/infer-out/report.xml', id: 'infer')
        }
      }
      post {
        failure {
          rocketSend channel: 'jenkinsohsome', message: "Reporting of $reponame-build nr. ${env.BUILD_NUMBER} *failed* on Branch - ${env.BRANCH_NAME}  (<${env.BUILD_URL}|Open Build in Jenkins>). Latest commit from  ${author}." , rawMessage: true
        }
      }
    }

    stage ('Check Dependencies') {
      when {
        expression {
          if(currentBuild.number > 1) {
            monthpre=new Date(currentBuild.previousBuild.rawBuild.getStartTimeInMillis())[Calendar.MONTH]
            echo monthpre.toString()
            monthnow=new Date(currentBuild.rawBuild.getStartTimeInMillis())[Calendar.MONTH]
            echo monthnow.toString()
            return monthpre!=monthnow
          }
          return false
        }
      }
      steps {
        script {
          updatenotify=sh(returnStdout: true, script: 'mvn versions:display-dependency-updates | grep -Pzo "(?s)The following dependencies.*\\n.* \\n"').trim()
          echo updatenotify
        }
        rocketSend channel: 'jenkinsohsome', emoji: ':wave:' , message: "You might have updates in your dependecies: ${updatenotify}" , rawMessage: true
      }
      post {
        failure {
          rocketSend channel: 'jenkinsohsome', emoji: ':disappointed:' , message: "Checking for updates in $reponame-build nr. ${env.BUILD_NUMBER} *failed* on Branch - ${env.BRANCH_NAME}  (<${env.BUILD_URL}|Open Build in Jenkins>). Latest commit from  ${author}." , rawMessage: true
        }
      }
    }


    stage ('Encourage') {
      when {
        expression {
          if(currentBuild.number > 1) {
            datepre=new Date(currentBuild.previousBuild.rawBuild.getStartTimeInMillis()).clearTime()
            echo datepre.format( 'yyyyMMdd' )
            datenow=new Date(currentBuild.rawBuild.getStartTimeInMillis()).clearTime()
            echo datenow.format( 'yyyyMMdd' )
            return datepre.numberAwareCompareTo(datenow)<0
          }
          return false
        }
      }
      steps {
        rocketSend channel: 'jenkinsohsome', message: "Hey, this is just your daily notice that Jenkins is still working for you on $reponame Branch ${env.BRANCH_NAME}! Happy and for free! Keep it up!" , rawMessage: true
      }
      post {
        failure {
          rocketSend channel: 'jenkinsohsome', emoji: ':wink:' , message: "Reporting of $reponame-build nr. ${env.BUILD_NUMBER} *failed* on Branch - ${env.BRANCH_NAME}  (<${env.BUILD_URL}|Open Build in Jenkins>). Latest commit from  ${author}." , rawMessage: true
        }
      }
    }

    stage ('Report Status Change') {
      when {
        expression {
          return ((currentBuild.number > 1) && (currentBuild.getPreviousBuild().result == 'FAILURE'))
        }
      }
      steps {
        rocketSend channel: 'jenkinsohsome', message: "We had some problems, but we are BACK TO NORMAL! Nice debugging: $reponame-build-nr. ${env.BUILD_NUMBER} *succeeded* on Branch - ${env.BRANCH_NAME}  (<${env.BUILD_URL}|Open Build in Jenkins>). Latest commit from  ${author}." , rawMessage: true
      }
      post {
        failure {
          rocketSend channel: 'jenkinsohsome', message: "Reporting of $reponame-build nr. ${env.BUILD_NUMBER} *failed* on Branch - ${env.BRANCH_NAME}  (<${env.BUILD_URL}|Open Build in Jenkins>). Latest commit from  ${author}." , rawMessage: true
        }
      }
    }
  }
}

