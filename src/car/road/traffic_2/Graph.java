/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package car.road.traffic_2;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Event;
import java.awt.Panel;

/**
 *
 * @author AgilI
 */
public class Graph extends java.applet.Applet {

    GraphPanel panel;
    int carnum;
    Thread LightThrd[] = new Thread[3];

    @Override
    public void init() {
        setLayout(new BorderLayout());
        panel = new GraphPanel(this);
        carnum = Integer.parseInt(getParameter("carnum"));
        carnum = Math.min(carnum, 70);
        for (int k = 0; k < carnum; k++) {
            panel.findNode(Integer.toString(k));
        }
        panel.lghtOrStp = 1;
        for (int k = 0; k < 3; k++) {
            LightThrd[k] = new Thread(panel.light[k]);
            panel.light[k].redpauss = (k + 1) * 1000 + 3000;
            panel.light[k].greenpauss = panel.light[k].redpauss;
            LightThrd[k].start();
        }
        panel.carpermin[0].time0 = System.currentTimeMillis();
        panel.carpermin[0].carnum = 0;

        Panel btpnl = new Panel();
        add("South", btpnl);

        btpnl.add(new Button("Start"));
        btpnl.add(new Button("End"));

        btpnl.add(new Button("Stop Sign"));
        btpnl.add(new Button("Traffic Light"));
        btpnl.add(new Button("New Schedule for Lights"));
    }

    public boolean actionPerformed(Event evt, Object arg) {
        if (((Button) evt.target).getLabel().equals("Traffic Light")) {
            if (panel.lghtOrStp == 0) {
                panel.lghtOrStp = 1;
                for (int k = 0; k < 3; k++) {
                    LightThrd[k] = new Thread(panel.light[k]);
                    panel.light[k].redpauss = (k + 1) * 1000 + 3000;
                    panel.light[k].greenpauss = panel.light[k].redpauss;
                    LightThrd[k].start();
                    panel.carpermin[0].time0 = System.currentTimeMillis();
                    panel.carpermin[0].carnum = 0;
                }
            }
        } else if (((Button) evt.target).getLabel().equals("Stop Sign")) {
            panel.lghtOrStp = 0;
            for (int k = 0; k < 3; k++) {
                if (LightThrd[k].isAlive()) {
                    LightThrd[k].stop();
                }
            }
            panel.carpermin[0].time0 = System.currentTimeMillis();
            panel.carpermin[0].carnum = 0;
        } else if (((Button) evt.target).getLabel().equals("End")) {
            for (int k = 0; k < 3; k++) {
                if (LightThrd[k].isAlive()) {
                    LightThrd[k].stop();
                }
            }
            panel.stop();
        } else if (((Button) evt.target).getLabel().equals("Start")) {
            if (panel.lghtOrStp == 1) {
                for (int k = 0; k < 3; k++) {
                    if (!LightThrd[k].isAlive()) {
                        LightThrd[k] = new Thread(panel.light[k]);
                        LightThrd[k].start();
                    }
                }
            }
            if (!panel.relaxer.isAlive()) {
                panel.start();
            }
        } else if (((Button) evt.target).getLabel().equals("New Schedule for Lights")) {
            if (panel.lghtOrStp == 1) {
                for (int k = 0; k < 3; k++) {
                    if (LightThrd[k].isAlive()) {
                        panel.light[k].redpauss = (int) (Math.random() * 6000) + 3000;
                        panel.light[k].greenpauss = (int) (Math.random() * 6000) + 3000;
                    }
                }
                panel.carpermin[0].time0 = System.currentTimeMillis();
                panel.carpermin[0].carnum = 0;
                panel.carpermin[0].carwt = 0;

            }
        }
        return true;
    }

    @Override
    public void start() {
        panel.start();
    }

    @Override
    public void stop() {
        panel.stop();
    }
}
