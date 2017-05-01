
package rubicon.players;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import rubicon.Data;
import rubicon.Upgrade;
import rubicon.Vars;

public abstract class Player {
    
    protected double x;
    protected double y;
    protected double xSpeed; //Pixels Per Seccond
    protected int width;
    protected int height;
    protected Image image = null;
    private final double powerCooldown;
    private double powerCooldownDecrease = 0; 
    
    public Player(int imageWidth, int imageHeight, double powerCooldown){
        width = imageWidth;
        height = imageHeight;
        this.powerCooldown = powerCooldown;
        this.x = Vars.screenWidth/2-width/2;
        this.y = 200;
    }
    public Player(String path, double powerCooldown){
        image = Vars.getImage(path);
        this.powerCooldown = powerCooldown;
        width = image.getWidth(null);
        height = image.getHeight(null);
        this.x = Vars.screenWidth/2-width/2;
        this.y = 200;
    }
    
    public abstract void startRun();
    
    public abstract void gameOver();
    
    public abstract String getDescription();
    
    public abstract int getCost();
    
    public Image getImage(){
        return image;
    } 
    
    public int getWidth(){
        return width;
    }
    
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(double xSpeed) {
        this.xSpeed = xSpeed;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    public abstract void activate();
    
    
    public void draw(Graphics g){
        g.drawImage(getImage(), (int)x, (int)y, null);
    }
    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y,width,height);
    }
    
    public void tic(long milis){
        
    }
    public void shiftRight(int r){
        x+=r;
    }
    public void shiftLeft(int l){
        x-=l;
    }
    
    public void applyUpgrades(Data d){
        if(d == null){
            powerCooldownDecrease = 0;
        }
        else{
            Upgrade u = d.getUpgrade("Power Cooldown");
            powerCooldownDecrease = u.getIncreaseAmount();
        }
    }
    
    public double getPowerCooldown(){
        return this.powerCooldown-this.powerCooldownDecrease;
    }
    
}
