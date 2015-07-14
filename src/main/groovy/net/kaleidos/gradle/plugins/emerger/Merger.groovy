package net.kaleidos.gradle.plugins.emerger

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

/**
 * This class has several methods to help creating and modifying
 * files of type {@link Properties}
 *
 **/
@Slf4j
@CompileStatic
class Merger {

    static final String KEY_MODULE_NAME = 'moduleName'
    static final String KEY_VERSION_NAME = 'moduleVersion'
    static final String KEY_EXTENSION_CLASSES = 'extensionClasses'
    static final String KEY_STATIC_EXTENSION_CLASSES = 'staticExtensionClasses'

    /**
     * Creates a new {@link Properties} instance with the module
     * name set
     *
     * @param name the name of the module
     * @return a new {@link Properties} instance
     **/
    static Properties createExtensionModule(final String name) {
        Properties properties = new Properties()
        properties.setProperty(KEY_MODULE_NAME, name)

        return properties
    }

    /**
     * Adds a version entry=value to a {@link Properties} instance
     *
     * @param previous the previous {@link Properties} instance
     * @param version the module version
     * @return a new {@link Properties} instance with previous and new values
     **/
    static Properties setVersion(final Properties previous, final String version) {
        Properties properties = new Properties()
        properties.putAll(previous)
        properties.setProperty(KEY_VERSION_NAME, version)

        return properties
    }

    /**
     * Merges the values of an entry with key 'key' with the value 'value'
     * @param previous the previous {@link Properties} instance
     * @param newValue the value that will be appended to existing value
     * @return a new {@link Properties} instance with previous and new values
     **/
    static Properties mergeByKey(final Properties previous, final String key, final String newValue) {
        Properties properties = new Properties()
        properties.putAll(previous)

        log.debug "inserting [$key:$newValue] into $properties"

        if (newValue) {
            String previousValue = previous.getProperty(key)
            String valueToSet = [previousValue,newValue].findAll().join(',')

            properties.setProperty(key, valueToSet)
        }

        return properties
    }

    /**
     * Merges all key entries specified by parameters from the source to the destination
     *
     * @param source the source properties file
     * @param destination the destination properties file
     * @param keys the key entries you want to merge
     * @return a new instance of {@link Properties} with all merged fields
     **/
    static Properties mergeByKeys(final Properties source, final Properties destination, final String... keys) {
        log.debug "merging keys: $keys from $destination -- to -- $source"

        return (Properties) keys.inject(source) { Properties acc, String k ->
            mergeByKey(acc, k, destination.getProperty(k))
        }
    }

    /**
     * Merges all key entries specified by parameters from the source to the destination
     *
     * @param source the source properties file
     * @param destination the destination properties file
     * @param keys the key entries you want to merge
     * @return a new instance of {@link Properties} with all merged fields
     **/
    static Properties mergeByKeys(final File source, final File destination, final String... keys) {
        Properties src = new Properties()
        Properties dst = new Properties()

        src.load(source.newInputStream())
        dst.load(destination.newInputStream())

        return mergeByKeys(src, dst, keys)
    }

}
