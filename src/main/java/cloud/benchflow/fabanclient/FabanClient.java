package cloud.benchflow.fabanclient;

import cloud.benchflow.fabanclient.commands.DeployCommand;
import cloud.benchflow.fabanclient.commands.KillCommand;
import cloud.benchflow.fabanclient.commands.StatusCommand;
import cloud.benchflow.fabanclient.commands.SubmitCommand;
import cloud.benchflow.fabanclient.configurations.*;
import cloud.benchflow.fabanclient.exceptions.*;
import cloud.benchflow.fabanclient.responses.DeployStatus;
import cloud.benchflow.fabanclient.responses.RunId;
import cloud.benchflow.fabanclient.responses.RunStatus;
import com.sun.istack.internal.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.jar.JarFile;

/**
 * @author Simone D'Avico <simonedavico@gmail.com>
 * @throws FabanClientException
 *
 * The faban client implementation.
 */
//TODO: check if the notnull annotation is usable outside of intellij
//TODO: implement pending
//TODO: implement showlogs
//TODO: implement kill
//TODO: "resource/argument not found exceptions should be checked?"
public class FabanClient extends Configurable<FabanClientConfigImpl> {

    private FabanClientConfig fabanConfig = new FabanClientDefaultConfig();

    /**
     * @param jarFile the benchmark to be deployed on the faban harness
     * @return a response enclosing the status of the operation
     */
    public DeployStatus deploy(@NotNull File jarFile) throws FabanClientException, JarFileNotFoundException {

        if(jarFile.exists()) {

            DeployConfig config = new DeployConfig(jarFile);
            DeployCommand deploy = new DeployCommand().withConfig(config);

            try {
                return deploy.exec(fabanConfig);
            } catch(IOException e) {
                throw new DeployException("Something went wrong while deploying "
                                          + jarFile.getName(), e);
            }

        } else {
            throw new JarFileNotFoundException("The specified jar file ( " +
                                            jarFile.getAbsolutePath() +
                                            " could not be found.");
        }

    }

    /**
     * @param jarFile the benchmark to be deployed on the faban harness
     * @param handler a function that receives a {@link DeployStatus} and returns a {@code <T>}
     * @param <R> the type of the handler input (has to extend {@link DeployStatus})
     * @param <T> the arbitrary return type
     * @return an object of type {@code <T>}
     */
    public <R extends DeployStatus, T> T deploy(@NotNull File jarFile, @NotNull Function<R, T> handler) throws FabanClientException, JarFileNotFoundException {
        return this.deploy(jarFile).handle(handler);
    }


    /**
     * @param jarFile the benchmark to be deployed on the faban harness
     * @param handler a consumer that receives a {@link DeployStatus}
     * @param <R> the type of the handler input (has to extend {@link DeployStatus})
     * @throws FabanClientException
     */
    public <R extends DeployStatus> void deploy(@NotNull File jarFile, @NotNull Consumer<R> handler) throws FabanClientException, JarFileNotFoundException {
        this.deploy(jarFile).handle(handler);
    }

    public RunStatus status(RunId runId) throws FabanClientException, RunIdNotFoundException {

        StatusConfig config = new StatusConfig(runId);
        StatusCommand status = new StatusCommand().withConfig(config);

        try {
            return status.exec(fabanConfig);
        } catch (IOException e) {
            throw new FabanClientException("Something went wrong while requesting status with runId" + runId, e);
        }

    }

    public <R extends RunStatus, T> T status(RunId runId, Function<R, T> handler) throws FabanClientException, RunIdNotFoundException {
        return this.status(runId).handle(handler);
    }

    public <R extends RunStatus> void status(RunId runId, Consumer<R> handler) throws FabanClientException, RunIdNotFoundException {
        this.status(runId).handle(handler);
    }

    public RunId submit(String benchmarkName, String profile, File configFile) throws FabanClientException, ConfigFileNotFoundException, BenchmarkNameNotFoundException {

        if(configFile.exists()) {
            SubmitConfig config = new SubmitConfig(benchmarkName, profile, configFile);
            SubmitCommand submit = new SubmitCommand().withConfig(config);

            try {
                return submit.exec(fabanConfig);
            } catch (IOException e) {
                throw new FabanClientException("Something went wrong while submitting the run for benchmark " +
                                                benchmarkName + " at profile " + profile + " with configuration " +
                                                configFile.getAbsolutePath(), e);
            }

        } else {
            throw new ConfigFileNotFoundException("Configuration file " +
                                                    configFile.getAbsolutePath() +
                                                  " could not be found.");
        }

    }

    public <R extends RunId, T> T submit(String benchmarkName, String profile, File configFile, Function<R, T> handler) throws FabanClientException, ConfigFileNotFoundException, BenchmarkNameNotFoundException {
        return this.submit(benchmarkName, profile, configFile).handle(handler);
    }

    public <R extends RunId> void submit(String benchmarkName, String profile, File configFile, Consumer<R> handler) throws FabanClientException, ConfigFileNotFoundException, BenchmarkNameNotFoundException {
        this.submit(benchmarkName, profile, configFile).handle(handler);
    }

    public RunStatus kill(RunId runId) throws FabanClientException, RunIdNotFoundException {

        StatusConfig config = new StatusConfig(runId);
        KillCommand kill = new KillCommand().withConfig(config);

        try {
            return kill.exec(fabanConfig);
        } catch (IOException e) {
            throw new FabanClientException("Unexpected IO error while trying to kill " + runId, e);
        }

    }
    
    public <R extends RunStatus, T> T kill(RunId runId, Function<R,T> handler) throws RunIdNotFoundException, FabanClientException {
        return this.kill(runId).handle(handler);
    }

    public <R extends RunStatus> void kill(RunId runId, Consumer<R> handler) throws RunIdNotFoundException, FabanClientException {
        this.kill(runId).handle(handler);
    }


}
