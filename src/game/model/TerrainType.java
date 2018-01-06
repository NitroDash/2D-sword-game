package game.model;

public enum TerrainType {
	TERRESTRIAL(true,false,true,false,false,true,true,true,false,true,true,true),
	AQUATIC(false,false,false,false,true,false,false,false,true,false,false,false),
	AMPHIBIOUS(true,false,true,false,true,true,true,true,true,true,true,true),
	INCORPOREAL(true,true,true,true,true,true,true,true,true,true,true,true),
	PUSHBLOCK(true,false,true,false,false,false,true,true,false,false,true,false);
	
	//ground, walls, enemy, chest, water, warp, pushblock, ice, water enemy, boss warp, lock, dealer
	
	private final boolean[] passable;
	
	TerrainType(boolean... pass) {
		this.passable=pass;
	}
	
	public boolean getPassable(int tile) {
		if (tile==Integer.MIN_VALUE) {
			return true;
		}
		return this.passable[tile];
	}
}
