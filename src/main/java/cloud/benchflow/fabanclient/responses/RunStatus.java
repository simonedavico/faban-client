package cloud.benchflow.fabanclient.responses;

/**
 * Created by simonedavico on 28/10/15.
 */
public class RunStatus implements Response {

    public enum Code { QUEUED, RECEIVED, STARTED, COMPLETED, FAILED, KILLED };


}
