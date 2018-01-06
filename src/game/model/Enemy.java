package game.model;

public abstract class Enemy extends Entity{
	public int damage;
	public boolean dangerous,vulnerable;
	
	public abstract void update();
	public Drop getDrop() {
		if (Math.random()<0.3) {
			return new Heart(x,y);
		} else if (Math.random()<0.5) {
			return new Money(x,y);
		} else {
			return null;
		}
	}
}
