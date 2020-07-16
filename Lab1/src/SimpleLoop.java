class SimpleLoop
{
   public static int sum(int low, int high)
   {
      /* TO DO:  Return the sum of the integers between
         low and high (inclusive).  Yes, this can be
         done without a loop, but the point is to
         practice the syntax for a loop.
      */
      if (low > high) {
         return 0;
      }
      int sums = 0;
      for(int i = 0; i <= high; i++) {
         sums = low + high;
      }
      return sums;
   }
}
