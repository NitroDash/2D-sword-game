package game.model;

import game.main.Resources;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BallNChain extends Weapon{

	public static final BufferedImage ball=Resources.loadImage("Ball.png");
	public static final BufferedImage chain=Resources.loadImage("Chain.png");
	
	private double angle, startAngle, radius;
	private static final double MAX_RADIUS=GameState.tileWidth*2;
	
	public BallNChain() {
		angle=Math.PI*2;
	}
	
	@Override
	public void update(Player p) {
		if (angle<Math.PI*2) {
			angle+=Math.PI/10;
			radius=MAX_RADIUS;
			this.x=(int)(Math.cos(angle+startAngle)*radius)+p.x+p.width/2;
			this.y=(int)(Math.sin(angle+startAngle)*radius)+p.y+p.height/2;
			GameState.currentState.playerDamageBox(x-5,y-5,10,10);
		}
	}

	@Override
	public void use(Player p) {
		if (angle<Math.PI*2) {return;};
		switch (p.direction){
		case 0:
			startAngle=-Math.PI/2;
			break;
		case 1:
			startAngle=Math.PI/2;
			break;
		case 2:
			startAngle=Math.PI;
			break;
		case 3:
			startAngle=0;
			break;
		}
		angle=0;
	}

	@Override
	public int getPlayerDX() {
		if (angle<Math.PI*2) {
			return 0;
		} else {
			return 1000;
		}
	}

	@Override
	public int getPlayerDY() {
		if (angle<Math.PI*2) {
			return 0;
		} else {
			return 1000;
		}
	}

	@Override
	public void render(Graphics g, Player p) {
		if (angle>=Math.PI*2) {return;}
		int centerX=p.x+p.width/2;
		int centerY=p.y+p.height/2;
		for (double i=0; i<radius; i+=4) {
			g.drawImage(chain, (int)((centerX*(radius-i)+x*i)/radius)-5, (int)((centerY*(radius-i)+y*i)/radius)-5, null);
		}
		g.drawImage(ball,x-5,y-5,null);
	}

	@Override
	public void renderDrop(Graphics g, int x, int y) {
		g.drawImage(ball,x+5,y+5,null);
	}

	@Override
	public void reset() {
		angle=Math.PI*2;
	}

}
