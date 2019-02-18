package org.openmicroscopy.dsl.extensions

import org.apache.velocity.runtime.RuntimeConstants
import org.gradle.api.Project
import org.gradle.api.provider.Property

class VelocityExtension {

    final Property<Properties> data

    void setProperty(String key, String value) {
        data.get().setProperty(key, value)
    }

    void setResourceLoaderClass(Map<String, String> resourceLoaderClass) {
        resourceLoaderClass.each { entry ->
            data.get().setProperty(entry.key, entry.value)
        }
    }

    void setResourceLoader(String resourceLoader) {
        data.get().setProperty(
                RuntimeConstants.RESOURCE_LOADER,
                resourceLoader
        )
    }

    void setFileResourceLoaderPath(String fileResourceLoaderPath) {
        data.get().setProperty(
                RuntimeConstants.FILE_RESOURCE_LOADER_PATH,
                fileResourceLoaderPath
        )
    }

    void setFileResourceLoaderCache(boolean fileResourceLoaderCache) {
        data.get().setProperty(
                RuntimeConstants.FILE_RESOURCE_LOADER_CACHE,
                fileResourceLoaderCache as String
        )
    }

    void setLoggerClassName(String loggerClassName) {
        data.get().setProperty(
                RuntimeConstants.RUNTIME_LOG_NAME,
                loggerClassName
        )
    }

    void setMaxNumberLoops(int max) {
        data.get().setProperty(
                RuntimeConstants.MAX_NUMBER_LOOPS,
                max as String
        )
    }

    void setSkipInvalidIterator(boolean skipInvalid) {
        data.get().setProperty(
                RuntimeConstants.SKIP_INVALID_ITERATOR,
                skipInvalid as String
        )
    }

    void setCheckEmptyObjects(boolean emptyCheck) {
        data.get().setProperty(
                RuntimeConstants.CHECK_EMPTY_OBJECTS,
                emptyCheck as String
        )
    }

    void setErrorMsgStart(String msgStart) {
        data.get().setProperty(
                RuntimeConstants.ERRORMSG_START,
                msgStart
        )
    }

    void setErrorMsgEnd(String msgEnd) {
        data.get().setProperty(
                RuntimeConstants.ERRORMSG_END,
                msgEnd
        )
    }

    void setParseDirectiveMaxdepth(int max) {
        data.get().setProperty(
                RuntimeConstants.DEFINE_DIRECTIVE_MAXDEPTH,
                max as String
        )
    }

    void setProvideScopeControl(boolean scope) {
        data.get().setProperty(
                RuntimeConstants.PROVIDE_SCOPE_CONTROL,
                scope as String
        )
    }

    VelocityExtension(Project project) {
        data = project.objects.property(Properties)
        data.set(new Properties())
    }
}