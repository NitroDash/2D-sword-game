package game.model;

import game.state.GameState;

import java.awt.Graphics;

public class WeaponDrop extends Drop{

	public Weapon weapon;
	
	public WeaponDrop(Weapon contents, int x, int y) {
		this.width=GameState.tileWidth;
		this.height=this.width;
		this.weapon=contents;
		this.x=x;
		this.y=y;
	}
	
	@Override
	public boolean getCollected(Player p) {
		if (GameState.inventory.weapons.size()<5) {
			for (int i=0; i<GameState.inventory.weapons.size(); i++) {
				if (this.weapon.getClass().isInstance(GameState.inventory.weapons.get(i))) {
					return false;
				}
			}
			GameState.inventory.weapons.add(this.weapon);
			if (p.weapon==null) {
				GameState.inventory.currentWeapon=GameState.inventory.weapons.size()-1;
				p.weapon=this.weapon;
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void render(Graphics g) {
		this.weapon.renderDrop(g, this.x, this.y);
	}

}
