import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class ExampleMap
{
   public static List<String> highEnrollmentStudents(
      Map<String, List<Course>> courseListsByStudentName, int unitThreshold)
   {
      List<String> overEnrolledStudents = new LinkedList<>();

      /*
         Build a list of the names of students currently enrolled
         in a number of units strictly greater than the unitThreshold.
      */
      int units;
      for(Map.Entry<String, List<Course>> key:courseListsByStudentName.entrySet()) {
         List<Course> courses = key.getValue();
         units = 0;
         for (Course classes:courses) {
            units += classes.getNumUnits();
         }
         if (units > unitThreshold) {
            overEnrolledStudents.add(key.getKey());
         }
      }
      return overEnrolledStudents;      
   }
}
