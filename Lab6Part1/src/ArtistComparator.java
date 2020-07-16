import java.util.Comparator;

public class ArtistComparator implements Comparator<Song> {
    public int compare(Song s1, Song s2) {
        return s1.getArtist().compareTo(s2.getArtist());

    }
}
