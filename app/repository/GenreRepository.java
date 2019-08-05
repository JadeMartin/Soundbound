package repository;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.ArtistGenre;
import models.MusicGenre;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Database access class for tables related to artist genres
 */
public class GenreRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;


    /**
     * Ebeans injector constructor method for Artist repository.
     *
     * @param ebeanConfig The ebeans config which the ebean server will be supplied from
     * @param executionContext the database execution context object for this instance.
     */
    @Inject
    public GenreRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }


    /**
     * Retrieves all genres out of the database
     *
     * @return A list of all retrieved MusicGenre objects
     */
    public List<MusicGenre> getAllGenres() {
        return ebeanServer.find(MusicGenre.class).findList();
    }


    /**
     * Retrieves all genres for a given artist
     *
     * @param artistId database id of artist to find genres for
     * @return List of found MusicGenre objects
     */
    public List<MusicGenre> getArtistGenres(int artistId) {
        List<Integer> genreIds = ebeanServer.find(MusicGenre.class).where().eq("artist", artistId).findIds();
        return ebeanServer.find(MusicGenre.class).where().idIn(genreIds).findList();
    }


    /**
     * Method to insert an entry into the artist_genre link table
     *
     * @param artistId id of artist to link
     * @param genreId id of genre to link
     * @return CompletionStage void
     */
    public CompletionStage<Void> insertArtistGenre(int artistId, int genreId) {
        return supplyAsync(() -> {
            ebeanServer.insert(new ArtistGenre(artistId, genreId));
            return null;
        });
    }


    /**
     * Removes an entry from the artist_genre link table
     *
     * @param artistId id of artist to unlink
     * @param genreId id of genre to unlink
     * @return CompletionStage void
     */
    public CompletionStage<Void> removeArtistGenre(int artistId, int genreId) {
        return supplyAsync(() -> {
            ebeanServer.find(ArtistGenre.class)
                    .where()
                    .eq("artist", artistId)
                    .eq("genre", genreId)
                    .delete();
            return null;
        });
    }

}
