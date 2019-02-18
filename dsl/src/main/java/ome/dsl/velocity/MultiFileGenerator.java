package ome.dsl.velocity;

import ome.dsl.SemanticType;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.Collection;

public class MultiFileGenerator extends Generator {

    /**
     * Callback for formatting final filename
     */
    public interface FileNameFormatter {
        String format(SemanticType t);
    }

    /**
     * Folder to write velocity generated content
     */
    private File outputDir;

    /**
     * callback for formatting output file name
     */
    private FileNameFormatter fileNameFormatter;

    private MultiFileGenerator(Builder builder) {
        super(builder);
        if (builder.outputDir == null) {
            throw new InvalidParameterException("Where are files supposed to be written to?");
        }

        if (builder.fileNameFormatter == null) {
            throw new InvalidParameterException("File name formatter is required");
        }

        outputDir = builder.outputDir;
        fileNameFormatter = builder.fileNameFormatter;
    }

    @Override
    public Void call() throws Exception {
        // Create list of semantic types from source files
        Collection<SemanticType> types = loadSemanticTypes(omeXmlFiles);
        if (types.isEmpty()) {
            return null; // Skip when no files, otherwise we overwrite.
        }

        // Velocity process the semantic types
        for (SemanticType st : types) {
            VelocityContext vc = new VelocityContext();
            vc.put("type", st);

            // Format the final filename using callback
            String filename = fileNameFormatter.format(st);
            parseTemplate(vc, template, new File(outputDir, filename));
        }
        return null;
    }

    public static class Builder extends Generator.Builder {
        private File outputDir;
        private FileNameFormatter fileNameFormatter;

        public Builder setOutputDir(File outputDir) {
            this.outputDir = outputDir;
            return this;
        }

        public Builder setFileNameFormatter(FileNameFormatter callback) {
            this.fileNameFormatter = callback;
            return this;
        }

        public MultiFileGenerator build() {
            return new MultiFileGenerator(this);
        }
    }
}
