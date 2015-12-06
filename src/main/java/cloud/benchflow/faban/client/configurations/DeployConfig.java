package cloud.benchflow.faban.client.configurations;

import java.io.InputStream;

/**
 * Created by simonedavico on 26/10/15.
 *
 * Configuration class for the deploy command
 */
public class DeployConfig implements Config {

    //private File jarFile;
    private InputStream jarFile;
    private boolean clearConfig;

    public DeployConfig(InputStream jarFile) {
        this(jarFile, true);
    }

    public DeployConfig(InputStream jarFile, boolean clearConfig) {
        this.jarFile = jarFile;
        this.clearConfig = clearConfig;
    }

    public InputStream getJarFile() {
        return jarFile;
    }

    public boolean clearConfig() {
        return clearConfig;
    }


}
