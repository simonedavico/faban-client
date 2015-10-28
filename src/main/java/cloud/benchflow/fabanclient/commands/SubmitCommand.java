package cloud.benchflow.fabanclient.commands;

import cloud.benchflow.fabanclient.configurations.Configurable;
import cloud.benchflow.fabanclient.configurations.FabanClientConfig;
import cloud.benchflow.fabanclient.configurations.SubmitConfig;
import cloud.benchflow.fabanclient.exceptions.MalformedURIException;
import cloud.benchflow.fabanclient.responses.RunId;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Simone D'Avico <simonedavico@gmail.com>
 *
 * Created on 28/10/15.
 */
public class SubmitCommand extends Configurable<SubmitConfig> implements Command<RunId> {

    private static String SUBMIT_URL = "";

    public RunId exec(FabanClientConfig fabanConfig) throws IOException {
        return submit(fabanConfig);
    }

    public RunId submit(FabanClientConfig fabanConfig) throws IOException {

        String benchmarkName = config.getBenchmarkName();
        String profile = config.getProfile();
        File configFile = config.getConfigFile();

        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {

            URI submitURL = new URIBuilder(fabanConfig.getMasterURL())
                                    .setPath(SUBMIT_URL)
                                    .build();

            HttpPost post = new HttpPost(submitURL);
            HttpEntity multipartEntity = MultipartEntityBuilder.create()
                                    .addTextBody("sun", fabanConfig.getUser())
                                    .addTextBody("sp", fabanConfig.getPassword())
                                    .addBinaryBody("configfile", configFile)
                                    .build();

            post.setEntity(multipartEntity);

            //TODO: check that this does indeed work
            //TODO: check for SC_NOT_FOUND and SC_NO_CONTENT
            RunId runId = httpClient.execute(post,
                    resp -> new RunId(new BasicResponseHandler().handleEntity(resp.getEntity())));

            return runId;

        } catch (URISyntaxException e) {
            throw new MalformedURIException("Attempted to submit run for benchmark " +
            benchmarkName + "and profile " + profile + "to malformed URL.");
        }

    }

}
