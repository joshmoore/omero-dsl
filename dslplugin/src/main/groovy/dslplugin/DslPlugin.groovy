package dslplugin

import org.apache.velocity.runtime.resource.loader.FileResourceLoader
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

class DslPlugin implements Plugin<Project> {

    DslPluginBase basePlugin

    @Override
    void apply(Project project) {
        // Apply configuration base plugin
        basePlugin = project.plugins.apply(DslPluginBase)

        // Now work on convention
        // applyDefaultConfigs(project)
    }

    /**
     * Sets up default values for Velocity configuration
     * @param project
     */
    void applyDefaultConfigs(final Project project) {
        project.afterEvaluate {
            // Set some defaults for velocity
            VelocityExtension ve = project.dsl.velocity
            ve.resourceLoader = "file"
            ve.resourceLoaderClass = ["file.resource.loader.class": FileResourceLoader.class.getName()]
            ve.fileResourceLoaderCache = false
            ve.loggerClassName = project.getLogger().getClass().getName()
            if (project.plugins.hasPlugin(JavaPlugin)) {
                ve.fileResourceLoaderPath = "${project.sourceSets.main.output.resourcesDir}"
            } else {
                ve.fileResourceLoaderPath = "${project.projectDir}/src/main/resources"
            }

            // Convert velocity extension to Properties type
            // basePlugin.velocityEngine.setProperties(ve.properties.get())

            // Assign default velocity config to each dsl task
            basePlugin.dslTasks.each { task ->
                if (project.plugins.hasPlugin(JavaPlugin)) {
                    // Ensure the DslTask runs before compileJava
                    project.tasks.getByPath("compileJava").dependsOn(task)
                }
            }
        }
    }
}

