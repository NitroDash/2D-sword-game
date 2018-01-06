package game.model;

import game.main.Resources;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SnowmanHead extends Enemy{

	private static final BufferedImage head=Resources.loadImage("SnowmanHead.png");
	private int biteCounter, direction, startCounter;
	
	public SnowmanHead(int x, int y) {
		this.x=x;
		this.y=y;
		this.width=GameState.tileWidth/2;
		this.height=this.width;
		this.maxhp=10;
		this.hp=10;
		this.collision=TerrainType.INCORPOREAL;
		vulnerable=true;
		dangerous=true;
		damage=5;
		startCounter=30;
	}
	
	@Override
	public void update() {
		if (knockbackCounter>0) {
			knockbackCounter--;
			return;
		}
		biteCounter++;
		if (biteCounter>=30) {
			biteCounter=0;
		}
		if (startCounter>0) {
			startCounter--;
			dx=0;
			dy=0;
			return;
		}
		double heading=this.headingTowards(GameState.currentState.getPlayer());
		if (Math.cos(heading)>0) {
			direction=0;
		} else {
			direction=1;
		}
		dx=0;
		dy=0;
		if (Math.random()*(Math.abs(Math.sin(heading))+Math.abs(Math.cos(heading)))<Math.abs(Math.sin(heading))) {
			dy=(Math.sin(heading)<=0) ? -2 : 2;
		} else {
			dx=(Math.cos(heading)<=0) ? -2 : 2;
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(head,x,y,x+width,y+height,10*(biteCounter/15),10*direction,10*(biteCounter/15)+10,10*direction+10,null);
	}

}
