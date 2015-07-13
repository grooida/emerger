package net.kaleidos.gradle.plugins

import org.gradle.api.DefaultTask
import org.gradle.api.resources.ResourceHandler
import org.gradle.api.tasks.TaskAction

class MergeExtensionModuleTask extends DefaultTask {

    @TaskAction
    void executeTask() {
        println project.resources.each { ResourceHandler handler ->
            println handler.text
        }
    }

}
