package cloud.benchflow.fabanclient.exceptions;

/**
 * Created by simonedavico on 27/10/15.
 */
public class FabanClientException extends RuntimeException {

    public FabanClientException() {};

    public FabanClientException(String message) {
        super(message);
    }

    public FabanClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
