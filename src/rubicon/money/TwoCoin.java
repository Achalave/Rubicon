
package rubicon.money;

import java.awt.Image;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import rubicon.Vars;
import rubicon.players.Schuter;

public class TwoCoin extends Money{

    private static Image image;
    private Area bounds;
    
    public TwoCoin(int x, int y){
        super(x,y);
        int diameter = 27;
        if(image == null){
            image = Vars.getImage("/images/Money/TwoCoin.png");
        }
        bounds = new Area(new Ellipse2D.Double(x, y, diameter, diameter));
        bounds.add(new Area(new Ellipse2D.Double(x+3,y+3,diameter,diameter)));
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
    public int getValue() {
        return 2;
    }

    @Override
    public Money getNew(int x, int y) {
        return new TwoCoin(x,y);
    }

    @Override
    public double getSpawnRate(double distance) {
        if(distance > 50)
            return 50;
        else if(distance > 65)
            return 100;
        return 0;
    }

}
