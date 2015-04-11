// RobotBuilder Version: 1.5
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc4579.RecycleRush3.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc4579.RecycleRush3.Robot;

/**
 *
 */
public class  DriveToAZ extends Command {
	
	private double travelSpeed,
				   targetDistance;

    public DriveToAZ() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);

        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.drivetrain);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	travelSpeed = -0.4;
    	targetDistance = 129.0; // TARGET DISTANCE IN INCHES (DISTANCE FROM ORIGINAL POSITION TO CENTER OF AUTOZONE RIGHT NOW, THIS IS APPRX) 
    	//Robot.drivetrain.drive(0.0, travelSpeed); // THIS WILL DRIVE STRAIGHT NOT NECESSARILLY ON BEARING TO THE AUTOZONE
    	setTimeout(2.15);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drivetrain.drive(0.0, travelSpeed); // THIS WILL DRIVE STRAIGHT NOT NECESSARILLY ON BEARING TO THE AUTOZONE
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
       // return Robot.measurement.getDistanceFwd() >= targetDistance; // WILL SUDDENLY STOP, HOPEFULLY THE ROBOT DOESN'T TIP OVER
    	return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.stop(); // PUT THIS HERE, BECAUSE THIS WILL MOST LIKELY BE THE LAST COMMAND IN AN AUTONOMOUS COMMAND GROUP
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}