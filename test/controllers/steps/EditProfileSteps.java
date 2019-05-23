package controllers.steps;


import controllers.ProvideApplication;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import models.Profile;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

/**
 * This class provides the steps need to test the EditProfile feature
 */
public class EditProfileSteps extends ProvideApplication {

    private Map<String, String> loginForm = new HashMap<>();
    private Map<String, String> editForm = new HashMap<>();

    private Result redirectResultEdit;
    private Result loginResult;


    // Scenario: I can perform an editDestinations of my profile - start
    @Given("I am logged into the application")
    public void iAmLoggedIntoTheApplication() {
        loginForm.put("email", "john@gmail.com");
        loginForm.put("password", "password");

        Http.RequestBuilder request = Helpers.fakeRequest()
                .method("POST")
                .uri("/login")
                .bodyForm(loginForm);

        loginResult = Helpers.route(provideApplication(), request);
    System.out.println(Profile.find.all().get(0).getEmail());
    System.out.println(loginResult.redirectLocation());
        assertEquals(303, loginResult.status());
    }

    @Given("I am on the edit profile page")
    public void iAmOnTheEditProfilePage() {
        // Mocking auto fill operation of users current data
        editForm.put("firstName", "John");
        editForm.put("lastName", "James");
        editForm.put("email", "john@gmail.com");
        editForm.put("password", "password");
        editForm.put("birthDate", "1970-01-13");
        editForm.put("passports", "NZ");
        editForm.put("gender", "Male");
        editForm.put("nationalities", "password");
        editForm.put("travellerTypes", "Backpacker,Gap Year");

        assertEquals("/profile", loginResult.redirectLocation().get());
    }

    @When("I change my first name to {string}")
    public void iChangeMyFirstNameTo(String string) {
        editForm.put("firstName", string);
    }

    @When("I change my traveller types to {string}")
    public void iChangeMyTravellerTypesTo(String string) {
        editForm.put("travellerTypes", string);
    }

    @When("I change my middle name to {string}")
    public void iChangeMyMiddleNameTo(String string) {
        editForm.put("middleName", string);
    }

    @When("I press the Save button")
    public void iPressTheSaveButton() {
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method("POST")
                .uri("/profile")
                .bodyForm(editForm)
                .session("connected", "1");

        redirectResultEdit = Helpers.route(provideApplication(), request);
    }

    @Then("I am redirected to my profile page")
    public void iAmRedirectedToMyProfilePage() {
        assertEquals(303, redirectResultEdit.status());
        assertEquals("/profile", redirectResultEdit.redirectLocation().get());
    }

    @Then("My new profile data is saved")
    public void myNewProfileDataIsSaved() {
        Profile profile = Profile.find.byId(1);
        if (profile == null) {
            fail();
        }
        assertEquals("Jenny", profile.getFirstName());
        assertEquals("Backpacker, Thrillseeker", profile.getTravellerTypes());
        assertEquals("Max", profile.getMiddleName());
    }
    // Scenario: I can perform an editDestinations of my profile - end

    // Scenario: I cannot save my profile with no traveller types - end
    // Includes steps from above
    @When("I try to save the edit")
    public void iTryToSaveTheEdit() {
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method("POST")
                .uri("/profile")
                .bodyForm(editForm)
                .session("connected", "1");
        try {
            redirectResultEdit = Helpers.route(provideApplication(), request);
            fail();
        } catch (IllegalStateException e) {
            assertTrue(true);
        }

    }

    @Then("I am not redirected to the profile page")
    public void iAmNotRedirectedToTheProfilePage() {
        assertNull(redirectResultEdit);
    }

    @Then("my edit is not saved")
    public void myEditIsNotSaved() {
        Profile profile = Profile.find.byId(1);
        if (profile == null) {
            fail();
        }
        assertEquals("Backpacker, Thrillseeker", profile.getTravellerTypes());
    }
}
