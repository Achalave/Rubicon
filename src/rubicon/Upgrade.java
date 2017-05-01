
package rubicon;

import java.io.Serializable;

public abstract class Upgrade implements Serializable{
    int level = 1;
    public int getCurrentLevel(){
        return level;
    }
    public abstract String getDescription();
    protected abstract int[] getCosts();
    protected abstract double[] increaseAmount();
    public double getIncreaseAmount(){
        if(level == 1)
            return 0;
        return increaseAmount()[level-2];
    }
    public int getUpgradeCost(){
        if(level > getCosts().length)
            return -1;
        return getCosts()[level-1];
    }
    public void levelUp(){
        level++;
    }
}
