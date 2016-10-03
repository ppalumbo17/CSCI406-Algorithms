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
	public ArrayList<Point> final_points = new ArrayList<Point>();
	public ArrayList<Point> current_points = new ArrayList<Point>();
	double final_distance;
	double total_time;
	int MAXIMUM_POINT = 1000000;
	int MINIMUM_POINT = 0;
	
	public void readFile(String fileName) throws IOException{
		Scanner fileScanner = new Scanner(new FileReader(fileName));
		int count = 0;
		if (count == 0){
			count = fileScanner.nextInt();
		}
		while(fileScanner.hasNextInt()){
			int x = fileScanner.nextInt();
			int y = fileScanner.nextInt();
			Point p = new Point(x,y);
			points.add(p);
		}	
	}
	
	public void randomPoints(int input){
		int x, y;
		Point newPoint;
		Random rand = new Random();
		for(int i = 0; i < input; i++){
			x = rand.nextInt(MAXIMUM_POINT)+MINIMUM_POINT;
			y = rand.nextInt(MAXIMUM_POINT)+MINIMUM_POINT;
			newPoint = new Point(x, y);
			points.add(newPoint);
		}
	}
	public double calcDistance(Point newPoint, Point originalPoint){
		return Math.sqrt(Math.pow((newPoint.x-originalPoint.x), 2)+Math.pow((newPoint.y-originalPoint.y), 2));
	}
	
	public static ArrayList<ArrayList<Point>> permutate(ArrayList<Point> current_points){
		if(current_points.size()==0){
			ArrayList<ArrayList<Point>> newPoints = new ArrayList<ArrayList<Point>>();
			newPoints.add(new ArrayList<Point>());
			return newPoints;
		}
		
		ArrayList<ArrayList<Point>> permutations = new ArrayList<ArrayList<Point>>();
		Point start = current_points.remove(0);
		ArrayList<ArrayList<Point>> next = permutate(current_points);
		for(ArrayList<Point> ps : next){
			for(int i = 0; i<=ps.size(); i++){
				ArrayList<Point> tmp = new ArrayList<Point>(ps);
				tmp.add(i, start);
				permutations.add(tmp);	
			}
		}
		return permutations;
	}
	public void exhaustive(){
		current_points.clear();
		current_points = points;
		double distance;
		final_points.clear();
		double clockStart = System.nanoTime();
		ArrayList<ArrayList<Point>> allPaths = permutate(current_points);
		double smallestDistance = Double.MAX_VALUE;
		for(ArrayList<Point> ps : allPaths){
			Point currentPoint = ps.get(0);
			Point startPoint = currentPoint;
			ps.remove(0);
			distance = 0;
			for(Point p : ps){
				distance += calcDistance(p, currentPoint);
				currentPoint = p;
			}
			distance += calcDistance(currentPoint, startPoint);
			if(distance < smallestDistance){
				smallestDistance = distance;
				final_points = ps;
				final_points.add(0, startPoint);
				final_points.add(startPoint);
			}
			final_distance = smallestDistance;
		}
		double clockEnd = System.nanoTime();
		total_time = clockEnd-clockStart;
	}
	public void nearestNeighbor(){
		current_points.clear();
		current_points = points;
		double distance;
		final_points.clear();
		Point currentPoint = current_points.get(0);
		Point start = currentPoint;
		current_points.remove(0);
		final_points.add(currentPoint);
		Point nextPoint;
		double totalDistance = 0;
		int pointLocal;
		double clockStart = System.nanoTime();
		while(current_points.size()>0){
			distance = 9999999;
			pointLocal=0;
			int i =0;
			nextPoint = currentPoint;
			for(Point p : current_points){
				double tmp_distance = calcDistance(p, currentPoint);
				if(tmp_distance < distance){
					distance = tmp_distance;
					nextPoint=p;
					pointLocal = i;
				}
				i++;
			}
			totalDistance += distance;
			current_points.remove(pointLocal);
			currentPoint = nextPoint;
			final_points.add(currentPoint);
			
		}
		totalDistance+=calcDistance(start,currentPoint);
		final_points.add(start);
		double clockEnd = System.nanoTime();
		final_distance = totalDistance;
		total_time = clockEnd-clockStart;
		
	}
	
	public void initialize(){
		points.clear();
		final_points.clear();
		current_points.clear();
		boolean answered = false;
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
				// System.out.println("Would you like to try again? (Y/N)");
				// input = reader.nextLine();
				// if(input.equalsIgnoreCase("Y")||input.equalsIgnoreCase("YES")){
				// 	initialize();
				// }
				// else{
				// 	System.exit(0);
				// }
				System.exit(0);
			}
		}
		else{
			System.out.println("How many points would you like? (Please Enter a number less than 11 for exhaustive search)");
			int in = reader.nextInt();
			try{
				randomPoints(in);
			}catch(Exception e){
				System.err.println(e);

			}
		}
		
		System.out.println("Which Algorithm would you like to use, nearest neighbor (N), or exhaustive (x)?");
		while(!answered){
			input = reader.nextLine();
			if(input.equalsIgnoreCase("N")||input.equalsIgnoreCase("NEAR")||input.equalsIgnoreCase("NEAREST")){
				System.out.println("You've chosen nearest neighbor search!");
				answered = true;
				nearestNeighbor();
			}
			else if(input.equalsIgnoreCase("X")||input.equalsIgnoreCase("EXHAUSTIVE")||input.equalsIgnoreCase("E")){
				System.out.println("You've chosen exhaustive search!");
				answered = true;
				exhaustive();
			}
			else if(input.equals("Q")){
				break;
			}
			else{
				System.out.println("Please input 'N' or 'X' to search or 'Q' to quit.");
			}
		}
//		testPoints();
//		System.out.println("Would you like to go again? (Y/N)");
//		input = reader.nextLine();
//		if(input.equalsIgnoreCase("Y")||input.equalsIgnoreCase("YES")){
//			initialize();
//		}
		
	}
	
	public void testPoints(){
		for(Point p : points){
			System.out.println("X: " + p.x + " Y: "+ p.y);
		}
		for(Point p1 : final_points){
			System.out.println("New X: " + p1.x + " New Y: "+ p1.y);
		}
		System.out.println("Total Distance Traveled: "+final_distance);
		System.out.println("Total Time Elapsed: "+total_time/1000000+ " milliseconds");
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
