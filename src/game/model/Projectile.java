package game.model;

import game.main.GameMain;

public abstract class Projectile extends Entity{
	
	public boolean enemy, vanishOnContact;
	public int damage;
	
	public void update() {
		x+=dx;
		y+=dy;
		if (x<0||x>GameMain.GAME_WIDTH||y<0||y>GameMain.GAME_HEIGHT) {
			this.hp=0;
		}
	}
}
