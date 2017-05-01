
package rubicon;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Data implements Serializable{
    long money = 0;
    double lastRunDistance = 0;
    int lastRunMoney = 0;
    double highScoreDist = 0;
    int highScoreMoney = 0;
    int playerIndex = 0;
    
    Map<Integer,Boolean> unlockedPlayers = new HashMap<Integer,Boolean>();
    Map<String, Upgrade> upgrades = new HashMap<String, Upgrade>();
    
    public Data(){
        upgrades.put("Handling", new Upgrade(){
            int[] costs = {200,400,800,1250,2500,4500};
            double[] increase = {50,100,150,200,250,350};
            @Override
            public String getDescription() {
                return "Change directions faster.";
            }

            @Override
            public int[] getCosts() {
                return costs;
            }

            @Override
            protected double[] increaseAmount() {
                return increase;
            }
            
        });
        upgrades.put("Move Speed", new Upgrade(){
            int[] costs = {200,400,800,1500};
            double[] increase = {25,50,75,100};
            @Override
            public String getDescription() {
                return "Move across the screen faster.";
            }

            @Override
            public int[] getCosts() {
                return costs;
            }
            @Override
            protected double[] increaseAmount() {
                return increase;
            }
            
        });
        upgrades.put("Power Cooldown", new Upgrade(){
            int[] costs = {500,1000,2000,4000,10000,30000};
            double[] increase = {.5,1,1.5,2,3,4.5};
            @Override
            public String getDescription() {
                return "Decrease the cooldown of your character's power.";
            }

            @Override
            public int[] getCosts() {
                return costs;
            }
            @Override
            protected double[] increaseAmount() {
                return increase;
            }
            
        });
        unlockedPlayers.put(0, Boolean.TRUE);
    }

    public void unlockPlayer(int index){
        unlockedPlayers.put(index, true);
    }
    
    public boolean isPlayerUnlocked(int index){
        Boolean b = unlockedPlayers.get(index);
        if(b == null)
            return false;
        return b;
    }
    
    public double getLastRunDistance() {
        return lastRunDistance;
    }

    public void setLastRunDistance(double lastRunDistance) {
        this.lastRunDistance = lastRunDistance;
    }

    public int getLastRunMoney() {
        return lastRunMoney;
    }

    public void setLastRunMoney(int lastRunMoney) {
        this.lastRunMoney = lastRunMoney;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }
    
    public Upgrade getUpgrade(String s){
        return upgrades.get(s);
    }
    
    public double getHighScoreDist() {
        return highScoreDist;
    }

    public void setHighScoreDist(double highScoreDist) {
        this.highScoreDist = highScoreDist;
    }


    public int getHighScoreMoney() {
        return highScoreMoney;
    }

    public void setHighScoreMoney(int highScoreMoney) {
        this.highScoreMoney = highScoreMoney;
    }


    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }
    
    public static void saveData(String path, Data data){
        try {
            File f = new File(path);
            if(!f.exists())
                f.createNewFile();
            FileOutputStream f_out = new FileOutputStream(path);
            ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
            obj_out.writeObject ( data );
            f_out.close();
        } catch (IOException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Data loadData(String path){
        try {
            FileInputStream f_in = new FileInputStream(path);
            ObjectInputStream obj_in = new ObjectInputStream (f_in);
            Object obj = obj_in.readObject();
            f_in.close();
            if (obj instanceof Data)
            {
                return (Data)obj;
            }
        } catch (IOException ex) {
            System.err.println("Failed to load data!");
            return null;
        } catch (ClassNotFoundException ex) {
            System.err.println("Failed to cast data!");
            return null;
        }
        return null;
    }
}
