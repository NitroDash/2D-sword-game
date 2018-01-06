package game.model;

import java.awt.Color;
import java.awt.Graphics;

public class SquirrelLaser extends Projectile{

	private static final int SHOT_SPEED=5;
	
	public SquirrelLaser(Squirrel shooter) {
		double theta=shooter.getTheta();
		dx=(int)(SHOT_SPEED*Math.cos(theta));
		dy=(int)(SHOT_SPEED*Math.sin(theta));
		x=shooter.x+shooter.width/2;
		y=shooter.y+shooter.height/2;
		hp=1;
		vanishOnContact=true;
		damage=5;
		enemy=true;
		collision=TerrainType.INCORPOREAL;
	}
	
	@Override
	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.drawLine(x-2*dx,y-2*dy,x+2*dx,y+2*dy);
	}

}
