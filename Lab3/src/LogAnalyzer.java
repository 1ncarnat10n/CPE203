import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LogAnalyzer
{
      //constants to be used when pulling data out of input
      //leave these here and refer to them to pull out values
   private static final String START_TAG = "START";
   private static final int START_NUM_FIELDS = 3;
   private static final int START_SESSION_ID = 1;
   private static final int START_CUSTOMER_ID = 2;
   private static final String BUY_TAG = "BUY";
   private static final int BUY_NUM_FIELDS = 5;
   private static final int BUY_SESSION_ID = 1;
   private static final int BUY_PRODUCT_ID = 2;
   private static final int BUY_PRICE = 3;
   private static final int BUY_QUANTITY = 4;
   private static final String VIEW_TAG = "VIEW";
   private static final int VIEW_NUM_FIELDS = 4;
   private static final int VIEW_SESSION_ID = 1;
   private static final int VIEW_PRODUCT_ID = 2;
   private static final int VIEW_PRICE = 3;
   private static final String END_TAG = "END";
   private static final int END_NUM_FIELDS = 2;
   private static final int END_SESSION_ID = 1;

      //a good example of what you will need to do next
      //creates a map of sessions to customer ids
   private static void processStartEntry(
      final String[] words,
      final Map<String, List<String>> sessionsFromCustomer)
   {
      if (words.length != START_NUM_FIELDS)
      {
         return;
      }

         //check if there already is a list entry in the map
         //for this customer, if not create one
      List<String> sessions = sessionsFromCustomer
         .get(words[START_CUSTOMER_ID]);
      if (sessions == null)
      {
         sessions = new LinkedList<>();
         sessionsFromCustomer.put(words[START_CUSTOMER_ID], sessions);
      }

         //now that we know there is a list, add the current session
      sessions.add(words[START_SESSION_ID]);
   }

      //similar to processStartEntry, should store relevant view
      //data in a map - model on processStartEntry, but store
      //your data to represent a view in the map (not a list of strings)
   private static void processViewEntry(final String[] words, final Map<String, List<View>> views) {
      if (words.length != VIEW_NUM_FIELDS) {return;}
      List<View> session_views = views.get(words[VIEW_SESSION_ID]);
      if (session_views == null) {
         session_views = new LinkedList<>();
         views.put(words[VIEW_SESSION_ID], session_views);
      }

      session_views.add(new View(words[VIEW_PRODUCT_ID], words[VIEW_PRICE]));

   }

      //similar to processStartEntry, should store relevant purchases
      //data in a map - model on processStartEntry, but store
      //your data to represent a purchase in the map (not a list of strings)
   private static void processBuyEntry(
      final String[] words, final Map<String, List<Buy>> buys) {

      if (words.length != BUY_NUM_FIELDS) {
         return;
      }
      List<Buy> items_bought = buys.get(words[BUY_SESSION_ID]);
      if (items_bought == null) {
         items_bought = new LinkedList<>();
         buys.put(words[BUY_SESSION_ID], items_bought);
      }

      items_bought.add(new Buy(words[BUY_PRODUCT_ID], words[BUY_PRICE], words[BUY_QUANTITY]));

   }

   private static void processEndEntry(final String[] words)
   {
      if (words.length != END_NUM_FIELDS)
      {
         return;
      }
   }

      //this is called by processFile below - its main purpose is
      //to process the data using the methods you write above
   private static void processLine(
      final String line,
      final Map<String, List<String>> sessionsFromCustomer,
      final Map<String, List<View>> views,
      final Map<String, List<Buy>> buys)
   {
      final String[] words = line.split("\\h");

      if (words.length == 0)
      {
         return;
      }

      switch (words[0])
      {
         case START_TAG:
            processStartEntry(words, sessionsFromCustomer);
            break;
         case VIEW_TAG:
            processViewEntry(words, views);
            break;
         case BUY_TAG:
            processBuyEntry(words, buys);
            break;
         case END_TAG:
            processEndEntry(words /* add arguments as needed */ );
            break;
      }
   }

      //write this after you have figured out how to store your data
      //make sure that you understand the problem
   private static void printSessionPriceDifference(
           final Map<String, List<View>> viewsFromSession,
           final Map<String, List<Buy>> buysFromCustomer)
   {
      System.out.println("Price Difference for Purchased Product by Session");
      double total = 0;
      double count = 0;
      double avg = 0;
      for(Map.Entry<String, List<Buy>> entry: buysFromCustomer.entrySet()) {
         System.out.println(entry.getKey());
         List<Buy> theBuys = entry.getValue();
         for (Buy product : theBuys) {
            System.out.print("    " + product.getProduct() + " ");
            List<View> theViews = viewsFromSession.get(entry.getKey());
            total = 0;
            count = 0;
            for(View views: theViews) {
               count += 1;
               total += views.getProduct_cost();
               }
            avg = total / count;
            System.out.print(product.getProduct_cost() - avg + "\n");
            }
         }
      }


      //write this after you have figured out how to store your data
      //make sure that you understand the problem
   private static void printCustomerItemViewsForPurchase(
           final Map<String, List<String>> sessionsFromCustomer,
           final Map<String, List<View>> viewsFromSession,
           final Map<String, List<Buy>> buysFromSession)
   {
      System.out.println("Number of Views for Purchased Product by Customer");
      for(Map.Entry<String, List<String>> entry: sessionsFromCustomer.entrySet()) {
         boolean done = false;
         List<String> sessions = entry.getValue();
         for(String sessionID: sessions) {
            List<Buy> theBuys = buysFromSession.get(sessionID);
               if(theBuys != null) {
                  if (!done) {
                     done = true;
                     System.out.println(entry.getKey());
                  }
                  for (Buy product : theBuys) {
                     System.out.print(" " + product.getProduct());
                     double count = 0;
                     for (String session : sessions) {
                        List<View> views = viewsFromSession.get(session);
                        if (views != null) {
                           for (View view : views) {
                              if (view.getProduct().equals(product.getProduct())) {
                                 count += 1;
                                 break;
                              }
                           }
                        }
                     }
                     System.out.println(" " + count);

                  }
               }
         }
      }
      /* add printing */
   }

      //write this after you have figured out how to store your data
      //make sure that you understand the problem
   private static void printStatistics(final Map<String, List<String>> sessionsFromCustomer,
                                       final Map<String, List<View>> viewsFromSession,
                                       final Map<String, List<Buy>> buysFromSession)
   {
      printSessionPriceDifference(viewsFromSession, buysFromSession);
      printCustomerItemViewsForPurchase(sessionsFromCustomer, viewsFromSession, buysFromSession);

      /* This is commented out as it will not work until you read
         in your data to appropriate data structures, but is included
         to help guide your work - it is an example of printing the
         data once propogated
       */
         printOutAverageViews(viewsFromSession, buysFromSession);
   }

   /* provided as an example of a method that might traverse your
      collections of data once they are written
      commented out as the classes do not exist yet - write them! */

   private static void printOutAverageViews(
      final Map<String, List<View>> viewsFromSession,
      final Map<String, List<Buy>> buysFromSession)
   {
      double count = 0;
      double sessions = 0;
      for(Map.Entry<String, List<View>> view:
         viewsFromSession.entrySet())
      {
         List<Buy> buys = buysFromSession.get(view.getKey());
         List<View> views = view.getValue();
         if (buys == null) {
            sessions += 1;
            for (View theview: views) {
               count += 1;
            }
         }
         else {
            sessions -= 1;
         }
         }
      System.out.println("Average Views Without Purchase: " + (count/sessions));
      }


      //called in populateDataStructures
   private static void processFile(
      final Scanner input,
      final Map<String, List<String>> sessionsFromCustomer,
      final Map<String, List<View>> views,
      final Map<String, List<Buy>> buys
      /* add parameters as needed */
      )
   {
      while (input.hasNextLine())
      {
         processLine(input.nextLine(), sessionsFromCustomer, views, buys);
      }
   }

      //called from main - mostly just pass through important data structures
   private static void populateDataStructures(
      final String filename,
      final Map<String, List<String>> sessionsFromCustomer,
      final Map<String, List<View>> views,
      final Map<String, List<Buy>> buys)

      throws FileNotFoundException
   {
      try (Scanner input = new Scanner(new File(filename)))
      {
         processFile(input, sessionsFromCustomer, views, buys);
      }
   }

   private static String getFilename(String[] args)
   {
      if (args.length < 1)
      {
         System.err.println("Log file not specified.");
         System.exit(1);
      }

      return args[0];
   }

   public static void main(String[] args)
   {
      /* Map from a customer id to a list of session ids associated with
       * that customer.
       */
      final Map<String, List<String>> sessionsFromCustomer = new HashMap<>();
      final Map<String, List<View>> views = new HashMap<>();
      final Map<String, List<Buy>> buys = new HashMap<>();
      /* create additional data structures to hold relevant information */
      /* they will most likely be maps to important data in the logs */
      final String filename = getFilename(args);

      try
      {
         populateDataStructures(filename, sessionsFromCustomer, views, buys);
         printStatistics(sessionsFromCustomer, views, buys);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }
}
