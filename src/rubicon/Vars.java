
package rubicon;

import rubicon.money.Ruby;
import rubicon.money.Money;
import rubicon.money.Gem;
import rubicon.money.Diamond;
import rubicon.money.OneCoin;
import rubicon.money.TwoCoin;
import rubicon.money.Emerald;
import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import rubicon.entities.Obsticle;
import rubicon.entities.Spike;
import rubicon.entities.SpikeSet;
import rubicon.players.Astronaut;
import rubicon.players.Businessman;
import rubicon.players.Ninja;
import rubicon.players.Pirate;
import rubicon.players.Player;
import rubicon.players.Schuter;

public class Vars {
    
    public static final Color backgroundColor = new Color(139,69,19);
    public static final int screenWidth = 1000;
    public static final int screenHeight = (int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight()-12;
    public static final double startScrollSpeed = 300;
    public static final double startAcceleration = 5;
    public static final double startSpawnPointIncrease = .05;
    public static double gemSpawnRateIncrease = 0;
    public static final Gem[] gems = {new Emerald(0,0), new Ruby(0,0), new Diamond(0,0)};
    
    public static final Player[] players = {new Businessman() , new Schuter(),new Astronaut(),new Ninja(),new Pirate()};
    
    //Entities
    public static final Obsticle[] entities = {new Spike(0,0), new SpikeSet(0,0)};
    
    //Coins
    public static final Money[] coins = {new OneCoin(0,0), new TwoCoin(0,0)};
    
    
    public static Obsticle getRandomEntity(double dist){
        ArrayList<Obsticle> poss = new ArrayList<Obsticle>();
        long totalPoss = 0;
        for(Obsticle e:entities){
            if(e.getSpawnRate(dist) > 0){
                poss.add(e);
                totalPoss += e.getSpawnRate(dist);
            }
        }
        
        int num = (int)(Math.random()*totalPoss);
        int numAt = 0;
        for(Obsticle e:poss){
            numAt += e.getSpawnRate(dist);
            if(numAt > num)
                return e;
        }
        return null;
    }
    
    public static Money getRandomCoin(int x, int y, double dist){
        ArrayList<Money> poss = new ArrayList<Money>();
        long totalPoss = 0;
        for(Money e:coins){
            if(e.getSpawnRate(dist) > 0){
                poss.add(e);
                totalPoss += e.getSpawnRate(dist);
            }
        }
        int num = (int)(Math.random()*totalPoss);
        double numAt = 0;
        for(Money e:poss){
            numAt += e.getSpawnRate(dist);
            if(numAt > num){
                numAt = 0;
                double rand = Math.random()*100;
                for(Gem g:gems){
                    numAt += g.getPercent()+gemSpawnRateIncrease;
                    if(numAt > rand){
                        return ((Money)g).getNew(x, y);
                    }
                }
                
                return e.getNew(x, y);
            }
        }
        return null;
    }
    
    
    public static Image getImage(String path){
        return new ImageIcon(Vars.class.getResource(path)).getImage();

//        try {
//            return ImageIO.read(new File("Game"+path));
//        } catch (IOException ex) {
//            Logger.getLogger(Vars.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
    }
    
    //~~~~~Obsticle Spawning~~~~~~~~
    //How Early to spawn
    public static final int spawnBelowScreen = 100;//in pixels
    //How far between each spawn (y)
    public static final double[] spawnInterval = {4,4.5};//in mole meters
    
    
    //~~~~~Money Spawning~~~~~~~~
    public static final int pixelsBetweenCoins = 25;
    public static final double coinSpawnRate = 70;
    public static final int maxCoinSize = 30;
    
    public static final double moleMeterConversion = 100;//pixels per mole meter
    
    //Cliff (all in pixels)
    public static int[] leftInterval = {50,100};
    public static int[] rightInterval;
    public static final int extra = 500;
    public static final int earlyGen = 500;
    public static final int[] heightInterval = {40,100};
    
    //Player
    //  MOVEMENT
    public static double xAcceleration = 400;//Pixels Per Seccond Squared 400 200
    public static double maxSpeed = 200;
}
