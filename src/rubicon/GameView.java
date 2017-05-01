
package rubicon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JPanel;
import rubicon.entities.Obsticle;
import rubicon.menus.GameHolster;
import rubicon.money.Money;
import rubicon.players.Jock;
import rubicon.players.Player;

public class GameView extends JPanel implements KeyListener{

//    public static void main(String[] args) {
//        GameView game = new GameView();
//        frame = new JFrame();
//        frame.setSize(Vars.screenWidth, Vars.screenHeight);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.addKeyListener(game);
//        frame.add(game);
//        frame.setVisible(true);
//        game.start();
//    }
    
    //Vars
    static JFrame frame;
    boolean run = true;
    //Game Mechanic Vars
    static Player player = Vars.players[0];
    public static double scrollSpeed = Vars.startScrollSpeed;
    public static double scrollAcceleration = Vars.startAcceleration;
    public static double spawnPointIncrease = Vars.startSpawnPointIncrease;
    private double nextSpawnDistance = 0;
    private double spawnPointMax = 3;
    private double distance = 0;
    int money = 0;
    double playerMaxSpeed = Vars.maxSpeed;
    double playerAcceleration = Vars.xAcceleration;
    Thread entityThread;
    //Other Vars and Lists
    Cliff cliff;
    private boolean[] keys = {false,false};
    public static List<Obsticle> entities = Collections.synchronizedList(new ArrayList<Obsticle>());
    public static List<Money> coins = Collections.synchronizedList(new ArrayList<Money>());   
    Data data;
    GameHolster holster;
    //Debug
    long elapsedTime;
    int jump;
    public static boolean tellHit = false;
    
    //Double Buffer
    Image db;
    
    //For spawning
    private List<Obsticle> spawners = Collections.synchronizedList(new ArrayList<Obsticle>());
    private List<Money> tempCoins = Collections.synchronizedList(new ArrayList<Money>());
    private List<int[]> tempPoints = Collections.synchronizedList(new ArrayList<int[]>());
    
    
    public GameView(GameHolster h){
        this.setBackground(new Color(139,69,19));
        holster = h;
        this.addKeyListener(this);
    }
    
    public void start(){
        db = new BufferedImage((int)getBounds().width,(int)getBounds().height,BufferedImage.SCALE_FAST);
        if(cliff == null)
            cliff = new Cliff(this.getBounds());
        run = true;
        this.requestFocus();
        //Setup the main thread
        new Thread(){
            @Override
            public void run(){
                long time = System.currentTimeMillis();
                long wait = 10;
                while(run){
                    try {
                        Thread.sleep(wait);
                        elapsedTime = System.currentTimeMillis()-time;
                        tic(System.currentTimeMillis()-time);
                        repaint();
                        time = System.currentTimeMillis();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GameView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
        //Setup music thread
        //playAudioClip();
    }
    
    public void stop(){
        run = false;
    }
    
    public void restart(){
        entities.clear();
        coins.clear();
        cliff.reset();
        scrollSpeed = Vars.startScrollSpeed;
        scrollAcceleration = Vars.startAcceleration;
        spawnPointIncrease = Vars.startSpawnPointIncrease;
        nextSpawnDistance = 0;
        spawnPointMax = 3;
        distance = 0;
        money = 0;
        player.setX(getWidth()/2 - player.getWidth()/2);
        player.setxSpeed(0);
        keys[0] = false;keys[1] = false;
        playerMaxSpeed = Vars.maxSpeed;
        playerAcceleration = Vars.xAcceleration;
        cliff = new Cliff(this.getBounds());
    }
    public void applyUpgrades(){
        player = Vars.players[data.getPlayerIndex()];
        player.startRun();
        playerAcceleration += data.getUpgrade("Handling").getIncreaseAmount();
        playerMaxSpeed += data.getUpgrade("Move Speed").getIncreaseAmount();
        player.applyUpgrades(data);
    }
    public void saveGameToData(){
        data.setMoney(money+data.getMoney());
        if(data.getHighScoreDist() < distance)
            data.setHighScoreDist(distance);
        if(data.getHighScoreMoney() < money)
            data.setHighScoreMoney(money);
        data.setLastRunDistance(distance);
        data.setLastRunMoney(money);
    }
    
    
    Clip audioClip = null;
    public void playAudioClip(){
        AudioInputStream inputStream = null;
        try {
            String musicName = "/music/TitleMusic.wav";
            inputStream = AudioSystem.getAudioInputStream(getClass().getResource(musicName));
            audioClip = AudioSystem.getClip();
            audioClip.open(inputStream);
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception ex) {
            Logger.getLogger(GameView.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(GameView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics gdb = db.getGraphics();
        gdb.setColor(Vars.backgroundColor);
        gdb.fillRect(0, 0, getWidth(), getHeight());
        if(player != null)
            player.draw(gdb);
        if(cliff != null)
            cliff.draw(gdb);
        for(int i=0; i<entities.size();i++){
            entities.get(i).draw(gdb);
        }
        for(int i=0; i<coins.size(); i++){
            coins.get(i).draw(gdb);
        }
        gdb.setColor(Color.WHITE);
        gdb.setFont(new java.awt.Font("Silom", 0, 40));
        Rectangle2D r = gdb.getFontMetrics().getStringBounds("Distance: "+(int)distance, gdb);
        int y = (int)(30+r.getHeight());
        gdb.drawString("Distance: "+(int)distance, (int)(getWidth()/2 - r.getWidth()/2), y);
        r = gdb.getFontMetrics().getStringBounds("Money: "+money, gdb);
        y += (int)(r.getHeight());
        gdb.drawString("Money: "+money, (int)(getWidth()/2 - r.getWidth()/2), y);
        
        gdb.setFont(new java.awt.Font("Silom", 0, 12));
        gdb.drawString("Velocity: "+(int)(scrollSpeed/Vars.moleMeterConversion), 0, 15);
        gdb.drawString("Spawn Point Max: "+spawnPointMax, 0, 30);
        gdb.drawString("FPS: "+(int)(1/(elapsedTime/1000.0)), 0, 45);
        gdb.drawString("Shift Per Frame: "+jump, 0, 60);
        gdb.drawString("SPS: "+jump/(elapsedTime/1000.0), 0, 75);
        g.drawImage(db, 0, 0, this);
        gdb.dispose();
    }
    
    public void tic(long milis){
        double time = milis/1000.0;
        player.tic(milis);
        spawnPointMax += spawnPointIncrease*time;
        scrollSpeed += scrollAcceleration*(time);
        int addDist = (int)Math.round(scrollSpeed*time);//in Pixels
        distance += addDist/Vars.moleMeterConversion;//In mole meters
        cliff.shift((scrollSpeed*time));
        jump = addDist;
        //Check for collision with an entity
        //Call entity tics, shift and handle removal
        for(int i=0; i<entities.size();i++){
            Obsticle e = entities.get(i);
            if(e.getBounds().getBounds().getY()+e.getBounds().getBounds().getHeight()<0){
                entities.remove(i);
            }
            e.tic(milis);
            e.shift(addDist);
            if(e.getBounds().getBounds().getY() <= player.getY()+player.getBounds().getHeight()){
                if(e.getBounds().intersects(player.getBounds())){
                    if(!(player instanceof Jock) && !tellHit){
                        stop();
                        player.gameOver();
                        saveGameToData();
                        restart();
                        holster.gameOver();
                        return;
                    }else{
                        ((Jock)player).hitObsticle();
                    }
                }
            }
        }
        
        //Check for collision with a Money
        //Call Money tics, shift and handle removal
        for(int i=0; i<coins.size();i++){
            Money c = coins.get(i);
            if(c.getBounds().getBounds().getY()+c.getBounds().getBounds().getHeight()<0){
                coins.remove(i);
            }
            c.tic(milis);
            c.shift(addDist);
            if(c.getBounds().getBounds().getY() <= player.getY()+player.getBounds().getHeight()){
                if(c.getBounds().intersects(player.getBounds())){
                    money += c.getValue();
                    coins.remove(i);
                }
            }
        }
        
        //Call shift for all the temporary entities made so far
        for(int i=0; i<spawners.size(); i++){
            Obsticle e = spawners.get(i);
            e.shift(addDist);
        }
        //Call shift for all the temporary coins made so far
        for(int i=0; i<tempCoins.size(); i++){
            Money e = tempCoins.get(i);
            e.shift(addDist);
        }
        
        //Add new entities in a new thread
//        while(entityThread != null && entityThread.isAlive()){
//            System.out.println("DOUBLE THREAD! System Time: "+System.currentTimeMillis());
//            try {
//                Thread.sleep(2);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(GameView.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        entityThread = new Thread(){
//            @Override
//            public void run(){
//                addEntities();
//            }
//        };
//        entityThread.start();
        
        //Add new entities on the same thread
        addEntities();
       
        //Movement handeling and collisions with the cliff
        //LEFT
        if(!cliff.leftIntersects(player.getBounds())){
            if(keys[0]){
                player.setxSpeed(player.getxSpeed()-(playerAcceleration*time));
                //Max Speed
                if(player.getxSpeed() < -playerMaxSpeed)
                    player.setxSpeed(-playerMaxSpeed);

                player.setX(player.getX() + (player.getxSpeed()*time));
            }else if(player.getxSpeed() < 0){
                player.setxSpeed(player.getxSpeed()+(playerAcceleration*time));
                if(player.getxSpeed() > 0)
                    player.setxSpeed(0);
                player.setX(player.getX()+(player.getxSpeed()*time));
            }
        }
        
        //RIGHT
        if(!cliff.rightIntersects(player.getBounds())){
            if(keys[1]){
                player.setxSpeed(player.getxSpeed()+(playerAcceleration*time));
                //Max Speed
                if(player.getxSpeed() > playerMaxSpeed)
                    player.setxSpeed(playerMaxSpeed);
                player.setX(player.getX()+(player.getxSpeed()*time));
            }else if(player.getxSpeed() > 0){
                player.setxSpeed(player.getxSpeed()-(playerAcceleration*time));
                if(player.getxSpeed() < 0)
                        player.setxSpeed(0);
                player.setX(player.getX()+(player.getxSpeed()*time));
            }
        }
        //Cliff Collision detection
        if(player.getX() < Vars.leftInterval[1] || player.getX()+player.getBounds().getWidth() > Vars.rightInterval[0]){
            while(cliff.leftIntersects(player.getBounds())){
                if(player.getxSpeed() < 0)
                    player.setxSpeed(0);
                player.shiftRight(1);
            }
            while(cliff.rightIntersects(player.getBounds())){
                if(player.getxSpeed() > 0)
                    player.setxSpeed(0);
                player.shiftLeft(1);
            }
        }
    }
    
    public void setData(Data d){
        data = d;
    }
    
    @Override
    public void keyTyped(KeyEvent ke) {
//        if(run == false){
//            System.out.println("Exiting game");
//            restart();
//            holster.gameOver();
//        }
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        
        if(ke.getKeyCode() == KeyEvent.VK_LEFT){
            keys[0] = true;
        }else if(ke.getKeyCode() == KeyEvent.VK_RIGHT){
            keys[1] = true;
        }else if(ke.getKeyCode() == KeyEvent.VK_SPACE){
            player.activate();
        }else if(ke.getKeyCode() == KeyEvent.VK_P){
            stop();
        }else if(ke.getKeyCode() == KeyEvent.VK_M){
            data.setMoney(999999);
        }else if(ke.getKeyCode() == KeyEvent.VK_S){
            audioClip.stop();
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        if(ke.getKeyCode() == KeyEvent.VK_LEFT){
            keys[0] = false;
        }else if(ke.getKeyCode() == KeyEvent.VK_RIGHT){
            keys[1] = false;
        }else if(ke.getKeyCode() == KeyEvent.VK_P){
            start();
        }
    }
    
    private void addEntities() {
        //Check if should actually spawn
        if(nextSpawnDistance <= distance){
            //How far inbetween the entities (initial)
            int startY = (int)this.getBounds().getHeight()+Vars.spawnBelowScreen;
            double beforeDist = distance*Vars.moleMeterConversion;
            int tempAddY = (int)(Math.random()*(Vars.spawnInterval[0]*Vars.moleMeterConversion));
            int[] between = {cliff.getIntersectingCliff(new Rectangle(0,startY+tempAddY-3,100,6)).getBounds().width,Vars.leftInterval[1]+5};
            //Temp entites to spawn
            int spawnPoints = 0;
            nextSpawnDistance = distance + (Math.random()*
                    (Vars.spawnInterval[1]-Vars.spawnInterval[0]))+Vars.spawnInterval[0];
            
            int x = (int)(Math.random()*(between[1]-between[0]))+between[1];
            between[0] = (int)player.getBounds().getWidth();
            between[1] = (int)player.getBounds().getWidth()+50;
            //Select Obsticles for spawning
            Obsticle entity = null;
            do{
                if(entity != null){
                    spawners.add(entity);
                    spawnPoints += entity.getSpawnPoints();
                    x += (int)(Math.random()*(between[1]-between[0]))+between[1];
                }
                int addY;
                if(tempAddY < 0){
                    addY = (int)(Math.random()*(Vars.spawnInterval[0]*Vars.moleMeterConversion));
                }else{
                    addY = tempAddY;
                    tempAddY = -1;
                }
                    //Add the template entity and set the position
                entity = Vars.getRandomEntity(distance);
                entity.setPosition(x, startY+addY-(int)((distance*Vars.moleMeterConversion)-beforeDist));
                //Add the position to the spawn points in case the obsticle gets instantiated
                int[] tempPoint = {x, startY+addY-(int)((distance*Vars.moleMeterConversion)-beforeDist)};
                tempPoints.add(tempPoint);
                beforeDist = distance*Vars.moleMeterConversion;
            }while(x+entity.getBounds().getBounds().getWidth() < getWidth()-(getWidth()-Vars.rightInterval[0]) || 
                    (x+entity.getBounds().getBounds().getWidth() < getWidth()-(getWidth()-Vars.rightInterval[1]) &&
                    !cliff.rightIntersects(entity.getBounds().getBounds())));
            
            //Get down to the spawn point max by removing randomly from the possible spawners
            while(spawnPointMax < spawnPoints){
                int r = (int)(Math.random()*spawners.size());
                Obsticle e = spawners.remove(r);
                tempPoints.remove(r);
                spawnPoints -= e.getSpawnPoints();
            }
            
            //Actually Instantiate the Obsticles selected to be spawned 
            //and add them to the spawned Obsticles
            for(int i=0; i<spawners.size(); i++){
                Obsticle o = spawners.remove(0).getNew(tempPoints.get(i)[0], tempPoints.get(i)[1]);
                spawners.add(o);
                entities.add(o);
            }
            
            //Spawn coins
            if((Math.random()*100)<=Vars.coinSpawnRate){
                int maxY = startY+(int)(Math.random()*(Vars.spawnInterval[0]*Vars.moleMeterConversion));
                int coinX = (int)(Math.random()*(Vars.rightInterval[0]-Vars.leftInterval[1]-Vars.maxCoinSize))+Vars.leftInterval[1];
                boolean done = false;
                while(!done && startY < maxY){
                    Money c = Vars.getRandomCoin(coinX, startY, distance);
                    for(Obsticle e:spawners){
                        if(e.getBounds().getBounds().intersects(c.getBounds().getBounds()))
                            done = true;
                    }
                    if(!done){
                        tempCoins.add(c);
                        startY += c.getBounds().getBounds().getHeight()+Vars.pixelsBetweenCoins;
                    }
                }
                coins.addAll(tempCoins);
            }
                
            //Clear the Lists
            spawners.clear();
            tempCoins.clear();
            tempPoints.clear();
        }
    }
    
}
