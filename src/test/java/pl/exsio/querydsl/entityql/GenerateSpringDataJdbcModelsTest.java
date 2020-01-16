package pl.exsio.querydsl.entityql;

import io.takari.maven.testing.TestMavenRuntime;
import io.takari.maven.testing.TestResources;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

public class GenerateSpringDataJdbcModelsTest {

    @Rule
    public final TestResources resources = new TestResources();

    @Rule
    public final TestMavenRuntime maven = new TestMavenRuntime();

    @Test
    public void shouldGenerateQFooBar() throws Exception {
        // given
        File basedir = resources.getBasedir("jdbc-basic");
        GenerateModelsMojo generate = (GenerateModelsMojo) maven.lookupConfiguredMojo(
                maven.newMavenSession(maven.readMavenProject(basedir)),
                maven.newMojoExecution("generate-models")
        );

        // when
        generate.execute();

        // then
        Generator generator = generate.getGenerators().get(0);
        Path qFooBarPath = Paths.get(
                generator.getDestinationPath() + "/" + generator.getDestinationPackage().replaceAll("\\.", "/") + "/QFooBar.java");
        then(qFooBarPath).isRegularFile();
    }
}
