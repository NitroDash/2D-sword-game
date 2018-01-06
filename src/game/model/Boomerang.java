package game.model;

import game.main.Resources;
import game.state.GameState;

import java.awt.Graphics;

public class Boomerang extends Weapon{

	private static final int radius=GameState.tileWidth*3;
	private static final double angularSpeed=Math.PI/20;
	
	private double theta,extraTheta;
	private int centerX, centerY,direction;
	
	@Override
	public void update(Player p) {
		if (extraTheta==0) {return;}
		if (extraTheta>Math.PI*2) {
			if (p.intersects(x,y,GameState.tileWidth,GameState.tileWidth)) {
				extraTheta=0;
				return;
			}
			int dx=p.x-x;
			int dy=p.y-y;
			double correction=angularSpeed*radius/Math.sqrt(dx*dx+dy*dy);
			x+=dx*correction;
			y+=dy*correction;
		} else {
			extraTheta+=angularSpeed;
			x=centerX+(int)(radius*Math.cos(theta+extraTheta));
			y=centerY+(int)(radius*Math.sin(theta+extraTheta));
		}
		GameState.currentState.playerDamageBox(x,y,GameState.tileWidth,GameState.tileWidth);
		direction++;
		if (direction>11) {
			direction=0;
		}
	}

	@Override
	public void use(Player p) {
		if (extraTheta!=0) {return;}
		extraTheta=Math.PI/60;
		switch (p.direction) {
		case 0:
			centerX=p.x;
			centerY=p.y-radius;
			theta=Math.PI/2;
			break;
		case 1:
			centerX=p.x;
			centerY=p.y+radius;
			theta=-Math.PI/2;
			break;
		case 2:
			centerX=p.x-radius;
			centerY=p.y;
			theta=0;
			break;
		case 3:
			centerX=p.x+radius;
			centerY=p.y;
			theta=Math.PI;
			break;
		}
	}

	@Override
	public int getPlayerDX() {
		return 1000;
	}

	@Override
	public int getPlayerDY() {
		return 1000;
	}

	@Override
	public void render(Graphics g, Player p) {
		if (extraTheta>Math.PI/60) {
			g.drawImage(Resources.weapons[1],x,y,x+GameState.tileWidth,y+GameState.tileWidth,(direction/3)*GameState.tileWidth,0,(direction/3+1)*GameState.tileWidth,GameState.tileWidth,null);
		}
	}

	@Override
	public void renderDrop(Graphics g, int x, int y) {
		g.drawImage(Resources.weapons[1],x,y,x+GameState.tileWidth,y+GameState.tileWidth,0,0,GameState.tileWidth,GameState.tileWidth,null);
	}

	@Override
	public void reset() {
		extraTheta=0;
	}

}
