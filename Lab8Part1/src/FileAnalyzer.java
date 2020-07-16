import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FileAnalyzer {

    public static void openfile(List<Point> points) {
        try {
            File file = new File("positions.txt");
            Scanner sc = new Scanner(file);
            while(sc.hasNext()) {
                String[] words = sc.nextLine().split(",");
                points.add(new Point(Double.parseDouble(words[0]), Double.parseDouble(words[1]), Double.parseDouble(words[2])));
                sc.nextLine();
            }
            System.out.println(points.size());
        }
        catch (Exception e){
            System.out.println("file not found");
        }
    }

    public static void writefile(List<Point> points) {
        try {
            PrintStream st = new PrintStream("drawMe.txt");
            for(Point p: points) {
                st.println(p.getX() + "," + p.getY() + "," + p.getZ());
            }
        }
        catch(Exception e) {
            System.out.println("error");
        }

    }

    public static void main(String[] args) {
        List<Point> points = new ArrayList<>();
        try {
            openfile(points);
        }
        catch(Exception e) {
            System.out.println("file not found");
        }
        points = points.stream().filter(p -> p.getZ() <= 2.0).map(p -> new Point(.5 * p.getX(), .5 * p.getY(), .5 * p.getZ())).map(p -> new Point(p.getX() - 100, p.getY() - 37, p.getZ())).collect(Collectors.toList());
        writefile(points);

    }


}
