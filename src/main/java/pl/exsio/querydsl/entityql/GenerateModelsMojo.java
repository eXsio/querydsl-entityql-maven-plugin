package pl.exsio.querydsl.entityql;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import java.io.File;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mojo(name = "generate-models")
public class GenerateModelsMojo extends AbstractMojo {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateModelsMojo.class);

    private final QExporter exporter = new QExporter();

    @Parameter
    private List<Generator> generators;

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    public void execute() throws MojoExecutionException {
        URLClassLoader classLoader = classLoader();
        try {
            for (Generator generator : generators) {
                generate(generator, classLoader);
            }
        } catch (Exception ex) {
            throw new MojoExecutionException("Failed to generate Static Models", ex);
        }
    }

    private void generate(Generator generator, URLClassLoader classLoader) throws Exception {
        LOGGER.info("Generating EntityQL Static Models from package {} to package {}, destination path: {}",
                generator.getSourcePackage(), generator.getDestinationPackage(), generator.getDestinationPath()
        );
        Reflections reflections = new Reflections(generator.getSourcePackage(), classLoader);
        Set<Class<?>> entityClasses = reflections.getTypesAnnotatedWith(Entity.class);
        LOGGER.info("Found {} Entity Classes to export in package {}", entityClasses.size(), generator.getSourcePackage());
        for (Class<?> entityClass : entityClasses) {
            LOGGER.info("Exporting class: {}", entityClass.getName());
            exporter.export(
                    EntityQL.qEntity(entityClass),
                    generator.getFilenamePattern(),
                    generator.getDestinationPackage(),
                    generator.getDestinationPath()
            );
        }
    }

    private URLClassLoader classLoader() throws MojoExecutionException {
        List<String> classpathElements = null;
        try {
            classpathElements = project.getCompileClasspathElements();
            List<URL> projectClasspathList = new ArrayList<>();
            for (String element : classpathElements) {
                try {
                    projectClasspathList.add(new File(element).toURI().toURL());
                } catch (MalformedURLException e) {
                    throw new MojoExecutionException(element + " is an invalid classpath element", e);
                }
            }
            return new URLClassLoader(projectClasspathList.toArray(new URL[0]), Thread.currentThread().getContextClassLoader());
        } catch (DependencyResolutionRequiredException e) {
            throw new MojoExecutionException("Dependency resolution failed", e);
        }
    }
}
