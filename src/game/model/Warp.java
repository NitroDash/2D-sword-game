package game.model;

public class Warp {
	public int x,y,destX,destY,destAreaX, destAreaY, destAreaFile;
	
	public Warp(int[] data, int x, int y) {
		this.x=x;
		this.y=y;
		this.destAreaX=data[0];
		this.destAreaY=data[1];
		this.destAreaFile=data[2];
		this.destX=data[3];
		this.destY=data[4];
	}
}
