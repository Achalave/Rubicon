
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


public class SpikeSet extends Obsticle{
    static Image image;
    static Area masterBounds;
    Area bounds;
    public SpikeSet(int x, int y){
        super(x,y);
        int width = 90;
        int height = 50;
        Polygon spike;
        if(image == null){
            image = Vars.getImage("/images/Obsticles/SpikeSet.png");
        }
        if(masterBounds == null){
            spike = new Polygon();
            spike.addPoint(width/4, 0);
            spike.addPoint(0, height);
            spike.addPoint(width, height);
            spike.addPoint(width-(width/4), 0);
            spike.addPoint(width/2, height-10);
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
    public double getSpawnRate(double distance) {
        return 90;
    }

    @Override
    public Obsticle getNew(int x, int y) {
        return new SpikeSet(x,y);
    }

    @Override
    public double getSpawnPoints() {
        return 3;
    }
    
}
