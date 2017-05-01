/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rubicon.players;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import rubicon.Animator;
import rubicon.GameView;
import rubicon.Vars;
import rubicon.entities.Obsticle;

public class Ninja extends Player{

    Animator star;
    Image poof;
    Image cooldownImage;
    Image readyImage;
    boolean thrown = false;
    boolean ready = true;
    boolean poofing = false;
    long poofTime = 0;
    Point2D[] points = new Point2D[3]; 
    final double starSpeed = -300;
    final double[] rads = {3*Math.PI/2-1,3*Math.PI/2,3*Math.PI/2+1};
    boolean[] goodStars = {true,true,true};
    long time = 0;
    double distance = 0;
    double maxDistance = 90;
    
    public Ninja(){
        super("/images/Players/Ninja/ninja.png",8.5);
        star = new Animator("/images/Players/Ninja/ninja star",.1,0,0);
        poof = Vars.getImage("/images/Players/Ninja/poof.png");
        star.setLoop(true);
        readyImage = image;
        cooldownImage = Vars.getImage("/images/Players/Ninja/ninjaReady.png");
    }
    
    @Override
    public void startRun() {
        
    }

    @Override
    public void gameOver() {
        thrown = false;
        time = 0;
        ready = true;
        goodStars[0]=true;
        goodStars[1]=true;
        goodStars[2]=true;
        distance = 0;
        poofing = false;
        poofTime = 0;
        image = readyImage;
    }

    @Override
    public void tic(long milis){
        if(!ready){
            time += milis;
            if(time >= getPowerCooldown()*1000.0){
                gameOver();
                return;
            }
            if(poofing){
                poofTime += milis;
                double dec = (GameView.scrollSpeed*(milis/1000.0));
                for(Point2D p:points){
                    p.setLocation(p.getX(), p.getY()-dec);
                }
                if(poofTime >= 1000){
                    poofing = false;
                }
            }
            if(thrown){
                
                star.tic(milis);
                double dist = starSpeed*(milis/1000.0);
                distance += Math.abs(dist);
                for(int i=0; i<points.length; i++){
                    if(goodStars[i]){
                        double rad = rads[i];
                        points[i].setLocation(points[i].getX()+dist*Math.cos(rad), points[i].getY()+dist*Math.sin(rad));
                    }
                }
                //Check collision
                Rectangle r = new Rectangle(0,0,star.getWidth(),star.getHeight());
                for(int i=0; i<GameView.entities.size(); i++){
                    Obsticle o = GameView.entities.get(i);
                    for(int k=0; k<points.length; k++){
                        Point2D p = points[k];
                        if(goodStars[k]){
                            r.setLocation((int)p.getX(),(int)p.getY());
                            if(o.getBounds().intersects(r)){
                                GameView.entities.remove(i);
                                goodStars[k] = false;
                            }
                        }
                    }
                }
                if(distance >= maxDistance){
                    thrown = false;
                    poofing = true;
                }
            }
        }
    }
    
    @Override
    public void draw(Graphics g){
        super.draw(g);
        for(int i=0; i<points.length; i++){
            if(goodStars[i]){
                Point2D p = points[i];
                if(thrown){
                    star.setX((int)p.getX());
                    star.setY((int)p.getY());
                    star.draw(g);
                }else if(poofing){
                    g.drawImage(poof, (int)p.getX(), (int)p.getY(), null);
                }
                
            }
        }
        
    }
    
    @Override
    public String getDescription() {
        return "Throws shurikens downward destroying close obsticles.";
    }

    @Override
    public int getCost() {
        return 4000;
    }

    @Override
    public void activate() {
        if(ready){
            image = cooldownImage;
            points[0] = new Point2D.Double(x+width/2-star.getWidth()/2,y+height);
            points[1] = new Point2D.Double(x+width/2-star.getWidth()/2,y+height);
            points[2] = new Point2D.Double(x+width/2-star.getWidth()/2,y+height);
            thrown = true;
            ready = false;
        }
    }

    
}
