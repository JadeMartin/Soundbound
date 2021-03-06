package repository;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.SqlUpdate;
import io.ebean.Transaction;
import models.DestinationPhoto;
import models.Photo;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * This class provides database access methods for handling PersonalPhotos
 */
public class DestinationPhotoRepository implements ModelUpdatableRepository<DestinationPhoto> {
    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;
    private final PhotoRepository photoRepository;

    @Inject
    public DestinationPhotoRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext, PhotoRepository photoRepository){
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
        this.photoRepository = photoRepository;
    }


    /**
     * Update an existing destination photo in the personal_photo table
     * Updates the profile id, the photo id, and destination id
     * @param photo destination photo that has had the changes applied
     * @param id id of the destination photo entry to be updated.
     * @return an optional of the destination photo image id that has been updated
     */
    public CompletionStage<Optional<Integer>> update(DestinationPhoto photo, int id) {
        return supplyAsync(() -> {
            Transaction txn = ebeanServer.beginTransaction();
            String updateQuery = "UPDATE destination_photo SET profile_id = ? and photo_id = ? and destination_id = ? WHERE destination_photo_id = ?";
            Optional<Integer> value = Optional.empty();
            try{
                if (ebeanServer.find(DestinationPhoto.class).setId(id).findOne() != null) {
                    SqlUpdate query = Ebean.createSqlUpdate(updateQuery);
                    query.setParameter(1, photo.getProfileId());
                    query.setParameter(2, photo.getPhotoId());
                    query.setParameter(2, photo.getDestinationId());
                    query.setParameter(3, id);
                    query.execute();
                    txn.commit();
                    value = Optional.of(id);
                }
            } finally {
                txn.end();
            }
            return value;
        }, executionContext);
    }

    /**
     * Delete an existing destination photo in the personal_photo table
     * @param id id of the destination photo to be deleted.
     * @return an optional of the destination photo image id that has been deleted
     */
    public CompletionStage<Optional<Integer>> delete(int id) {
        return supplyAsync(() -> {
            Transaction txn = ebeanServer.beginTransaction();
            String deleteQuery = "delete from destination_photo where destination_photo_id = ?";
            SqlUpdate query = Ebean.createSqlUpdate(deleteQuery);
            query.setParameter(1, id);
            query.execute();
            txn.commit();
            return Optional.of(id);
        }, executionContext);
    }

    /**
     * Inserts a new destination photo into the database
     *
     * @param photo the destination photo to be inserted
     * @return Optional of the destination photo id in a completion stage
     */
    public CompletionStage<Optional<Integer>> insert(DestinationPhoto photo) {
        return supplyAsync(() -> {
            try {
                ebeanServer.insert(photo);
            } catch (Exception e) {
                e.printStackTrace();
            }
           return Optional.of(photo.getDestinationPhotoId());
        }, executionContext);
    }

    /**
     * Method to find a certain destination photo by id
     *
     * @param id id of the entry to be found.
     * @return Optional DestiantionPhoto wrapped in a completion stage
     */
    public CompletionStage<Optional<DestinationPhoto>> findById(int id) {
        return supplyAsync(() -> Optional.ofNullable(ebeanServer.find(DestinationPhoto.class).where().eq("destination_photo_id", id).findOne()));
    }

    public Optional<DestinationPhoto> findByProfileIdPhotoIdDestId(int profileId, int photoId, int destinationId) {
        return Optional.ofNullable(ebeanServer.find(DestinationPhoto.class).where()
                .eq("profile_id", profileId)
                .eq("destination_id", destinationId)
                .eq("photo_id", photoId)
                .findOne());
    }

    /**
     * Method to find all destination photos
     *
     * @return Optional Map holding all destinationPhotos found
     */
    public Optional<List<Photo>> getAllDestinationPhotos() {
        List<DestinationPhoto> destPhotos = ebeanServer.find(DestinationPhoto.class).findList();
        List<Photo> photos = new ArrayList<>();
        for (DestinationPhoto destPhoto : destPhotos) {
            photoRepository.getImage(destPhoto.getPhotoId()).ifPresent(photos::add);
        }
        return Optional.of(photos);
    }


    /**
     * Logic to check if a photo is linked to another destination
     * @param profileId Id of the profile linked to a photo
     * @param photoId id of the photo being linked
     * @param destinationId Id of the destination that the photo and profile is linked to
     * @return
     */
    public boolean isLinkedToDestByOtherUser(int profileId, int photoId, int destinationId) {
        List<DestinationPhoto>  destPhotoList = ebeanServer.find(DestinationPhoto.class).where()
                .eq("photo_id", photoId)
                .eq("destination_id", destinationId)
                .findList();
        if (!destPhotoList.isEmpty()) {
            if (destPhotoList.size() > 1) {
                return true;
            }
            return destPhotoList.get(0).getProfileId() != profileId;
        }
        return false;
    }
}