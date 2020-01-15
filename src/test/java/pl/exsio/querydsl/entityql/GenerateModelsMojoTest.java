package pl.exsio.querydsl.entityql;

import io.takari.maven.testing.TestMavenRuntime;
import io.takari.maven.testing.TestResources;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class GenerateModelsMojoTest {

    @Rule
    public final TestResources resources = new TestResources();

    @Rule
    public final TestMavenRuntime maven = new TestMavenRuntime();

    @Test
    public void testBasicJpa() throws Exception {
        GenerateModelsMojo generate = getMojo("jpa-basic");
        System.out.println(generate.getGenerators());
        assertThat(generate.getGenerators()).isNotNull();
        assertThat(generate.getGenerators().size()).isEqualTo(1);
    }

    private GenerateModelsMojo getMojo(String project) throws Exception {
        File basedir = resources.getBasedir(project);
        return (GenerateModelsMojo) maven.lookupConfiguredMojo(
                maven.newMavenSession(maven.readMavenProject(basedir)),
                maven.newMojoExecution("generate-models")
        );
    }
}
