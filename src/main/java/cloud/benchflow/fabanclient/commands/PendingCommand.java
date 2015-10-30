package cloud.benchflow.fabanclient.commands;

import cloud.benchflow.fabanclient.configurations.Configurable;
import cloud.benchflow.fabanclient.configurations.FabanClientConfig;
import cloud.benchflow.fabanclient.exceptions.MalformedURIException;
import cloud.benchflow.fabanclient.responses.RunId;
import cloud.benchflow.fabanclient.responses.RunQueue;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * @author Simone D'Avico <simonedavico@gmail.com>
 *
 * Created on 30/10/15.
 */
public class PendingCommand extends Configurable implements Command<RunQueue> {

    private static String PENDING_URL = "/pending";

    public RunQueue exec(FabanClientConfig fabanConfig) throws IOException {
        return pending(fabanConfig);
    }

    private RunQueue pending(FabanClientConfig fabanConfig) throws IOException {

        try(CloseableHttpClient httpClient = HttpClients.createDefault()){

            URI pendingURL = new URIBuilder(fabanConfig.getMasterURL())
                                .setPath(PENDING_URL)
                                .build();

            RunQueue queue = new RunQueue();

            HttpGet get = new HttpGet(pendingURL);
            CloseableHttpResponse resp = httpClient.execute(get);
            HttpEntity ent = resp.getEntity();
            InputStream in = resp.getEntity().getContent();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in, ent.getContentEncoding().getValue()))) {

                String line;
                while((line = reader.readLine()) != null) {
                    queue.add(new RunId(line));
                }

            }

            return queue;

        } catch (URISyntaxException e) {
            throw new MalformedURIException("Malformed pending request to faban harness: " + e.getInput(), e);
        }

    }

}
