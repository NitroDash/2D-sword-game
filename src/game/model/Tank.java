package game.model;

import game.main.GameMain;
import game.main.Resources;
import game.main.SoundPlayer;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Tank extends Enemy{

	private static final BufferedImage tank=Resources.loadImage("Tank.png");
	
	private int animCounter, laserCountdown, laserdy;
	private boolean changedMusic;
	
	static int numKilled=0;
	
	private TankBall ball;
	
	public Tank(int x, int y) {
		this.x=x;
		this.y=y;
		if (this.x<GameMain.GAME_WIDTH/2) {
			this.direction=0;
		} else {
			this.direction=1;
		}
		this.damage=5;
		this.width=GameState.tileWidth*2;
		this.height=GameState.tileWidth;
		this.vulnerable=true;
		this.dangerous=true;
		this.collision=TerrainType.TERRESTRIAL;
		this.maxhp=50;
		this.hp=maxhp;
		laserCountdown=110-this.hp+(int)(Math.random()*60);
		laserdy=(int)(Math.random()*3-1.5);
		if (numKilled>0) {
			hp=0;
		}
	}
	
	@Override
	public void update() {
		if (numKilled>0) {return;}
		if (!this.vulnerable) {
			if (knockbackCounter>0) {
				knockbackCounter--;
			}
			dx=0;
			dy=0;
			ball.slow();
			laserCountdown--;
			if (laserCountdown<=0) {
				SoundPlayer.playSong(1);
				hp=0;
			}
			return;
		}
		if (!changedMusic) {
			changedMusic=true;
			this.ball=new TankBall(this);
			GameState.currentState.addEnemy(this.ball);
			SoundPlayer.playSong(4);
		}
		if (knockbackCounter>0) {
			knockbackCounter--;
			if (knockbackCounter==0) {
				this.ball.shoot();
			}
		}
		dx=0;
		dy=0;
		if (Math.random()<0.2) {
			double heading=headingTowards(GameState.currentState.getPlayer());
			if (Math.abs(Math.cos(heading))>Math.abs(Math.sin(heading))) {
				dx=(Math.cos(heading)>0)?1:-1;
			} else {
				dy=(Math.sin(heading)>0)?1:-1;
			}
		}
		if (dx>0) {
			direction=0;
		} else if (dx<0) {
			direction=1;
		}
		animCounter++;
		if (animCounter>=30) {
			animCounter=0;
		}
		laserCountdown--;
		if (laserCountdown<0) {
			GameState.currentState.addProjectile(new TankLaser(this,this.laserdy));
		}
		if (laserCountdown<-10) {
			laserCountdown=110-this.hp+(int)(Math.random()*60);
			laserdy=(int)(Math.random()*3-1.5);
		}
		if (this.hp<=0) {
			this.hp=1;
			this.vulnerable=false;
			laserCountdown=120;
			dangerous=false;
		}
	}

	@Override
	public void render(Graphics g) {
		if (numKilled>0) {return;}
		g.drawImage(tank,x,y,x+width,y+height,40*(animCounter/10),20*direction,40*(animCounter/10)+40,20*direction+20,null);
	}
	
	public Drop getDrop() {
		if (numKilled>0) {
			return null;
		} else {
			numKilled++;
			return new HealthExtender(x+width/2,y+height/2);
		}
	}

}
