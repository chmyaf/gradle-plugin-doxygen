# Doxygen plugin for Gradle

Doxygen API documentation generator.

## Requirements

This plugin requires the Doxygen binary.

## Usage

### Include plugin

```build.gradle```:

```groovy
buildscript {
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
    bin = '/opt/tools/bin/doxygen'
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
|generateLatex|boolean|GENERATE_LATEX|```false```|
|input|ArrayList<String>|INPUT|```${sourceSets.main.allSource.srcDirs}```|
|outputDirectory|String|OUTPUT_DIRECTORY|```build/docs/doxygen```|
|projectName|String|PROJECT_NAME|```${project.name}```|
|projectNumber|String|PROJECT_NUMBER|```${project.version}```|
|quiet|boolean|QUIET|```true```|
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