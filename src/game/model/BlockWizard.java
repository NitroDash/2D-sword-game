package game.model;

import game.main.GameMain;
import game.main.Resources;
import game.main.SoundPlayer;
import game.state.GameState;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BlockWizard extends Enemy{

	private static final BufferedImage blockWizard=Resources.loadImage("BlockWizard.png");
	
	public static final Color shadowColor=new Color(0.5f,0.5f,0.5f,0.5f);
	
	private BlockWizardBlock block;
	private int z, dz;
	private int state, stateCounter, image;
	private boolean switchedMusic;
	
	public static int numKilled=0;
	
	public BlockWizard(int x, int y) {
		if (x<GameMain.GAME_WIDTH/2) {
			direction=0;
		} else {
			direction=1;
		}
		hp=25;
		maxhp=25;
		this.x=x;
		this.y=y;
		this.collision=TerrainType.TERRESTRIAL;
		this.width=GameState.tileWidth;
		this.height=width;
		this.damage=5;
		this.vulnerable=false;
		this.dangerous=true;
		this.block=new BlockWizardBlock(this);
		GameState.currentState.addEnemy(this.block);
		state=0;
		stateCounter=60;
		image=0;
		switchedMusic=false;
		if (numKilled>=1) {
			hp=0;
		}
	}
	
	@Override
	public void update() {
		if (!switchedMusic&&hp>0) {
			switchedMusic=true;
			SoundPlayer.playSong(4);
		}
		incrementKnockback();
		switch (state) {
		case 0://Standing
			stateCounter--;
			if (stateCounter==0) {
				state=1;
				stateCounter=20;
				vulnerable=false;
				image=1;
			}
			break;
		case 1://Casting spell
			stateCounter--;
			if (stateCounter==0) {
				state=2;
				dangerous=false;
				dz=8;
				block.rise();
			}
			break;
		case 2://Rising
			z+=dz;
			if (z>GameMain.GAME_HEIGHT*3/2) {
				dz=0;
				state=3;
				stateCounter=30;
			}
			break;
		case 3://In air
			stateCounter--;
			if (stateCounter==0) {
				state=4;
				dz=-8;
				image=0;
				x=(int)(Math.random()*(GameMain.GAME_WIDTH-3*GameState.tileWidth))+GameState.tileWidth;
				y=(int)(Math.random()*(GameMain.GAME_HEIGHT-3*GameState.tileWidth))+GameState.tileWidth;
				if (x<GameMain.GAME_WIDTH/2) {
					direction=0;
				} else {
					direction=1;
				}
				block.drop();
			}
			break;
		case 4://Falling
			z+=dz;
			if (z<0) {
				z=0;
				dz=0;
				state=0;
				dangerous=true;
				vulnerable=true;
				stateCounter=60;
			}
			break;
		}
	}

	private void incrementKnockback() {
		if (knockbackCounter>0) {
			knockbackCounter--;
			if (knockbackCounter==0) {
				dx=0;
				dy=0;
			}
		}
	}
	
	@Override
	public void render(Graphics g) {
		if (hp==0) {
			return;
		}
		g.drawImage(blockWizard, x, y-z, x+width, y-z+height, image*20, direction*20, (image+1)*20, (direction+1)*20, null);
		if (state==4) {
			g.setColor(shadowColor);
			double shadowRatio=1-(double)z/(GameMain.GAME_HEIGHT*3/2);
			int shadowWidth=(int)(width*shadowRatio);
			int shadowHeight=(int)(height*shadowRatio);
			g.fillOval(x+(width-shadowWidth)/2, y+(height-shadowHeight)/2, shadowWidth, shadowHeight);
		}
	}

	@Override
	public Drop getDrop() {
		if (numKilled==0) {
			numKilled++;
			return new HealthExtender(x,y);
		} else {
			return null;
		}
	}
}
