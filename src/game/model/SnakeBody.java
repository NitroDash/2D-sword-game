package game.model;

import game.main.Resources;
import game.state.GameState;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Reed on 7/8/2017.
 */
public class SnakeBody extends SnakeSegment {

    private static final BufferedImage body= Resources.loadImage("SnakeSegment.png");

    public SnakeBody(SnakeSegment parent, int index) {
        this.previous=parent;
        if (index==0) {
            image=0;
        } else {
            image=index%2+1;
        }
        if (image==0) {
            this.width=30;
        } else {
            this.width=10;
        }
        this.height=30;
        this.direction=parent.direction;
        linkToParent();
        this.y=parent.y;
        this.maxhp=1;
        this.hp=1;
        this.vulnerable=false;
        this.dangerous=true;
        this.damage=5;
        this.waterLevel=parent.waterLevel;
        this.collision=TerrainType.INCORPOREAL;
        GameState.currentState.addEnemy(this);
        if (index>0) {
            next=new SnakeBody(this,index-1);
        }
    }

    @Override
    public void update() {
        linkToParent();
        setHeightToPath();
        if (previous.hp==0) {
            hp=0;
        }
    }

    private void linkToParent() {
        waterLevel=previous.waterLevel;
        if (previous.direction==0) {
            this.x=previous.getRight();
        } else {
            this.x=previous.getLeft()-width;
        }
        direction=previous.direction;
    }

    @Override
    public void render(Graphics g) {
        if (y<waterLevel) {
            int renderX=x;
            int renderWidth=width;
            if (direction==0) {
                renderX+=width;
                renderWidth*=-1;
            }
            int imageX=20+10*image;
            int imageWidth=10;
            if (image==0){
                imageWidth=30;
                imageX=0;
            }
            g.drawImage(body,renderX,y,renderX+renderWidth,Math.min(y+height,waterLevel),imageX,0,imageX+imageWidth,Math.min(30,waterLevel-y),null);
        }
    }

    public Drop getDrop() {
        return null;
    }
}
