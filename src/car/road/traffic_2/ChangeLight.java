/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package car.road.traffic_2;

/**
 *
 * @author AgilI
 */
public class ChangeLight implements Runnable {

    int signal;
    int pauss, redpauss, greenpauss;
    Thread lighter;

    ChangeLight() {
        signal = 1;
        redpauss = 6000;
        greenpauss = 6000;
    }

    @Override
    public void run() {
        signal = 1;
        while (true) {
            if (signal == 1) {
                signal = 0;
                pauss = greenpauss;
            } else {
                signal = 1;
                pauss = redpauss;
            }
            try {
                Thread.sleep(pauss);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void start() {
        lighter = new Thread(this);
        lighter.start();
    }

    public void stop() {
        lighter.stop();
    }
}
