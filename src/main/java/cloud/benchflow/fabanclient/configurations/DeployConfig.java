package cloud.benchflow.fabanclient.configurations;

import java.io.File;

/**
 * Created by simonedavico on 26/10/15.
 *
 * Configuration class for the deploy command
 */
public class DeployConfig implements Config {

    private File jarFile;
    private boolean clearConfig;

    public DeployConfig(File jarFile) {
        this(jarFile, true);
    }

    public DeployConfig(File jarFile, boolean clearConfig) {
        this.jarFile = jarFile;
        this.clearConfig = clearConfig;
    }

    public File getJarFile() {
        return jarFile;
    }

    public boolean clearConfig() {
        return clearConfig;
    }


}
