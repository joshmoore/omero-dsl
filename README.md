## OMERO DSL Plugin

The OMERO DSL plugin for Gradle provides a plugin named `dsl`.
This plugin manages the reading of `*.ome.xml` mappings and compilation of `.vm` velocity templates.

### Build

To build the plugin run:
```shell
$./gradlew build
```

To publish the plugin run:
```shell
$./gradlew publishToMavenLocal
```

### Usage

Include the following to the top of your _build.gradle_ file:

```groovy
buildscript {
    repositories {
        jcenter()
        mavenLocal()
    }

    dependencies {
        classpath 'org.openmicroscopy:dslplugin:1.0'
    }
} 

apply plugin: 'org.openmicroscopy.dslplugin'
```

### Configuring plugin

The omero-dsl plugin introduces the `dsl {}` block to define configuration options for
_Velocity_ and _SemanticType_ processing.  

omero-dsl supports two kinds of code generation, source code (multiple files) and resource code (single file).

To generate resource (single) files or source code (multiple files), start by adding a `resource { }` or `code { }` 
block to `dsl {}`. Inside the `resource` or `code` block you can add **one** or **more** inner 
blocks. Inner blocks that you add can be named anything you want and will result in gradle tasks being created with 
the prefix `generate[YOUR BLOCK NAME HERE]`.

Code Example:

```groovy
dsl {
    code {
        java {
            template = "src/main/resources/templates/object.vm"
            outputPath = "src/generated/java"
            omeXmlFiles = project.fileTree(dir: "src/main/resources/mappings", include: '**/*.ome.xml')
            formatOutput = { st ->
                "${st.getPackage()}/${st.getShortname()}.java"
            }
        }
    }
}
```

This will add a task `generatejava` under the group `omero-dsl` in gradle _(to print a list of available gradle
tasks run `./gradlew tasks` in a terminal)_.

Resource Example:

```groovy
dsl {
    resource {
        hibernate {
            template = "src/main/resources/templates/cfg.vm"
            outputFile = "src/generated/resources/hibernate.cfg.xml"
            omeXmlFiles = project.fileTree(dir: "src/main/resources/mappings", include: '**/*.ome.xml')
        }
    }
}
```

This will add a task `generateHibernate` under the group `omero-dsl` in gradle _(to print a list of available gradle
tasks run `./gradlew tasks` in a terminal)_. 

_If you are using Intellij, refresh the _Gradle Toolbar_ and the task will appear in the list once the IDE completes 
its work._

For convenience, you can set the following three properties if templates and generated files reside in similar locations.
For example:

```groovy
dsl {
    // -- Optional ---
    templateFiles fileTree(dir: "src/main/resources/templates", include: '**/*.vm')
    mappingFiles fileTree(dir: "src/main/resources/mappings", include: '**/*.ome.xml')
    outputPath "src/generated"
    // ---------------

    code {
        java {
            template "object.vm" // This will be searched for in 'src/main/resources/templates'
            outputPath "java" // This will output to src/generated/java
            formatOutput { st ->
                "${st.getPackage()}/${st.getShortname()}.java"
            }
        }
    }
    
    resource {
        hibernate {
            template "cfg.vm" // This will be searched for in 'src/main/resources/templates'
            outputFile "resources/hibernate.cfg.xml" // This will output to src/generated/resources/hibernate.cfg.xml
        }
    }
}
```

###### (Optional)
In order to configure the [VelocityEngine](http://velocity.apache.org), add the `velocity` 
extension to your _build.gradle_ file, for example:

```groovy
dsl {
    velocity {
        resource_loader = 'file'
        resource_loader_class = ['file.resource.loader.class': 'org.apache.velocity.runtime.resource.loader.FileResourceLoader']
        file_resource_loader_path = 'src/main/resources/templates'
        file_resource_loader_cache = false
    }
}
```

### `dsl` Properties

| Property      | Type           | Default              | Description                                    |
|---------------|----------------|----------------------|------------------------------------------------|
| omeXmlFiles   | FileCollection | jar'd .ome.xml files | `.ome.xml` mapping files in project            |
| templateFiles | FileCollection |           -          | Velocity `.vm` files in project                |
| outputPath    | File           |           -          | Base output directory to place generated files |

### `code` Properties

| Property name | type           | Default value | Description                                                                      |
|---------------|----------------|---------------|----------------------------------------------------------------------------------|
| outputPath    | File           |       -       | Output directory to generate files in                                            |
| formatOutput  | Closure        |       -       | Closure that receives a `SemanticType` object for tweaking generated files names |
| databaseTypes       | String         |       -       | Path to .properties file describing type for the database engine syntax to use   |                                                   |
| template      | File           |       -       | Velocity file, can be absolute or filename to search through `dsl.templateFiles` |
| omeXmlFiles   | FileCollection |       -       | Collection of `.ome.xml` files, overrides `dsl.mappingFiles` if set              |

### `resource` Properties

| Property name | type           | Default value | Description                                                                      |
|---------------|----------------|---------------|----------------------------------------------------------------------------------|
| outputFile    | File           |       -       | File to generate                                                                 |
| databaseTypes       | String         |       -       | Path to .properties file describing type for the database engine syntax to use   |                                                    |
| template      | File           |       -       | Velocity file, can be absolute or filename to search through `dsl.templateFiles` |
| omeXmlFiles   | FileCollection |       -       | Collection of `.ome.xml` files, overrides `dsl.mappingFiles` if set              |

### Gradle Task

Additional configurations to the `dsl` extension add a new task 

| Type      | Description                                       |
| --------- | ------------------------------------------------- |
| tasks.DslBaseTask   | Generates Java source from ome.xml and .vm files  |

If, like in the examples above, you create configurations `javaModels` and `sqlModels`, these tasks will run
before `compileJava`.

| Task name   | Depends On        |
| ----------- | ----------------- |
| compileJava | processJavaModels |
| compileJava | processSqlModels  |
