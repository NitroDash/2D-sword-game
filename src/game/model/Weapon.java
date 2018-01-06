package game.model;

import java.awt.Graphics;

public abstract class Weapon {
	public int x,y;
	
	public abstract void update(Player p);
	public abstract void use(Player p);
	public abstract int getPlayerDX();
	public abstract int getPlayerDY();
	public abstract void render(Graphics g, Player p);
	public abstract void renderDrop(Graphics g, int x, int y);
	public abstract void reset();
}
