package game.model;

import game.main.Resources;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Squirrel extends Enemy{

	private int[] directionImage={0,1,4,3,5,2};
	
	public int direction, turnCounter;
	private static BufferedImage squirrel;
	
	public Squirrel(int x, int y) {
		this.x=x;
		this.y=y;
		this.width=GameState.tileWidth/2;
		this.height=this.width;
		if (squirrel==null) {
			squirrel=Resources.loadImage("Squirrel.png");
		}
		direction=(int)Math.floor(Math.random()*6);
		maxhp=10;
		hp=maxhp;
		damage=5;
		vulnerable=true;
		dangerous=true;
		collision=TerrainType.TERRESTRIAL;
	}
	
	@Override
	public void update() {
		if (knockbackCounter>0) {
			knockbackCounter--;
		} else {
			dx=0;
			dy=0;
			if (Math.random()<0.01) {
				GameState.currentState.addProjectile(new SquirrelLaser(this));
			}
			if (Math.random()<0.05) {
				turnCounter++;
				if (turnCounter>=10) {
					if (Math.random()<0.5) {
						direction++;
						direction%=6;
					} else {
						direction--;
						if (direction<0) {direction=5;}
					}
					turnCounter=0;
				}
			}
		}
	}

	@Override
	public void render(Graphics g) {
		int img=directionImage[direction];
		g.drawImage(squirrel, x-width/2, y, x+3*width/2, y+height, 20*(img%3), 10*(img/3), 20*(img%3+1), 10*(img/3+1), null);
	}
	
	public double getTheta() {
		switch (directionImage[direction]) {
		case 0:
			return 0;
		case 1:
			return Math.PI/5;
		case 2:
			return -Math.PI/5;
		case 3:
			return Math.PI;
		case 4:
			return 4*Math.PI/5;
		case 5:
			return -4*Math.PI/5;
		default:
			return 0;
		}
	}

}
