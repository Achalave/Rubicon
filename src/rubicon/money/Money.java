
package rubicon.money;

import rubicon.Entity;

public abstract class Money extends Entity{
    public Money(int x, int y){
        super(x,y);
    }
    public abstract Money getNew(int x, int y);
    public abstract int getValue();
}
