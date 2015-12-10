package cloud.benchflow.faban.client.commands;

import cloud.benchflow.faban.client.configurations.DeployConfig;
import cloud.benchflow.faban.client.configurations.FabanClientConfig;
import cloud.benchflow.faban.client.responses.DeployStatus;
import cloud.benchflow.faban.client.configurations.Configurable;
import cloud.benchflow.faban.client.exceptions.MalformedURIException;
import com.google.common.io.ByteStreams;
import org.apache.http.HttpEntity;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * @author Simone D'Avico <simonedavico@gmail.com>
 *
 * Created on 26/10/15.
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

    private DeployStatus deploy(FabanClientConfig fabanConfig) throws IOException {

        ResponseHandler<DeployStatus> dh = resp -> new DeployStatus(resp.getStatusLine().getStatusCode());

        InputStream jarFile = this.config.getJarFile();
        Boolean clearConfig = config.clearConfig();

        try (CloseableHttpClient httpclient = HttpClients.createDefault()){

            URI deployURL = new URIBuilder(fabanConfig.getMasterURL())
                                .setPath(DEPLOY_URL)
                                .build();
            HttpPost post = new HttpPost(deployURL);

            byte[] boh = ByteStreams.toByteArray(jarFile);

            HttpEntity multipartEntity = MultipartEntityBuilder.create()
                                  .addTextBody("user", fabanConfig.getUser())
                                  .addTextBody("password", fabanConfig.getPassword())
                                  .addTextBody("clearconfig", clearConfig.toString())
                                  .addBinaryBody("jarfile", ByteStreams.toByteArray(jarFile),
                                                 ContentType.DEFAULT_BINARY, this.config.getDriverName())
                                  .build();

            post.setEntity(multipartEntity);
            post.setHeader("Accept", "text/plain");

            DeployStatus dresp = httpclient.execute(post, dh);

            return dresp;

        } catch (URISyntaxException e) { //this should never occur
            throw new MalformedURIException("Attempted to deploy to malformed URI: " + e.getInput(), e);
        }

    }

}
