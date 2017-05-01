
package rubicon.players;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import rubicon.Animator;
import rubicon.GameView;
import rubicon.Vars;

public class Astronaut extends Player{
    
    
    private double preSpeed;
    private boolean slowed = false;
    private boolean active = false;
    private long time; 
    private Animator wings;
    private Animator jetpack;
    private int place = 0;
    private Image readyImage;
    private Image cooldownImage;
    
    public Astronaut(){
        super("/images/Players/Astronaut/AstronautReady.png",15);
        jetpack = new Animator("/images/Players/Astronaut/jetpack",1,(int)x,(int)y);
        jetpack.setLoop(true);
        wings = new Animator("/images/Players/Astronaut/wings",.3,(int)x,(int)y);
        readyImage = image;
        cooldownImage = Vars.getImage("/images/Players/Astronaut/Astronaut.png");
    }
    
    
    @Override
    public void tic(long milis){
        if(active){
            time += milis;
            if(place == 1 || place == 3){
                wings.tic(milis);
                if(!wings.isRunning()){
                    if(place == 1)
                        place = 2;
                    else
                        place = 0;
                }
            }
            else if(place  == 2)
                jetpack.tic(milis);
            if(time/1000 >= getPowerCooldown()){
                image = readyImage;
                time = 0;
                active = false;
            }
        }
        if(slowed){
            
            if(time/1000 < 5){
                preSpeed += GameView.scrollAcceleration*(milis/1000.0);
                GameView.scrollSpeed = Vars.startScrollSpeed-150;
            }
            else{
                slowed = false;
                GameView.scrollSpeed = preSpeed;
                place = 3;
                wings.reverse();
                wings.restart();
            }
            
        }
    }
    
    @Override
    public void activate() {
        if(!active){
            image = cooldownImage;
            slowed = true;
            active = true;
            preSpeed = GameView.scrollSpeed;
            place = 1;
            wings.forward();
            wings.restart();
        }
    }

    @Override
    public void startRun() {
        GameView.scrollAcceleration = GameView.scrollAcceleration - 1;
    }

    @Override
    public void gameOver() {
        image = readyImage;
        slowed = false;
        active = false;
        time = 0;
        place = 0;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y,width,height);
    }
    
    @Override
    public void draw(Graphics g){
        //Draw Bounds
//        g.setColor(Color.cyan);
//        ((Graphics2D)g).draw(getBounds());
        if(place == 1 || place == 3){
            int offsetX = 21;
            wings.setX((int)x-offsetX);
            wings.draw(g);
        }
        else if(place == 2){
            int offsetX = 21;
            jetpack.setX((int)x-offsetX);
            jetpack.draw(g);
        }
        else
            super.draw(g);
    }

    @Override
    public String getDescription() {
        return "Active: Activates a jetpack that temporairily slows down movement speed.";
    }

    @Override
    public int getCost() {
        return 4000;
    }
    
}
