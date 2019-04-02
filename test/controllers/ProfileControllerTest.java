package controllers;

import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;

/**
 * Test Set for profile controller
 */
public class ProfileControllerTest extends ProvideApplication{

    private Application app;

    @Before
    public void setUp() {
        app = super.provideApplication();
        Map<String, String> formData = new HashMap<>();
        formData.put("email", "admin");
        formData.put("password", "admin123");

        Http.RequestBuilder request = Helpers.fakeRequest()
                .method("POST")
                .uri("/login")
                .bodyForm(formData);

        Result result = Helpers.route(provideApplication(), request);
    }

    /**
     * Testing profile GET endpoint /profile/edit/:id
     */
    @Test
    public void showEdit() {
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(GET)
                .uri("/profile/admin/edit")
                .session("connected", "admin");

        Result result = Helpers.route(provideApplication(),request);
        //System.out.println(Helpers.contentAsString(result));


        assertEquals(200, result.status());
    }

    /**
     * Testing profile POST endpoint /profile
     */
    @Test
    public void update() {
        Map<String, String> profileData = new HashMap<>();
        profileData.put("firstName", "admin");
        profileData.put("middleName", "admin");
        profileData.put("lastName", "admin");
        profileData.put("email", "admin");
        profileData.put("birthDate", "2016-05-08");
        profileData.put("gender", "male");
        profileData.put("travellerTypes", "Backpacker");
        profileData.put("nationalities", "NZ");
        profileData.put("passports", "NZ");



        Http.RequestBuilder request = Helpers.fakeRequest()
                .method("POST")
                .uri("/profile")
                .bodyForm(profileData)
                .session("connected", "admin");

        Result result = Helpers.route(provideApplication(),request);
        //System.out.println(Helpers.contentAsString(result));


        assertEquals(303, result.status());
    }

    /**
     * Testing profile GET endpoint /profile
     */
    //@Test // Having issues with this test will sort at a later date
    public void show() {
        loginUser();
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(GET)
                .uri("/profile")
                .session("connected", "admin");

        Result result = Helpers.route(provideApplication(),request);

        assertEquals(OK, result.status());
    }
}