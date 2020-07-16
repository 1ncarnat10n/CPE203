import java.util.List;
import java.util.Objects;

class Student
{
   private final String surname;
   private final String givenName;
   private final int age;
   private final List<CourseSection> currentCourses;

   public Student(final String surname, final String givenName, final int age,
      final List<CourseSection> currentCourses)
   {
      this.surname = surname;
      this.givenName = givenName;
      this.age = age;
      this.currentCourses = currentCourses;
   }

   public boolean equals(Object o) {
      if (o != null) {
         if (this.getClass() == o.getClass()) {
            Student s = (Student)o;
            return (Objects.equals(surname, s.surname) && Objects.equals(givenName, s.givenName) && Objects.equals(currentCourses, s.currentCourses) && this.age == s.age);
         }
      }
      return false;
   }

   public int hashCode() {
      return Objects.hashCode(this);
   }

}
