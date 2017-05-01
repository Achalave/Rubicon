
package rubicon.menus;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import rubicon.Cliff;
import rubicon.Data;
import rubicon.GameView;
import rubicon.Vars;

public class Menu extends JFrame implements GameHolster, DataProvider{
    
    public static void main(String[] args){
        setupFiles();
        new Menu().setVisible(true);
        
    }
    
    JPanel cards;
    GameView game = new GameView(this);
    Data data;
    Cliff cliff;
    boolean run = true;
    String gameName;
    LoadGame gameChooser;
    NewGame gameNamer;
    DataView dataView = new DataView();
    PlayerSelect playerSelect;
    UpgradePanel[] upgrades = {new UpgradePanel("Move Speed"), new UpgradePanel("Handling"), new UpgradePanel("Power Cooldown")};
    Clip audioClip = null;
    
    public Menu(){
        setSize(Vars.screenWidth,Vars.screenHeight);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CardLayout cl = new CardLayout();
        cards = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                if(run)
                    cliff.draw(g);
            }
        };
        cards.setBackground(new Color(139,69,19));
        cards.setOpaque(true);
        cards.setDoubleBuffered(true);
        cards.setBounds(0,0,getWidth(),getHeight());
        cards.setLayout(cl);
        setupPanels();
        //setupAudio();
        add(cards);
        
        cliff = new Cliff(getBounds());
        start();
        
        setExtendedState(JFrame.MAXIMIZED_VERT);
    }
    
    private static void setupFiles(){
        File f = new File("Game");
        if(!f.exists()){
            f.mkdir();
        }
        f = new File("Game/playerdata");
        if(!f.exists())
            f.mkdir();
        f = new File("Game/leaderboards");
        if(!f.exists())
            f.mkdir();
    }
    
    private void setupAudio(){
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
    
    private void start(){
        run = true;
        new Thread(){
            @Override
            public void run(){
                long time = System.currentTimeMillis();
                long wait = 10;
                while(run){
                    try {
                        Thread.sleep(wait);
                        tic(System.currentTimeMillis()-time);
                        repaint();
                        time = System.currentTimeMillis();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GameView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
    }
    
    public void stopCliff(){
        run = false;
        cliff.reset();
        repaint();
    }
    
    private void tic(long milis){
        double speed = 100;
        double time = milis/1000.0;
        cliff.shift(speed*time);
    }
    
    private void centerComponents(ArrayList<Component>components){
        int vGap = 20;
        int bHeight = 30;
        int bWidth = 200;
        
        int height = 0;
        for(Component c:components){
            int h;
            if(c instanceof JButton){
                h = bHeight;
            }
            else
                h = c.getPreferredSize().height;
            height += h+vGap;
        }
        int y = (getHeight()/2)-(height/2);
        for(Component c:components){
            int h,w;
            if(c instanceof JButton){
                h = bHeight;
                w = bWidth;
            }
            else{
                h = c.getPreferredSize().height;
                w = c.getPreferredSize().width;
            }
            int x = (getWidth()/2)-(w/2);
            c.setBounds(x, y,w,h);
            y+=h+vGap;
            
        }
    }
    
    private JButton getNewButton(String s, ActionListener a){
        JButton b = new JButton(s);
        b.addActionListener(a);
        return b;
    }
    
    private void setView(String p){
        ((CardLayout)cards.getLayout()).show(cards, p);
    }
    

    @Override
    public void gameOver() {
        dataView.update(data);
        start();
        setView("ingame");
        if(audioClip != null){
            audioClip.setFramePosition(0);
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    
    public void stopMusic(){
        if(audioClip != null){
            audioClip.stop();
        }
    }
    
    @Override
    public void paintComponents(Graphics g){
        super.paintComponents(g);
        BufferedImage db = new BufferedImage(getWidth(),getHeight(),BufferedImage.SCALE_SMOOTH);
        Graphics2D gd = (Graphics2D)db.getGraphics();
        gd.setColor(new Color( (float) 0.68169f, (float) 0.68169f, (float) 0.68169f, Color.TRANSLUCENT));
        gd.drawRect(0, 0, getWidth(), getHeight());
        gd.setColor(new Color( (float) 0.68169f, (float) 0.68169f, (float) 0.68169f, Color.OPAQUE));
        
        if(run)
            cliff.draw(gd);
        
        ((Graphics2D)g).drawImage(db, 0, 0, this);
    }
    
    
    private void setupPanels(){
        //Create the main screen
        //----------------------
        JPanel temp = new JPanel();
        temp.setLayout(null);
        temp.setOpaque(false);
        ArrayList<Component> buttons = new ArrayList<Component>();
        buttons.add(getNewButton("New Game",
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae) {
                    setView("newgame");
                    gameNamer.refreshData();
                }
            }));
        buttons.add(getNewButton("Load Game",
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae) {
                    gameChooser.refreshData();
                    setView("loadgame");
                }
            })
        );
        buttons.add(getNewButton("Battle Mode",
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae) {
                    stopCliff();
                    setView("game");
                    game.start();
                }
            })
        );
        buttons.add(getNewButton("Exit Game",
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae) {
                    System.exit(0);
                }
            })
        );
        for(Component b:buttons){
            temp.add(b);
        }
        centerComponents(buttons);
        //Add the logo
        ImageIcon image = new ImageIcon(Vars.getImage("/images/Rubicon.png"));
        JLabel label = new JLabel("", image, JLabel.CENTER);
        Dimension d = label.getPreferredSize();
        temp.add(label);
        label.setBounds((int)(getWidth()/2-d.getWidth()/2), 50, (int)d.getWidth(), (int)d.getHeight());
        cards.add(temp, "main");
        
        //Create an ingame screen
        //-----------------------
        buttons.clear();
        temp = new JPanel();
        temp.setLayout(null);
        temp.setOpaque(false);
        dataView.setBounds((int)(getWidth()/2-dataView.getPreferredSize().getWidth()/2), 120, (int)dataView.getPreferredSize().getWidth(), (int)dataView.getPreferredSize().getHeight());
        temp.add(dataView);
        buttons.add(getNewButton("Play Game",
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae) {
                    stopCliff();
                    stopMusic();
                    game.setData(data);
                    setView("game");
                    game.applyUpgrades();
                    game.start();
                }
            })
        );
        buttons.add(getNewButton("Upgrade",
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae) {
                    //Update all upgrades
                    for(UpgradePanel p: upgrades)
                        p.update(data);
                    playerSelect.setData(data);
                    setView("upgrade");
                }
            })
        );
        buttons.add(getNewButton("Save and Exit",
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae) {
                    Data.saveData("Game/playerdata/"+gameName,data);
                    setView("main");
                }
            })
        );       
        for(Component b:buttons){
            temp.add(b);
        }
        centerComponents(buttons);
        cards.add(temp, "ingame");
        
        
        //Create an upgrade screen
        //------------------------
        final JLabel moneyLabel = new JLabel("Testing");
        moneyLabel.setFont(new java.awt.Font("Silom", 0, 50));
        temp = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                moneyLabel.setText("Money: "+data.getMoney());
                Dimension d = moneyLabel.getPreferredSize();
                moneyLabel.setBounds((int)(getWidth()/2-d.getWidth()/2), moneyLabel.getY(), (int)d.getWidth(), (int)d.getHeight());
                super.paintComponent(g);
            }
        };
        temp.setLayout(null);
        temp.setOpaque(false);
        buttons.clear();
        
        buttons.add(moneyLabel);
        for(UpgradePanel p:upgrades){
            buttons.add(p);
            p.addListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent ae) {
                    for(UpgradePanel p: upgrades){
                        p.update();
                    }
                }
                
            });
            
        }
        playerSelect = new PlayerSelect();
        buttons.add(playerSelect);
        buttons.add(getNewButton("Back",
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae) {
                    dataView.update(data);
                    setView("ingame");
                }
            })
        );
        for(Component b:buttons){
            temp.add(b);
        }
        
        centerComponents(buttons);
        cards.add(temp, "upgrade");
        
        //Create a load game screen
        //-------------------------
        temp = new JPanel();
        temp.setLayout(null);
        temp.setOpaque(false);
        buttons.clear();
        gameChooser = new LoadGame(new ActionListener(){
            //Called if select is chosen
            @Override
            public void actionPerformed(ActionEvent ae) {
                gameName = gameChooser.getGameName();
                data = gameChooser.getSelectedData();
                dataView.update(data);
                setView("ingame");
            }
            
        }, new ActionListener(){
            //Called if cancel is chosen
            @Override
            public void actionPerformed(ActionEvent ae) {
                setView("main");
            }
        });
        buttons.add(gameChooser);
        for(Component b:buttons){
            temp.add(b);
        }
        centerComponents(buttons);
        cards.add(temp, "loadgame");
        
        //Create a new game screen
        //-------------------------
        temp = new JPanel();
        temp.setLayout(null);
        temp.setOpaque(false);
        buttons.clear();
        gameNamer = new NewGame(new ActionListener(){
            //Called if accept is chosen
            @Override
            public void actionPerformed(ActionEvent ae) {
                gameName = gameNamer.getGameName();
                data = new Data();
                dataView.update(data);
                setView("ingame");
            }
            
        }, new ActionListener(){
            //Called if cancel is chosen
            @Override
            public void actionPerformed(ActionEvent ae) {
                setView("main");
            }
            
        });
        buttons.add(gameNamer);
        for(Component b:buttons){
            temp.add(b);
        }
        centerComponents(buttons);
        cards.add(temp, "newgame");
        
        cards.add(game, "game");
    }

    @Override
    public Data getData() {
        return data;
    }
    
}
