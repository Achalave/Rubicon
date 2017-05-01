
package rubicon;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cliff {
    double shift;
    int leftX;
    int rightX;
    List<Polygon> left = Collections.synchronizedList(new ArrayList<Polygon>());
    List<Polygon> right = Collections.synchronizedList(new ArrayList<Polygon>());
    Rectangle screen;
    
    public Cliff(Rectangle screen){
        this.screen = screen;
        setup();
    }
    
    private void setup(){
        leftX = 0;
        rightX = (int)screen.getWidth();
        Vars.rightInterval = new int[2];
        Vars.rightInterval[0] = screen.width-Vars.leftInterval[1];
        Vars.rightInterval[1] = screen.width-Vars.leftInterval[0];
        generate();
    }
    
    private void generate(){
        int leftHeight = 0;
        int rightHeight = 0;
        if(!left.isEmpty()){
            Polygon leftern = left.get(left.size()-1);
            leftHeight = leftern.getBounds().height + leftern.getBounds().y;
        }
        if(!right.isEmpty()){
            Polygon rightern = right.get(right.size()-1);
            rightHeight = rightern.getBounds().height + rightern.getBounds().y;
        }
        
        if(leftHeight < screen.height+Vars.earlyGen){
            //Left Side
            Polygon l = new Polygon();
            l.addPoint(0, leftHeight);
            l.addPoint(leftX, leftHeight);
            while(leftHeight < screen.height+Vars.extra){
                leftX = (int)Math.round((Math.random()*(Vars.leftInterval[1]-Vars.leftInterval[0]))+Vars.leftInterval[0]);
                leftHeight += (int)Math.round(((Math.random()*(Vars.heightInterval[1]-Vars.heightInterval[0]))+Vars.heightInterval[0]));
                l.addPoint(leftX, leftHeight);
            }
            l.addPoint(0, leftHeight);
            left.add(l);
        }
        if(rightHeight < screen.height+Vars.earlyGen){
            //Right Side
            Polygon r = new Polygon();
            r.addPoint(screen.width, rightHeight);
            r.addPoint(rightX, rightHeight);
            while(rightHeight < screen.height+Vars.extra){
                rightX = (int)Math.round((Math.random()*(Vars.rightInterval[1]-Vars.rightInterval[0]))+Vars.rightInterval[0]);
                rightHeight += (int)Math.round(((Math.random()*(Vars.heightInterval[1]-Vars.heightInterval[0]))+Vars.heightInterval[0]));
                r.addPoint(rightX, rightHeight);
            }
            r.addPoint(screen.width, rightHeight);
            right.add(r);
        }
    }
    
    private void cleanArrays(){
        if(left.isEmpty() || right.isEmpty())
            return;
        if(left.get(0).getBounds().getHeight()+left.get(0).getBounds().getY() < 0)
            left.remove(0);
        if(right.get(0).getBounds().getHeight()+right.get(0).getBounds().getY() < 0)
            right.remove(0);
    }
    
    public void shift(double y){
        shift += y;
        int shiftAmmount = (int)shift;
        shift-=shiftAmmount;
        for(int i=0; i<left.size(); i++){
            Polygon s = left.get(i);
            s.translate(0, -shiftAmmount);
        }
        for(int i=0; i<right.size(); i++){
            Polygon s = right.get(i);
            s.translate(0, -shiftAmmount);
        }
        cleanArrays();
        generate();
    }
    
    public boolean rightIntersects(Rectangle r){
        for(int i=0; i<right.size(); i++){
            Polygon p = right.get(i);
            if(p.intersects(r))
                return true;
        }
        return false;
    }
    public boolean leftIntersects(Rectangle r){
        for(int i=0; i<left.size(); i++){
            Polygon p = left.get(i);
            if(p.intersects(r))
                return true;
        }
        return false;
    }
    
    public Polygon getIntersectingCliff(Rectangle r){
        if(left.isEmpty())
            return new Polygon();
        for(int i=0; i<left.size(); i++){
            if(left.get(i).intersects(r))
                return left.get(i);
        }
        return null;
    }
    
    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        for(int i=0; i<left.size(); i++){
            g2d.setColor(Color.black);
            g2d.fill(left.get(i));
        }
        for(int i=0; i<right.size(); i++){
            g2d.setColor(Color.black);
            g2d.fill(right.get(i));
        }
//        g2d.setColor(Color.RED);
//        g2d.drawRect(0,0,Vars.leftInterval[1],screen.height);
//        g2d.drawRect(Vars.rightInterval[0],0,screen.width-Vars.rightInterval[0],screen.height);
    }
    
    public void reset(){
        left.clear();
        right.clear();
        setup();
    }
}
