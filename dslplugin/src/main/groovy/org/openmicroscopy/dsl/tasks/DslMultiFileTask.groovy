package org.openmicroscopy.dsl.tasks

import ome.dsl.SemanticType
import ome.dsl.velocity.Generator
import ome.dsl.velocity.MultiFileGenerator
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputDirectory

class DslMultiFileTask extends DslBaseTask {

    @Nested
    MultiFileGenerator.FileNameFormatter formatOutput

    /**
     * Set this when you want to generate multiple files
     * Note: also requires setting {@link this.formatOutput}
     */
    @OutputDirectory
    File outputDir

    void formatOutput(Closure formatter) {
        setFormatOutput(formatter)
    }

    void setFormatOutput(Closure formatter) {
        formatOutput = new MultiFileGenerator.FileNameFormatter() {
            @Override
            String format(SemanticType t) {
                return formatter(t)
            }
        }
    }

    void outputDir(Object dir) {
        setOutputDir(dir)
    }

    void setOutputDir(Object dir) {
        this.outputDir = project.file(dir)
    }

    @Override
    protected Generator.Builder createGenerator() {
        return new MultiFileGenerator.Builder()
                .setOutputDir(outputDir)
                .setFileNameFormatter(formatOutput)
    }

}
