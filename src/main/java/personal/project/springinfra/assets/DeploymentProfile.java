package personal.project.springinfra.assets;

public final class DeploymentProfile {
    public final static String DEVELOPMENT = "development";
    public final static String PRODUCTION = "production";

    private DeploymentProfile() {
        throw new UnsupportedOperationException("Couldn't build new instance from this utility class");
    }
}
