package cloud.benchflow.fabanclient.responses;

import cloud.benchflow.fabanclient.exceptions.IllegalRunStatusException;

/**
 * Created by simonedavico on 28/10/15.
 */
public class RunStatus implements Response {

    private Code status;

    public enum Code { QUEUED, RECEIVED, STARTED, COMPLETED, FAILED, KILLED, KILLING, DENIED };

    public RunStatus(String statusCode, RunId runId) {
        switch (statusCode) {
            case "QUEUED":
                this.status = Code.QUEUED;
            case "RECEIVED":
                this.status = Code.RECEIVED;
            case "STARTED":
                this.status = Code.STARTED;
            case "COMPLETED":
                this.status = Code.COMPLETED;
            case "FAILED":
                this.status = Code.FAILED;
            case "KILLED":
                this.status = Code.KILLED;
            case "KILLING":
                this.status = Code.KILLING;
            case "DENIED":
                this.status = Code.DENIED;
            default:
                throw new IllegalRunStatusException("RunId " + runId +
                                                    "returned illegal run status " +
                                                    statusCode);
        }
    }

    public Code getStatus() {
        return this.status;
    }


}
