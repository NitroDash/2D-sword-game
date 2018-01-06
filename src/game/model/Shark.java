package game.model;

import game.main.Resources;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Shark extends Enemy{

	private static final BufferedImage shark=Resources.loadImage("Shark.png");
	
	private boolean fast;
	private int directionCounter;
	private double heading;
	
	public Shark(int x, int y) {
		this.x=x;
		this.y=y;
		this.width=GameState.tileWidth;
		this.height=width;
		this.vulnerable=true;
		this.dangerous=true;
		this.damage=5;
		this.collision=TerrainType.AQUATIC;
		this.hp=15;
		this.maxhp=15;
		this.direction=0;
	}
	
	@Override
	public void update() {
		if (GameState.currentState.onlyOn(GameState.currentState.getPlayer(),4)) {
			fast=true;
			heading=headingTowards(GameState.currentState.getPlayer());
		} else {
			directionCounter++;
			fast=false;
			if (directionCounter>=60) {
				directionCounter=0;
				heading=Math.random()*Math.PI*2;
			}
		}
		dx=0;
		dy=0;
		if (fast||directionCounter%8==0) {
			if (Math.random()*(Math.abs(Math.sin(heading))+Math.abs(Math.cos(heading)))<Math.abs(Math.sin(heading))) {
				dy=(Math.sin(heading)<=0) ? -2 : 2;
			} else {
				dx=(Math.cos(heading)<=0) ? -2 : 2;
			}
			if (dx<0) {
				direction=1;
			} else if (dx>0) {
				direction=0;
			}
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(shark,x,y,x+width,y+height,20*direction,0,20*(direction+1),20,null);
	}

}
