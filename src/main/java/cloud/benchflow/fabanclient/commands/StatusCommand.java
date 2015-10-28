package cloud.benchflow.fabanclient.commands;

import cloud.benchflow.fabanclient.configurations.Configurable;
import cloud.benchflow.fabanclient.configurations.FabanClientConfig;
import cloud.benchflow.fabanclient.configurations.StatusConfig;
import cloud.benchflow.fabanclient.exceptions.MalformedURIException;
import cloud.benchflow.fabanclient.responses.RunId;
import cloud.benchflow.fabanclient.responses.RunStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Simone D'Avico <simonedavico@gmail.com>
 *
 * Created on 28/10/15.
 */
public class StatusCommand extends Configurable<StatusConfig> implements Command<RunStatus> {


    private static String STATUS_PATH = "/submit";

    public RunStatus exec(FabanClientConfig fabanConfig) throws IOException {
        return status(fabanConfig);
    }

    private RunStatus status(FabanClientConfig fabanConfig) throws IOException {

        RunId runId = config.getRunId();

        try(CloseableHttpClient httpclient = HttpClients.createDefault()) {

            URI statusURL = new URIBuilder(fabanConfig.getMasterURL())
                                .setPath(STATUS_PATH + "/" + runId)
                                .build();

            HttpGet get = new HttpGet(statusURL);

            //send get request, receive response and return object

            //TODO: check that the call to .handleEntity(..) actually returns the expected string
            RunStatus runStatus = httpclient.execute(get,
                    (resp) -> new RunStatus(new BasicResponseHandler().handleEntity(resp.getEntity()), runId));

            return runStatus;

        } catch (URISyntaxException e) {
            throw new MalformedURIException("Attempted to check status from malformed URI: " + e.getInput(), e);
        }

    }


}
