package game.model;

import game.main.Resources;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class PushBlock extends Entity{

	private static final BufferedImage pushblock=Resources.loadImage("PushBlock.png");
	
	private int oldX, oldY;
	
	public PushBlock(int x, int y) {
		this.x=x;
		this.y=y;
		this.width=GameState.tileWidth;
		this.height=width;
		this.collision=TerrainType.PUSHBLOCK;
	}
	
	public void update(Player p) {
		if (dx+dy!=0) {
			if (x%(width/2)==0&&y%(height/2)==0) {
				if (!GameState.currentState.onlyOn(this,7)||!GameState.currentState.isClear(x+width/4+(3*width/4)*dx,y+height/4+(3*width/4)*dy,width/2,height/2)||(oldX==x&&oldY==y)) {
					dx=0;
					dy=0;
				}
			}
		}
		if (this.intersects(p)) {
			boolean push=false;
			if ((Math.min(this.x+this.width, p.x+p.width)-Math.max(this.x,p.x))*(Math.min(this.y+this.height, p.y+p.height)-Math.max(this.y,p.y))>=GameState.tileWidth/2) {
				push=true;
			}
			ejectFromArea(p);
			if (push&&dx+dy==0) {
				if (p.x>=this.x+this.width) {
					dx=-1;
					dy=0;
				} else if (p.x+p.width<=this.x) {
					dx=1;
					dy=0;
				} else if (p.y>=this.y+this.width) {
					dx=0;
					dy=-1;
				} else {
					dx=0;
					dy=1;
				}
				if (!GameState.currentState.isClear(x+width/4+(3*width/4)*dx,y+height/4+(3*width/4)*dy,width/2,height/2)) {
					dx=0;
					dy=0;
				}
			}
		}
		oldX=x;
		oldY=y;
	}
	
	@Override
	public void render(Graphics g) {
		g.drawImage(pushblock,x,y,width,height,null);
	}

}
