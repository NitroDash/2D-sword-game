package game.model;

import game.state.GameState;

import java.awt.Graphics;

public class TankBall extends Enemy{

	private Tank parent;
	private int radius, dRadius;
	private double heading, dHeading;
	
	private int MAX_RADIUS=GameState.tileWidth*5;
	private int MIN_RADIUS=GameState.tileWidth;
	
	public TankBall(Tank parent) {
		this.parent=parent;
		this.centerAtParent();
		this.width=10;
		this.height=10;
		this.vulnerable=false;
		this.dangerous=true;
		this.damage=10;
		this.hp=1;
		this.maxhp=1;
		this.collision=TerrainType.INCORPOREAL;
		radius=MIN_RADIUS;
		dHeading=Math.PI/12;
	}
	
	@Override
	public void update() {
		if (dRadius!=0) {
			radius+=dRadius;
			if (radius>=MAX_RADIUS) {
				dRadius*=-1;
			} else if (radius<=MIN_RADIUS) {
				radius=MIN_RADIUS;
				dRadius=0;
			}
		} else {
			heading+=dHeading;
		}
		this.centerAtParent();
		this.x+=(int)(Math.cos(heading)*radius);
		this.y+=(int)(Math.sin(heading)*radius);
		if (this.parent.hp<=0) {
			this.hp=0;
		}
	}
	
	private void centerAtParent() {
		this.center(parent.x+parent.width/2,parent.y+parent.height/2);
	}
	
	public void shoot() {
		if (dRadius!=0) {return;}
		dRadius=4;
		heading=headingTowards(GameState.currentState.getPlayer());
	}
	
	public void slow() {
		dHeading*=0.95;
		dangerous=false;
	}

	@Override
	public void render(Graphics g) {
		int centerX=parent.x+parent.width/2;
		int centerY=parent.y+parent.height/2;
		for (double i=0; i<radius; i+=4) {
			g.drawImage(BallNChain.chain, (int)((centerX*(radius-i)+(x+5)*i)/radius)-5, (int)((centerY*(radius-i)+(y+5)*i)/radius)-5, null);
		}
		g.drawImage(BallNChain.ball,x,y,null);
	}
	
	public Drop getDrop() {
		return new WeaponDrop(new BallNChain(),x,y);
	}

}
