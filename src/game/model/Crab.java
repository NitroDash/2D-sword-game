package game.model;

import game.main.Resources;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Crab extends Enemy{

	private static final BufferedImage crab=Resources.loadImage("Crab.png");
	
	private int animCounter;
	private double heading;
	private boolean fast;
	
	public Crab(int x, int y) {
		this.x=x;
		this.y=y;
		this.width=GameState.tileWidth;
		this.height=width;
		this.vulnerable=true;
		this.dangerous=true;
		this.hp=15;
		this.maxhp=15;
		this.damage=10;
		this.collision=TerrainType.TERRESTRIAL;
		this.animCounter=0;
		fast=false;
	}
	
	@Override
	public void update() {
		animCounter++;
		if (fast) {
			animCounter++;
			if (distTo(GameState.currentState.getPlayer())>=GameState.tileWidth*4) {
				fast=false;
				heading=Math.random()*Math.PI*2;
			}
		}
		if (animCounter>=60) {
			animCounter=0;
			if (distTo(GameState.currentState.getPlayer())<=GameState.tileWidth*5) {
				heading=headingTowards(GameState.currentState.getPlayer());
				fast=true;
			} else {
				heading=Math.random()*Math.PI*2;
			}
		}
		if (knockbackCounter>0) {
			knockbackCounter--;
		} else {
			dx=0;
			dy=0;
			if (fast||animCounter%12==0) {
				if (Math.random()*(Math.abs(Math.sin(heading))+Math.abs(Math.cos(heading)))<Math.abs(Math.sin(heading))) {
					dy=(Math.sin(heading)<=0) ? -1 : 1;
				} else {
					dx=(Math.cos(heading)<=0) ? -1 : 1;
				}
			}
		}
	}

	@Override
	public void render(Graphics g) {
		int image=animCounter/30;
		g.drawImage(crab,x,y,x+width,y+height,20*image,0,20+20*image,20,null);
	}

}
