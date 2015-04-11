// RobotBuilder Version: 1.5
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc4579.RecycleRush3.subsystems;

import org.usfirst.frc4579.RecycleRush3.RobotMap;
import org.usfirst.frc4579.RecycleRush3.commands.*;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;


/**
 *
 */
public class Measurement extends Subsystem {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    Gyro gyro = RobotMap.measurementGyro;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private double	totalDistance = 0., // Total distance travelled
    				distanceX = 0., // Total sideways distance: X-axis on accel
    				distanceY = 0., // Total forwards distance: Z-axis on accel
    				driftRate = 0., // Gyro drift rate in degrees/second
    				lastTime = 0.,  // Calculates delta t.
    				startTime = 0.0, //Calculates gyro time since initialization.
    				velocity = 0.,   // V (Velocity)
    				accelX = 0.,     //given from getY() due to the roborio orientation
    				accelY = 0.,     //given from getZ() due to the roborio orientation
    				accel = 0.0,     //total acceleration.
    				gyroAngle = 0.,
	                accelOffsetX = 0.0, 
			        accelOffsetY = 0.0;    //units of g's.

    private boolean validity = false; // True after initialization.
    private Timer timer = new Timer();
    private Accelerometer robotAccelerometer = RobotMap.robotAccelerometer;
    
    static final double GtoINCHES = 386.088;   //convert from g's to inches/sec/sec.
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
        setDefaultCommand(new Accumulate());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
	
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public void calibrate() {
    	double t;
    	//Note:  X direction is to the right, Y is forward. Gyro is plus degrees to the right.
    	System.out.println("Initiating Calibration***");
        t = Timer.getFPGATimestamp();
    	gyro.reset();    // Initializes gyro to zero
    	Timer.delay(1.); // Delay to get sample
    	//calculate degrees of drift per second.
    	driftRate = gyro.getAngle() / (Timer.getFPGATimestamp()-t);
    	
    	//calibrate built in accelerometer on two axes.
    	int reps = 100;
    	accelOffsetX = 0.0;
    	accelOffsetY = 0.0;
    	for (int i=0; i<reps; i++)  {
    		accelOffsetX += robotAccelerometer.getY();  //units of g's.
    		accelOffsetY += robotAccelerometer.getZ();
    	}
    	accelOffsetX /= (double)reps;   //units of g's.
    	accelOffsetY /= (double)reps;
    	System.out.println("Time for calibration: " + (Timer.getFPGATimestamp()-t) + " seconds..."); 
    	System.out.print("DRIFT RATE: " + driftRate + " deg/sec\nAccelOffsetX: " + accelOffsetX + " g's\nAccelOffsetY: " + accelOffsetY + " g's");
    }
    
    public void initGyro()  {
    	gyro.reset();
    	timer.stop();
    	timer.reset();
    	startTime = 0.0;
    	timer.start();   //this counts time since initialization of gyro heading.    	
    }
    
    public void initAccel()  {
    	accelX = 0.0;
    	accelY = 0.0;
    	velocity = 0.0;
    	totalDistance = 0.0;
    	distanceX = 0.0;
    	distanceY = 0.0;
    	lastTime = 0.0;
    	setValid();    	
    }
    public void initialize() {
    	initGyro();
    	initAccel();
    }
    
    public void accumulate() {
    	double t, deltaT;
    	//read hardware values.
    	double x = robotAccelerometer.getY();   //note change of axes.  Units of g's.
    	double y = robotAccelerometer.getZ();   // Y is forward.
    	double g = gyro.getAngle();
    	System.out.println("Raw x=" + x +"  y=" + y + "  g=" + g);
    	//use calibrations.
    	accelX = (x - accelOffsetX) * GtoINCHES;  //units of inches/sec/sec.
    	accelY = (y - accelOffsetY) * GtoINCHES;
    	gyroAngle = g - (driftRate * (timer.get() - startTime));
    	System.out.println("accelX=" + accelX +"  accelY=" + accelY + "  gyroAngle=" + gyroAngle);
    	//calculate total acceleration.
        accel = Math.sqrt((accelX * accelX) + (accelY * accelY));
        System.out.println("accel=" + accel);
        //calculate delta t
        t = timer.get();
        deltaT = t - lastTime;
        lastTime = t;
    	//deltaT = 0.020;     //optional for debugging.
    	System.out.println("DeltaT= " + deltaT);
    	//calculate velocity and distances
    	velocity += accel * deltaT;
    	totalDistance += 0.5 * accel * deltaT * deltaT;
    	distanceX += 0.5 * accelX * deltaT * deltaT;
    	distanceY += 0.5 * accelY * deltaT * deltaT;
    	System.out.println("totalDistance="+ totalDistance + "  distanceX=" + distanceX + "  distanceY=" + distanceY);
    }
    
    public double getVelocity() {
    	return velocity;
    }
    
    public double getDistance() {
    	return totalDistance;
    }
    
    public double getDistanceFwd() {
    	return distanceY;
    }
    
    public double getDistanceSide() {
    	return distanceX;
    }
    
    public double getAngle() {
    	return gyroAngle;
    }
    
    public double getGyroRate() {
    	return gyro.getRate();
    }
    
    public void setValid() {
    	validity = true;
    }
    
    public void setInvalid() {
    	validity = false;
    }
    
    public boolean isValid() {
    	return validity;
    }
    
}
