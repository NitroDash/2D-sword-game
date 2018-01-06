package game.model;

import game.main.Resources;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SpikeTrap extends Enemy{

	private int counter;
	
	private static BufferedImage spikeTrap=Resources.loadImage("SpikeTrap.png");
	
	public SpikeTrap(int x, int y) {
		counter=(int)(Math.random()*-300)-240;
		vulnerable=false;
		dangerous=false;
		this.x=x;
		this.y=y;
		width=GameState.tileWidth;
		height=width;
		this.hp=1;
		this.maxhp=1;
		damage=5;
		collision=TerrainType.TERRESTRIAL;
	}
	
	@Override
	public void update() {
		counter++;
		if (counter>=0) {
			dangerous=true;
		}
		if (counter>=60) {
			counter=(int)(Math.random()*-300)-240;
			dangerous=false;
		}
	}

	@Override
	public void render(Graphics g) {
		if (counter<0) {
			g.drawImage(spikeTrap, x, y, x+GameState.tileWidth, y+GameState.tileWidth, 0, 0, 20, 20, null);
		} else {
			g.drawImage(spikeTrap, x, y, x+GameState.tileWidth, y+GameState.tileWidth, 20, 0, 40, 20, null);
		}
	}

}
