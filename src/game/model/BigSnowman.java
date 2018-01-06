package game.model;

import game.main.GameMain;
import game.main.Resources;
import game.main.SoundPlayer;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BigSnowman extends Enemy{

	private int image, animCounter,state,stateCounter,destX,destY;
	private static int numKilled=0;
	
	private boolean changedMusic;
	
	private SnowmanHead[] heads;
	
	private static final BufferedImage snowman=Resources.loadImage("BigSnowman.png");
	
	public BigSnowman(int x, int y) {
		this.x=x;
		this.y=y;
		this.width=GameState.tileWidth*3/2;
		this.height=GameState.tileWidth*5/4;
		this.image=1;
		this.collision=TerrainType.TERRESTRIAL;
		this.animCounter=0;
		this.vulnerable=false;
		this.dangerous=true;
		this.damage=10;
		this.hp=3;
		this.maxhp=3;
		this.state=0;
		this.stateCounter=60;
		setDest();
	}
	
	private void setDest() {
		destX=(int)(Math.random()*(GameMain.GAME_WIDTH-GameState.tileWidth*4))+GameState.tileWidth;
		destY=(int)(Math.random()*(GameMain.GAME_HEIGHT-GameState.tileWidth*4))+GameState.tileWidth;
	}
	
	@Override
	public void update() {
		if (numKilled>0) {
			hp=0;
			return;
		}
		if (!changedMusic) {
			changedMusic=true;
			SoundPlayer.playSong(4);
		}
		animCounter++;
		if (animCounter>=60) {
			animCounter=0;
		}
		double heading=headingTowards(destX,destY);
		dx=0;
		dy=0;
		if (Math.random()*(Math.abs(Math.sin(heading))+Math.abs(Math.cos(heading)))>Math.abs(Math.sin(heading))) {
			dx=(destX>x)?1:-1;
		} else {
			dy=(destY>y)?1:-1;
		}
		if (distTo(destX,destY)<20) {
			setDest();
		}
		switch (state) {
		case 0://Resting
			stateCounter--;
			if (stateCounter==0) {
				stateCounter=10;
				image=3;
				state=1;
			}
			break;
		case 1://Recharging heads
			stateCounter--;
			if (stateCounter==0) {
				image=0;
				stateCounter=60;
				state=2;
			}
			break;
		case 2://Waiting to shoot
			stateCounter--;
			if (stateCounter==0) {
				image=1;
				heads=new SnowmanHead[2];
				heads[0]=new SnowmanHead(x,y);
				heads[0].knockbackCounter=20;
				heads[0].dx=-2;
				heads[0].dy=-2;
				heads[1]=new SnowmanHead(x+GameState.tileWidth,y);
				heads[1].knockbackCounter=20;
				heads[1].dx=2;
				heads[1].dy=-2;
				GameState.currentState.addEnemy(heads[0]);
				GameState.currentState.addEnemy(heads[1]);
				state=3;
			}
			break;
		case 3://Waiting for heads to die
			if (heads[0].hp<=0&&heads[1].hp<=0) {
				state=4;
				image=2;
				GameState.currentState.addEnemy(new BigSnowmanHead(x+3*width/8,y,this));
			}
			break;
		case 4://Waiting for main head to die
			break;
		case 5://Dead
			stateCounter--;
			dx=0;
			dy=0;
			animCounter=0;
			if (stateCounter==0) {
				hp=0;
				SoundPlayer.playSong(1);
			}
			break;
		}
	}
	
	public void headDead() {
		image=1;
		hp--;
		if (hp<=0) {
			hp=1;
			state=5;
			stateCounter=60;
		} else {
			state=0;
			stateCounter=60;
		}
	}

	@Override
	public void render(Graphics g) {
		if (numKilled!=0) {return;}
		g.drawImage(snowman, x-(GameState.tileWidth-width/2), y-(GameState.tileWidth-height/2), x+width/2+GameState.tileWidth, y+height/2+GameState.tileWidth, 40*image, 40*(animCounter/30), 40*image+40, 40*(animCounter/30)+40, null);
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
