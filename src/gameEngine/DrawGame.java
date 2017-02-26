package gameEngine;

import java.awt.Dimension;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class DrawGame extends JFrame {


	public static int WIDTH = 1600;
	public static int HEIGHT = 900;


	DrawGame() {	
		getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setUndecorated(true);
		setVisible(true);
	}

	public void ExitOnLose() {
		setVisible(false);
		dispose();
	}

	public static void main(String[] args) {
			
		while (true) {
			DrawGame window = new DrawGame();
			Snake snake = new Snake();
			window.getContentPane().add(snake);
			window.addKeyListener(snake);
			window.pack();
			window.setFocusable(true);
			while (!snake.getEndGame()) {
				window.repaint();
				if (snake.getQuitGame())
					System.exit(0);
			}
			for (int i = 0; i < 30000000; i++) {
				window.repaint();
				if (snake.getQuitGame())
					System.exit(0);
			} 
		}
	}



}
