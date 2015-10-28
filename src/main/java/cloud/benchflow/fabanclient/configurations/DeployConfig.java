package cloud.benchflow.fabanclient.configurations;

import java.io.File;

/**
 * Created by simonedavico on 26/10/15.
 *
 * Configuration class for the deploy command
 */
public class DeployConfig implements Config {

    private File jarFile;

    public DeployConfig(File jarFile) {
        this.jarFile = jarFile;
    }

    public File getJarFile() {
        return jarFile;
    }
}
