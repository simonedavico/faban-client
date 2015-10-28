package cloud.benchflow.fabanclient.configurations;

import java.net.URI;

/**
 * Created by simonedavico on 26/10/15.
 */
public interface FabanClientConfig extends Config {

    String getUser();

    String getPassword();

    URI getMasterURL();
}
