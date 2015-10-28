package cloud.benchflow.fabanclient.commands;

import cloud.benchflow.fabanclient.configurations.FabanClientConfig;
import cloud.benchflow.fabanclient.responses.Response;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author Simone D'Avico <simonedavico@gmail.com>
 *
 * Interface for a generic command.
 */
public interface Command<T extends Response> {

    default T exec(FabanClientConfig fabanConfig) throws Exception {
        throw new NotImplementedException();
    }

}
