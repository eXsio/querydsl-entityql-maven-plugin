package pl.exsio.querydsl.entityql.entity.scanner;

import org.apache.maven.plugin.MojoExecutionException;

import java.util.Map;

public interface QEntityScannerFactory {

    QEntityScanner createScanner(Map<String, String> params) throws MojoExecutionException;
}
