package net.kaleidos.gradle.plugins.emerger

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream

import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j

/**
 * A set of utilities to make it easier to deal with Jar/Aar files
 **/
@Slf4j
@CompileStatic
class Files {

    static final String FILE_SUFFIX_AAR = 'aar'
    static final String FILE_SUFFIX_JAR = 'jar'
    static final String ENTRY_CLASSES_JAR = 'classes.jar'
    static final String ENTRY_EXTENSION_MODULE = 'META-INF/services/org.codehaus.groovy.runtime.ExtensionModule'

    /**
     * Extracts all properties from a given extension module entry located within
     * a file
     *
     * @param file file we want to extract the extension module properties from
     * @return an instance of {@link Properties} with all module properties in case the extension module was found
     * otherwise it will return an empty {@link Properties} instance
     **/
    static Properties extractExtensionModulePropertiesFrom(final File file) {
        return file.name.endsWith(FILE_SUFFIX_AAR) ?
            extractExtensionModulePropertiesFromAar(file) :
            extractExtensionModulePropertiesFromJar(file)
    }

    /**
     * Extracts all properties from a given extension module entry located within
     * a Jar file
     *
     * @param file jar file we want to extract the extension module properties from
     * @return an instance of {@link Properties} with all module properties in case the extension module was found
     * otherwise it will return an empty {@link Properties} instance
     **/
    static Properties extractExtensionModulePropertiesFromJar(final File file) {
        Properties properties = new Properties()

        if (!file?.exists()) {
            log.debug "file $file doesnt exist: skip"

            return properties
        }

        JarFile jarFile = new JarFile(file)
        JarEntry jarEntry = jarFile.getJarEntry(ENTRY_EXTENSION_MODULE)

        if (jarEntry) {
            log.debug "processing $file"

            properties.load(jarFile.getInputStream(jarEntry))
        }

        return properties
    }

    /**
     * Inspects a classes.jar file inside an AAR Android file to look for extension module descriptors
     *
     * @param file aar file we want to extract the extension module properties from
     * @return an instance of {@link Properties} with all module properties in case the extension module was found
     * otherwise it will return an empty {@link Properties} instance
     **/
    @SuppressWarnings('AssignmentInConditional')
    static Properties extractExtensionModulePropertiesFromAar(final File file) {
        Properties properties = new Properties()

        if (!file?.exists()) {
            log.debug "file $file doesnt exist: skip"

            return properties
        }

        ZipFile aarFile = new ZipFile(file)
        ZipEntry aarEntry = aarFile.getEntry(ENTRY_CLASSES_JAR)

        if (aarEntry) {
            log.debug "found jar file within $file"

            ZipInputStream jarInputStream = new ZipInputStream(aarFile.getInputStream(aarEntry))
            ZipEntry nextEntry = null

            while ((nextEntry = jarInputStream.nextEntry)) {
                if (nextEntry.name == ENTRY_EXTENSION_MODULE) {
                    properties.load(new NoCloseInputStream(jarInputStream))
                    break
                }
            }
        }

        return properties
    }

    /**
     * http://stackoverflow.com/questions/5788158/how-to-extract-class-files-from-nested-jar
     **/
    @InheritConstructors
    static class NoCloseInputStream extends FilterInputStream {
        @Override
        void close() throws IOException { }
    }

}