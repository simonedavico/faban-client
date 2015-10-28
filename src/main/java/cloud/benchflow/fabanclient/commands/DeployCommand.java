package cloud.benchflow.fabanclient.commands;

import cloud.benchflow.fabanclient.configurations.Configurable;
import cloud.benchflow.fabanclient.configurations.DeployConfig;
import cloud.benchflow.fabanclient.configurations.FabanClientConfig;
import cloud.benchflow.fabanclient.exceptions.MalformedURIException;
import cloud.benchflow.fabanclient.responses.DeployStatus;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;



/**
 * Created by simonedavico on 26/10/15.
 */
public class DeployCommand extends Configurable<DeployConfig> implements Command<DeployStatus>  {

    private static String DEPLOY_URL = "/deploy";

    /**
     *
     * @param fabanConfig the harness configuration
     * @return a response containing the status of the operation
     * @throws IOException
     */
    public DeployStatus exec(FabanClientConfig fabanConfig)  throws IOException {
        return deploy(fabanConfig);
    }

    //TODO: add clearconfig param to post request
    private DeployStatus deploy(FabanClientConfig fabanConfig) throws IOException {

        File jarFile = this.config.getJarFile();

        try (CloseableHttpClient httpclient = HttpClients.createDefault()){

            URI deployURL = new URIBuilder(fabanConfig.getMasterURL())
                                .setPath(DEPLOY_URL)
                                .build();
            HttpPost post = new HttpPost(deployURL);
            HttpEntity multipartEntity = MultipartEntityBuilder.create()
                                  .addTextBody("user", fabanConfig.getUser())
                                  .addTextBody("password", fabanConfig.getPassword())
                                  .addBinaryBody("jarfile", jarFile)
                                  .build();

            post.setEntity(multipartEntity);
            post.setHeader("Accept", "text/plain");
            DeployStatus dresp = httpclient.execute(post, (resp) -> new DeployStatus(resp.getStatusLine().getStatusCode()));

            return dresp;

        } catch (URISyntaxException e) { //this should never occur
            throw new MalformedURIException("Attempted to deploy to malformed URI: " + e.getInput(), e);
        }

    }

}
