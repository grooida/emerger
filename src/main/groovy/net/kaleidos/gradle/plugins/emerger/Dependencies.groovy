package net.kaleidos.gradle.plugins.emerger

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ResolvedArtifact

/**
 * .Set of utilities to access a given {@link Project} dependencies. Specially
 *  jar libraries.
 */
@CompileStatic
class Dependencies {

    /**
     * Gets all project libraries accessible files
     *
     * @param project the project we want the libraries from
     * @return a list of files
     **/
    static File[] getDependencyFilesFrom(final Project project) {
        return project.configurations.collect(Dependencies.&getResolvedArtifactFilesFrom).flatten() as File[]
    }

    /**
     * Gets a set of artifactFiles from a given project configuration
     *
     * @param configuration the current project configuration
     * @return a set of files of the available artifacts from the current project's configuration
     **/
    static List<File> getResolvedArtifactFilesFrom(final Configuration configuration) {
        return configuration.resolvedConfiguration
                .resolvedArtifacts
                .collect { ResolvedArtifact artifact -> artifact.file }
    }

}
