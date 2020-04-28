package com.example.catalog;

import com.example.catalog.persistence.entities.Album;
import com.example.catalog.persistence.entities.Artist;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static com.example.catalog.Constants.GENDER_FEMALE;
import static com.example.catalog.Constants.GENDER_MALE;
import static java.util.Calendar.JANUARY;
import static java.util.Calendar.MAY;

public class TestConstants {

    //<editor-fold desc="Artists">
    public static Artist ARTIST_ADELE() {
        Artist artist = new Artist();
        artist.setId(432414L);
        artist.setGender(GENDER_FEMALE);
        artist.setLabel("XL Recordings");
        artist.setName("Adele");
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(0L);
        dob.set(1988, MAY, 5);
        artist.setDateOfBirth(dob);
        return artist;
    }

    public static Artist ARTIST_JUSTIN_TIMBERLAKE() {
        Artist artist = new Artist();
        artist.setId(1345094L);
        artist.setGender(GENDER_MALE);
        artist.setLabel("Interscope Records");
        artist.setName("Justin Timberlake");
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(0L);
        dob.set(1981, JANUARY, 31);
        artist.setDateOfBirth(dob);
        return artist;
    }

    public static List<Artist> ARTISTS_ALL = Arrays.asList(ARTIST_ADELE(), ARTIST_JUSTIN_TIMBERLAKE());
    //</editor-fold>

    //<editor-fold desc="Albums">
    public static Album ALBUM_ADELE_21() {
        Album album = new Album();
        album.setId(210543L);
        album.setName("21");
        album.setGenre("Pop");
        album.setLabel("XL Recordings");
        album.setNumberOfTracks(12L);
        album.setDownloadCount(3123334L);
        Calendar release = Calendar.getInstance();
        release.setTimeInMillis(0L);
        release.set(2011, JANUARY, 24);
        album.setReleaseDate(release);
        return album;
    }
    //</editor-fold>
}
