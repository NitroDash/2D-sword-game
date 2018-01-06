package game.model;

import game.main.GameMain;
import game.main.Resources;
import game.main.SoundPlayer;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BlockWizardBlock extends Enemy{

	private static final BufferedImage block=Resources.loadImage("BlockWizardBlock.png");
	
	private BlockWizard parent;
	private int z, dz;
	
	public BlockWizardBlock(BlockWizard parent) {
		this.damage=25;
		this.collision=TerrainType.INCORPOREAL;
		this.width=GameState.tileWidth*2;
		this.height=this.width/2;
		this.vulnerable=false;
		this.dangerous=false;
		this.z=GameMain.GAME_HEIGHT*3/2;
		this.hp=1;
		this.maxhp=1;
		this.parent=parent;
	}
	
	@Override
	public void update() {
		if (dangerous) {
			dangerous=false;
		}
		z+=dz;
		if (z<0) {
			z=0;
			dz=0;
			dangerous=true;
		} else if (z>GameMain.GAME_HEIGHT*3/2) {
			dz=0;
		} else if (z==0&&dz==0) {
			this.ejectFromArea(GameState.currentState.getPlayer());
		}
		if (parent.hp==0) {
			this.hp=0;
			SoundPlayer.playSong(1);
		}
	}

	public void drop() {
		if (parent.hp<=15) {
			center(GameState.currentState.getPlayer().x, GameState.currentState.getPlayer().y);
			x+=(int)((Math.random()-0.5)*GameState.tileWidth*3);
			y+=(int)((Math.random()-0.5)*GameState.tileWidth*2);
		} else {
			x=(int)(Math.random()*(GameMain.GAME_WIDTH-4*GameState.tileWidth))+2*GameState.tileWidth;
			y=(int)(Math.random()*(GameMain.GAME_HEIGHT-3*GameState.tileWidth))+GameState.tileWidth;
		}
		dz=-8;
	}
	
	public void rise() {
		dz=8;
		dangerous=false;
	}
	
	@Override
	public void render(Graphics g) {
		if (dz!=0) {
			g.setColor(BlockWizard.shadowColor);
			double shadowRatio=1-(double)z/(GameMain.GAME_HEIGHT*3/2);
			int shadowWidth=(int)(width*shadowRatio);
			int shadowHeight=(int)(height*shadowRatio);
			g.fillRoundRect(x+(width-shadowWidth)/2, y+(height-shadowHeight)/2, shadowWidth, shadowHeight,4,4);
		}
		g.drawImage(block,x,y-z,null);
	}
	
	@Override
	public Drop getDrop() {
		return null;
	}

}
