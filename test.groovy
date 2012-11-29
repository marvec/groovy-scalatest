@Grapes([
  @Grab(group='org.scala-lang', module='scala-library', version='2.9.2'),
  @Grab(group='org.scalatest', module='scalatest_2.9.1', version='1.8'),
  @Grab(group='org.scala-lang', module='scala-compiler', version='2.9.2'),
//  @Grab(group='org.codehaus.groovy', module='groovy-all', version='2.0.1'),
  @GrabConfig(systemClassLoader = true)
])

import groovy.grape.Grape
import org.scalatest.tools.Runner
import scala.tools.ant.*

def url = this.class.protectionDomain.codeSource.location
def toolDir = new File(url.toURI()).getParentFile()

// configure classpath
def cl = getClass().getClassLoader()
cl.addClasspath(new File(toolDir, 'src').absolutePath)
cl.addClasspath(new File(toolDir, 'test').absolutePath)
cl.addClasspath(new File(toolDir, 'resources').absolutePath)

def ant = new AntBuilder()

//def scalac = new scala.tools.ant.Scalac()
ant.taskdef(name: 'groovyc', classname: 'org.codehaus.groovy.ant.Groovyc')
ant.taskdef(resource: 'scala/tools/ant/antlib.xml')
ant.taskdef(name: 'scalatest', classname: 'org.scalatest.tools.ScalaTestAntTask')

ant.mkdir(dir: 'target/test-classes')
ant.mkdir(dir: 'target/classes')

ant.groovyc(srcdir: 'src', destdir: 'target/classes')

ant.scalac(srcdir: 'test', destdir: 'target/test-classes', fork: false) {
  classpath {
    pathelement(location: 'target/classes')
    fileset(dir: System.getenv('GROOVY_HOME') + '/embeddable') {
      include(name: 'groovy-all-*.jar')
      exclude(name: '*indy*')
    }
    Grape.resolve(group: 'org.scala-lang', module: 'scala-library', version:'2.9.2').each {
      pathelement(location: new File(it).absolutePath)
    }
  }
}

ant.scalatest(suite: 'org.jboss.qa.test.SuperUtilSuite') {
  runpath {
    pathelement(location: 'target/classes')
    pathelement(location: 'target/test-classes')
    fileset(dir: System.getenv('GROOVY_HOME') + '/embeddable') {
      include(name: 'groovy-all-*.jar')
      exclude(name: '*indy*')
    }
    Grape.resolve(group: 'org.scala-lang', module: 'scala-library', version:'2.9.2').each {
      pathelement(location: new File(it).absolutePath)
    }
  }
}

