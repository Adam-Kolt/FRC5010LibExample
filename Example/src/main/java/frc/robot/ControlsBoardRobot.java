// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.frc5010.common.arch.GenericRobot;
import org.frc5010.common.sensors.Controller;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

/** This is an example robot class. */
public class ControlsBoardRobot extends GenericRobot {
  DisplayValueSubsystem displayValueSubsystem = new DisplayValueSubsystem();
  ThriftyMotorRunner runner;
  DeviceFactory deviceFactory = new DeviceFactory(mechVisual);

  ExampleSubsystem exampleSubsystem = new ExampleSubsystem(deviceFactory.percentControlledMotor(), 
    deviceFactory.velocityControlledMotor());

  public ControlsBoardRobot(String directory) {
    super(directory);
    runner = new ThriftyMotorRunner(2);
  }

  @Override
  public void configureButtonBindings(Controller driver, Controller operator) {
    driver.createXButton().onTrue(exampleSubsystem.setControlMotorReference(() -> 5000))
        .onFalse(exampleSubsystem.setControlMotorReference(() -> 0));
    driver.createYButton().onTrue(exampleSubsystem.setControlMotorReference(() -> 2000))
        .onFalse(exampleSubsystem.setControlMotorReference(() -> 0));
  }

  @Override
  public void setupDefaultCommands(Controller driver, Controller operator) {
    runner.setDefaultCommand(Commands.run(() -> runner.runMotor(driver.getRightYAxis()), runner));
    exampleSubsystem.setDefaultCommand(exampleSubsystem.getDefaultCommand(() -> driver.getLeftYAxis()));
  }

  @Override
  public void initAutoCommands() {
  }

  @Override
  public Command generateAutoCommand(Command autoCommand) {
    return autoCommand;
  }

}
