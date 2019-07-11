# Doxygen plugin for Gradle

Doxygen API documentation generator.

## Requirements

This plugin requires the Doxygen binary.

## Usage

### Include plugin

#### Using the pluginsDSL

```groovy
plugins {
  id "com.chmyaf.gradle.plugin.doxygen" version "X.Y.Z"
}
```

#### Using legacy plugin application

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.chmyaf.gradle.plugin:doxygen:+'
    }
}
apply plugin: 'com.chmyaf.gradle.plugin.doxygen'

```

### Configuration

Configuration example:

```groovy
doxygen {
    bin = '/usr/bin/doxygen'
    quiet = false
}
```

#### Application options

|Option|Type|Default value|Description|
|------|----|-------------|-----------|
|bin|String|```doxygen```|Path to Doxygen binary|
|doxyfile|String| |Path to Doxygen config file|


#### Doxyfile options

|Option|Type|Doxyfile parameter|Default value|
|------|----|------------------|-------------|
|extractAnonMethods|boolean|EXTRACT_ANON_METHODS|```true```|
|extractLocalClasses|boolean|EXTRACT_LOCAL_CLASSES|```true```|
|extractLocalMethods|boolean|EXTRACT_LOCAL_METHODS|```true```|
|extractPackage|boolean|EXTRACT_PACKAGE|```true```|
|extractPrivate|boolean|EXTRACT_PRIVATE|```true```|
|extractStatic|boolean|EXTRACT_STATIC|```true```|
|generateLatex|boolean|GENERATE_LATEX|```false```|
|input|ArrayList<String>|INPUT|```${sourceSets.main.allSource.srcDirs}```|
|javadocAutobrief|boolean|JAVADOC_AUTOBRIEF|```true```|
|outputDirectory|String|OUTPUT_DIRECTORY|```build/docs/doxygen```|
|projectName|String|PROJECT_NAME|```${project.name}```|
|projectNumber|String|PROJECT_NUMBER|```${project.version}```|
|quiet|boolean|QUIET|```true```|
|recursive|boolean|RECURSIVE|```true```|
|warnNoParamDoc|boolean|WARN_NO_PARAMDOC|```true```|

#### Other parameters

|Option|Type|Default value|Description|
|------|----|-------------|-----------|
|doxyfileDefault|String|```${project.buildDir}/Doxyfile```|Default path to Doxyfile|

## License

This is Open Source software released under [Apache 2.0 license](./LICENSE).

## Links

* [Apache Linense, Version 2.0](https://apache.org/licenses/LICENSE-2.0)
* [Gradle](https://gradle.org/)
* [Doxygen](http://www.doxygen.nl/)
