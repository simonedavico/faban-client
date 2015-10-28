package cloud.benchflow.fabanclient.configurations;

import cloud.benchflow.fabanclient.responses.RunId;

/**
 * Created by simonedavico on 28/10/15.
 */
public class StatusConfig implements Config {

    private RunId runId;

    public StatusConfig(RunId runId) {
        this.runId = runId;
    }

    public RunId getRunId() {
        return runId;
    }

}
