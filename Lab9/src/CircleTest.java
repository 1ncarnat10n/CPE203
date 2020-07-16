public class CircleTest {
    public static void main(String[] args)
    {
        try
        {
            Circle c1 = new Circle(-2);
            System.out.println(c1);
            Circle c2 = new Circle(0);
            System.out.println(c1);
            Circle c3 = new Circle(1);
            System.out.println(c1);
        }
        catch (CircleException e) {
            System.out.println(e.getMessage());
            return;
        }
//        catch (NegativeRadiusException e) {
//            System.out.println(e.getMessage() + ":" + e.radius());
//        }
        finally {
            System.out.println("In finally.");
        }
        System.out.println("Done.");
    }
}
