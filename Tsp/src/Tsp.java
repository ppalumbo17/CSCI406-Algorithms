/**
 * 
 */

/**
 * @author Peter Palumbo
 *
 */

import java.io.*;
import java.util.*;
import java.awt.Point;
public class Tsp {

	public ArrayList<Point> points = new ArrayList<Point>();
	
	public void readFile(String fileName) throws IOException{
		Scanner reader = new Scanner(new FileReader(fileName));
		int count = 0;
		if (count == 0){
			count = reader.nextInt();
		}
		while(reader.hasNextInt()){
			int x = reader.nextInt();
			int y = reader.nextInt();
			Point p = new Point(x,y);
			points.add(p);
		}	
	}
	
	public void initialize(){
		System.out.println("Would you like to supply your own file? (Y/N)");
		Scanner reader = new Scanner(System.in);
		String input = reader.nextLine();
		if(input.equalsIgnoreCase("Y")||input.equalsIgnoreCase("YES")){
			System.out.println("What is the name of the file?");
			input = reader.nextLine();
			try{
				readFile(input);	
			}catch(IOException e){
				System.err.println(e);
			}
		}
	}
	
	public void testPoints(){
		for(Point p : points){
			System.out.println("X: " + p.x + " Y: "+ p.y);
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Tsp tsp = new Tsp();
		tsp.initialize();
		tsp.testPoints();
		
	}

}
