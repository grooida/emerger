package net.kaleidos.gradle.plugins.emerger

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

/**
 * This tasks merges all Groovy extension module descriptors found in
 * project dependencies and generates a merge result
 *
 */
class EmergerTask extends DefaultTask {

    static final String[] KEYS = ['extensionClasses', 'staticExtensionClasses']
    static final String EMERGER_VERSION = '1.0.0'
    static final String EMERGER_MODULE_NAME = 'emerger'

    /** Files we may get the extension modules from */
    @InputFiles
    Set<File> dependencies

    /** Merged result */
    @OutputFile
    File outputResult

    /**
     * Task trigger
     **/
    @TaskAction
    void mergeExtensions() {
        Properties source       = initExtensionModuleDescriptor()
        Properties properties   = dependencies.inject(source, this.&mergeBetweenSourceAnd)

        properties.store(new FileWriter(outputResult), EMERGER_MODULE_NAME)
    }

    /**
     * Merges the current source properties with the following file available
     *
     * @param source The source
     * @param next The file with possible new values
     * @return the merged result
     **/
    Properties mergeBetweenSourceAnd(final Properties source, final File next) {
        return Merger.mergeByKeys(source, Files.extractExtensionModulePropertiesFrom(next), KEYS)
    }

    /**
     * Creates the base for the merge result property file
     *
     * @return a new instance of {@link Properties} with the default key/value entries
     **/
    Properties initExtensionModuleDescriptor() {
        return use(Merger) {
            Merger
                .createExtensionModule(EMERGER_MODULE_NAME)
                .setVersion(EMERGER_VERSION)
        }
    }

}
