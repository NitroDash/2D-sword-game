package game.model;

import game.main.Resources;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Key extends Drop{

	public static final BufferedImage key=Resources.loadImage("Key.png");
	public static final BufferedImage keyIcon=Resources.loadImage("KeyIcon.png");
	
	public Key(int x, int y) {
		this.x=x;
		this.y=y;
		this.width=GameState.tileWidth/2;
		this.height=width*2;
	}
	
	@Override
	public boolean getCollected(Player p) {
		GameState.inventory.keys++;
		return true;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(key, x, y, null);
	}

}
