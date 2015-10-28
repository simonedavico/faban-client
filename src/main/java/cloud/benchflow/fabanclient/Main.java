package cloud.benchflow.fabanclient;

import cloud.benchflow.fabanclient.responses.DeployStatus;

import java.io.File;

/**
 * Created by simonedavico on 26/10/15.
 */
public class Main {

    public static void main(String[] args) {

        //get an instance of faban client
        FabanClient client = new FabanClient();

        //assume we want to deploy...

            //we can simply deploy and get the response
        DeployStatus resp = client.deploy(new File(""));
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
        client.deploy(new File(""), (DeployStatus x) -> System.out.println(x.getCode()));

        //pass a callback and get a return value
        ciao = client.deploy(new File(""), (DeployStatus x) -> {
            System.out.println(x.getCode());
            return true;
        });


    }

}
