
package rubicon.entities;

import java.awt.Image;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import rubicon.Vars;
import rubicon.players.Schuter;

public class Spike extends Obsticle{
    
    static Image image;
    Area bounds;
    static Area masterBounds;
    
    public Spike(int x, int y){
        super(x,y);
        int width = 50;
        int height = 50;
        Polygon spike;
        if(image == null){
            image = Vars.getImage("/images/Obsticles/Spike.png");
        }
        if(masterBounds == null){
            spike = new Polygon();
            spike.addPoint((width/2), 0);
            spike.addPoint(0, height);
            spike.addPoint(width, height);
            masterBounds = new Area(spike);
        }
        bounds = (Area)masterBounds.clone();
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        bounds.transform(at);
    }
    
    
    @Override
    public Area getBounds() {
        return bounds;
    }

    @Override
    public Image getImage() {
        return image;
    }
    

    @Override
    public Obsticle getNew(int x, int y) {
        return new Spike(x,y);
    }

    @Override
    public double getSpawnRate(double distance) {
        return 100;
    }

    @Override
    public double getSpawnPoints() {
        return 1;
    }

}
