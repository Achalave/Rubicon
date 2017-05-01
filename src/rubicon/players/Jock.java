
package rubicon.players;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import rubicon.GameView;
import rubicon.Vars;

public class Jock extends Player{

    Image helm;
    boolean hasHelm = true;
    boolean helmSpin = false;
    long time;
    double helmX,helmY;
    double helmRad = 0;
    AffineTransform af;
    
    public Jock(){
        super("/images/Players/jock/jock.png",13);
        helm = Vars.getImage("/images/Players/jock/helm.png");
        helmX = (int)(x+(width/2-helm.getWidth(null)/2));
        helmY = (int)y;
        af = new AffineTransform();
        af.translate(helmX, helmY);
    }
    
    @Override
    public void draw(Graphics g){
        super.draw(g);
        if(hasHelm){
            if(!helmSpin){
                double xTemp = (x+(width/2-helm.getWidth(null)/2));
                af.translate(xTemp-helmX, 0);
                helmX = xTemp;
            }
            ((Graphics2D)g).drawImage(helm, af, null);
        }
    }
    
    @Override
    public void startRun() {
        GameView.tellHit=true;
    }

    public void hitObsticle(){
        //GameView.tellHit = false;
        helmSpin = true;
    }
    
    @Override
    public void gameOver() {
        GameView.tellHit = false;
        helmSpin = false;
        hasHelm = true;
        time = 0;
        helmRad = 0;
        af = new AffineTransform();
    }

    @Override
    public String getDescription() {
        return "Passive: Has an extra life.";
    }

    @Override
    public int getCost() {
        return 3500;
    }

    @Override
    public void activate() {

    }
    
    @Override
    public void tic(long milis){
//        if(helmSpin){
//            helmRad += .001;
//            af.translate(-helmX-helm.getWidth(null)/2, -helmY-helm.getHeight(null)/2);System.out.println(af.getTranslateX()+" "+af.getTranslateY());
//            af.rotate(helmRad);
//            af.translate(helmX+helm.getWidth(null)/2, helmY+helm.getHeight(null)/2);
//        }
        if(helmSpin){
            helmRad += .003;
            af = new AffineTransform();
            af.translate(-helm.getWidth(null)/2, -helm.getHeight(null)/2);
            //af.rotate(helmRad);
            //af.translate(helmX, helmY);
        }
    }
    
}
