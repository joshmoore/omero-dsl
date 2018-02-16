package com.openmicroscopy

import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.runtime.RuntimeConstants
import org.apache.velocity.runtime.resource.loader.FileResourceLoader
import org.gradle.api.Plugin
import org.gradle.api.Project

class DslPlugin implements Plugin<Project> {

    VelocityExtension velocityExtension

    @Override
    void apply(Project project) {
        // Create velocity extensions
        velocityExtension = project.extensions.create('velocity', VelocityExtension)

        // NamedDomainObjectContainer<DslOperationJava> dslOperationContainer = project.container(DslOperationJava.class)
        def dslOperationContainer = project.container(DslOperationJava.class)

        // Add the container instance to our project
        // with the name ome.
        project.extensions.add("dsljava", dslOperationContainer)

        dslOperationContainer.all({ DslOperationJava operation ->
            def env = operation.getName()
            def capitalizedName = env.substring(0, 1).toUpperCase() + env.substring(1)
            def taskName = "process" + capitalizedName
            def dslTask = project.tasks.create(taskName, DslTask)

            project.afterEvaluate {
                dslTask.mapFilesPath = operation.mapFilesPath
                dslTask.velocityFile = operation.velocityFile
                dslTask.outputPath = operation.outputPath
                dslTask.velocityEngine = configureVelocity()
            }
        })
    }

    VelocityEngine configureVelocity() {
        def velocity = new VelocityEngine()
        velocity.setProperty(
                RuntimeConstants.RESOURCE_LOADER,
                velocityExtension.resource_loader)
        velocity.setProperty(
                RuntimeConstants.FILE_RESOURCE_LOADER_PATH,
                velocityExtension.file_resource_loader_path)
        velocity.setProperty(
                "file.resource.loader.class",
                FileResourceLoader.class.getName())
        velocity.setProperty(
                RuntimeConstants.FILE_RESOURCE_LOADER_CACHE,
                velocityExtension.file_resource_loader_cache)
        velocity.init()

        return velocity
    }

}
