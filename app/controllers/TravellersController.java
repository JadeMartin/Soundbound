package controllers;

import models.PartnerFormData;
import models.Profile;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is the controller for the travellers.scala.html file, it provides the route to the
 * travellers page
 */
public class TravellersController extends Controller {


    private final Form<PartnerFormData> form;
    private MessagesApi messagesApi;

    @Inject
    public TravellersController(FormFactory formFactory, MessagesApi messagesApi) {
        this.form = formFactory.form(PartnerFormData.class);
        this.messagesApi = messagesApi;
    }

    /**
     * Function to search travellers n gender nationality, age and traveller type fields, calls search functions for
     * each field
     * @param request http request
     * @return renders traveller view with queried list of travellers
     */
    public Result search(Http.Request request){
        Form<PartnerFormData> searchForm = form.bindFromRequest(request);
        PartnerFormData searchData = searchForm.get();
        List<Profile> resultData = Profile.find.all();
        //todo change queryProfiles to be list of profiles generated by search functionality
        if (searchForm.get().searchGender != ""){
            resultData = listGender(searchData);
        }
        if (searchForm.get().searchNationality != ""){
            resultData = searchNat(resultData, searchData);
        }

        if (searchForm.get().searchAgeRange != 0){
            resultData = searchAge(resultData, searchData);
        }

        if (searchForm.get().searchTravellerTypes != ""){
            resultData = searchTravelTypes(resultData, searchData);
        }

        return ok(travellers.render(form, resultData, request, messagesApi.preferred(request)));
    }

    /**
     * Removes Nationalities from result list
     * @param resultData current list to return
     * @param searchData Form holding search terms
     * @return queried list including nationality search
     */
    public List<Profile> searchNat(List<Profile> resultData, PartnerFormData searchData){
        return null;
    }

    /**
     * Removes ages from result list
     * @param resultData current list to return
     * @param searchData Form holding search terms
     * @return queried list including age search
     */
    public List<Profile> searchAge(List<Profile> resultData, PartnerFormData searchData){
        return null;
    }

    /**
     * Removes traveller types from result list
     * @param resultData current list to return
     * @param searchData Form holding search terms
     * @return queried list including traveller types search
     */
    public List<Profile> searchTravelTypes(List<Profile> resultData, PartnerFormData searchData){
        List<Profile> resultProfiles = new ArrayList<>();

        System.out.println("Partner Data " + searchData.searchTravellerTypes);
        String travellerTypeTerm = searchData.searchTravellerTypes;

        System.out.println("Traveller search String " + travellerTypeTerm);

        if (!travellerTypeTerm.equals("")) {
            for (Profile profile : resultData) {
                if (profile.getTravellerTypes().contains(travellerTypeTerm)) {
                    resultProfiles.add(profile);
                }
            }
        } else {
            resultProfiles = resultData;
        }
        return resultProfiles;
    }

    /**
     * Method to search for travel partners (profiles) with a search term. The search term can be any of the following attributes:
     * nationality, gender, age range, type of traveller.
     * @param searchForm
     * @return return list of profiles
     */
    public List<Profile> listGender(PartnerFormData searchForm) {
        List<Profile> resultProfiles = new ArrayList<>();
        List<Profile> profiles = Profile.find.all();

        System.out.println("Partner Data " + searchForm.searchGender);
        String genderTerm = searchForm.searchGender;
//        String travellerTypeTerm = searchForm.searchTravellerTypes;

        System.out.println("Gender search String " + genderTerm);

        if (!genderTerm.equals("")) {
            for (Profile profile : profiles) {
                if (profile.getGender().contains(genderTerm)) {
                    resultProfiles.add(profile);
                }
            }
        } else {
            resultProfiles = profiles;
        }
        return resultProfiles;
    }

    /**
     * This method shows the travellers page on the screen
     * @return
     */
    public Result show(Http.Request request) {
        List<Profile> profiles = Profile.find.all();
        return ok(travellers.render(form, profiles, request, messagesApi.preferred(request)));
    }
}
/**
 * Method to load up search form page and pass through the input form and https request for use in the listOne method
 * @param request an HTTP request that will be sent with the function call
 * @return a rendered view of the search profile form
 *
public Result searchProfile(Http.Request request) {
Form<SearchFormData> profileForm = formFactory.form(SearchFormData.class);
return ok(views.html.searchProfileForm.render(profileForm, request));
}*/

/**
 * Method to load up search form page and pass through the input form and https request for use in the listPartner method
 * @param request an HTTP request that will be sent with the function call
 * @return
 *
public Result searchPartner(Http.Request request) {
Form<PartnerFormData> partnerForm = formFactory.form(PartnerFormData.class);
return ok(views.html.searchPartnerForm.render(partnerForm, request));
}*/

/**
 * Display one profile based on user input (email)
 * @param request an HTTP request that will be sent with the function call
 * @return a rendered view of one profile and all its attributes
 *
public Result listOne(Http.Request request) {
Form<SearchFormData> profileForm = formFactory.form(SearchFormData.class).bindFromRequest(request);
SearchFormData profileData = profileForm.get();
Profile userProfile = Profile.find.byId(profileData.email);

if (userProfile == null) {
return notFound("Profile not found!");
}
return ok(views.html.displayProfile.render(userProfile));
}*/
