package net.kaleidos.gradle.plugins

import mariogacia.github.com.emerger.MergeExtensionModuleTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class EmergerPlugin implements Plugin<Project> {

    @Override
    void apply(final Project project) {
        project.tasks.create('mergeExtensionModule', MergeExtensionModuleTask)
    }

}
