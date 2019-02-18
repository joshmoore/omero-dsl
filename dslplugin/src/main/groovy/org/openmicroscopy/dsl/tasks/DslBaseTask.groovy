package org.openmicroscopy.dsl.tasks


import ome.dsl.velocity.Generator
import org.apache.velocity.app.VelocityEngine
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.FileCollection
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

abstract class DslBaseTask extends DefaultTask {

    private static final def Log = Logging.getLogger(DslBaseTask)

    @InputFiles
    FileCollection omeXmlFiles = project.files()

    @InputFiles
    FileCollection databaseTypes = project.files()

    @Input
    String databaseType

    @InputFile
    File template

    @Input
    @Optional
    Properties velocityProperties = new Properties()

    @TaskAction
    void apply() {
        VelocityEngine ve = new VelocityEngine(velocityProperties)

        // Build our file generator
        def builder = createGenerator()
        builder.velocityEngine = ve
        builder.profile = databaseType
        builder.template = template
        builder.databaseTypes = getDatabaseTypeProperties()
        builder.omeXmlFiles = getOmeXmlFiles()
        builder.build().call()
    }

    abstract protected Generator.Builder createGenerator()

    void omeXmlFiles(Object... paths) {
        omeXmlFiles = omeXmlFiles + project.files(paths)
    }

    void setOmeXmlFiles(Object... paths) {
        omeXmlFiles = project.files(paths)
    }

    void databaseTypes(Object... paths) {
        this.databaseTypes = databaseTypes + project.files(paths)
    }

    void setDatabaseTypes(Object... paths) {
        this.databaseTypes = project.files(paths)
    }

    void databaseType(String type) {
        this.databaseType = type
    }

    Properties getDatabaseTypeProperties() {
        Properties databaseTypeProps = new Properties()
        File databaseTypeFile = getDatabaseTypes()
        if (!databaseTypeFile) {
            throw new GradleException("Can't find ${databaseType}-types.properties")
        }
        databaseTypeFile.withInputStream { databaseTypeProps.load(it) }
        return databaseTypeProps
    }

    File getDatabaseTypes() {
        return getFilesInCollection(databaseTypes, "-types.properties").find {
            it.name == "$databaseType-types.properties"
        }
    }

    List<File> getOmeXmlFiles() {
        return getFilesInCollection(omeXmlFiles, ".ome.xml")
    }

    List<File> getFilesInCollection(FileCollection collection, String extension) {
        def directories = collection.findAll {
            it.isDirectory()
        }

        def files = collection.findAll {
            it.isFile() && it.name.endsWith("$extension")
        }

        files = files + directories.collectMany {
            project.fileTree(dir: it, include: "**/*$extension").files
        }

        return files
    }

}
