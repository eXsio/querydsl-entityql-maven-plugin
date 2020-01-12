package pl.exsio.querydsl.entityql;

public class Generator {

    private final static String SEPARATOR = System.getProperty("file.separator");

    private String sourcePackage;

    private String destinationPackage;

    private String filenamePattern = "Q%s.java";

    private String destinationPath = "${project.basedir}" + SEPARATOR + "src" + SEPARATOR + "main" + SEPARATOR + "java";

    public Generator(String sourcePackage, String destinationPackage, String filenamePattern, String destinationPath) {
        this.sourcePackage = sourcePackage;
        this.destinationPackage = destinationPackage;
        this.filenamePattern = filenamePattern;
        this.destinationPath = destinationPath;
    }

    public Generator() {
    }

    public String getSourcePackage() {
        return sourcePackage;
    }

    public void setSourcePackage(String sourcePackage) {
        this.sourcePackage = sourcePackage;
    }

    public String getDestinationPackage() {
        return destinationPackage;
    }

    public void setDestinationPackage(String destinationPackage) {
        this.destinationPackage = destinationPackage;
    }

    public String getFilenamePattern() {
        return filenamePattern;
    }

    public void setFilenamePattern(String filenamePattern) {
        this.filenamePattern = filenamePattern;
    }

    public String getDestinationPath() {
        return destinationPath;
    }

    public void setDestinationPath(String destinationPath) {
        this.destinationPath = destinationPath;
    }

    @Override
    public String toString() {
        return "Generator{" +
                "sourcePackage='" + sourcePackage + '\'' +
                ", destinationPackage='" + destinationPackage + '\'' +
                ", filenamePattern='" + filenamePattern + '\'' +
                ", destinationPath='" + destinationPath + '\'' +
                '}';
    }
}
