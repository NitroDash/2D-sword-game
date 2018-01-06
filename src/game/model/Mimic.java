package game.model;

import game.main.Resources;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Mimic extends Enemy{

	private static final BufferedImage mimic=Resources.loadImage("Mimic.png");
	private int animCounter;
	private double heading;
	
	public Mimic(int x, int y) {
		this.x=x;
		this.y=y;
		this.width=GameState.tileWidth;
		this.height=width;
		this.vulnerable=false;
		this.dangerous=false;
		this.damage=5;
		this.hp=20;
		this.maxhp=20;
		this.collision=TerrainType.TERRESTRIAL;
	}
	
	@Override
	public void update() {
		if (knockbackCounter>0) {
			knockbackCounter--;
			return;
		}
		if (vulnerable) {
			animCounter++;
			if (animCounter>60) {
				animCounter=0;
				heading=headingTowards(GameState.currentState.getPlayer());
			}
			dx=0;
			dy=0;
			if (animCounter<40) {
				if (Math.random()*(Math.abs(Math.sin(heading))+Math.abs(Math.cos(heading)))>Math.abs(Math.sin(heading))) {
					dx=(Math.cos(heading)>0)?2:-2;
				} else {
					dy=(Math.sin(heading)>0)?2:-2;
				}
			}
		} else {
			if (distTo(GameState.currentState.getPlayer())<GameState.tileWidth*3/2) {
				vulnerable=true;
				dangerous=true;
				heading=headingTowards(GameState.currentState.getPlayer());
			}
		}
	}

	@Override
	public void render(Graphics g) {
		int image=(vulnerable)?((animCounter<40)?1:2):0;
		g.drawImage(mimic, x, y, x+width, y+height, image*20, 0, image*20+20, 20, null);
	}
	
	@Override
	public Drop getDrop() {
		return new Money(x,y,(int)(Math.random()*3+3));
	}

}
