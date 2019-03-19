package controllers;
import models.Destination;
import models.Profile;
import play.i18n.MessagesApi;

import play.data.FormFactory;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import repository.DestinationRepository;
import repository.ProfileRepository;
import views.html.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import javax.inject.Singleton;


public class DestinationsController extends Controller {

    private MessagesApi messagesApi;
    private List<Destination> destinationsList = new ArrayList<>();
    private final Form<Destination> form;
    private final Form<Profile> userForm;
    private final DestinationRepository destinationRepository;
    private final ProfileRepository profileRepository;
    private final SessionController sessionController = new SessionController();


    @Inject
    public DestinationsController(FormFactory formFactory, MessagesApi messagesApi, DestinationRepository destinationRepository, ProfileRepository profileRepository) {
        this.form = formFactory.form(Destination.class);
        this.userForm = formFactory.form(Profile.class);
        this.messagesApi = messagesApi;
        this.destinationRepository = destinationRepository;
        this.profileRepository = profileRepository;
    }

    /**
     * Displays a page showing the destinations to the user
     * @param request
     * @return the list of destinations
     */
    public Result show(Http.Request request) {
        Profile user = sessionController.getCurrentUser(request);
        Optional<ArrayList<Destination>> destListTemp = profileRepository.getDestinations(user.getEmail());
        try {
          destinationsList = destListTemp.get();
        } catch(NoSuchElementException e) {
           destinationsList = new ArrayList<Destination>();
        }
        System.out.println("hi"+destinationsList+user.getEmail());
        return ok(destinations.render(destinationsList, request, messagesApi.preferred(request)));
    }

    /**
     * Displays a page to create a destination
     * @param request
     * @return
     */
    public Result showCreate(Http.Request request) {
        Destination dest = new Destination();
        dest.setLatitude(0.0);
        dest.setLongitude(0.0);
        Form<Destination> destinationForm = form.fill(dest);
        return ok(createDestinations.render(destinationForm, request, messagesApi.preferred(request)));
    }

    /**
     * This method displays the edit page for the destinations to the user
     * @param request
     * @param id
     * @return
     */
    public Result edit(Http.Request request, Integer id) {
        Destination destination =  new Destination();
        for (Destination dest : destinationsList) {
            if (dest.getDestinationId() == id) {
                destination = dest;
                break;
            }
        }
        //System.out.println("yo: "+destination.getName());
        Form<Destination> destinationForm = form.fill(destination);
        return ok(edit.render(id, destinationForm, request, messagesApi.preferred(request)));
    }

    /**
     * This method updates destination in the database
     * @param request
     * @param id The ID of the destination to edit.
     * @return
     */
    public Result update(Http.Request request, Integer id){
        Form<Destination> destinationForm = form.bindFromRequest(request);
        Destination dest = destinationForm.get();
        destinationRepository.update(dest, id);
        //destinationsList.add(dest);
        return redirect(routes.DestinationsController.show());
    }

    /**
     * Adds a new destination to the database
     * @param request
     * @return
     */
    public Result saveDestination(Http.Request request) {
        Profile user = sessionController.getCurrentUser(request);
        if (user == null) {
            // redirect to log in
            return ok(createUser.render(userForm, request, messagesApi.preferred(request)));
        }
        Form<Destination> destinationForm = form.bindFromRequest(request);
       // if (destinationForm.hasErrors()) {
            // show on UI
        //}
        Destination destination = destinationForm.get();
        destination.setUserEmail(user.getEmail());
        destinationRepository.insert(destination);
        // Run insert db operation, then redirect
        return redirect(routes.DestinationsController.show());
    }

    /**
     * Deletes a destination in the database
     * @param id ID of the destination to delete
     * @return
     */
    public Result delete(Http.Request request, Integer id) {
        System.out.println("DELETED");
        Profile profile = sessionController.getCurrentUser(request);
        destinationRepository.delete(id);
        return redirect(routes.DestinationsController.show());

    }

}
