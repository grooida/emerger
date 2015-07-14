package net.kaleidos.gradle.plugins.emerger

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class DependenciesSpec extends Specification {

    void 'getting dependencies from a given configuration'() {
        given: 'a simple project'
        Project project = ProjectBuilder.builder().build()

        and: 'adding a couple of dependencies'
        project.with {
            repositories {
                jcenter()
            }
            configurations {
                compile
            }
            dependencies {
                compile 'org.jodd:jodd-petite:3.6.6'
            }
        }

        when: 'asking for project dependencies files'
        List<File> files = Dependencies.getResolvedArtifactFilesFrom(project.configurations.find())

        then: 'we should get them'
        files
    }

    void 'getting dependencies from a project'() {
        given: 'a simple project'
        Project project = ProjectBuilder.builder().build()

        and: 'adding a couple of dependencies'
        project.with {
            repositories {
                jcenter()
            }
            configurations {
                compile
            }
            dependencies {
                compile 'org.jodd:jodd-petite:3.6.6'
            }
        }

        when: 'asking for project dependencies files'
        List<File> files = Dependencies.getDependencyFilesFrom(project)

        then: 'we should get them'
        files
    }

}
