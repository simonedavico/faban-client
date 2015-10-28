package cloud.benchflow.fabanclient.exceptions;


/**
 * Created by simonedavico on 28/10/15.
 */
public class JarFileNotFoundException extends FabanClientException {

    public JarFileNotFoundException(String message) {
        super(message);
    }

    public JarFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
