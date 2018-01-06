package game.model;

import game.main.Resources;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class StaminaExtender extends Drop{

	private static final BufferedImage stamina=Resources.loadImage("StaminaExtender.png");
	
	public StaminaExtender(int x, int y) {
		this.x=x;
		this.y=y;
		this.width=GameState.tileWidth/2;
		this.height=this.width;
	}
	
	@Override
	public boolean getCollected(Player p) {
		p.MAX_STAMINA+=25;
		p.stamina=p.MAX_STAMINA;
		return true;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(stamina,x,y,null);
	}

}
