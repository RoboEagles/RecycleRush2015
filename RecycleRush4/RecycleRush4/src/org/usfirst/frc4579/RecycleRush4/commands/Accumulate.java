// RobotBuilder Version: 1.5
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc4579.RecycleRush4.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc4579.RecycleRush4.Robot;

/**
 *
 */
public class  Accumulate extends Command {
	
	/*
	 * THIS COMMAND MUST RUN IN TELEOP AND BE UNINTERUPTED FOR THE ACCUMULATION AND CALCULATION OF SENSOR DATA
	 */

    public Accumulate() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);

        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.measurement);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.measurement.initialize();
    	System.out.println("Initial Robot Angle (0): " + Robot.measurement.getAngle());
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//System.out.println("Acceleration Magnitude: " + Robot.measurement.getAcceleration());
    	//System.out.println("Gyro Angle: " + Robot.measurement.getAngle()); THESE PRINTS ARE HANDLED IN ACCUMULATE()
    	Robot.measurement.accumulate();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.measurement.setInvalid();
    	System.out.println("**** Accumulate measurements has ended!");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    	System.out.println("**** Accumulate measurements has been interrupted!");
    }
}
