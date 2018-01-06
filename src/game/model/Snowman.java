package game.model;

import game.main.Resources;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Snowman extends Enemy{

	private static final BufferedImage snowman=Resources.loadImage("Snowman.png");
	
	private int image=0;
	
	public Snowman(int x, int y) {
		this.x=x;
		this.y=y;
		this.width=GameState.tileWidth;
		this.height=width;
		this.maxhp=10000;
		this.hp=10000;
		this.vulnerable=true;
		this.dangerous=false;
		this.collision=TerrainType.TERRESTRIAL;
	}
	
	@Override
	public void update() {
		dx=0;
		dy=0;
		if (this.intersects(GameState.currentState.getPlayer())) {
			ejectFromArea(GameState.currentState.getPlayer());
		}
		if (this.hp<this.maxhp){
			vulnerable=false;
			hp=maxhp;
			GameState.currentState.addEnemy(new SnowmanHead(x,y));
			image=1;
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(snowman,x,y,x+width,y+height,20*image,0,20*image+20,20,null);
	}

}
