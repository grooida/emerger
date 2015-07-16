#What is Emerger ?

Emerger is a Gradle plugin for Groovy/Android development.

Groovy [extension modules](http://www.groovy-lang.org/metaprogramming.html#_extension_modules) need a descriptor file to
declare the new language extensions added by the developer to the existing types.

When developing Android applications, when the app is packaged the Android plugin makes sure there are no collisions
having files with the same name. The problem is that the Groovy ExtensionModule descriptors always are named the same
way and always are located under the same path.

*Emerger* merges all project dependencies extension module descriptors in just one file in order to pass the Android
restrictions.

The same happens with Groovy [global transformations] (http://docs.groovy-lang.org/latest/html/documentation/#transforms-global).
But this time we don't care about packaging these files, because they're only used during compilation time.
Emerger adds a rule to exclude those files from packaging.

#How to use it ?

Emerger depends on the [Android/Groovy plugin](https://github.com/groovy/groovy-android-gradle-plugin). So make sure
you have applied that plugin before going any further.

Then declare the *Emerger* plugin dependency and apply it to your project. Here's an example:

<pre>
<code>
buildscript {
    repositories {
        jcenter()
         maven {
            url  "http://dl.bintray.com/kaleidos/maven"
         }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:1.2.3' // Android tools
        classpath 'org.codehaus.groovy:gradle-groovy-android-plugin:0.3.6' // Android/Groovy
        classpath 'net.kaleidos.gradle.plugins:emerger:0.0.1' // Emerger
    }
}

apply plugin: 'com.android.application'
apply plugin: 'groovyx.grooid.groovy-android'
apply plugin: 'net.kaleidos.gradle.plugins.emerger'
</code>
</pre>

Now you can use any Groovy library dependency in your Android app without worrying about extension module
or global transformation collisions.

