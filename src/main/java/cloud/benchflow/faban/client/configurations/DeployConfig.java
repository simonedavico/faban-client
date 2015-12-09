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
    private String driverName;
    private boolean clearConfig;

    public DeployConfig(InputStream jarFile, String driverName) {
        this(jarFile, driverName, true);
    }

    public DeployConfig(InputStream jarFile, String driverName, boolean clearConfig) {
        this.jarFile = jarFile;
        this.driverName = driverName;
        this.clearConfig = clearConfig;
    }

    public InputStream getJarFile() {
        return jarFile;
    }

    public boolean clearConfig() {
        return clearConfig;
    }

    public String getDriverName() { return driverName; }
}
