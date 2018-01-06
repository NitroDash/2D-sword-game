package game.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import game.main.Resources;
import game.state.GameState;

public class Player extends Entity{
	private static final int WALK_SPEED=1;
	
	private static BufferedImage img;
	private static BufferedImage swimImg;
	
	private int walkCounter,oldX,oldY;
	public int stamina, MAX_STAMINA;
	
	private boolean stepping;
	
	public Weapon weapon;
	
	public Player(int x, int y) {
		this.x=x;
		this.y=y;
		this.width=GameState.tileWidth-4;
		this.height=GameState.tileWidth/2;
		img=Resources.loadImage("Player.png");
		swimImg=Resources.loadImage("PlayerSwimming.png");
		MAX_STAMINA=100;
		stamina=MAX_STAMINA;
		maxhp=50;
		hp=maxhp;
		collision=TerrainType.AMPHIBIOUS;
		stepping=true;
	}
	
	public void update(boolean[] keys) {
		if (knockbackCounter>0) {
			knockbackCounter--;
		} else {
			if ((oldX==x&&oldY==y)||!GameState.currentState.onlyOn(this, 7)) {
				dx=0;
				dy=0;
				if (keys[0]) {
					dy-=WALK_SPEED;
					direction=0;
				}
				if (keys[1]) {
					dy+=WALK_SPEED;
					direction=1;
				}
				if (keys[2]) {
					dx-=WALK_SPEED;
					direction=2;
				}
				if (keys[3]) {
					dx+=WALK_SPEED;
					direction=3;
				}
				stepping=true;
			} else {
				stepping=false;
			}
			if ((!keys[4]||(dx==0&&dy==0))&&stamina<MAX_STAMINA) {
				stamina++;
			}
			if (dx==0&&dy==0) {
				walkCounter=0;
			} else {
				if (keys[4]&&stamina>=0&&Math.max(Math.abs(dx), Math.abs(dy))<2) {
					dx*=2;
					dy*=2;
					incrementWalk();
					stamina-=2;
				}
				incrementWalk();
			}
			if (keys[5]&&!GameState.currentState.onlyOn(this,4)) {
				if (weapon!=null) {
					weapon.use(this);
				}
			}
		}
		if (weapon!=null) {
			weapon.update(this);
			dx=(weapon.getPlayerDX()==1000)?dx:weapon.getPlayerDX();
			dy=(weapon.getPlayerDY()==1000)?dy:weapon.getPlayerDY();
		}
		oldX=x;
		oldY=y;
	}
	
	private void incrementWalk() {
		if (stepping) {
			walkCounter++;
			walkCounter%=30;
		}
	}
	
	public void resetWeapon() {
		if (weapon!=null) {
			weapon.reset();
		}
	}

	@Override
	public void render(Graphics g) {
		if (weapon!=null) {
			weapon.render(g,this);
		}
		if (GameState.currentState.onlyOn(this,4)) {
			g.drawImage(swimImg,x-2,y-height,x+width+2,y+height,GameState.tileWidth*direction,0,GameState.tileWidth*(direction+1),GameState.tileWidth,null);
		} else {
			int image=direction*2+(walkCounter/15);
			g.drawImage(img,x-2,y-height,x+width+2,y+height,GameState.tileWidth*image,0,GameState.tileWidth*(image+1),GameState.tileWidth,null);
		}
	}
}
