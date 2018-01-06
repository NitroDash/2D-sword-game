package game.model;

import game.main.Resources;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Chest extends Entity{

	public boolean open;
	
	private static BufferedImage chest;
	
	public Chest(int x, int y) {
		this.x=x;
		this.y=y;
		this.width=GameState.tileWidth;
		this.height=width;
		if (chest==null) {
			chest=Resources.loadImage("Chest.png");
		}
	}
	
	public Chest(int x, int y, boolean open) {
		this.x=x;
		this.y=y;
		this.open=open;
		this.width=GameState.tileWidth;
		this.height=width;
		if (chest==null) {
			chest=Resources.loadImage("Chest.png");
		}
	}
	
	@Override
	public void render(Graphics g) {
		if (open) {
			g.drawImage(chest,x,y,x+width,y+height,width,0,2*width,height,null);
		} else {
			g.drawImage(chest,x,y,x+width,y+height,0,0,width,height,null);
		}
	}

}
