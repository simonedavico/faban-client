package cloud.benchflow.fabanclient;

import cloud.benchflow.fabanclient.commands.DeployCommand;
import cloud.benchflow.fabanclient.configurations.*;
import cloud.benchflow.fabanclient.exceptions.DeployException;
import cloud.benchflow.fabanclient.exceptions.FabanClientException;
import cloud.benchflow.fabanclient.exceptions.JarFileNotFoundException;
import cloud.benchflow.fabanclient.responses.DeployStatus;
import com.sun.istack.internal.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Simone D'Avico <simonedavico@gmail.com>
 * @throws FabanClientException
 *
 * The faban client implementation.
 */
//TODO: check if the notnull annotation is usable outside of intellij
public class FabanClient extends Configurable<FabanClientConfigImpl> {

    private FabanClientConfig fabanConfig = new FabanClientDefaultConfig();

    /**
     * @param jarFile the benchmark to be deployed on the faban harness
     * @return a response enclosing the status of the operation
     */
    public DeployStatus deploy(@NotNull File jarFile) throws FabanClientException {

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
    public <R extends DeployStatus, T> T deploy(@NotNull File jarFile, @NotNull Function<R, T> handler) throws FabanClientException {
        return this.deploy(jarFile).handle(handler);
    }


    /**
     * @param jarFile the benchmark to be deployed on the faban harness
     * @param handler a consumer that receives a {@link DeployStatus}
     * @param <R> the type of the handler input (has to extend {@link DeployStatus})
     * @throws FabanClientException
     */
    public <R extends DeployStatus> void deploy(@NotNull File jarFile, @NotNull Consumer<R> handler) throws FabanClientException {
        this.deploy(jarFile).handle(handler);
    }

}
