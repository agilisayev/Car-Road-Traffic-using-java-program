/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package car.road.traffic;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.Timer;

/**
 *
 * @author AgilI
 * 
 */
public class TrafficLight implements ActionListener {

    private Image layoutImgage;
    private Timer t_Timer;
    private Vertical position;
    private AffineTransform transform;
    private ImageObserver imObserver;
    private int id;
    private int timer = 0;
    private Color[] curretLightColor;
    private Vertical left_light_posison;
    private Vertical right_light_posison;
    private Vertical forward_posison;
    private int trafficTime = 3;
    private int orientation;

    public Image getLayoutImg() {
        return layoutImgage;
    }

    public void setLayoutImg(Image layoutImg) {
        this.layoutImgage = layoutImg;
    }

    public Timer getT_Timer() {
        return t_Timer;
    }

    public void setT_Timer(Timer t_Timer) {
        this.t_Timer = t_Timer;
    }

    public Vertical getPosition() {
        return position;
    }

    public void setPosition(Vertical position) {
        this.position = position;
    }

    public AffineTransform getTransform() {
        return transform;
    }

    public void setTransform(AffineTransform transform) {
        this.transform = transform;
    }

    public ImageObserver getImObserver() {
        return imObserver;
    }

    public void setImObserver(ImageObserver imObserver) {
        this.imObserver = imObserver;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public Color[] getCurretLightColor() {
        return curretLightColor;
    }

    public void setCurretLightColor(Color[] curretLightColor) {
        this.curretLightColor = curretLightColor;
    }

    public Vertical getLeft_light_posison() {
        return left_light_posison;
    }

    public void setLeft_light_posison(Vertical left_light_posison) {
        this.left_light_posison = left_light_posison;
    }

    public Vertical getRight_light_posison() {
        return right_light_posison;
    }

    public void setRight_light_posison(Vertical right_light_posison) {
        this.right_light_posison = right_light_posison;
    }

    public Vertical getForward_posison() {
        return forward_posison;
    }

    public void setForward_posison(Vertical forward_posison) {
        this.forward_posison = forward_posison;
    }

    public int getTrafficTime() {
        return trafficTime;
    }

    public void setTrafficTime(int trafficTime) {
        this.trafficTime = trafficTime;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public enum TrafficState {
        RED, YELLOW, GREEN
    };
    public boolean leftGo, forwardGo, rightGo;

    public TrafficLight(File imgSrc, int x, int y, int angle, int orient, int id, ImageObserver imageObserver) {
        this.imObserver = imageObserver;
        this.transform = new AffineTransform();
        this.orientation = orient;
        this.leftGo = false;
        this.rightGo = false;
        this.forwardGo = false;
        this.t_Timer = new Timer(1, this);
        this.id = id;
        this.curretLightColor = new Color[3];
        this.curretLightColor[0] = Color.red;
        this.curretLightColor[1] = Color.red;
        this.curretLightColor[2] = Color.red;
        try {
            this.layoutImgage = ImageIO.read(imgSrc);
            this.t_Timer = new Timer(1, this);
            this.position = new Vertical(x, y);
            this.transform.setToTranslation(position.x, position.y);
            this.transform.rotate(Math.toRadians(angle), layoutImgage.getWidth(imObserver) / 2, layoutImgage.getWidth(imObserver) / 2);
        } catch (Exception e) {
            System.out.println("An error occurred! --> " + e.getMessage());
            e.printStackTrace();
        }
        this.t_Timer.start();  // start Traffic Lights--->

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer++;
        if (id == 1) {
            if (timer > 500 && timer < (trafficTime * 1000 - 700)) {
                leftGo = true;
                forwardGo = true;
                rightGo = false;
            }
            if (timer < trafficTime * 1000 && timer > (trafficTime * 1000) - 499) {
                leftGo = false;
                forwardGo = false;
                curretLightColor[0] = Color.yellow;
                curretLightColor[1] = Color.yellow;
            }
            if (timer > trafficTime * 1000) {
                curretLightColor[0] = Color.red;
                curretLightColor[1] = Color.red;
            }
            if (timer > (trafficTime + 4) * 1000) {
                timer = 0;
            }
        }
        if (id == 2) {

            if (timer > trafficTime * 1000 && timer < ((trafficTime + 4) * 1000) - 500) {
                forwardGo = true;
                rightGo = true;
                leftGo = false;
            }

            if (timer < (trafficTime + 4) * 1000 && timer > ((trafficTime + 4) * 1000) - 499) {
                forwardGo = false;
                rightGo = false;
                curretLightColor[2] = Color.yellow;
                curretLightColor[1] = Color.yellow;
            }

            if (timer > (trafficTime + 4) * 1000) {
                curretLightColor[2] = Color.red;
                curretLightColor[1] = Color.red;
            }
            if (timer > (trafficTime + 4) * 1000) {
                timer = 0;
            }
        }
        if (id == 3) {
            if (timer > 150 && timer < 900) {
                curretLightColor[2] = Color.yellow;
                curretLightColor[1] = Color.yellow;
            }
            if (timer > 550 && timer < (trafficTime * 1000) - 700) {
                leftGo = false;
                forwardGo = true;
                rightGo = true;
            }
            if (timer < trafficTime * 1000 && timer > (trafficTime * 1000) - 499) {
                rightGo = false;
                forwardGo = false;
                curretLightColor[2] = Color.yellow;
                curretLightColor[1] = Color.yellow;
            }
            if (timer > trafficTime * 1000) {
                rightGo = false;
                forwardGo = false;
                curretLightColor[2] = Color.red;
                curretLightColor[1] = Color.red;
            }
            if (timer > (trafficTime + 4) * 1000) {
                timer = 0;
            }
        }

        if (id == 4) {

            if (timer > trafficTime * 1000 && timer < ((trafficTime + 4) * 1000) - 500) {
                forwardGo = true;
                rightGo = false;
                leftGo = true;
            }
            if (timer < (trafficTime + 4) * 1000 && timer > ((trafficTime + 4) * 1000) - 499) {
                forwardGo = false;
                leftGo = false;

                curretLightColor[0] = Color.YELLOW;
                curretLightColor[1] = Color.YELLOW;
            }
            if (timer > (trafficTime + 4) * 1000) {
                curretLightColor[0] = Color.RED;
                curretLightColor[1] = Color.RED;
                timer = 0;
            }
        }
        /*
        color index, 
        0-left go, 
        1-forward go,
        2-right go
         */
        if (leftGo) {
            curretLightColor[0] = Color.GREEN;
        }

        if (forwardGo) {
            curretLightColor[1] = Color.GREEN;
        }

        if (rightGo) {
            curretLightColor[2] = Color.GREEN;
        }

    }
}
