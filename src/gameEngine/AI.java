package gameEngine;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
  

public class AI { 
	
	Random rand = new Random();
	
	private HashSet<Point> openList;
	private HashSet<Point> closedList;
	ArrayList<Point> points;
	ArrayList<Point> lose;
	
	private int grid_size = Snake.GRID_SIZE;
	private int width = DrawGame.WIDTH;
	private int height = DrawGame.HEIGHT; 
	
	AI() {
		openList = new HashSet<Point>();	
		closedList = new HashSet<Point>();
	}
	
	public Point chooseDirection(Point food, Point head, ArrayList<Point> tail) {
		populateClosedList(tail); //populates the closed List 
		populateOpenList(head);
		
		Point p = new Point(evaluatePoint(food));
		Point vel = new Point(p.x - head.x, p.y - head.y);
		return vel;
	}
	
	private void populateClosedList(ArrayList<Point> tail) {
		closedList = new HashSet<Point>();
		for (Point p: tail) {
			closedList.add(p);
		}
	}
	
	public void populateOpenList(Point head) {
		openList = new HashSet<Point>();
		points = new ArrayList<Point>();
		lose = new ArrayList<Point>();
		
		Point up = new Point(head.x, head.y - grid_size);
		Point left = new Point(head.x - grid_size, head.y);
		Point right = new Point(head.x + grid_size, head.y);
		Point down = new Point(head.x, head.y + grid_size);
		
		points.add(up);
		points.add(left);
		points.add(right);
		points.add(down);
		
		
		for (Point p : points) {
			if (checkIfValid(p) && !closedList.contains(p)) openList.add(p);
			if (checkIfValid(p)) lose.add(p);
		}
	}
	
	public Point evaluatePoint(Point food) {  //picks the best point to go to 
		Point choose = new Point();
		double min_dis = 1000000000;

		if (openList.isEmpty()) return new Point(lose.get(rand.nextInt(lose.size())));
		
		for (Point p : openList) {
			if (min_dis > p.distanceSq(food)) {
				choose.setLocation(p);
				min_dis = p.distanceSq(food);
			}
		}
		return choose;
	}
	
	public boolean checkIfValid(Point p) { //checks if a point is valid to add 
		if (p.x >= width || p.y >= height || p.x < 0 || p.y < 0) {
			return false;
		}		
		return true;		
	}
	
}
