package game.main;

import javax.swing.JFrame;

public class GameMain {
	private static final String GAME_TITLE = "Adventure Game";
	public static final int GAME_WIDTH = 400;
	public static final int GAME_HEIGHT = 260;
	public static final int WINDOW_WIDTH=800;
	public static final int WINDOW_HEIGHT=600;
	public static Game sGame;
	public static JFrame frame;
	public static void main(String[] args) {
		frame = new JFrame(GAME_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		sGame = new Game(WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.add(sGame);
		frame.pack();
		frame.setVisible(true);
	}
}