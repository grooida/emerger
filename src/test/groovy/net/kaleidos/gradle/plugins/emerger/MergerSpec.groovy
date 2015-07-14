package net.kaleidos.gradle.plugins.emerger

import static net.kaleidos.gradle.plugins.emerger.Merger.KEY_EXTENSION_CLASSES
import static net.kaleidos.gradle.plugins.emerger.Merger.KEY_MODULE_NAME
import static net.kaleidos.gradle.plugins.emerger.Merger.KEY_STATIC_EXTENSION_CLASSES
import static net.kaleidos.gradle.plugins.emerger.Merger.KEY_VERSION_NAME

import spock.lang.Specification

class MergerSpec extends Specification {

    static final FILE_EXTENSION_MODULE_ONE = MergerSpec.getResource('/extension_one.properties')
    static final FILE_EXTENSION_MODULE_TWO = MergerSpec.getResource('/extension_two.properties')

    static final Properties BASE_EXTENSION_MODULE = Merger.setVersion(
        Merger.createExtensionModule('spec-module'),
        '1.0.0-SNAPSHOT'
    )

    void 'create base extension module'() {
        when: 'creating a basic ExtensionModule file'
        Properties properties = BASE_EXTENSION_MODULE

        then: 'we should be able to check its values'
        with(properties) {
            getProperty(KEY_VERSION_NAME) == '1.0.0-SNAPSHOT'
            getProperty(KEY_MODULE_NAME)  == 'spec-module'
        }
    }

    void 'merging a given value with an existing properties file'() {
        given: 'some previous entries'
        def map = [(KEY_EXTENSION_CLASSES): 'com.company.Class1']
        def properties = BASE_EXTENSION_MODULE

        and: 'initializing properties file'
        properties.putAll(map)

        when: 'merging new value with existing one'
        Properties mergedProperties = Merger.mergeByKey(properties, KEY_EXTENSION_CLASSES, 'net.anothercompany.Class2')

        then: 'we should have a new properties file with merged values'
        with(mergedProperties) {
            getProperty(KEY_VERSION_NAME)       == '1.0.0-SNAPSHOT'
            getProperty(KEY_MODULE_NAME)        == 'spec-module'
            getProperty(KEY_EXTENSION_CLASSES)  == 'com.company.Class1,net.anothercompany.Class2'
        }
    }

    void 'merging to property files'() {
        given: 'a basic property file'
        def basic = BASE_EXTENSION_MODULE
        basic.putAll((KEY_EXTENSION_CLASSES): 'net.Class1')

        and: 'another with different values'
        def other = new Properties()
        other.putAll((KEY_EXTENSION_CLASSES): 'com.Class1', (KEY_STATIC_EXTENSION_CLASSES): 'com.static.Class1')

        when: 'merging both'
        Properties merged = Merger.mergeByKeys(basic, other, [KEY_EXTENSION_CLASSES, KEY_STATIC_EXTENSION_CLASSES] as String[])

        then: 'checking values'
        with(merged) {
            getProperty(KEY_VERSION_NAME)               == '1.0.0-SNAPSHOT'
            getProperty(KEY_MODULE_NAME)                == 'spec-module'
            getProperty(KEY_EXTENSION_CLASSES)          == 'net.Class1,com.Class1'
            getProperty(KEY_STATIC_EXTENSION_CLASSES)   == 'com.static.Class1'
        }
    }

    // static Properties mergeByKeys(final File source, final File destination, final String... keys) {
    void 'merging two extension module files'() {
        given: 'one extension module file'
        File source = new File(FILE_EXTENSION_MODULE_ONE.toURI())

        and: 'another extension module file'
        File destination = new File(FILE_EXTENSION_MODULE_TWO.toURI())

        when:
        Properties merged = Merger.mergeByKeys(source, destination, [KEY_EXTENSION_CLASSES, KEY_STATIC_EXTENSION_CLASSES] as String[])

        then:
        with(merged) {
            getProperty(KEY_VERSION_NAME)               == '1.0.0'
            getProperty(KEY_MODULE_NAME)                == 'extension-one'
            getProperty(KEY_EXTENSION_CLASSES)          == 'com.companyone.Class1,com.companytwo.Class1'
            getProperty(KEY_STATIC_EXTENSION_CLASSES)   == 'com.companytwo.static.Class1'
        }
    }

}
