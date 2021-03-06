// RobotBuilder Version: 1.5
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc4579.RecycleRush4.subsystems;

import org.usfirst.frc4579.RecycleRush4.Robot;
import org.usfirst.frc4579.RecycleRush4.commands.Drive;
import org.usfirst.frc4579.RecycleRush4.RobotMap;

import com.RoboEagles4579.filters.FirstOrderLPF;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;


/**
 *
 */
public class DriveTrain extends Subsystem {

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    SpeedController leftMotor = RobotMap.driveTrainLeftMotor;
    SpeedController rightMotor = RobotMap.driveTrainRightMotor;
    RobotDrive driveSystem = RobotMap.driveTrainDriveSystem;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
	public double width = 20.;             //width between drive wheels, inches.    
	public double spinPoint = 0.8;         //This is a Joystick value, 0 -> 1, where you want the
                                           //robot to spin on the inside wheel.
	
	private FirstOrderLPF lpfX = new FirstOrderLPF(), // Low pass filters (for joystick axis)
						  lpfY = new FirstOrderLPF(),
						  lpfZ = new FirstOrderLPF();
	private final double epsilon = 0.0019569471624266;  //Minimum joystick turn value.
	//x is a constant used to map the turn input values to a radius, and specifically
	//to control how much turn input (spinPoint) it takes to rotate on the inside wheel.
	private final double x = spinPoint != 0 && spinPoint != 1.0 ? 1.0 / 2.0 * (Math.log(1/spinPoint)) : 0.9;

    
    public void drive(double turnError, double speed) { // SHOULD BE CALLED IN ITERATIONS AS TO ADJUST FOR TURNING
    	double k = 1.0/180.0,//Positive turn error turns right
    		   speedAdjust = turnError * k;   	
    	leftMotor.set(speed + speedAdjust);
    	rightMotor.set(-(speed - speedAdjust));
    }

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
        setDefaultCommand(new Drive());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
    }
    
    public void driveManual(Joystick stick)  {
    	//square the inputs.
    	
    	double initX = stick.getX(),
    			initY = stick.getY(),
    			initZ = stick.getZ();
    	System.out.println("\nX: " + initX + " Y: " + initY + " Z: " + initZ + "\n");
    	double x = initX,
    			signX = x >= 0.0 ? 1.0: -1.0;
    	double y = initY,
    			signY = y >= 0.0 ? 1.0 : -1.0;
    	double z = initZ,
    			signZ = z >= 0.0 ? 1.0 : -1.0;
    	x = Math.pow(x, 2) * signX;
    	y = Math.pow(y, 2) * signY;
    	z = Math.pow(z, 2) * signZ;
    	System.out.println("\nX: " + x + " Y: " + y + " Z: " + z + "\n");
    	//low pass filter the inputs.
    	x = lpfX.filter(x);
    	y = lpfY.filter(y);
    	z = lpfZ.filter(z);
    	/* This should improve driving by eliminating loss of power in y direction.
    	 * Since joystick is radial	
    	 */
    	//y = Math.sqrt(x * x + y * y) * (y / Math.abs(y));
    	//drive the robot.
    	System.out.println("\nX: " + x + " Y: " + y + " Z: " + z + "\n");
    	driveSystem.arcadeDrive(-initY, -initZ);	
    }
    
    public void driveAuto(double speed, double rotation)  {
    	//in the library, the rotation value is used to create a delta speed to each wheel.
    	driveSystem.arcadeDrive(speed, rotation);
    }
    
    public void stop()  {
    	leftMotor.set(0);
    	rightMotor.set(0);
    }
    
    public double getWidth()  {
    	return width;
    }
    
    public void setWidth(double width) {
    	this.width = width;
    }
    
    public double getSpinPoint()  {
    	return spinPoint;
    }
    
    public void setSpinPoint(double spinPoint)  {
    	this.spinPoint = spinPoint;
    }
    
    /*
     * This drive method allows for setting the turn radius from the joystick in a
     * predictable manner.   Set the robot width and the spinPoint on the joystick
     * as constants in this class header, or using the setter functions included.
     * 
     * @param speed is a value from -1 to +1, where positive is forward motion.
     * @param turn is a joystick value from -1 to +1, where positive is a right turn.
     * @param squareSpeed is a boolean indicating if the speed input should be squared before use.
     */
    public void driveControlled(double speed, double turn, boolean squareSpeed)  {
    	//Note: minimum turn input is 1/511 = 0.0019569471624266 = epsilon
    	if (turn <= epsilon) {
    		driveRadius (speed, 500 * width);
    		return;
    	}
    	double turnSign = turn / Math.abs(turn);
    	if (squareSpeed) speed *= speed;
    	//Do all calculations with a positive turn setting.
    	turn = Math.abs(turn);
    	//Map the turn input to a radius value.
    	double radius = width * ((Math.log(1 / Math.pow(turn, x))) + (width / 2.0));
    	//Hand the rest of the work to the other method.
    	driveRadius(speed, turnSign * radius);
    }
    
    /* 
     * This drive method allows for setting the turn radius directly, and the turn
     * is controlled by using the gyro rate value.  To go straight, use a very
     * large radius value, more than 200 times the robot width.  Since this method is 
     * expected to be used in Autonomous only, there is no need to square the speed input.
     * 
     * @param double speed - speed value from [-1 to 0 to +1].  Negative values move backwards.
     * @param double radius - the expected turn radius in inches measured from the outside wheel
     *                        of the robot wheel base.  Negative radius values turn left.
     */
    public void driveRadius(double speed, double radius)  {
    	//Use positive radius value for computation.
    	double radiusAbs = Math.abs(radius);
    	//Calculate the fast and slow wheel speeds.
    	double slow = speed * (radiusAbs - width) / radiusAbs;
    	//Calculate the expected rate of turn based on wheel speed ratio.
    	double omega = speed / slow * (radius/Math.abs(radius)) * 2.0 * Math.PI;
    	//Adjust the slow wheel to hold a constant rate of turn.
    	double rateActual = Robot.measurement.gyro.getRate();
    	slow = slow * omega / rateActual;
    	//Set motor speeds.
    	//Case for straight ahead.
    	if (radiusAbs > 200 * width)  {
    		leftMotor.set(speed);
    		rightMotor.set(speed);
    	}
    	//Case for right turn.
    	else if (radius > 0.0)  {
    		leftMotor.set(speed);
    		rightMotor.set(slow);
    	}
    	//Case for left turn.
    	else if (radius < 0.0)  {
    		leftMotor.set(slow);
    		rightMotor.set(speed);
    	}
    	System.out.println("speed="+speed+" radius="+radius);
    	System.out.println(" slow="+slow+ " omega="+omega+" gyro="+rateActual);
    }    
}

