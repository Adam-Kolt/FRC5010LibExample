// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.frc5010.common.arch.GenericRobot;
import org.frc5010.common.config.json.DrivetrainPropertiesJson;
import org.frc5010.common.constants.SwerveConstants;
import org.frc5010.common.drive.GenericDrivetrain;
import org.frc5010.common.sensors.Controller;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

/** This is an example robot class. */
public class ExampleRobot extends GenericRobot{
  SwerveConstants swerveConstants;
  GenericDrivetrain drivetrain;
  DisplayValueSubsystem displayValueSubsystem = new DisplayValueSubsystem();
  ThriftyMotorRunner runner;

  public ExampleRobot(String directory) {
    super(directory);
    drivetrain = (GenericDrivetrain) getSubsystem(DrivetrainPropertiesJson.DRIVE_TRAIN);
    runner = new ThriftyMotorRunner(2);
  }

  @Override
  public void configureButtonBindings(Controller driver, Controller operator) {

  }

  @Override
  public void setupDefaultCommands(Controller driver, Controller operator) {
    drivetrain.setDefaultCommand(drivetrain.createDefaultCommand(driver));
    runner.setDefaultCommand(Commands.run(() -> runner.runMotor(driver.getRightYAxis()), runner));
  }

  @Override
  public void initAutoCommands() {
    drivetrain.setAutoBuilder();
  }

  @Override
  public Command generateAutoCommand(Command autoCommand) {
    return drivetrain.generateAutoCommand(autoCommand);
  }

}
