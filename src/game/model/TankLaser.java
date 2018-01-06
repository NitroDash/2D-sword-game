package game.model;

import game.main.Resources;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class TankLaser extends Projectile{

	private static final BufferedImage laser=Resources.loadImage("TankLaser.png");
	
	public TankLaser(Tank parent, int dy) {
		this.x=parent.x+parent.width/2;
		this.y=parent.y+4;
		dx=(parent.direction==0)?10:-10;
		x+=(parent.direction==0)?10:-10;
		this.dy=dy;
		vanishOnContact=false;
		hp=1;
		enemy=true;
		damage=5;
	}
	
	@Override
	public void render(Graphics g) {
		g.drawImage(laser,x-5,y-2,null);
	}

}
