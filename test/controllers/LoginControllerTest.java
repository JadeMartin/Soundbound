package controllers;

import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;

/**
 * Test set for Login Controller
 */
public class LoginControllerTest {

    /**
     * Testing login POST endpoint /login
     */
    @Test
    public void login() {
        Map<String, String> formData = new HashMap<>();
        formData.put("email", "john@gmail.com");
        formData.put("password", "password");

        Http.RequestBuilder request = Helpers.fakeRequest()
                .method("POST")
                .uri("/login")
                .bodyForm(formData);

        Result result = Helpers.route(TestApplication.getApplication(), request);

        assertEquals(303, result.status());

    }

    /**
     * Testing login GET endpoint /
     */
    @Test
    public void loadLoginOnStart(){
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(GET)
                .uri("/");
        Result result = Helpers.route(TestApplication.getApplication(),request);

        assertEquals(OK, result.status());
    }

    /**
     * Testing login GET endpoint /login
     */
    @Test
    public void show(){
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(GET)
                .uri("/login");
        Result result = Helpers.route(TestApplication.getApplication(),request);

        assertEquals(OK, result.status());
    }
}
