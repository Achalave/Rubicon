
package rubicon.money;

import java.awt.Image;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import rubicon.players.Schuter;

public class Diamond extends Money implements Gem{

    private static Image image;
    private Area bounds;
    
    public Diamond(int x, int y){
        super(x,y);
        int diameter = 27;
        if(image == null){
            try {
                image = ImageIO.read(getClass().getResource("/images/Money/theblues.png"));
            } catch (IOException ex) {
                Logger.getLogger(Schuter.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        return 150;
    }

    @Override
    public Money getNew(int x, int y) {
        return new Diamond(x,y);
    }

    @Override
    public double getSpawnRate(double distance) {
        return 0;
    }

    @Override
    public double getPercent() {
        return .09;
    }

}
