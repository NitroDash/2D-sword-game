package game.model;

import game.main.Resources;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class ShadyDealer extends Entity{

	private static final BufferedImage dealer=Resources.loadImage("ShadyDealer.png");
	
	private int imageCounter;
	
	public ShadyDealer(int x, int y) {
		this.x=x;
		this.y=y;
		this.width=GameState.tileWidth;
		this.height=width;
		this.collision=TerrainType.TERRESTRIAL;
		imageCounter=0;
	}
	
	public void update() {
		if (imageCounter>0) {
			imageCounter--;
		} else {
			if (Math.random()<0.01) {
				imageCounter=39;
			}
		}
		ejectFromArea(GameState.currentState.getPlayer());
	}
	
	@Override
	public void render(Graphics g) {
		int image=0;
		if (imageCounter>0) {
			image=1+imageCounter/20;
		}
		g.drawImage(dealer,x,y,x+width,y+height,image*20,0,image*20+20,20,null);
	}

}
