package game.model;

import game.main.Resources;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Cactus extends Enemy{

	private static final BufferedImage cactus=Resources.loadImage("Cactus.png");
	private int image;
	
	public Cactus(int x, int y) {
		this.x=x;
		this.y=y;
		this.width=GameState.tileWidth;
		this.height=width;
		this.hp=1;
		this.maxhp=1;
		this.collision=TerrainType.TERRESTRIAL;
		this.vulnerable=false;
		this.dangerous=true;
		this.damage=5;
		this.image=(int)(2*Math.random());
	}
	
	@Override
	public void update() {
		
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(cactus,x,y,x+width,y+height,20*image,0,20*image+20,20,null);
	}

}
