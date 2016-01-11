package cloud.benchflow.faban.test.client;

import cloud.benchflow.faban.client.FabanClient;
import cloud.benchflow.faban.client.configurations.FabanClientConfig;
import cloud.benchflow.faban.client.configurations.FabanClientConfigImpl;
import cloud.benchflow.faban.client.exceptions.JarFileNotFoundException;
import cloud.benchflow.faban.client.exceptions.RunIdNotFoundException;
import cloud.benchflow.faban.client.responses.DeployStatus;
import cloud.benchflow.faban.client.responses.RunId;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * @author Simone D'Avico <simonedavico@gmail.com>
 *
 * Created on 26/10/15.
 */
public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException, RunIdNotFoundException {

        //get an instance of faban client
        FabanClientConfig fcprova = new FabanClientConfigImpl("deployer","adminadmin",new URI("http://195.176.181.105:9980"));
        FabanClient client = new FabanClient().withConfig(fcprova);
        Path bm = Paths.get("./src/test/resources/foofoofoo.jar");

        try {
            client.deploy(bm.toFile()).handle((DeployStatus s) -> System.out.println(s.getCode()));
        } catch (JarFileNotFoundException e) {
            e.printStackTrace();
        }
        //FabanClientConfigImpl config = new FabanClientConfigImpl("","",new URI("http://195.176.181.55:9980/"));
        //FabanClient fc = new FabanClient();//.withConfig(config);
        //System.out.println(fc.status(new RunId("wfmsbenchmark.1Z")).getStatus());

        //assume we want to deploy...

            //we can simply deploy and get the response
//        DeployStatus resp = null;
//        try {
//            resp = client.deploy(new File(""));
//        } catch (JarFileNotFoundException e) {
//            e.printStackTrace();
//        }
//        DeployStatus.Code code = resp.getCode();
//
//        //handle it like this
//        switch(code) {
//            case CONFLICT:
//                break;
//            case NOT_ACCEPTABLE:
//                break;
//            case CREATED:
//                break;
//            default:
//                break;
//        }
//
//        //or handle the response with a lambda function
//        boolean ciao = resp.handle((DeployStatus x) -> { System.out.println(x.getCode()); return true; });
//
//        //or handle the response with a consumer
//        resp.handle((DeployStatus x) -> {
//            System.out.println(x.getCode());
//        });
//
//        //We can also directly pass a callback to the deploy call
//        try {
//            client.deploy(new File(""), (DeployStatus x) -> System.out.println(x.getCode()));
//        } catch (JarFileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        //pass a callback and get a return value
//        try {
//            ciao = client.deploy(new File(""), (DeployStatus x) -> {
//                System.out.println(x.getCode());
//                return true;
//            });
//        } catch (JarFileNotFoundException e) {
//            e.printStackTrace();
//        }


    }

}
