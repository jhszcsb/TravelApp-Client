package frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan
@Configuration
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

        RestUrlAccessor accessor = new RestUrlAccessor();

        System.out.println("Reading ALL Travelers: ");
        accessor.getTravelers();
        //System.out.println("Reading Traveler by ID: ");
        //accessor.getTravelerById("1");
        //System.out.println("Creating new Traveler: ");
        //accessor.createNewTraveler();
        //System.out.println("Updating Traveler by ID: ");
        //accessor.updateTraveler("1");
        //System.out.println("Delete Traveler by ID: ");
        //accessor.deleteTraveler("4");

    }
}
