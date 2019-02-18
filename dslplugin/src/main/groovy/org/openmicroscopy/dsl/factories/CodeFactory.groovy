package org.openmicroscopy.dsl.factories

import org.gradle.api.NamedDomainObjectFactory
import org.gradle.api.Project
import org.openmicroscopy.dsl.extensions.CodeExtension

class CodeFactory implements NamedDomainObjectFactory<CodeExtension> {
    final Project project

    CodeFactory(Project project) {
        this.project = project
    }

    @Override
    CodeExtension create(String name) {
        return new CodeExtension(name, project)
    }
}

