package cloud.benchflow.fabanclient.responses;

import org.apache.http.HttpStatus;


/**
 * @author Simone D'Avico <simonedavico@gmail.com>
 */
//TODO: make all responses Jackson compliant for easy JSON serialization
public class DeployStatus implements Response {

    public enum Code { CONFLICT, NOT_ACCEPTABLE, CREATED, UNDEFINED };

    private Code code;

    public DeployStatus() {
        this.code = Code.UNDEFINED;
    }

    public DeployStatus(int statusCode) {
        switch(statusCode) {
            case(HttpStatus.SC_CREATED):
                this.code = Code.CREATED;
                break;
            case(HttpStatus.SC_CONFLICT):
                this.code = Code.CONFLICT;
                break;
            case(HttpStatus.SC_NOT_ACCEPTABLE):
                this.code = Code.NOT_ACCEPTABLE;
                break;
            default:
                this.code = Code.UNDEFINED;
        }
    }

    public Code getCode() {
        return this.code;
    }

}
