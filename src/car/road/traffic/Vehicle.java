/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package car.road.traffic;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Timer;

/**
 *
 * @author AgilI
 * 
 */
public class Vehicle implements ActionListener {

    private TrafficLight trafficLight;
    private ImageObserver imageObserver;
    private Image image;
    private float velocity;
    private float acceleration = 0f;
    private float vehVelo;
    private float curAngle;
    private Vertical vehiclePosition;
    private Timer timer;

    public enum VehicleDirection {
        LEFT, RIGHT, UP, DOWN, DOWN_LEFT, RIGHT_UP,
        RIGHT_DOWN, LEFT_UP, UP_LEFT, UP_RIGHT
    };

    public enum VehicleState {
        BRAKE, MOVE_X, MOVE_Y, TURNING
    };

    public enum Command {
        GO_STRAIGHT, TURN_LEFT, TURN_RIGHT
    };
    private Vehicle.VehicleState vState, vPrev;
    private Vehicle.VehicleDirection vDirection;
    private AffineTransform transform;
    private boolean passedTrafficLight;

    //Active Cruise Control variables
    private Vehicle vehicleAhead;

    //For controlling turning
    private Vehicle.Command command;

    //Traffic lights position constants that vehicles should pass
    private final int movingRightPos = 301;
    private final int movingLeftPos = 1150;
    private final int movingDownPos = 121;
    private final int movingUpPos = 652;

    //Position Constants
    private Vertical RIGHT_LEFT_POS = new Vertical(1500, 265);
    private Vertical LEFT_RIGHT_POS = new Vertical(-300, 463);
    private Vertical DOWN_UP_POS = new Vertical(920, 840);
    private Vertical UP_DOWN_POS = new Vertical(520, -100);

    private final Vertical DOWN_LEFT = new Vertical(360, 0);
    private final Vertical RIGHT_UP = new Vertical(0, 325);
    private final Vertical RIGHT_DOWN = new Vertical(0, 454);
    private final Vertical LEFT_UP = new Vertical(1000, 145);
    private final Vertical UP_LEFT = new Vertical(540, 600);
    private final Vertical UP_RIGHT = new Vertical(651, 593);

    private boolean turn = false;

    public TrafficLight getTrafficLight() {
        return trafficLight;
    }

    public void setTrafficLight(TrafficLight trafficLight) {
        this.trafficLight = trafficLight;
    }

    public ImageObserver getImageObserver() {
        return imageObserver;
    }

    public void setImageObserver(ImageObserver imageObserver) {
        this.imageObserver = imageObserver;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public float getVehVelo() {
        return vehVelo;
    }

    public void setVehVelo(float vehVelo) {
        this.vehVelo = vehVelo;
    }

    public float getCurAngle() {
        return curAngle;
    }

    public void setCurAngle(float curAngle) {
        this.curAngle = curAngle;
    }

    public Vertical getVehiclePosition() {
        return vehiclePosition;
    }

    public void setVehiclePosition(Vertical vehiclePosition) {
        this.vehiclePosition = vehiclePosition;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public VehicleState getvState() {
        return vState;
    }

    public void setvState(VehicleState vState) {
        this.vState = vState;
    }

    public VehicleState getvPrev() {
        return vPrev;
    }

    public void setvPrev(VehicleState vPrev) {
        this.vPrev = vPrev;
    }

    public VehicleDirection getvDirection() {
        return vDirection;
    }

    public void setvDirection(VehicleDirection vDirection) {
        this.vDirection = vDirection;
    }

    public AffineTransform getTransform() {
        return transform;
    }

    public void setTransform(AffineTransform transform) {
        this.transform = transform;
    }

    public boolean isPassedTrafficLight() {
        return passedTrafficLight;
    }

    public void setPassedTrafficLight(boolean passedTrafficLight) {
        this.passedTrafficLight = passedTrafficLight;
    }

    public Vehicle getVehicleAhead() {
        return vehicleAhead;
    }

    public void setVehicleAhead(Vehicle vehicleAhead) {
        this.vehicleAhead = vehicleAhead;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Vertical getRIGHT_LEFT_POS() {
        return RIGHT_LEFT_POS;
    }

    public void setRIGHT_LEFT_POS(Vertical RIGHT_LEFT_POS) {
        this.RIGHT_LEFT_POS = RIGHT_LEFT_POS;
    }

    public Vertical getLEFT_RIGHT_POS() {
        return LEFT_RIGHT_POS;
    }

    public void setLEFT_RIGHT_POS(Vertical LEFT_RIGHT_POS) {
        this.LEFT_RIGHT_POS = LEFT_RIGHT_POS;
    }

    public Vertical getDOWN_UP_POS() {
        return DOWN_UP_POS;
    }

    public void setDOWN_UP_POS(Vertical DOWN_UP_POS) {
        this.DOWN_UP_POS = DOWN_UP_POS;
    }

    public Vertical getUP_DOWN_POS() {
        return UP_DOWN_POS;
    }

    public void setUP_DOWN_POS(Vertical UP_DOWN_POS) {
        this.UP_DOWN_POS = UP_DOWN_POS;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public Vehicle(File screen, int vel, Vehicle.VehicleState state,
            Vehicle.VehicleDirection direction, TrafficLight trafficLight,
            ImageObserver imageObserver, Vehicle vehicleAhead, int index) {
        this.vState = state;
        this.vehicleAhead = vehicleAhead;
        this.vPrev = vState;
        this.vDirection = direction;
        this.imageObserver = imageObserver;
        this.trafficLight = trafficLight;
        this.command = Vehicle.Command.GO_STRAIGHT;
        try {
            image = ImageIO.read(screen);
            switch (index % 3) {
                case 0:
                    //right lane
                    UP_DOWN_POS = new Vertical(UP_DOWN_POS.x + 100, UP_DOWN_POS.y);
                    DOWN_UP_POS = new Vertical(DOWN_UP_POS.x + 100, DOWN_UP_POS.y);
                    LEFT_RIGHT_POS = new Vertical(LEFT_RIGHT_POS.x, LEFT_RIGHT_POS.y + 70);
                    RIGHT_LEFT_POS = new Vertical(RIGHT_LEFT_POS.x, RIGHT_LEFT_POS.y + 60);
                    command = Vehicle.Command.TURN_RIGHT;
                    break;
                case 1:
                    break;
                case 2:
                    //left lane
                    UP_DOWN_POS = new Vertical(UP_DOWN_POS.x - 120, UP_DOWN_POS.y);
                    DOWN_UP_POS = new Vertical(DOWN_UP_POS.x - 100, DOWN_UP_POS.y);
                    LEFT_RIGHT_POS = new Vertical(LEFT_RIGHT_POS.x, LEFT_RIGHT_POS.y - 70);
                    RIGHT_LEFT_POS = new Vertical(RIGHT_LEFT_POS.x, RIGHT_LEFT_POS.y - 70);
                    command = Vehicle.Command.TURN_LEFT;
                    break;
            }
            switch (vDirection) {
                case LEFT:
                    if (vehicleAhead != null && vehicleAhead.vehiclePosition.x > movingLeftPos) {
                        vehiclePosition = new Vertical(vehicleAhead.vehiclePosition.x + 300, RIGHT_LEFT_POS.y);
                    } else {
                        vehiclePosition = RIGHT_LEFT_POS;
                    }
                    break;

                case RIGHT:
                    if (vehicleAhead != null && vehicleAhead.vehiclePosition.x < movingRightPos) {
                        vehiclePosition = new Vertical(vehicleAhead.vehiclePosition.x - 300, LEFT_RIGHT_POS.y);
                    } else {
                        vehiclePosition = LEFT_RIGHT_POS;
                    }
                    break;

                case UP:
                    if (vehicleAhead != null && vehicleAhead.vehiclePosition.y > movingUpPos) {
                        vehiclePosition = new Vertical(DOWN_UP_POS.x, vehicleAhead.vehiclePosition.y + 200);
                    } else {
                        vehiclePosition = DOWN_UP_POS;
                    }
                    break;

                case DOWN:
                    if (vehicleAhead != null && vehicleAhead.vehiclePosition.y <= movingDownPos) {
                        vehiclePosition = new Vertical(UP_DOWN_POS.x, vehicleAhead.vehiclePosition.y - 200);
                    } else {
                        vehiclePosition = UP_DOWN_POS;
                    }
                    break;
                case DOWN_LEFT:
                    vehiclePosition = DOWN_LEFT;
                    break;
                case RIGHT_UP:
                    vehiclePosition = RIGHT_UP;
                    break;
                case RIGHT_DOWN:
                    vehiclePosition = RIGHT_DOWN;
                    break;
                case LEFT_UP:
                    vehiclePosition = LEFT_UP;
                    break;
                case UP_LEFT:
                    vehiclePosition = UP_LEFT;
                    break;
                case UP_RIGHT:
                    vehiclePosition = UP_RIGHT;
                    break;
                default:
                    break;
            }

            transform = new AffineTransform();
            velocity = vel;
            vehVelo = velocity;
            curAngle = 0;
            transform.setToTranslation(vehiclePosition.x, vehiclePosition.y);

            if (vState == Vehicle.VehicleState.MOVE_Y) {
                curAngle = 90;
                transform.setToTranslation(vehiclePosition.x, vehiclePosition.y);
                transform.rotate(Math.toRadians(curAngle), image.getWidth(imageObserver) / 2, image.getHeight(imageObserver) / 2);
            }

            timer = new Timer(10, this);
            timer.start();

        } catch (IOException e) {
             e.printStackTrace();
            System.out.println("Can't find  image" + e.getMessage());
           
        }
    }

    public boolean isInView() {
        if (vDirection == Vehicle.VehicleDirection.LEFT && vehiclePosition.x < -100) {
            return false;
        }
        if (vDirection == Vehicle.VehicleDirection.RIGHT && vehiclePosition.x > 1500) {
            return false;
        }
        if (vDirection == Vehicle.VehicleDirection.UP && vehiclePosition.y < -100) {
            return false;
        }
        if (vDirection == Vehicle.VehicleDirection.DOWN && vehiclePosition.y > 850) {
            return false;
        }
        return true;
    }

    private void accelerate(int current_position, int final_position) {
        float disting = final_position - current_position;
        float t = disting / velocity;

        if (Math.abs(t) > 0) {
            acceleration = (0 - velocity) / t;
        } else {
            acceleration = 0;
        }

    }

    private void steerTowards(float angle, float t) {
        float angularVel = angle / t;
        if (angle == 0 || angle == 90 || angle == 270) {
            angularVel = (angle - curAngle) / t;
        }
        curAngle += angularVel;
        transform.rotate(Math.toRadians(curAngle), image.getWidth(imageObserver), image.getHeight(imageObserver));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (vehicleAhead != null && !vehicleAhead.isInView()) {
            vehicleAhead = null;
        }
        if (trafficLight != null && (trafficLight.forwardGo || passedTrafficLight) && !turn) {
            vState = vPrev;
        } else if (trafficLight != null && !trafficLight.forwardGo) {
            vState = Vehicle.VehicleState.BRAKE;
        }
        switch (vState) {
            case BRAKE:
                switch (vDirection) {
                    case LEFT:
                        if (velocity > 0) {
                            velocity *= -1;
                            curAngle = 180;
                        }
                        if (vehicleAhead == null) {
                            accelerate(vehiclePosition.x, movingLeftPos);
                            velocity += acceleration;
                            if (vehiclePosition.x >= movingLeftPos) {

                                this.vehiclePosition.x += velocity;
                            }
                        } else {
                            if (vehicleAhead.vehiclePosition.x < 1450 && vehicleAhead.vehiclePosition.x > movingLeftPos) {
                                accelerate(vehiclePosition.x, vehicleAhead.getVehiclePosition().x + 120);
                                if (vehiclePosition.x >= vehicleAhead.vehiclePosition.x + 120) {
                                    velocity += acceleration;
                                    this.vehiclePosition.x += velocity;
                                }
                            }

                        }
                        break;
                    case RIGHT:

                        if (vehicleAhead != null) {
                            if ((vehicleAhead.vehiclePosition.x > -50 && vehicleAhead.vehiclePosition.x < movingRightPos)) {
                                accelerate(vehiclePosition.x, vehicleAhead.getVehiclePosition().x - 150);

                                if (acceleration > 0) {
                                    acceleration *= -1;
                                }

                                velocity += acceleration;
                                if (vehiclePosition.x <= vehicleAhead.getVehiclePosition().x - 150) {
                                    this.vehiclePosition.x += velocity;
                                }

                            }
                        } else {
                            accelerate(vehiclePosition.x, movingRightPos);
                            velocity += acceleration;
                            if (vehiclePosition.x <= movingRightPos) {
                                this.vehiclePosition.x += velocity;
                            }
                        }
                        break;

                    case UP:
                        curAngle = -90;
                        if (velocity > 0) {
                            velocity *= -1;
                        }

                        if (vehicleAhead != null && vehicleAhead.passedTrafficLight) {
                            vehicleAhead = null;
                        }
                        if (vehicleAhead == null) {
                            accelerate(vehiclePosition.y, movingUpPos);
                            velocity += acceleration;
                            if (vehiclePosition.y >= movingUpPos) {
                                this.vehiclePosition.y += velocity;
                            }
                        } else {
                            if (vehicleAhead.vehiclePosition.y > 650 && vehicleAhead.vehiclePosition.y > movingUpPos) {
                                accelerate(vehiclePosition.y, vehicleAhead.vehiclePosition.y + 150);
                                velocity += acceleration;
                                if (vehiclePosition.y > vehicleAhead.vehiclePosition.y + 150) {

                                    this.vehiclePosition.y += velocity;
                                }
                            }
                        }
                        break;
                    case DOWN:
                        if (vehicleAhead != null && vehicleAhead.passedTrafficLight) {
                            vehicleAhead = null;
                        }

                        if (acceleration > 0) {
                            acceleration *= -1;
                        }

                        if (vehicleAhead == null) {
                            accelerate(vehiclePosition.y, movingDownPos);
                            velocity += acceleration;
                            if (vehiclePosition.y <= movingDownPos) {

                                this.vehiclePosition.y += velocity;
                            }
                        } else {
                            if (vehicleAhead.vehiclePosition.y > -50 && vehicleAhead.vehiclePosition.y < movingDownPos) {
                                //if(vehicleAhead.vehiclePosition.y > vehiclePosition.y)
                                accelerate(vehiclePosition.y, vehicleAhead.vehiclePosition.y - 140);
                                velocity += acceleration;
                                if (vehiclePosition.y <= vehicleAhead.vehiclePosition.y - 140) {

                                    this.vehiclePosition.y += velocity;
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }

                this.transform.setToTranslation(vehiclePosition.x, vehiclePosition.y);
                this.transform.rotate(Math.toRadians(curAngle), image.getWidth(imageObserver) / 2, image.getHeight(imageObserver) / 2);
                //nothing now
                break;

            case TURNING:
                this.trafficLight = null;
                switch (command) {
                    case TURN_LEFT:
                        switch (vDirection) {
                            case DOWN:
                                if (curAngle == 180) {
                                    vDirection = Vehicle.VehicleDirection.LEFT;
                                    vState = Vehicle.VehicleState.MOVE_X;
                                }
                                steerTowards(180, 40);
                                vehiclePosition.x -= Math.abs(velocity - 2);
                                if (vehiclePosition.y <= movingDownPos + 80) {
                                    vehiclePosition.y += Math.abs(velocity - 3);
                                }
                                this.transform.setToTranslation(vehiclePosition.x, vehiclePosition.y);
                                this.transform.rotate(Math.toRadians(curAngle), image.getWidth(imageObserver) / 2, image.getHeight(imageObserver) / 2);
                                break;
                            case LEFT_UP:
                                if (curAngle == 270) {
                                    vDirection = Vehicle.VehicleDirection.LEFT;
                                    vState = VehicleState.MOVE_X;

                                }

                                steerTowards(270, 10);
                                vehiclePosition.y -= Math.abs(velocity - 2);
                                if (vehiclePosition.x >= movingLeftPos - 100) {
                                    vehiclePosition.x -= 5;
                                }

                                this.transform.setToTranslation(vehiclePosition.x, vehiclePosition.y);
                                this.transform.rotate(Math.toRadians(curAngle), image.getWidth(imageObserver) / 2, image.getHeight(imageObserver) / 2);
                                break;
                            default:
                                break;

                        }
                        break;

                    case TURN_RIGHT:
                        switch (vDirection) {
                            case UP_RIGHT:
                                if (curAngle == 0) {						
                                    vDirection = Vehicle.VehicleDirection.RIGHT;
                                    vState = Vehicle.VehicleState.MOVE_X;

                                }
                                steerTowards(0, 9);

                                vehiclePosition.x += 5;
                                if (vehiclePosition.y >= movingUpPos - 120) {
                                    vehiclePosition.y -= 6;
                                }

                                this.transform.setToTranslation(vehiclePosition.x, vehiclePosition.y);
                                this.transform.rotate(Math.toRadians(curAngle), image.getWidth(imageObserver) / 2, image.getHeight(imageObserver) / 2);
                                break;

                            case RIGHT_DOWN:
                                if (curAngle == 90) {
                                    vDirection = Vehicle.VehicleDirection.DOWN;
                                    vState = Vehicle.VehicleState.MOVE_Y;
                                }
                                steerTowards(90, 9);

                                if (vehiclePosition.x <= movingRightPos + 50) {
                                    vehiclePosition.x += 2;
                                }
                                vehiclePosition.y += 5;
                                this.transform.setToTranslation(vehiclePosition.x, vehiclePosition.y);
                                this.transform.rotate(Math.toRadians(curAngle), image.getWidth(imageObserver) / 2, image.getHeight(imageObserver) / 2);
                                break;
                            default:
                                break;
                        }
                        break;

                    default:
                        break;
                }
                break;

            case MOVE_X:
                velocity = vehVelo;

                if (vDirection == Vehicle.VehicleDirection.LEFT) {
                    if (!passedTrafficLight && !trafficLight.forwardGo) {
                        vState = Vehicle.VehicleState.BRAKE;
                    }

                    if (vehiclePosition.x < movingLeftPos && !turn) {
                        passedTrafficLight = true;
                        if (command == Vehicle.Command.TURN_LEFT) {
                            vDirection = Vehicle.VehicleDirection.LEFT_UP;
                            vState = Vehicle.VehicleState.TURNING;
                            turn = true;
                        }
                    }
                    if (velocity > 0) {
                        velocity *= -1;
                        curAngle = 180;
                    }
                    if (vehicleAhead != null) {
                        accelerate(vehiclePosition.x, vehicleAhead.vehiclePosition.x + 120);
                        if (acceleration > 0) {
                            acceleration = 0;
                            velocity = vehicleAhead.getVelocity();
                        }
                        velocity += acceleration;
                    }

                } else if (vDirection == Vehicle.VehicleDirection.RIGHT) {
                    if (!passedTrafficLight && !trafficLight.forwardGo) {
                        vState = Vehicle.VehicleState.BRAKE;
                    }

                    if (vehiclePosition.x > movingRightPos) {
                        passedTrafficLight = true;

                        if (command == Vehicle.Command.TURN_RIGHT) {
                            vDirection = Vehicle.VehicleDirection.RIGHT_DOWN;
                            vState = Vehicle.VehicleState.TURNING;
                            turn = true;
                        }
                    }
                    if (velocity < 0) {
                        velocity *= -1;
                        curAngle = 0;
                    }
                    if (vehicleAhead != null) {
                        if (acceleration > 0) {
                            accelerate(vehiclePosition.x, vehicleAhead.vehiclePosition.x - 150);
                        } else {
                            acceleration = 0;
                        }
                        acceleration *= -1;
                        velocity += acceleration;
                    }
                }
                this.vehiclePosition.x += velocity;
                this.transform.setToTranslation(vehiclePosition.x, vehiclePosition.y);
                this.transform.rotate(Math.toRadians(curAngle), image.getWidth(imageObserver) / 2, image.getHeight(imageObserver) / 2);
                break;

            case MOVE_Y:
                velocity = vehVelo;
                switch (vDirection) {
                    case UP:
                        if (vehicleAhead != null && vehicleAhead.vState != this.vState) {
                            velocity = vehicleAhead.velocity;
                            vehicleAhead = null;

                        }
                        if (!passedTrafficLight && !trafficLight.forwardGo) {
                            vState = Vehicle.VehicleState.BRAKE;

                        }
                        if (vehiclePosition.y < movingUpPos) {
                            passedTrafficLight = true;
                            if (command == Vehicle.Command.TURN_RIGHT) {
                                vDirection = Vehicle.VehicleDirection.UP_RIGHT;
                                vState = Vehicle.VehicleState.TURNING;
                                turn = true;
                            }
                        }
                        if (velocity > 0) {		//
                            velocity *= -1;
                            curAngle = -90;
                        }
                        if (vehicleAhead != null) {
                            accelerate(vehiclePosition.y, vehicleAhead.vehiclePosition.y + 130);
                            velocity += acceleration;
                        }
                        break;

                    case DOWN:

                        if (!passedTrafficLight && !trafficLight.forwardGo) {
                            vState = Vehicle.VehicleState.BRAKE;
                        }

                        if (vehiclePosition.y > movingDownPos) {
                            passedTrafficLight = true;

                            if (command == Vehicle.Command.TURN_LEFT) {
                                vState = VehicleState.TURNING;
                                turn = true;
                            }
                        }
                        if (velocity < 0) {
                            curAngle = 90;
                            velocity *= -1;
                        }
                        if (vehicleAhead != null) {
                            accelerate(vehiclePosition.y, vehicleAhead.vehiclePosition.y - 150);
                            if (acceleration < 0) {
                                acceleration = 0;
                                velocity = vehicleAhead.getVelocity();
                            }
                            acceleration *= -1;
                            velocity += acceleration;
                        }

                        if (vehicleAhead != null && vehicleAhead.vState != this.vState) {
                            velocity = vehicleAhead.velocity;
                            vehicleAhead = null;
                        }
                        break;
                    default:
                        break;

                }
                this.vehiclePosition.y += velocity;
                this.transform.setToTranslation(vehiclePosition.x, vehiclePosition.y);
                this.transform.rotate(Math.toRadians(curAngle), image.getWidth(imageObserver) / 2, image.getHeight(imageObserver) / 2);
                break;
        }
    }

}
