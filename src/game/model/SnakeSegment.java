package game.model;

/**
 * Created by Reed on 7/8/2017.
 */
public abstract class SnakeSegment extends Enemy {
    SnakeSegment previous, next;
    int waterLevel, image;

    static int peakX,peakY;

    int getLeft() {
        return x;
    }

    int getRight() {
        return x+width;
    }

    int getCenter() {
        return x+width/2;
    }

    void setHeightToPath() {
        y=peakY+(int)(Math.pow(Math.max(0,Math.abs(getCenter()-peakX)-10),2)/150);
    }

    static void moveCenterTowards(int x, int y) {
        if (Math.sqrt(Math.pow(x-peakX,2)+Math.pow(y-peakY,2))<5) {
            peakX=x;
            peakY=y;
        } else {
            double vecX=x-peakX;
            double vecY=y-peakY;
            double mag=Math.sqrt(vecX*vecX+vecY*vecY);
            vecX*=5/mag;
            vecY*=5/mag;
            peakX+=vecX;
            peakY+=vecY;
        }
    }

    public boolean intersects(Entity other) {
        if (y>=waterLevel) {return false;}
        return (x+width>other.x&&x<other.x+other.width&&Math.min(y+height,waterLevel)>other.y&&y<other.y+other.height);
    }
}
