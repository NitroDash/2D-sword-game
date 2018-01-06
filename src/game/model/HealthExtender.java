package game.model;

import game.main.Resources;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class HealthExtender extends Drop{

	private static final BufferedImage health=Resources.loadImage("HealthExtender.png");
	
	public HealthExtender(int x, int y) {
		this.x=x;
		this.y=y;
		this.width=GameState.tileWidth/2;
		this.height=this.width;
	}
	
	@Override
	public boolean getCollected(Player p) {
		p.maxhp+=25;
		p.hp=p.maxhp;
		return true;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(health,x,y,null);
	}

}
