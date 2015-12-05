package frontend;


import frontend.resthelper.RestHelper;

//@ComponentScan
//@Configuration
//@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {

        //SpringApplication.run(Application.class, args);

        RestHelper accessor = new RestHelper();

        //System.out.println("Reading ALL Travelers: ");
        //accessor.getTravelers();
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
