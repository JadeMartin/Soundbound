package repository;

import io.ebean.*;
import models.Image;
import models.Image;
import play.db.ebean.EbeanConfig;
import javax.inject.Inject;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;


public class ImageRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public ImageRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext){
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    public CompletionStage<Integer> insert(Image image){
        return supplyAsync(() -> {
            try {
                ebeanServer.insert(image);
                System.out.println("SUCCESS. ID:" + image.getImageId());
                System.out.println("Type: " + image.getVisible());

            } catch (Exception e) {
                System.out.print(e);
            }
            return image.getImageId();
        }, executionContext);
    }

    /**
     * Update profile in database using Profile model object,
     * and the raw password from an input field, which will be set if it is not null.
     * @param newProfile Profile object with new details
     * @param password String of unhashed password.
     * @return
     */
    public CompletionStage<Optional<Integer>> updateVisibility(Integer id) {
        System.out.println("CALL FROM REPOSITORY");
        return supplyAsync(() -> {
            Transaction txn = ebeanServer.beginTransaction();
            Optional<Integer> value = Optional.empty();
            try {
                Image targetImage = ebeanServer.find(Image.class).setId(id).findOne();
                if (targetImage != null) {
                    if(targetImage.getVisible() == 1) {
                        targetImage.setVisible(0); // Public to private
                    } else {
                        targetImage.setVisible(1); // Private to public
                    }
                    targetImage.update();
                    txn.commit();
                    value = Optional.of(targetImage.getVisible());
                }
            } finally {
                txn.end();
            }
            return value;
        }, executionContext);
    }

    /**
     * Function to get all the images created by the signed in user.
     * @param email user email
     * @return imageList list of images uploaded by the user
     */
    public Optional<List<Image>> getImages(String email) {
        List<Image> imageList =
                ebeanServer.find(Image.class)
                        .where().eq("email", email)
                        .findList();
        return Optional.of(imageList);
    }
}
