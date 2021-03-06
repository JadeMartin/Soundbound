package controllers.steps.Destinations;

import controllers.TestApplication;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import models.Destination;
import org.junit.Assert;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PublicDestinationSteps {
    private Map<String, String> destForm = new HashMap<>();
    private Result redirectDestination;


    @When("he fills in name with {string}")
    public void heFillsInNameWith(String string) { destForm.put("name", string); }

    @When("he fills in type with {string}")
    public void heFillsInTypeWith(String string) { destForm.put("type", string); }

    @When("he fills in country with {string}")
    public void heFillsInCountryWith(String string) { destForm.put("country", string); }

    @When("he presses save")
    public void hePressesSave() {
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method("POST")
                .uri("/destinations")
                .bodyForm(destForm)
                .session("connected", "1");
        redirectDestination = Helpers.route(TestApplication.getApplication(), request);
        assertEquals(303, redirectDestination.status());
    }

    @Then("he is redirected to the create destination page and destination is not saved")
    public void heIsRedirectedToTheCreateDestinationPageAndDestinationIsNotSaved() {
//        assertEquals(303, redirectDestination.status());
//        assertEquals("/destinations/create", redirectDestination.redirectLocation().get());
    }

    @Given("user with {string} has a private destination with name {string}, type {string}, and country {string}")
    public void johnJamesHasPrivateDestination(String profileId, String name, String type, String country) {
        Destination destination = new Destination();
        int id = Integer.parseInt(profileId);
        destination.setProfileId(id);
        destination.setName(name);
        destination.setType(type);
        destination.setCountry(country);
        TestApplication.getDestinationRepository().insert(destination);
    }

    @Given("user creates a public destination with name {string}, type {string}, and country {string}")
    public void createPublicDestination(String name, String type, String country) {
        Destination destination = new Destination();
        destination.setProfileId(1);
        destination.setName(name);
        destination.setType(type);
        destination.setCountry(country);
        destination.setVisible(1);
        TestApplication.getDestinationRepository().insert(destination);
    }


    @Then("user with id {string} private destination with name {string}, type {string}, and country {string} doesnt exist")
    public void steveMillersProfileDoesntExist(String profileId, String name, String type, String country) {
        int id = Integer.parseInt(profileId);
        List<Destination> destinationList = TestApplication.getDestinationRepository().getUserDestinations(id);
        for (Destination destination : destinationList) {
            List<String> destDetails = new ArrayList<>();
            destDetails.add(destination.getName());
            destDetails.add(destination.getType());
            destDetails.add(destination.getCountry());
            List<String> oldDest = new ArrayList<>();
            oldDest.add(name);
            oldDest.add(type);
            oldDest.add(country);
            assertNotEquals(destDetails, oldDest);
        }
    }

    @Then("user with id {string} is following the new public destination with name {string}, type {string}, and country {string}")
    public void steveMillerFollowingNewPublicDest(String profileId, String name, String type, String country) {
        int id = Integer.parseInt(profileId);
        Optional<ArrayList<Integer>> optionalListDests = TestApplication.getDestinationRepository().getFollowedDestinationIds(id, 0);
        if (optionalListDests.isPresent()) {
            ArrayList<Integer> listDests = optionalListDests.get();
            boolean match = false;
            for (Integer destId: listDests) {
                Destination destination = TestApplication.getDestinationRepository().lookup(destId);
                if (destination.getName().equals(name) && destination.getType().equals(type) && destination.getCountry().equals(country)
                        && destination.getVisible() == 1) {
                    match = true;
                }
            }
            assertEquals(true, match);
        } else {
            Assert.fail();
        }
    }

    @When("user with id {string} updates his private destination with name {string}, type {string}, and country {string} to be public")
    public void updatePrivateDestToBePublic(String profileId, String name, String type, String country) {
        Destination destination = new Destination();
        int id = Integer.parseInt(profileId);
        destination.setProfileId(id);
        destination.setName(name);
        destination.setType(type);
        destination.setCountry(country);
        destination.setVisible(1);
        TestApplication.getDestinationRepository().update(destination, destination.getDestinationId());
    }

    @And("^he does not fill out a traveller type$")
    public void heDoesNotFillOutATravellerType() throws Throwable {
        destForm.put("travellerTypesStringDest", "");
    }
}
