import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.*;

import org.junit.Test;

public class TestCases
{
   private static final Song[] songs = new Song[] {
         new Song("Decemberists", "The Mariner's Revenge Song", 2005),
         new Song("Rogue Wave", "Love's Lost Guarantee", 2005),
         new Song("Avett Brothers", "Talk on Indolence", 2006),
         new Song("Gerry Rafferty", "Baker Street", 1998),
         new Song("City and Colour", "Sleeping Sickness", 2007),
         new Song("Foo Fighters", "Baker Street", 1997),
         new Song("Queen", "Bohemian Rhapsody", 1975),
         new Song("Gerry Rafferty", "Baker Street", 1978)
      };

   @Test
   public void testArtistComparator()
   {
      ArtistComparator a1 = new ArtistComparator();
      ArtistComparator a2 = new ArtistComparator();
      ArtistComparator a3 = new ArtistComparator();
      ArtistComparator a4 = new ArtistComparator();
      assertTrue(a1.compare(songs[0], songs[1]) < 0);
      assertTrue(a2.compare(songs[1], songs[2]) > 0);
      assertTrue(a3.compare(songs[3], songs[3]) == 0);
      assertTrue(a4.compare(songs[2], songs[5]) < 0);
   }

   @Test
   public void testLambdaTitleComparator()
   {
      Comparator<Song> title_comp = (s1, s2) -> s1.getTitle().compareTo(s2.getTitle());
      assertTrue(title_comp.compare(songs[0], songs[1]) > 0);
      assertTrue(title_comp.compare(songs[2], songs[2]) == 0);
      assertTrue(title_comp.compare(songs[1], songs[2]) < 0);
      assertTrue(title_comp.compare(songs[6], songs[7]) > 0);

   }

   @Test
   public void testYearExtractorComparator()
   {
      Comparator<Song> year_comp = Comparator.comparing(Song::getYear, Comparator.reverseOrder());
      assertTrue(year_comp.compare(songs[1], songs[2]) > 0);
      assertTrue(year_comp.compare(songs[2], songs[2]) == 0);
      assertTrue(year_comp.compare(songs[2], songs[3]) < 0);
   }

   @Test
   public void testComposedComparator()
   {
      ArtistComparator a1 = new ArtistComparator();
      Comparator<Song> year_comp = Comparator.comparing(Song::getYear, Comparator.reverseOrder());
      ComposedComparator com_comp = new ComposedComparator(a1, year_comp);
      assertTrue(com_comp.compare(songs[3], songs[7]) < 0);
      assertTrue(com_comp.compare(songs[7], songs[7]) == 0);
   }

   @Test
   public void testThenComparing()
   {
      Comparator<Song> title_comp = (s1, s2) -> s1.getTitle().compareTo(s2.getTitle());
      ArtistComparator a1 = new ArtistComparator();
      Comparator<Song> then_comp = title_comp.thenComparing((s1, s2) -> a1.compare(s1, s2));
      assertTrue(then_comp.compare(songs[3], songs[5]) > 0);
   }

   @Test
   public void runSort()
   {
      ArtistComparator a1 = new ArtistComparator();
      Comparator<Song> title_comp = (s1, s2) -> s1.getTitle().compareTo(s2.getTitle());
      Comparator<Song> year_comp = Comparator.comparing(Song::getYear);
      List<Song> songList = new ArrayList<>(Arrays.asList(songs));
      List<Song> expectedList = Arrays.asList(
         new Song("Avett Brothers", "Talk on Indolence", 2006),
         new Song("City and Colour", "Sleeping Sickness", 2007),
         new Song("Decemberists", "The Mariner's Revenge Song", 2005),
         new Song("Foo Fighters", "Baker Street", 1997),
         new Song("Gerry Rafferty", "Baker Street", 1978),
         new Song("Gerry Rafferty", "Baker Street", 1998),
         new Song("Queen", "Bohemian Rhapsody", 1975),
         new Song("Rogue Wave", "Love's Lost Guarantee", 2005)
         );

      songList.sort(a1.thenComparing(title_comp.thenComparing(year_comp))

      );

      assertEquals(songList, expectedList);
   }
}
