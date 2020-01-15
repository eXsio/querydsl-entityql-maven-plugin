package pl.exsio.querydsl.entityql;

import io.takari.maven.testing.TestMavenRuntime;
import io.takari.maven.testing.TestResources;
import org.junit.Rule;
import org.junit.Test;
import pl.exsio.querydsl.entityql.jdbc.UpperCaseWithUnderscoresNamingStrategy;

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
        assertThat(generate.getGenerators()).isNotNull();
        assertThat(generate.getGenerators().size()).isEqualTo(1);
        assertThat(generate.getGenerators().get(0).getType()).isEqualTo(Generator.Type.JPA);
        assertThat(generate.getGenerators().get(0).getSourcePackage()).isEqualTo("pl.exsio.querydsl.entityql.examples.entity");
        assertThat(generate.getGenerators().get(0).getDestinationPackage()).isEqualTo("pl.exsio.querydsl.entityql.examples.entity.generated");
        assertThat(generate.getGenerators().get(0).getFilenamePattern()).isEqualTo("Q%s.java");
    }

    @Test
    public void testFullJpa() throws Exception {
        GenerateModelsMojo generate = getMojo("jpa-full");
        assertThat(generate.getGenerators()).isNotNull();
        assertThat(generate.getGenerators().size()).isEqualTo(1);
        assertThat(generate.getGenerators().get(0).getType()).isEqualTo(Generator.Type.JPA);
        assertThat(generate.getGenerators().get(0).getSourcePackage()).isEqualTo("pl.exsio.querydsl.entityql.examples.entity");
        assertThat(generate.getGenerators().get(0).getDestinationPackage()).isEqualTo("pl.exsio.querydsl.entityql.examples.entity.generated");
        assertThat(generate.getGenerators().get(0).getDestinationPath()).isEqualTo("src\\main\\java");
        assertThat(generate.getGenerators().get(0).getFilenamePattern()).isEqualTo("Q%s.groovy");
    }

    @Test
    public void testBasicJdbc() throws Exception {
        GenerateModelsMojo generate = getMojo("jdbc-basic");
        assertThat(generate.getGenerators()).isNotNull();
        assertThat(generate.getGenerators().size()).isEqualTo(1);
        assertThat(generate.getGenerators().get(0).getType()).isEqualTo(Generator.Type.SPRING_DATA_JDBC);
        assertThat(generate.getGenerators().get(0).getSourcePackage()).isEqualTo("pl.exsio.querydsl.entityql.examples.entity");
        assertThat(generate.getGenerators().get(0).getDestinationPackage()).isEqualTo("pl.exsio.querydsl.entityql.examples.entity.generated");
        assertThat(generate.getGenerators().get(0).getFilenamePattern()).isEqualTo("Q%s.java");
        assertThat(generate.getGenerators().get(0).getParams()).containsKey("namingStrategy");
        assertThat(generate.getGenerators().get(0).getParams().get("namingStrategy")).isEqualTo(UpperCaseWithUnderscoresNamingStrategy.class.getName());
    }

    private GenerateModelsMojo getMojo(String project) throws Exception {
        File basedir = resources.getBasedir(project);
        return (GenerateModelsMojo) maven.lookupConfiguredMojo(
                maven.newMavenSession(maven.readMavenProject(basedir)),
                maven.newMojoExecution("generate-models")
        );
    }
}
