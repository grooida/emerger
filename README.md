#What is Emerger ?

Emerger is a Gradle plugin for Groovy/Android development.

Groovy [extension modules](http://www.groovy-lang.org/metaprogramming.html#_extension_modules) need a descriptor file to
declare the new language extensions added by the developer. When developing Android applications, when the app is packaged
the Android plugin makes sure there are no collisions having files with the same name. The problem is that the Groovy
ExtensionModule descriptors always are named the same way and always are located under the same path.

Emerger merges all project dependencies extension module descriptors in just one file in order to pass the Android
restrictions.

#How to use it ?

At the moment Emerger is not publish in any public repository (e.g Maven Central or Bintray) so in the meantime
you can install it in your local repository, just clone the repo, and in the project directory execute:

<pre>
<code>
./gradlew clean build install
</code>
</pre>

Emerger depends on the [Android/Groovy plugin](https://github.com/groovy/groovy-android-gradle-plugin).

In order to use it in your Android project, first declare the plugin dependency and apply it to your project. Here's an example:

<pre>
<code>
buildscript {
    repositories {
        jcenter()
        mavenLocal()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:1.2.3' // Android tools
        classpath 'org.codehaus.groovy:gradle-groovy-android-plugin:0.3.6' // Android/Groovy
        classpath 'net.kaleidos.gradle.plugins:emerger:0.0.1-SNAPSHOT' // Emerger
    }
}

apply plugin: 'com.android.application'
apply plugin: 'groovyx.grooid.groovy-android'
apply plugin: 'net.kaleidos.gradle.plugins.emerger'
</code>
</pre>

