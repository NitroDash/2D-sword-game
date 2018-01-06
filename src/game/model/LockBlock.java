package game.model;

import game.main.Resources;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class LockBlock extends Entity{

	private static final BufferedImage lock=Resources.loadImage("LockBlock.png");
	
	private boolean visible;
	private int areaX, areaY, areaFile;
	
	public LockBlock(int x, int y, int areaX,int areaY, int areaFile) {
		if (ChestData.getUnlocked(areaX,areaY,areaFile)) {
			visible=false;
			return;
		}
		this.visible=true;
		this.areaX=areaX;
		this.areaY=areaY;
		this.areaFile=areaFile;
		this.x=x;
		this.y=y;
		this.collision=TerrainType.INCORPOREAL;
		this.width=GameState.tileWidth;
		this.height=width;
	}
	
	public void update(Player p) {
		if (!visible) {return;}
		if (this.intersects(p)) {
			if (GameState.inventory.keys>0) {
				GameState.inventory.keys--;
				visible=false;
				x=-50;
				y=-50;
				ChestData.unlock(areaX,areaY,areaFile);
			} else {
				ejectFromArea(p);
			}
		}
	}
	
	@Override
	public void render(Graphics g) {
		if (visible) {
			g.drawImage(lock,x,y,null);
		}
	}

}
