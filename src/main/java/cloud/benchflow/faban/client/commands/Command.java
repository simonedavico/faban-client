package cloud.benchflow.faban.client.commands;

import cloud.benchflow.faban.client.configurations.FabanClientConfig;
import cloud.benchflow.faban.client.responses.Response;
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
