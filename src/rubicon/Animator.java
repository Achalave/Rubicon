package rubicon;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import rubicon.players.Player;

public class Animator {

    int x = 0, y = 0;
    boolean forward = true;
    boolean loop = false;
    int width = 0, height = 0;
    ArrayList<Image> images = new ArrayList<Image>();
    int index = 0;
    long time = 0;
    long timeBetween;

    public Animator(String path, double duration, int x, int y) {
        this.x = x;
        this.y = y;
        InputStream is = getClass().getResourceAsStream(path);
        Scanner scan = new Scanner(is);
        //String[] paths = new File(path).list();
        //Arrays.sort(paths);
        while (scan.hasNext()) {
            String p = scan.nextLine();
            p = path + "/" + p;
            if (p.indexOf(".") >= 0) {
                Image i = Vars.getImage(p);
                images.add(i);
                if (i.getHeight(null) > height) {
                    height = i.getHeight(null);
                }
                if (i.getWidth(null) > width) {
                    width = i.getWidth(null);
                }
            }
        }
        timeBetween = (long) ((duration / images.size()) * 1000);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setLoop(boolean b) {
        loop = b;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void reverse() {
        forward = false;
    }

    public void forward() {
        forward = true;
    }

    public void restart() {
        if (forward) {
            index = 0;
        } else {
            index = images.size() - 1;
        }
    }

    public void setDuration(double duration) {
        timeBetween = (long) ((duration / images.size()) * 1000);
    }

    public void tic(long milis) {
        if ((forward && index < images.size() - 1) || (!forward && index > 0)) {
            time += milis;
            if (time >= timeBetween) {
                time -= timeBetween;
                if (forward) {
                    index++;
                } else {
                    index--;
                }
            }
        } else if (loop) {
            restart();
        }
    }

    public void draw(Graphics g) {
        g.drawImage(images.get(index), x + (width - images.get(index).getWidth(null)) / 2, y, null);
    }

    public boolean isRunning() {
        return loop || ((forward && index < images.size() - 1) || (!forward && index > 0));
    }
}
