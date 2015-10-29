package cloud.benchflow.fabanclient.commands;

import cloud.benchflow.fabanclient.configurations.Configurable;
import cloud.benchflow.fabanclient.configurations.FabanClientConfig;
import cloud.benchflow.fabanclient.configurations.StatusConfig;
import cloud.benchflow.fabanclient.exceptions.FabanClientException;
import cloud.benchflow.fabanclient.exceptions.IllegalRunIdException;
import cloud.benchflow.fabanclient.exceptions.MalformedURIException;
import cloud.benchflow.fabanclient.exceptions.RunIdNotFoundException;
import cloud.benchflow.fabanclient.responses.RunId;
import cloud.benchflow.fabanclient.responses.RunStatus;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
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

    public RunStatus exec(FabanClientConfig fabanConfig) throws IOException, RunIdNotFoundException {
        return status(fabanConfig);
    }

    private RunStatus status(FabanClientConfig fabanConfig) throws IOException, RunIdNotFoundException {

        RunId runId = config.getRunId();

        ResponseHandler<RunStatus> sh = resp -> new RunStatus(new BasicResponseHandler().handleEntity(resp.getEntity()), runId);

        try(CloseableHttpClient httpclient = HttpClients.createDefault()) {

            URI statusURL = new URIBuilder(fabanConfig.getMasterURL())
                                .setPath(STATUS_PATH + "/" + runId)
                                .build();

            HttpGet get = new HttpGet(statusURL);

            CloseableHttpResponse resp = httpclient.execute(get);

            int status = resp.getStatusLine().getStatusCode();
            if(status == HttpStatus.SC_NOT_FOUND) throw new RunIdNotFoundException();
            if(status == HttpStatus.SC_BAD_REQUEST) throw new FabanClientException("Illegal status request");

            //TODO: check that the call to .handleEntity(..) actually returns the expected string
            RunStatus runStatus = sh.handleResponse(resp);

            return runStatus;

        } catch (URISyntaxException e) {
            throw new MalformedURIException("Attempted to check status from malformed URI: " + e.getInput(), e);
        }

    }


}
