package gameEngine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Snake extends JPanel implements ActionListener, KeyListener {
	
	AI ai = new AI();    //creates the AI

	public final static int TIMER = 50;
	
	Timer time = new Timer(TIMER,this); //17 ms delay, approx 60 frames per second

	public static final int SNAKE_SIDE = 50;
	public static final int GRID_SIZE = 100;   
	private boolean EndGame;  //checks for collisions 
	private boolean QuitGame; //checks to quit the game 
	private int KeyCode;

	Random rand = new Random();

	private Point food;
	private Point head;
	ArrayList<Point> tail;
	Color headColor = new Color(255,100,255);
	Color tailColor = new Color(255,200,255);
	Color EndColor = new Color(225,100,80);

	private int x_vel;
	private int y_vel;

	Snake() {
		time.start();
		food = new Point(0,0);
		head = new Point(DrawGame.WIDTH/2, DrawGame.HEIGHT/2 + SNAKE_SIDE); //start snake at true center

		tail = new ArrayList<Point>();
		EndGame = false;
		QuitGame = false;
		x_vel = 0;
		y_vel = 0;
		setFood();
	}
	
	// sets the food location
	public void setFood() {
		HashSet<Point> snake = new HashSet<Point>(); //contains where not to spawn the food
		snake.add(head);

		if (tail.size() > 0) 
			for (Point i : tail) {
				snake.add(i); //adds the tail locations
			}

		int x = 0;
		int y = 0;
		Point temp = new Point(x,y);  

		do {
			x = rand.nextInt((DrawGame.WIDTH)/GRID_SIZE)*GRID_SIZE;
			y = rand.nextInt((DrawGame.HEIGHT)/GRID_SIZE)*GRID_SIZE;
			temp = new Point(x,y);
		} while (snake.contains(temp));

		food.setLocation(x, y);	
	}


	// checks for the snakes collision and sets the boolean EndGame  
	public void checkEndGame() {
		if (head.getX() > DrawGame.WIDTH - SNAKE_SIDE || head.getX() < 0) //temporary game enders
			EndGame = true;													//TO-DO check for direction and wall orientation
		if (head.getY() > DrawGame.HEIGHT - SNAKE_SIDE || head.getY() < 0)
			EndGame = true;

		for (Point p : tail) {		// checks for head collison with tail
			if (p.x == head.x && p.y == head.y) {
				EndGame = true;
			}
		}
	}


	public boolean getEndGame() { //getter for the DrawGame function
		return EndGame;
	}
	
	public boolean getQuitGame() {
		return QuitGame;
	}

	public void checkDirection() {
		
		Point vel = new Point(ai.chooseDirection(food, head, tail));
		if (x_vel != -vel.x || y_vel != - vel.y) {
			x_vel = vel.x;
			y_vel = vel.y;	
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
			
		if (!EndGame) {
			checkDirection();
			if ((head.x == food.x && head.y == food.y)) {
				setFood();

				for (int t = 0; t < 2; t++) {

					tail.add(new Point(head.x,head.y));

					for (int i = tail.size() - 1; i > 0; i--) {  
						tail.set(i, new Point(tail.get(i - 1).x, tail.get(i - 1).y));
					}         
					tail.set(0,new Point(head.x,head.y));

					head.setLocation(head.x + x_vel/2, head.y + y_vel/2);
				}
			}

			else {
				for (int t = 0; t < 2; t++) {
					if (tail.size() > 0) {

						for (int i = tail.size() - 1; i > 0; i--) {
							tail.set(i, new Point(tail.get(i - 1).x, tail.get(i - 1).y));
						} 
						tail.set(0, new Point(head.x, head.y));
					}
					head.setLocation(head.x + x_vel/2, head.y + y_vel/2);
				}
			}
			checkEndGame();
		}
	}

	//Renders the Game Board 
	@Override
	public void paintComponent(Graphics g) { 
		super.paintComponent(g);
		super.setBackground(Color.BLACK);

		g.setColor(Color.CYAN);
		g.fillOval(food.x, food.y, SNAKE_SIDE, SNAKE_SIDE);

		if (EndGame) g.setColor(EndColor);
		else g.setColor(headColor);

		g.fillRect(head.x, head.y, SNAKE_SIDE, SNAKE_SIDE);
		
		
		if (EndGame) g.setColor(EndColor);
		else g.setColor(tailColor);
		for (int i = 0; i < tail.size(); i++) {
			g.fillRect(tail.get(i).x, tail.get(i).y, SNAKE_SIDE, SNAKE_SIDE);
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		KeyCode = e.getKeyCode();
		
		if (KeyCode == KeyEvent.VK_ESCAPE) {
			QuitGame = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {		
	}

}
