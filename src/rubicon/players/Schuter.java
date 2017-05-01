/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rubicon.players;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import rubicon.Animator;
import rubicon.GameView;
import rubicon.Vars;
import rubicon.entities.Obsticle;


public class Schuter extends Player{
    private boolean dropped = false;
    private Granade granade;
    private long time;
    public Schuter(){
        super("/images/Players/Schuter/SchuterSmall.png",15);
        granade = new Granade();
    }
    
    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y,width,height);
    }
    
    @Override
    public void tic(long milis){
        if(dropped){
            time += milis;
            granade.tic(milis);
            if(time >= getPowerCooldown()*1000){
                dropped = false;
                time = 0;
                granade.stop();
            }
        }
    }
    
    @Override
    public void activate() {
        dropped = true;
        granade.drop();
    }
    
    @Override
    public void draw(Graphics g){
        super.draw(g);
        if(!dropped){
            granade.setX((int)x+30);
            granade.setY((int)y+61);
        }
        granade.draw(g);
    }
    
    @Override
    public void startRun() {
        GameView.scrollAcceleration = GameView.scrollAcceleration - 1;
    }

    @Override
    public void gameOver() {
        dropped = false;
        time = 0;
        granade.stop();
    }

    @Override
    public String getDescription() {
        return "Active: Drops a grenade blowing up the first obsticle it touches.\nPassive: Accelerates at a slower rate.";
    }

    @Override
    public int getCost() {
        return 3200;
    }
    
    
}

class Granade{
    Image image;
    Animator explosion;
    boolean exploaded = false;
    boolean dropped = false;
    boolean done = false;
    long time;
    int speed = 350;
    double x,y;
    public Granade(){
        explosion = new Animator("/images/Players/Schuter/explosion",.9,0,0);
        image = Vars.getImage("/images/Players/Schuter/granade.png");
    }
    
    public void draw(Graphics g){
        if(!exploaded)
            g.drawImage(image, (int)x, (int)y, null);
        else if(!done){
            explosion.draw(g);
        }
    }
    
    public void drop(){
        dropped = true;
    }
    
    public void stop(){
        dropped = false;
        exploaded = false;
        done = false;
    }
    
    public void tic(long milis){
        if(dropped && !exploaded){
            y += speed*milis/1000.0;
            if(y > Vars.screenHeight){
                exploaded = true;
                return;
            }
            for(int i=0; i<GameView.entities.size(); i++){
                Obsticle e = GameView.entities.get(i);
                if(e.getBounds().intersects(new Rectangle((int)x,(int)y,image.getWidth(null),image.getHeight(null)))){
                    GameView.entities.remove(i);
                    exploaded = true;
                    int offset = explosion.getWidth() - image.getWidth(null);
                    explosion.forward();
                    explosion.restart();
                    explosion.setX((int)x-offset);
                    explosion.setY((int)y);
                    return;
                }
            }
        }
        else if(!done){
            //Move explosion with scroll
            explosion.setY(explosion.getY()-(int)(GameView.scrollSpeed*(milis/1000.0)));
            explosion.tic(milis);
            if(!explosion.isRunning()){
                done = true;
            }
        }
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
}
