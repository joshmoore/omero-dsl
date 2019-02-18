package org.openmicroscopy.dsl.utils

import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.util.GradleVersion

class ResourceLoader {

    static def loadFiles(Project project, List<String> fileList) {
        return fileList.collect {
            loadFile(project, it)
        }
    }

    static def loadFiles(Project project, String[] fileList) {
        return fileList.collect {
            loadFile(project, it)
        }
    }

    static def loadFile(Project project, String resFile) {
        if (GradleVersion.current() >= GradleVersion.version('4.8')) {
            def url = IOUtils.getResource("/" + resFile) // getResource(project, resFile)
            if (!url) {
                throw new GradleException("can't find resource file ${resFile}")
            }
            return project.resources.text.fromUri(url.toURI()).asFile()
        } else {
            return loadFileOrExtract(project, resFile)
        }
    }

    static def loadFileOrExtract(Project project, String resourceFile) {
        def result = new File("${project.buildDir}/${resourceFile}")
        // Check if file exists in build directory
        if (!result.exists()) {
            // Copy it to the projects build directory
            def inputStream = IOUtils.getResourceAsStream("/${resourceFile}")
            FileUtils.copyInputStreamToFile(inputStream, result)
        }
        return result
    }

}
