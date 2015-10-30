package cloud.benchflow.fabanclient;

import cloud.benchflow.fabanclient.commands.*;
import cloud.benchflow.fabanclient.configurations.*;
import cloud.benchflow.fabanclient.exceptions.*;
import cloud.benchflow.fabanclient.responses.DeployStatus;
import cloud.benchflow.fabanclient.responses.RunId;
import cloud.benchflow.fabanclient.responses.RunQueue;
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
//TODO: implement showlogs
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

    /**
     *
     * @param runId a run id
     * @return the status of the run
     * @throws RunIdNotFoundException
     */
    public RunStatus status(RunId runId) throws FabanClientException, RunIdNotFoundException {

        StatusConfig config = new StatusConfig(runId);
        StatusCommand status = new StatusCommand().withConfig(config);

        try {
            return status.exec(fabanConfig);
        } catch (IOException e) {
            throw new FabanClientException("Something went wrong while requesting status with runId" + runId, e);
        }

    }

    /**
     *
     * @param runId a run id
     * @param handler a callback function
     * @param <R> The input to the {@param handler} function, a run status
     * @param <T> The return type of the {@param handler} function
     * @return An instance of {@link T}
     * @throws FabanClientException
     * @throws RunIdNotFoundException if passed a non existent run id
     */
    public <R extends RunStatus, T> T status(RunId runId, Function<R, T> handler) throws FabanClientException, RunIdNotFoundException {
        return this.status(runId).handle(handler);
    }

    /**
     *
     * @param runId a run id
     * @param handler a callback function
     * @param <R> The input to the {@param handler} consumer, a run status
     * @throws RunIdNotFoundException
     */
    public <R extends RunStatus> void status(RunId runId, Consumer<R> handler) throws FabanClientException, RunIdNotFoundException {
        this.status(runId).handle(handler);
    }

    /**
     *
     * @param benchmarkName benchmark shortname
     * @param profile a profile name
     * @param configFile a config xml file for the run
     * @return the run id for the run
     * @throws ConfigFileNotFoundException
     * @throws BenchmarkNameNotFoundException
     */
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

    /**
     *
     * @param benchmarkName benchmark shortname
     * @param profile a profile name
     * @param configFile a config xml file for the run
     * @param handler a callback function {@link R} -> {@link T}
     * @param <R> a subclass of {@link RunId}
     * @param <T> return type of the {@param handler} function
     * @return the run id for the run
     * @throws ConfigFileNotFoundException
     * @throws BenchmarkNameNotFoundException
     */
    public <R extends RunId, T> T submit(String benchmarkName, String profile, File configFile, Function<R, T> handler) throws FabanClientException, ConfigFileNotFoundException, BenchmarkNameNotFoundException {
        return this.submit(benchmarkName, profile, configFile).handle(handler);
    }

    /**
     *
     * @param benchmarkName benchmark shortname
     * @param profile a profile name
     * @param configFile a config xml file
     * @param handler a callback consumer {@link R} -> void
     * @param <R> a subclass of {@link RunId}
     * @throws ConfigFileNotFoundException
     * @throws BenchmarkNameNotFoundException
     */
    public <R extends RunId> void submit(String benchmarkName, String profile, File configFile, Consumer<R> handler) throws FabanClientException, ConfigFileNotFoundException, BenchmarkNameNotFoundException {
        this.submit(benchmarkName, profile, configFile).handle(handler);
    }

    /**
     *
     * @param runId a run id
     * @return status of the kill operation
     * @throws RunIdNotFoundException
     */
    public RunStatus kill(RunId runId) throws FabanClientException, RunIdNotFoundException {

        StatusConfig config = new StatusConfig(runId);
        KillCommand kill = new KillCommand().withConfig(config);

        try {
            return kill.exec(fabanConfig);
        } catch (IOException e) {
            throw new FabanClientException("Unexpected IO error while trying to kill " + runId, e);
        }

    }

    /**
     *
     * @param runId a run id
     * @param handler a callback function {@link R} -> {@link T}
     * @param <R> a subclass of {@link RunStatus}
     * @param <T> return type of {@param handler}
     * @return an object of type {@link T}
     * @throws RunIdNotFoundException
     */
    public <R extends RunStatus, T> T kill(RunId runId, Function<R,T> handler) throws RunIdNotFoundException, FabanClientException {
        return this.kill(runId).handle(handler);
    }

    /**
     *
     * @param runId a run id
     * @param handler a callback consumer {@link R} -> void
     * @param <R> a subclass of {@link RunStatus}
     * @throws RunIdNotFoundException
     */
    public <R extends RunStatus> void kill(RunId runId, Consumer<R> handler) throws RunIdNotFoundException, FabanClientException {
        this.kill(runId).handle(handler);
    }

    /**
     *
     * @return a queue of pending run ids
     */
    public RunQueue pending() throws FabanClientException {

        PendingCommand pending = new PendingCommand();

        try {
            return pending.exec(fabanConfig);
        } catch (IOException e) {
            throw new FabanClientException("Unexpected IO error while requesting for pending runs", e);
        }

    }

    /**
     *
     * @param handler a callback function {@link R} -> {@link T}
     * @param <R> a subclass of {@link RunStatus}
     * @param <T> return type of {@param handler}
     * @return a queue of pending run ids
     */
    public <R extends RunQueue, T> T pending(Function<R,T> handler) {
        return this.pending().handle(handler);
    }

    /**
     *
     * @param handler a callback consumer {@link R} -> void
     * @param <R> a subclass of {@link RunQueue}
     */
    public <R extends RunQueue> void pending(Consumer<R> handler) {
        this.pending().handle(handler);
    }


}
