package controllers;

import models.*;
import org.junit.Before;
import play.Application;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;
import repository.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProvideApplication extends WithApplication {

    protected DestinationRepository destinationRepository;
    protected PhotoRepository photoRepository;
    protected ProfileRepository profileRepository;
    protected TripDestinationsRepository tripDestinationsRepository;
    protected TripRepository tripRepository;
    protected NationalityRepository nationalityRepository;
    protected PassportCountryRepository passportRepository;

    private static boolean setUpComplete = false;


    @Override
    public Application provideApplication() {
        return new GuiceApplicationBuilder().in(Mode.TEST).build();
    }


    void loginUser() {
        Map<String, String> formData = new HashMap<>();
        formData.put("email", "john@gmail.com");
        formData.put("password", "password");

        Http.RequestBuilder request = Helpers.fakeRequest()
                .method("POST")
                .uri("/login")
                .bodyForm(formData);

        Result result = Helpers.route(provideApplication(), request);
    }

    @Before
    public void setUpDb() {
        profileRepository = app.injector().instanceOf(ProfileRepository.class);
        destinationRepository = app.injector().instanceOf(DestinationRepository.class);
        photoRepository = app.injector().instanceOf(PhotoRepository.class);
        tripDestinationsRepository = app.injector().instanceOf(TripDestinationsRepository.class);
        tripRepository = app.injector().instanceOf(TripRepository.class);
        nationalityRepository = app.injector().instanceOf(NationalityRepository.class);
        passportRepository = app.injector().instanceOf(PassportCountryRepository.class);


        passportRepository.insert(new PassportCountry("Australia"));
        nationalityRepository.insert(new Nationality("Yeet"));
        nationalityRepository.insert(new Nationality("UK"));
        nationalityRepository.insert(new Nationality("NZ"));
        nationalityRepository.insert(new Nationality("EU"));
        nationalityRepository.insert(new Nationality("USA"));
        passportRepository.insert(new PassportCountry("New Zealand"));


        profileRepository.insert(new Profile("John", "James", "john@gmail.com",
                "password", new Date(), "NZ", "Male", new Date(), "NZ",
                "Backpacker,GapYear", new ArrayList<Trip>(), false)).thenApplyAsync(id -> {
            if (id.isPresent()) {
                destinationRepository.insert(new Destination(id.get(), "China", "Country", "China", "China", 67.08, 102.75, 0));
                destinationRepository.insert(new Destination(id.get(), "Rome", "City", "Italy", "Rome", 69.08, 109.75, 1));
            }
            return "done";
        });
        if (!setUpComplete) {

            setUpComplete = true;
        }
    }

    public ArrayList<Destination> getUserDest(int id) {
        return destinationRepository.getUserDestinations(id);
    }


}
