/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rubicon.players;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import rubicon.Animator;

/**
 *
 * @author michaelhaertling
 */
public class Businessman extends Player{
    public Businessman(){
        super("/images/Players/Businessman/Businessman.png",0);
    }
    
    @Override
    public void startRun() {
    }

    @Override
    public void gameOver() {
    }

    @Override
    public String getDescription() {
        return "Just your basic starting character.";
    }

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public void activate() {
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y,width,height);
    }
    
    
}
