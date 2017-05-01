
package rubicon;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

public abstract class Entity {
    protected double x;
    protected double y;
    
    public Entity(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public abstract Area getBounds();
    public abstract Image getImage();
    public abstract double getSpawnRate(double distance);
    
    public void draw(Graphics g){
        g.drawImage(getImage(), (int)x, (int)y, null);
//        g.setColor(Color.cyan);
//        ((Graphics2D)g).draw(getBounds());
    }
    
    public void shift(double shift){
        y -= shift;
        AffineTransform af = new AffineTransform();
        af.translate(0, -shift);
        getBounds().transform(af);
    }
    
    public void tic(long milis){
        
    }
    
    public void setPosition(double x, double y){
        AffineTransform af = new AffineTransform();
        af.translate(x-this.x, y-this.y);
        getBounds().transform(af);
        this.x = x;
        this.y = y;
    }
    @Override
    public String toString(){
        return getClass()+" X:"+x+" Y:"+y;
    }
}
