package org.openmicroscopy.dsl

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.openmicroscopy.dsl.extensions.DslExtension
import spock.lang.Specification


class DslExtensionTest extends Specification {

    @Rule
    TemporaryFolder testProjectDir = new TemporaryFolder()
    Project project
    DslExtension dsl

    def setup() {
        project = ProjectBuilder.builder().withProjectDir(testProjectDir.root).build()
        dsl = project.extensions.create('dslExt', DslExtension, project)
    }

    def "OutputPath is absolute"() {
        when:
        dsl.outputPath "someFolder"

        then:
        dsl.outputDir.isAbsolute()
    }

    def "Templates support multiple dirs"() {
        given:
        def folderA = createFilesInFolder(testProjectDir.newFolder("A"))
        def folderB = createFilesInFolder(testProjectDir.newFolder("B"))

        when:
        dsl.templateFiles project.fileTree(dir: folderA, include: '*.file')
        dsl.templateFiles project.fileTree(dir: folderB, include: '*.file')

        then:
        dsl.templateFiles.size() == 6
    }

    def "OmeXmlFiles support multiple dirs"() {
        given:
        def folderA = createFilesInFolder(testProjectDir.newFolder("A"))
        def folderB = createFilesInFolder(testProjectDir.newFolder("B"))

        when:
        dsl.omeXmlFiles project.fileTree(dir: folderA, include: '*.file')
        dsl.omeXmlFiles project.fileTree(dir: folderB, include: '*.file')

        then:
        dsl.omeXmlFiles.size() == 6
    }

    // Create fake ome.xml files
    def createFilesInFolder(File folder) {
        createFile(folder, "fileA.file")
        createFile(folder, "fileB.file")
        createFile(folder, "fileC.file")
        return folder
    }

    def createFile(File folder, String fileName) {
        def file = new File(folder, fileName)
        if (!file.createNewFile()) {
            throw new IOException("File already exists")
        }
        return file
    }

}
