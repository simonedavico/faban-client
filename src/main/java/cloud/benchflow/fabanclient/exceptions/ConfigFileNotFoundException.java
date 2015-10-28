package cloud.benchflow.fabanclient.exceptions;

/**
 * @author Simone D'Avico <simonedavico@gmail.com>
 *         <p/>
 *         Created on 28/10/15.
 */
public class ConfigFileNotFoundException extends FabanClientException {
    public ConfigFileNotFoundException(String message) {
        super(message);
    }

    public ConfigFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
