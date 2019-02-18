package org.openmicroscopy.dsl.factories

import org.gradle.api.NamedDomainObjectFactory
import org.gradle.api.Project
import org.openmicroscopy.dsl.extensions.ResourceExtension

class ResourceFactory implements NamedDomainObjectFactory<ResourceExtension> {
    final Project project;

    ResourceFactory(Project project) {
        this.project = project
    }

    @Override
    ResourceExtension create(String name) {
        return new ResourceExtension(name, project)
    }
}

