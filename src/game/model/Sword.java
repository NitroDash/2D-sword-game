package game.model;

import game.main.Resources;
import game.state.GameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Sword extends Weapon{
	
	protected int direction, counter;
	
	@Override
	public void update(Player p) {
		if (counter>0) {
			counter--;
			if (counter>13) {
				GameState.currentState.playerDamageBox(x, y, GameState.tileWidth, GameState.tileWidth);
			}
		}
	}

	public void use(Player p) {
		if (counter>0) {return;}
		x=p.x+((p.direction==2) ? -p.width/2 : (p.direction==3) ? p.width/2 : 0);
		y=p.y+((p.direction==0) ? -2*p.height : (p.direction==1) ? 0 : -p.height);
		direction=p.direction;
		counter=20;
	}
	
	@Override
	public void render(Graphics g, Player p) {
		if (counter>10) {
			g.drawImage(Resources.weapons[0],x, y, x+GameState.tileWidth, y+GameState.tileWidth, direction*GameState.tileWidth, 0, (direction+1)*GameState.tileWidth, GameState.tileWidth, null);
		}
	}

	@Override
	public int getPlayerDX() {
		if (counter>0) {
			return 0;
		} else {
			return 1000;
		}
	}

	@Override
	public int getPlayerDY() {
		if (counter>0) {
			return 0;
		} else {
			return 1000;
		}
	}

	@Override
	public void renderDrop(Graphics g, int x, int y) {
		g.drawImage(Resources.weapons[0],x,y,x+GameState.tileWidth,y+GameState.tileWidth,0,0,GameState.tileWidth,GameState.tileWidth,null);
	}

	@Override
	public void reset() {
		counter=0;
	}

}
