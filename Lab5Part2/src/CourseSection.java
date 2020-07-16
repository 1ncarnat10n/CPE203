import java.time.LocalTime;

class CourseSection
{
   private final String prefix;
   private final String number;
   private final int enrollment;
   private final LocalTime startTime;
   private final LocalTime endTime;

   public CourseSection(final String prefix, final String number,
      final int enrollment, final LocalTime startTime, final LocalTime endTime)
   {
      this.prefix = prefix;
      this.number = number;
      this.enrollment = enrollment;
      this.startTime = startTime;
      this.endTime = endTime;
   }

   public boolean equals(Object o) {
      boolean enroll = false;
      boolean num_eq = false;
      boolean st_eq = false;
      boolean end_eq = false;
      boolean pre_eq = false;
      if (o == null) {
         return false;
      }
      if (o.getClass() != this.getClass()) {
         return false;
      }
      CourseSection c = (CourseSection)o;
      if (prefix == null) {
         if (c.prefix != null) {
            return false;
         }
         pre_eq = true;
      }
      if (prefix != null) {
         if (c.prefix == null) {
            return false;
         }
         if (c.prefix.equals(this.prefix)) {
            pre_eq = true;
         }
      }

      if (number == null) {
         if (c.number != null) {
            return false;
         }
         num_eq = true;
      }
      if (number != null) {
         if (c.number == null) {
            return false;
         }
         if (c.number.equals(this.number)) {
            num_eq = true;
         }
      }

      if (startTime == null) {
         if (c.startTime != null) {
            return false;
         }
         st_eq = true;
      }
      if (startTime != null) {
         if (c.startTime == null) {
            return false;
         }
         if (c.startTime.equals(this.startTime)) {
            st_eq = true;
         }
      }

      if (endTime == null) {
         if (c.endTime != null) {
            return false;
         }
         end_eq = true;
      }
      if (endTime != null) {
         if (c.endTime == null) {
            return false;
         }
         if (c.endTime.equals(this.endTime)) {
            end_eq = true;
         }
      }

      if (this.enrollment == c.enrollment) {
         enroll = true;
      }
      return (pre_eq && num_eq && st_eq && end_eq && enroll);

   }

   public int hashCode() {
      int prefix_code = 0;
      int number_code = 0;
      int enrollment_code = 0;
      int startTime_code = 0;
      int endTime_code = 0;

      if (prefix != null) {
         prefix_code = prefix.hashCode();
      }
      if (number != null) {
         number_code = number.hashCode();
      }
      if ((Integer)enrollment != null) {
         enrollment_code = ((Integer)enrollment).hashCode();
      }
      if (startTime != null) {
         startTime_code = startTime.hashCode();
      }
      if (endTime != null) {
         endTime_code = endTime.hashCode();
      }
      return prefix_code + number_code + enrollment_code + startTime_code + endTime_code;

   }

   // additional likely methods not defined since they are not needed for testing
}
