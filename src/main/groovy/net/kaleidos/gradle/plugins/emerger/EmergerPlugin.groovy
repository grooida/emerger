package net.kaleidos.gradle.plugins.emerger

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * This plugins merges extension module descriptors in order to add only one descriptor to the produced apk.
 * It also adds some exclusions to the applied Android project.
 *
 **/
class EmergerPlugin implements Plugin<Project> {

    static final String EXTENSION_MODULE_PATH = "META-INF/services/org.codehaus.groovy.runtime.ExtensionModule"
    static final String MESSAGE_REQUIRED_PLUGIN = 'You must apply the Groovy/Android plugin before using emerger plugin'

    @Override
    void apply(final Project project) {
        Plugin<Project> groovyAndroidPlugin = project.plugins.findPlugin('groovyx.grooid.groovy-android')

        /* It only makes sense along with the Groovy/Android plugin */
        if (!groovyAndroidPlugin) {
            throw new GradleException(MESSAGE_REQUIRED_PLUGIN)
        }

        def variants = getProjectVariantsFrom(project)

        variants.all { variant ->
            String taskName      = "mergeExtensionModules${variant.name.capitalize()}"
            String generated = "$project.buildDir/intermediates/javaResources/$flavorName/$buildType.name/$EXTENSION_MODULE_PATH"
            Task generationTask  = project.task(taskName, type: EmergerTask) {
                outputResult   = project.file(generated)
                dependencies  = Dependencies.getDependencyFilesFrom(project)
            }

            // Makes the magic happen (inserts resources so devs can use it)
            variant.registerJavaGeneratingTask(generationTask, generationTask.outputResult)
        }

    }

    // TODO: this should return List<VariantConfiguration>
    def getProjectVariantsFrom(Project project) {

        def variants = null

        if (project.android.hasProperty('applicationVariants')) {
            variants = project.android.applicationVariants
        }
        else if (project.android.hasProperty('libraryVariants')) {
            variants = project.android.libraryVariants
        }

        return variants
    }

}


















