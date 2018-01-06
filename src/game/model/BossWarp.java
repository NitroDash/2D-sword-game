package game.model;

import game.main.Resources;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BossWarp extends Entity{

	private static final BufferedImage bossWarp=Resources.loadImage("BossWarp.png");
	
	public BossWarp(int x, int y) {
		this.x=x;
		this.y=y;
		this.width=GameState.tileWidth;
		this.height=width;
		this.collision=TerrainType.INCORPOREAL;
	}
	
	@Override
	public void render(Graphics g) {
		g.drawImage(bossWarp,x,y,null);
	}

}
