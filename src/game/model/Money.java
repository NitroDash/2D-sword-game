package game.model;

import game.main.Resources;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Money extends Drop{

	private static int[] values={1,5,10,20,50,100,500};
	private int value;
	
	public static BufferedImage money=Resources.loadImage("Money.png");
	
	public Money(int x, int y) {
		this.x=x;
		this.y=y;
		this.width=GameState.tileWidth/2;
		this.height=width;
		this.value=getRandomValue();
	}
	
	public Money(int x, int y, int value) {
		this.x=x;
		this.y=y;
		this.width=GameState.tileWidth/2;
		this.height=width;
		this.value=value;
	}
	
	private static int getRandomValue() {
		double log=Math.log(Math.random());
		return (int)Math.floor(Math.min(6,Math.max(0,-log)));
	}
	
	@Override
	public boolean getCollected(Player p) {
		GameState.currentState.money+=values[value];
		return true;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(money,x,y,x+width,y+height,value*width,0,(value+1)*width,height,null);
	}

}
