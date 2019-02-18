package org.openmicroscopy.dsl.extensions


import org.gradle.api.Project

class ResourceExtension extends OperationExtension {

    File outputFile

    ResourceExtension(String name, Project project) {
        super(name, project)
    }

    void setOutputFile(String file) {
        outputFile = new File(file)
    }

    void outputFile(String file) {
        setOutputFile(file)
    }

    void outputFile(File file) {
        outputFile = file
    }
}

