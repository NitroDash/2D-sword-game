package game.model;

import game.main.Resources;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Blob extends Enemy{

	private int animCounter;
	private double heading;
	
	private static BufferedImage blob=Resources.loadImage("Blob.png");
	
	public Blob(int x, int y) {
		this.x=x;
		this.y=y;
		this.height=10;
		this.width=10;
		this.animCounter=0;
		this.damage=5;
		this.maxhp=10;
		this.hp=maxhp;
		dangerous=true;
		vulnerable=true;
		collision=TerrainType.TERRESTRIAL;
	}
	
	public void update() {
		animCounter++;
		if (animCounter>=60) {
			animCounter=0;
			heading=Math.random()*Math.PI*2;
		}
		if (knockbackCounter>0) {
			knockbackCounter--;
		} else {
			dx=0;
			dy=0;
			if (animCounter%12==0) {
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
		int img=(animCounter<30) ? animCounter/10 : 5-animCounter/10;
		g.drawImage(blob, x, y, x+width, y+height, img*width, 0, (img+1)*width, height, null);
	}

}
