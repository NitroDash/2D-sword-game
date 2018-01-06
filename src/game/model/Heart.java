package game.model;

import game.main.Resources;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Heart extends Drop{

	private static BufferedImage heart;
	
	public Heart(int x, int y) {
		this.x=x;
		this.y=y;
		this.width=GameState.tileWidth/2;
		this.height=width;
		if (heart==null) {
			heart=Resources.loadImage("Heart.png");
		}
	}
	
	@Override
	public boolean getCollected(Player p) {
		if (p.hp==p.maxhp){
			return false;
		}
		p.hp=Math.min(p.maxhp, p.hp+10);
		return true;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(heart, x, y, null);
	}

}
