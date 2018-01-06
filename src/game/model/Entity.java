package game.model;

import java.awt.Graphics;

public abstract class Entity {
	public int x,y,dx,dy,width,height,hp,maxhp,direction,knockbackCounter;
	public TerrainType collision;
	
	public abstract void render(Graphics g);
	public boolean intersects(Entity other) {
		return (x+width>other.x&&x<other.x+other.width&&y+height>other.y&&y<other.y+other.height);
	}
	
	public boolean intersects(int x, int y, int width, int height) {
		return (this.x+this.width>x&&this.x<x+width&&this.y+this.height>y&&this.y<y+height);
	}
	
	public double distTo(int x, int y) {
		return Math.sqrt(Math.pow(x-this.x, 2)+Math.pow(y-this.y,2));
	}
	
	public double distTo(Entity other) {
		return distTo(other.x,other.y);
	}
	
	public double headingTowards(int x, int y) {
		double theta=0;
		if (x!=this.x) {
			theta=Math.atan((double)(y-this.y)/(x-this.x));
			if (x<this.x) {
				theta+=Math.PI;
			}
		} else {
			if (y>this.y){
				theta=Math.PI/2;
			} else {
				theta=-Math.PI/2;
			}
		}
		return theta;
	}
	
	public double headingTowards(Entity other) {
		return headingTowards(other.x,other.y);
	}
	
	public void addKnockback(Entity other, int damage) {
		double dist=Math.sqrt(Math.pow(other.x+other.width/2-x-width/2, 2)+Math.pow(other.y+other.height/2-y-height/2, 2));
		dx=(int)(4*(x+width/2-other.x-other.width/2)/dist);
		dy=(int)(4*(y+height/2-other.y-other.height/2)/dist);
		knockbackCounter=7;
		hp=Math.max(0,hp-damage);
	}
	
	public void ejectFromArea(Entity other) {
		if (this.intersects(other)) {
			if (Math.min(Math.abs(other.x+other.width-this.x),Math.abs(this.x+this.width-other.x))<Math.min(Math.abs(other.y+other.height-this.y),Math.abs(this.y+this.height-other.y))) {
				if (other.x+other.width/2<this.x+this.width/2) {
					other.x=this.x-other.width;
				} else {
					other.x=this.x+this.width;
				}
			} else {
				if (other.y+other.height/2<this.y+this.height/2) {
					other.y=this.y-other.height;
				} else {
					other.y=this.y+this.height;
				}
			}
		}
	}
	
	public void center(int x, int y) {
		this.x=x-this.width/2;
		this.y=y-this.height/2;
	}
}
