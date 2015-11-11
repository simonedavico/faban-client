package cloud.benchflow.faban.client;

import cloud.benchflow.faban.client.exceptions.JarFileNotFoundException;
import cloud.benchflow.faban.client.responses.DeployStatus;

import java.io.File;

/**
 * @author Simone D'Avico <simonedavico@gmail.com>
 *
 * Created on 26/10/15.
 */
public class Main {

    public static void main(String[] args) {

        //get an instance of faban client
        FabanClient client = new FabanClient();

        //assume we want to deploy...

            //we can simply deploy and get the response
        DeployStatus resp = null;
        try {
            resp = client.deploy(new File(""));
        } catch (JarFileNotFoundException e) {
            e.printStackTrace();
        }
        DeployStatus.Code code = resp.getCode();

        //handle it like this
        switch(code) {
            case CONFLICT:
                break;
            case NOT_ACCEPTABLE:
                break;
            case CREATED:
                break;
            default:
                break;
        }

        //or handle the response with a lambda function
        boolean ciao = resp.handle((DeployStatus x) -> { System.out.println(x.getCode()); return true; });

        //or handle the response with a consumer
        resp.handle((DeployStatus x) -> {
            System.out.println(x.getCode());
        });

        //We can also directly pass a callback to the deploy call
        try {
            client.deploy(new File(""), (DeployStatus x) -> System.out.println(x.getCode()));
        } catch (JarFileNotFoundException e) {
            e.printStackTrace();
        }

        //pass a callback and get a return value
        try {
            ciao = client.deploy(new File(""), (DeployStatus x) -> {
                System.out.println(x.getCode());
                return true;
            });
        } catch (JarFileNotFoundException e) {
            e.printStackTrace();
        }


    }

}
