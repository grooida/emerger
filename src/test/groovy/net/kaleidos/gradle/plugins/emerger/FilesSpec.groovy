package net.kaleidos.gradle.plugins.emerger

import static net.kaleidos.gradle.plugins.emerger.Merger.KEY_EXTENSION_CLASSES
import static net.kaleidos.gradle.plugins.emerger.Merger.KEY_MODULE_NAME
import static net.kaleidos.gradle.plugins.emerger.Merger.KEY_STATIC_EXTENSION_CLASSES
import static net.kaleidos.gradle.plugins.emerger.Merger.KEY_VERSION_NAME

import spock.lang.Specification

class FilesSpec extends Specification {

    static final URL SAMPLE_FILE_JAR = FilesSpec.getResource('/sample.jar')
    static final URL SAMPLE_FILE_AAR = FilesSpec.getResource('/sample.aar')

    void 'extract extension module properties from JAR file'() {
        when: 'getting properties from a given file'
        File jarFile = new File(SAMPLE_FILE_JAR.toURI())
        Properties properties = Files.extractExtensionModulePropertiesFromJar(jarFile)

        and: 'making sure it has data'
        assert properties.stringPropertyNames()

        then: 'we should be able to extract all values from it'
        with(properties) {
            getProperty(KEY_VERSION_NAME) == '1.0'
            getProperty(KEY_MODULE_NAME) == 'widgets'
            getProperty(KEY_EXTENSION_CLASSES) == 'net.sample.Class2,net.sample.Class1'
            getProperty(KEY_STATIC_EXTENSION_CLASSES) == 'net.sample.static.Class2,net.sample.static.Class1'
        }
    }

    void 'extract extension module properties from AAR file'() {
        when: 'getting properties from a given file'
        File aarFile = new File(SAMPLE_FILE_AAR.toURI())
        Properties properties = Files.extractExtensionModulePropertiesFromAar(aarFile)

        then: 'we should be able to extract all values from it'
        with(properties) {
            getProperty(KEY_VERSION_NAME) == '1.0'
            getProperty(KEY_MODULE_NAME) == 'widgets'
            getProperty(KEY_EXTENSION_CLASSES) == 'net.sample.Class2,net.sample.Class1'
            getProperty(KEY_STATIC_EXTENSION_CLASSES) == 'net.sample.static.Class2,net.sample.static.Class1'
        }
    }

    void 'extract extension module properties from different type of files'() {
        when: 'getting properties from a given file'
        File aarFile = new File(fileURL.toURI())
        Properties properties = Files.extractExtensionModulePropertiesFrom(aarFile)

        then: 'we should be able to extract all values from it'
        with(properties) {
            getProperty(KEY_VERSION_NAME) == '1.0'
            getProperty(KEY_MODULE_NAME) == 'widgets'
            getProperty(KEY_EXTENSION_CLASSES) == 'net.sample.Class2,net.sample.Class1'
            getProperty(KEY_STATIC_EXTENSION_CLASSES) == 'net.sample.static.Class2,net.sample.static.Class1'
        }

        where: 'possible type of files are'
        fileURL << [SAMPLE_FILE_AAR, SAMPLE_FILE_JAR]
    }

}