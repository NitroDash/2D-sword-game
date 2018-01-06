package game.model;

import game.main.GameMain;
import game.main.Resources;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BigSnowmanHead extends Enemy{

	private BigSnowman parent;
	
	private static final BufferedImage head=Resources.loadImage("BigSnowmanHead.png");
	
	private int destX, destY;
	
	public BigSnowmanHead(int x, int y, BigSnowman parent) {
		this.x=x;
		this.y=y;
		this.width=GameState.tileWidth/2;
		this.height=width;
		this.collision=TerrainType.INCORPOREAL;
		this.dangerous=true;
		this.vulnerable=true;
		this.damage=5;
		this.parent=parent;
		this.hp=30-5*parent.hp;
		this.maxhp=hp;
		setDest();
	}
	
	private void setDest() {
		destX=(int)(Math.random()*(GameMain.GAME_WIDTH-GameState.tileWidth*3))+GameState.tileWidth;
		destY=(int)(Math.random()*(GameMain.GAME_HEIGHT-GameState.tileWidth*3))+GameState.tileWidth;
	}
	
	@Override
	public void update() {
		if (knockbackCounter>0) {
			knockbackCounter--;
		}
		if (vulnerable&&hp<=0) {
			hp=1;
			vulnerable=false;
		}
		if (!vulnerable) {
			destX=parent.x+width*3/2;
			destY=parent.y+width;
		}
		double heading=headingTowards(destX,destY);
		dx=0;
		dy=0;
		if (Math.random()*(Math.abs(Math.sin(heading))+Math.abs(Math.cos(heading)))>Math.abs(Math.sin(heading))) {
			dx=(destX>x)?2:-2;
		} else {
			dy=(destY>y)?2:-2;
		}
		if (distTo(destX,destY)<5) {
			if (!vulnerable) {
				hp=0;
				parent.headDead();
			} else {
				setDest();
			}
		}
	}

	@Override
	public void render(Graphics g) {
		if (knockbackCounter>3) {
			g.drawImage(head,x,y,x+width,y+height,10,0,20,10,null);
		} else {
			g.drawImage(head,x,y,x+width,y+height,0,0,10,10,null);
		}
	}
	
	@Override
	public Drop getDrop() {
		return null;
	}

}
