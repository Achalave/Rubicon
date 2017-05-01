
package rubicon.entities;

import rubicon.Entity;

public abstract class Obsticle extends Entity{
    
    public Obsticle(int x, int y){
        super(x,y);
    }
    public abstract Obsticle getNew(int x, int y);
    public abstract double getSpawnPoints();
}
