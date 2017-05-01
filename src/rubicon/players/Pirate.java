
package rubicon.players;

import java.awt.Rectangle;
import rubicon.Vars;

public class Pirate extends Player{
    public Pirate(){
        super("/images/Players/pirate.png",15);
    }
    
    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y,width,height);
    }
    
    @Override
    public void tic(long milis){

    }
    
    @Override
    public void activate() {

    }
    
    
    @Override
    public void startRun() {
        Vars.gemSpawnRateIncrease = 1.75;
    }

    @Override
    public void gameOver() {
        Vars.gemSpawnRateIncrease = 0;
    }

    @Override
    public String getDescription() {
        return "Passive: Higher rate of gem spawning.";
    }

    @Override
    public int getCost() {
        return 3200;
    }
}
