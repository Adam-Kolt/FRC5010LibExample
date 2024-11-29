// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frc5010.common.arch;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.event.EventLoop;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

import org.frc5010.common.motors.function.GenericFunctionalMotor;
import org.frc5010.common.units.Time;

/** Base class for subsystems that provides default logging and network table support */
public class GenericSubsystem extends SubsystemBase
    implements WpiHelperInterface, GenericDeviceHandler {
  /** The network table values */
  protected final WpiNetworkTableValuesHelper values = new WpiNetworkTableValuesHelper();
  /** The log prefix */
  protected String logPrefix = getClass().getSimpleName();
  /** The mechanism simulation */
  protected Mechanism2d mechanismSimulation;
  /** The map of devices created by the configuration system */
  protected Map<String, Object> devices = new HashMap<>();
  /** The map of alternative EventLoops running at different rates */
  protected Map<String, EventLoop> teleopEventLoops = new HashMap<>();

  /** Creates a new LoggedSubsystem. */
  public GenericSubsystem(Mechanism2d mechanismSimulation) {
    this.mechanismSimulation = mechanismSimulation;
    WpiNetworkTableValuesHelper.register(this);
  }

  public GenericSubsystem() {
    WpiNetworkTableValuesHelper.register(this);
  }

  public GenericSubsystem(String configFile) {
    try {
      GenericRobot.subsystemParser.parseSubsystem(this, configFile);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    WpiNetworkTableValuesHelper.register(this);
  }

  /**
   * Get the mechanism simulation visual
   *
   * @return the mechanism visual
   */
  public Mechanism2d getMechVisual() {
    return mechanismSimulation;
  }

  /**
   * Set the mechanism simulation visual
   *
   * @param mechSim the mechanism visual
   */
  public void setMechSimulation(Mechanism2d mechSim) {
    mechanismSimulation = mechSim;
  }

  /**
   * Add a device to the configuration
   *
   * @param name the name of the device
   * @param device the device
   */
  @Override
  public void addDevice(String name, Object device) {
    devices.put(name, device);
  }

  /**
   * Get a device from the configuration
   *
   * @param name the name of the device
   * @return the device
   */
  @Override
  public Object getDevice(String name) {
    return devices.get(name);
  }

  /**
   * Called when the command is created and registers it with the network tables
   *
   * @param builder - The sendable builder
   */
  @Override
  public void initSendable(SendableBuilder builder) {
    log(logPrefix + ": Initializing sendables.");
    values.initSendables(builder, this.getClass().getSimpleName());
  }

  @Override
  public void periodic() {
    devices.values().stream()
        .forEach(
            it -> {
              if (it instanceof GenericFunctionalMotor) {
                ((GenericFunctionalMotor) it).draw();
              }
            });
  }

  @Override
  public void simulationPeriodic() {
    devices.values().stream()
        .forEach(
            it -> {
              if (it instanceof GenericFunctionalMotor) {
                ((GenericFunctionalMotor) it).simulationUpdate();
              }
            });
  }

  /**
   * Create a new teleop event loop at a specific rate and register it with the TimedRobot
   * 
   * @param name the name of the event loop
   * @param rate the rate of the event loop
   */
  public void createTeleopEventLoop(String name, Time rate) {
    EventLoop eventLoop = new EventLoop();
    teleopEventLoops.put(name, eventLoop);
    // TODO: Register the event loop with the TimedRobot
  }


  /**
   * Create a new teleop event loop at a specific rate and register it with the TimedRobot
   * 
   * @param name the name of the event loop
   * @param rate the rate of the event loop
   * @param offset the offset of the event loop relative to regular 20 ms loop
   */
  public void createTeleopEventLoop(String name, Time rate, Time offset) {
    EventLoop eventLoop = new EventLoop();
    teleopEventLoops.put(name, eventLoop);
    // TODO: Register the event loop with the TimedRobot
  }


  /**
   * Get a teleop event loop by name
   * 
   * @param name the name of the event loop
   * @return the event loop
   */
  public EventLoop getEventLoop(String name) {
    return teleopEventLoops.get(name);
  }

  /**
   * Create a new trigger which runs on a specified event loop, with an different timing from the main 20ms loop
   * 
   * @param name the name of the event loop
   * @param condition the condition for the trigger
   * @return the trigger
   */
  public Trigger altTrigger(String name, BooleanSupplier condition) {
    return new Trigger(getEventLoop(name), condition);
  }
}
