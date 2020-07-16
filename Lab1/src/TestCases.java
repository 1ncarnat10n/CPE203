import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestCases
{
   private static final double DELTA = 0.00001;

   @Test
   public void testSimpleIf1()
   {
      assertEquals(1.7, SimpleIf.max(1.2, 1.7), DELTA);
   }

   @Test
   public void testSimpleIf2()
   {
      assertEquals(9.0, SimpleIf.max(9.0, 3.2), DELTA);
   }

   @Test
   public void testSimpleIf3()
   {
      assertEquals(-2.0, SimpleIf.max(-5.0, -2.0), DELTA);
   }

   @Test
   public void testSimpleIf4()
   {
      assertEquals(0, SimpleIf.max(0, 0), DELTA);
   }

   @Test
   public void testSimpleLoop1()
   {
      assertEquals(7, SimpleLoop.sum(3, 4));
   }

   @Test
   public void testSimpleLoop2()
   {
      assertEquals(7, SimpleLoop.sum(-2, 9));
   }

   @Test
   public void testSimpleLoop3()
   {
      assertEquals(0, SimpleLoop.sum(-5, 5));
      assertEquals(0, SimpleLoop.sum(5, -4));
   }

   @Test
   public void testSimpleArray1()
   {
      /* What are those parameters?  They are newly allocated arrays
         with initial values. */
      assertArrayEquals(
         new int[] {1, 4, 9},
         SimpleArray.squareAll(new int[] {1, 2, 3}));
   }

   @Test
   public void testSimpleArray2()
   {
      assertArrayEquals(
         new int[] {49, 25},
         SimpleArray.squareAll(new int[] {7, 5}));
   }

   @Test
   public void testSimpleArray3()
   {
      assertArrayEquals(new int[] {4, 0, 16}, SimpleArray.squareAll(new int[] {-2, 0, 4}));
   }

   @Test
   public void testSimpleList1()
   {
      List<Integer> input =
         new LinkedList<Integer>(Arrays.asList(new Integer[] {1, 2, 3}));
      List<Integer> expected =
         new ArrayList<Integer>(Arrays.asList(new Integer[] {1, 4, 9}));

      assertEquals(expected, SimpleList.squareAll(input));
   }

   @Test
   public void testSimpleList2()
   {
      List<Integer> input = new LinkedList<Integer>(Arrays.asList(new Integer[] {0, -1, 10, 6}));
      List<Integer> expected = new ArrayList<Integer>(Arrays.asList(new Integer[] {0, 1, 100, 36}));
      assertEquals(expected, SimpleList.squareAll(input));
   }

   @Test
   public void testBetterLoop1()
   {
      assertTrue(BetterLoop.contains(new int[] {7, 5}, 5));
   }

   @Test
   public void testBetterLoop2()
   {
      assertTrue(BetterLoop.contains(new int[] {7, 5, 2, 4}, 4));
   }

   @Test
   public void testBetterLoop3()
   {
      assertFalse(BetterLoop.contains(new int[] {0, 2, 4, 6},5));
   }

   @Test
   public void testExampleMap1()
   {
      List<String> expected = Arrays.asList("Julie", "Zoe");
      Map<String, List<Course>> courseListsByStudent = new HashMap<>();

      courseListsByStudent.put("Julie",
         Arrays.asList(
            new Course("CPE 123", 4),
            new Course("CPE 101", 4),
            new Course("CPE 202", 4),
            new Course("CPE 203", 4),
            new Course("CPE 225", 4)));
      courseListsByStudent.put("Paul",
         Arrays.asList(
            new Course("CPE 101", 4),
            new Course("CPE 202", 4),
            new Course("CPE 203", 4),
            new Course("CPE 225", 4)));
      courseListsByStudent.put("Zoe",
         Arrays.asList(
            new Course("CPE 123", 4),
            new Course("CPE 203", 4),
            new Course("CPE 471", 4),
            new Course("CPE 473", 4),
            new Course("CPE 476", 4),
            new Course("CPE 572", 4)));

      /*
       * Why compare HashSets here?  Just so that the order of the
       * elements in the list is not important for this test.
       */
      assertEquals(new HashSet<>(expected),
         new HashSet<>(ExampleMap.highEnrollmentStudents(
            courseListsByStudent, 16)));
   }

   @Test
   public void testExampleMap2()
   {
      List<String> expected = Arrays.asList("Adrian", "Josh", "Chris");
      Map<String, List<Course>> courseListsByStudent = new HashMap<>();

      courseListsByStudent.put("Adrian",
              Arrays.asList(
                      new Course("CPE 123", 2),
                      new Course("CPE 101", 2),
                      new Course("CPE 202", 3),
                      new Course("CPE 203", 4),
                      new Course("CPE 225", 4)));
      courseListsByStudent.put("Josh",
              Arrays.asList(
                      new Course("CHEM 100", 4),
                      new Course("CHEM 223", 4),
                      new Course("CHEM 345", 5),
                      new Course("CHEM 406", 4)));
      courseListsByStudent.put("Chris",
              Arrays.asList(
                      new Course("MATH 123", 4),
                      new Course("MATH 203", 4),
                      new Course("MATH 471", 4),
                      new Course("MATH 473", 2),
                      new Course("MATH 476", 2),
                      new Course("MATH 572", 3)));

      assertEquals(new HashSet<>(expected),
              new HashSet<>(ExampleMap.highEnrollmentStudents(
                      courseListsByStudent, 12)));
   }
}
