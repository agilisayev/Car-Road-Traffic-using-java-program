/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package car.road.traffic_1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author AgilI
 * 
 */

public class CarRoadTraffic extends JPanel implements ActionListener {

    private static Thread thread;
    private Image car;
    private Image terrain;
    private Timer timer = new Timer(1, this);
    private int x;
    private int vel = 3;
    private float angle;
    private Random random = new Random();
    private ArrayList<Vehicle> vehiclesRight;
    private ArrayList<Vehicle> vehiclesDown;
    private ArrayList<Vehicle> vehiclesLeft;
    private ArrayList<Vehicle> vehiclesUp;

    private String[] carImages
            = {
                "resources\\car1.png",
                "resources\\car2.png",
                "resources\\car3.png",
                "resources\\car4.png"
            };

    private ArrayList<TrafficLight> trafficLights;
    private int carSpawnTimer = 0;

    Vehicle v1, v2, v3, v4;
    float move = 0;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(terrain, 0, 0, this);
        g2D.drawImage(v1.getImage(), v1.getTransform(), this);
        for (int i = 0; i < vehiclesRight.size(); i++) {
            Vehicle v = vehiclesRight.get(i);
            if (v.isInView()) {
                g2D.drawImage(v.getImage(), v.getTransform(), this);
            } else {
                vehiclesRight.remove(v);
            }
        }
        for (int i = 0; i < vehiclesLeft.size(); i++) {
            Vehicle v = vehiclesLeft.get(i);
            if (v.isInView()) {
                g2D.drawImage(v.getImage(), v.getTransform(), this);
            } else {
                vehiclesLeft.remove(v);
            }
        }
        for (int i = 0; i < vehiclesDown.size(); i++) {
            Vehicle v = vehiclesDown.get(i);
            if (v.isInView()) {
                g2D.drawImage(v.getImage(), v.getTransform(), this);
            } else {
                vehiclesDown.remove(v);
            }
        }
        for (int i = 0; i < vehiclesUp.size(); i++) {
            Vehicle v = vehiclesUp.get(i);
            if (v.isInView()) {
                g2D.drawImage(v.getImage(), v.getTransform(), this);
            } else {
                vehiclesUp.remove(v);
            }
        }
        AffineTransform identity = g2D.getTransform();
        identity.setToTranslation(300, 200 + move);
        identity.rotate(Math.toRadians(angle), car.getWidth(this), car.getHeight(this));
        g2D.drawImage(car, identity, this);
        for (TrafficLight t : trafficLights) {
            Color colors[] = t.getCurretLightColor();
            if (t.getOrientation() == 0) {
                g2D.setColor(colors[1]);
                g2D.fillRect(t.getForward_posison().x, t.getForward_posison().y, 21, 29);
                g2D.setColor(colors[0]);
                g2D.fillRect(t.getLeft_light_posison().x, t.getLeft_light_posison().y, 20, 29);
                g2D.setColor(colors[2]);
                g2D.fillRect(t.getRight_light_posison().x, t.getRight_light_posison().y, 20, 29);
            } else {
                g2D.setColor(colors[1]);
                g2D.fillRect(t.getForward_posison().x, t.getForward_posison().y, 29, 22);
                g2D.setColor(colors[0]);
                g2D.fillRect(t.getLeft_light_posison().x, t.getLeft_light_posison().y, 29, 22);
                g2D.setColor(colors[2]);
                g2D.fillRect(t.getRight_light_posison().x, t.getRight_light_posison().y, 29, 22);
            }
            g2D.drawImage(t.getLayoutImg(), t.getTransform(), this);
        }
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        carSpawnTimer++;
        if (x < 0 || x > 550) {
            vel = -vel;
        }
        x = x + vel;
        if (angle < 450) {
            move += 0.2;
        } else {
            move += 2;
        }
        steerTowards(450, 120);
        if (carSpawnTimer % 500 == 0) {
            int carImageId = 0;
            int vAheadID = 1000;
            for (int i = 0; i < 20; i++) {
                if (vehiclesRight.size() < 30) {
                    int line = (vehiclesRight.size()) / 3;
                    if (line > 0) {
                        vAheadID = vehiclesRight.size() - 3;
                    }
                    carImageId = random.nextInt(carImages.length);
                    if (carImageId < 0) {
                        carImageId = 0;
                    }
                    if (vehiclesRight.size() > 0 && vAheadID < vehiclesRight.size()) {
                        vehiclesRight.add(new Vehicle(new File(carImages[carImageId]), 6, Vehicle.VehicleState.MOVE_X, Vehicle.VehicleDirection.RIGHT, trafficLights.get(1), this, vehiclesRight.get(vAheadID), vehiclesRight.size()));
                    } else {
                        vehiclesRight.add(new Vehicle(new File(carImages[carImageId]), 6, Vehicle.VehicleState.MOVE_X, Vehicle.VehicleDirection.RIGHT, trafficLights.get(1), this, null, vehiclesRight.size()));
                    }
                }

                if (vehiclesDown.size() < 20) {
                    int line = (vehiclesDown.size()) / 3;
                    int laneID = vehiclesDown.size() % 3;

                    if (line > 0) {
                        switch (laneID) {
                            case 0:
                                vAheadID = 3 * line - 3;
                                break;
                            case 1:
                                vAheadID = 3 * line - 2;
                                break;
                            case 2:
                                vAheadID = 3 * line - 1;
                                break;
                        }

                    }
                    carImageId = random.nextInt(carImages.length);
                    if (carImageId < 0) {
                        carImageId = 0;
                    }
                    int spd = 7 - random.nextInt(2);
                    if (vehiclesDown.size() > 0 && vAheadID < vehiclesDown.size()) {
                        vehiclesDown.add(new Vehicle(new File(carImages[carImageId]), spd, Vehicle.VehicleState.MOVE_Y, Vehicle.VehicleDirection.DOWN, trafficLights.get(0), this, vehiclesDown.get(vAheadID), vehiclesDown.size()));
                    } else {
                        vehiclesDown.add(new Vehicle(new File(carImages[carImageId]), spd, Vehicle.VehicleState.MOVE_Y, Vehicle.VehicleDirection.DOWN, trafficLights.get(0), this, null, vehiclesDown.size()));
                    }
                }

                if (vehiclesLeft.size() < 30) {
                    int line = (vehiclesLeft.size()) / 3;
                    int laneID = vehiclesLeft.size() % 3;

                    if (line > 0) {
                        switch (laneID) {
                            case 0:
                                vAheadID = 3 * line - 3;
                                break;
                            case 1:
                                vAheadID = 3 * line - 2;
                                break;
                            case 2:
                                vAheadID = 3 * line - 1;
                                break;
                        }

                    }
                    carImageId = random.nextInt(carImages.length);
                    if (carImageId < 0) {
                        carImageId = 0;
                    }
                    int spd = 7 - random.nextInt(2);
                    if (vehiclesLeft.size() > 0 && vAheadID < vehiclesLeft.size()) {
                        vehiclesLeft.add(new Vehicle(new File(carImages[carImageId]), spd, Vehicle.VehicleState.MOVE_X, Vehicle.VehicleDirection.LEFT, trafficLights.get(3), this, vehiclesLeft.get(vAheadID), vehiclesLeft.size()));
                    } else {
                        vehiclesLeft.add(new Vehicle(new File(carImages[carImageId]), spd, Vehicle.VehicleState.MOVE_X, Vehicle.VehicleDirection.LEFT, trafficLights.get(3), this, null, vehiclesLeft.size()));
                    }
                }
                if (vehiclesUp.size() < 30) {
                    int line = (vehiclesUp.size()) / 3;
                    if (line > 0) {
                        vAheadID = vehiclesUp.size() - 3;
                    }
                    carImageId = random.nextInt(carImages.length);
                    if (carImageId < 0) {
                        carImageId = 0;
                    }
                    int spd = 7 - random.nextInt(2);
                    if (vehiclesUp.size() > 0 && vAheadID < vehiclesUp.size()) {
                        vehiclesUp.add(new Vehicle(new File(carImages[carImageId]), spd, Vehicle.VehicleState.MOVE_Y, Vehicle.VehicleDirection.UP, trafficLights.get(2), this, vehiclesUp.get(vAheadID), vehiclesUp.size()));
                    } else {
                        vehiclesUp.add(new Vehicle(new File(carImages[carImageId]), spd, Vehicle.VehicleState.MOVE_Y, Vehicle.VehicleDirection.UP, trafficLights.get(2), this, null, vehiclesUp.size()));
                    }
                }
            }
            carSpawnTimer = 0;
        }
        repaint();
    }

    private void steerTowards(float angle, float t) {
        float angularVel = angle / t;
        if (Math.abs(angle) < Math.abs(angle)) {
            angle += angularVel;
        }
    }

    public CarRoadTraffic() {
        this.trafficLights = new ArrayList<TrafficLight>();

        TrafficLight t1 = new TrafficLight(new File("resources\\trafficLight.png"), 349, 147, 180, 0, 1, this);
        t1.setLeft_light_posison(new Vertical(349, 147));
        t1.setForward_posison(new Vertical(349 + 23, 147));
        t1.setRight_light_posison(new Vertical(349 + 47, 147));
        this.trafficLights.add(t1);

        TrafficLight t2 = new TrafficLight(new File("resources\\trafficLight.png"), 302, 530, 90, 1, 2, this);
        t2.setLeft_light_posison(new Vertical(322, 511));
        t2.setForward_posison(new Vertical(322, 533));
        t2.setRight_light_posison(new Vertical(322, 558));
        this.trafficLights.add(t2);

        TrafficLight t3 = new TrafficLight(new File("resources\\trafficLight.png"), 1077, 585, 0, 0, 3, this);
        t3.setLeft_light_posison(new Vertical(1079, 585));
        t3.setForward_posison(new Vertical(1077 + 25, 585));
        t3.setRight_light_posison(new Vertical(1077 + 48, 585));
        this.trafficLights.add(t3);

        TrafficLight t4 = new TrafficLight(new File("resources\\trafficLight.png"), 1125, 195, -90, 1, 4, this);
        t4.setLeft_light_posison(new Vertical(1145, 175));
        t4.setForward_posison(new Vertical(1145, 175 + 25));
        t4.setRight_light_posison(new Vertical(1145, 175 + 47));
        this.trafficLights.add(t4);

        this.v1 = new Vehicle(new File("resources\\car1.jpg"), 6, Vehicle.VehicleState.MOVE_X, Vehicle.VehicleDirection.RIGHT, trafficLights.get(1), this, null, 0);
        this.v2 = new Vehicle(new File("resources\\car1.jpg"), 5, Vehicle.VehicleState.MOVE_Y, Vehicle.VehicleDirection.DOWN, trafficLights.get(0), this, null, 0);
        this.v3 = new Vehicle(new File("resources\\car1.jpg"), 6, Vehicle.VehicleState.MOVE_X, Vehicle.VehicleDirection.LEFT, trafficLights.get(3), this, null, 0);
        this.v4 = new Vehicle(new File("resources\\car1.jpg"), 5, Vehicle.VehicleState.MOVE_Y, Vehicle.VehicleDirection.UP, trafficLights.get(2), this, null, 0);

        this.vehiclesRight = new ArrayList<Vehicle>();
        this.vehiclesLeft = new ArrayList<Vehicle>();
        this.vehiclesDown = new ArrayList<Vehicle>();
        this.vehiclesUp = new ArrayList<Vehicle>();
        this.vehiclesRight.add(v1);
        this.vehiclesDown.add(v2);
        this.vehiclesLeft.add(v3);
        this.vehiclesUp.add(v4);
        try {
            this.car = ImageIO.read(new File("resources\\car1.jpg"));
            this.terrain = ImageIO.read(new File("resources\\road1.jpg"));
        } catch (IOException e) {
            System.out.println("an Error occured! " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame jF = new JFrame("[Car Road Traffic program]");
        CarRoadTraffic traffic = new CarRoadTraffic();
        jF.setContentPane(traffic);
        jF.setSize(1370, 750);
        jF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jF.setVisible(true);
        //  thread = new Thread();
        //  thread.start();
    }

}
