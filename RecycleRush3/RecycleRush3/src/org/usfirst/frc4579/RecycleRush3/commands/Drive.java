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

import org.usfirst.frc4579.RecycleRush3.OI;
import org.usfirst.frc4579.RecycleRush3.Robot;
import org.usfirst.frc4579.RecycleRush3.RobotMap;

/**
 *
 */
public class  Drive extends Command {
    
	public Drive() {
        // Use requires() here to declare subsystem dependencies
        // e.g. requires(chassis);

        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.drivetrain);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println("Driving has initialized");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drivetrain.driveManual(Robot.oi.driveStick); // CHECKED DOCS, THIS SHOULD BE IN execute()
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.stop(); // SO THAT THE MOTORS STOP WHEN THE COMMAND IS STOPPED (ICE)
    	System.out.println("**** Driving has been ended!");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	System.out.println("**** Driving has been interrupted!");
    	end();
    }
}