// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frc5010.common.motors.function;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.simulation.BatterySim;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;
import edu.wpi.first.wpilibj.simulation.RoboRioSim;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import edu.wpi.first.wpilibj.util.Color8Bit;
import org.frc5010.common.constants.RobotConstantsDef;
import org.frc5010.common.motors.MotorController5010;
import org.frc5010.common.motors.MotorFactory;
import org.frc5010.common.sensors.encoder.GenericEncoder;
import org.frc5010.common.sensors.encoder.SimulatedEncoder;
import org.frc5010.common.telemetry.DisplayDouble;

/** Add your docs here. */
public class VelocityControlMotor extends GenericControlledMotor {
  protected MechanismLigament2d speedometer;
  protected MechanismLigament2d setpoint;
  protected MechanismRoot2d root;
  protected FlywheelSim simMotor;
  protected SimulatedEncoder simEncoder;
  protected GenericEncoder encoder;
  protected DisplayDouble reference =
      new DisplayDouble(0, "reference", VelocityControlMotor.class.getSimpleName());
  protected DisplayDouble velocity =
      new DisplayDouble(0, "velocity", VelocityControlMotor.class.getSimpleName());
  protected DisplayDouble control =
      new DisplayDouble(0, "control", VelocityControlMotor.class.getSimpleName());

  public VelocityControlMotor(MotorController5010 motor) {
    super(motor);
    encoder = _motor.getMotorEncoder();
  }

  public VelocityControlMotor setupSimulatedMotor(double gearing, double jKgMetersSquared) {
    simMotor = new FlywheelSim(_motor.getMotorSimulationType(), gearing, jKgMetersSquared);
    simEncoder =
        new SimulatedEncoder(
            MotorFactory.getNextSimEncoderPort(), MotorFactory.getNextSimEncoderPort());
    return this;
  }

  @Override
  public VelocityControlMotor setVisualizer(
      Mechanism2d visualizer, Pose3d robotToMotor, String visualName) {
    super.setVisualizer(visualizer, robotToMotor, visualName);

    root =
        visualizer.getRoot(
            _visualName,
            robotToMotor.getX() * RobotConstantsDef.robotVisualH,
            robotToMotor.getZ() * RobotConstantsDef.robotVisualV);
    speedometer =
        new MechanismLigament2d(
            visualName + "-velocity", 5, 0, 5, new Color8Bit(MotorFactory.getNextVisualColor()));
    setpoint =
        new MechanismLigament2d(
            visualName + "-setpoint", 5, 0, 5, new Color8Bit(MotorFactory.getNextVisualColor()));
    root.append(speedometer);
    root.append(setpoint);
    return this;
  }

  @Override
  public void draw() {
    double currentVelocity = 0;
    if (RobotBase.isReal()) {
      currentVelocity = encoder.getVelocity();
    } else {
      currentVelocity = simEncoder.getVelocity();
    }
    velocity.setValue(currentVelocity);
    reference.setValue(getReference());
    speedometer.setAngle(currentVelocity / _motor.getMaxRPM() * 180);
    setpoint.setAngle(getReference() / _motor.getMaxRPM() * 180);
  }

  @Override
  public void simulationUpdate() {
    control.setValue(calculateControlEffort(simEncoder.getVelocity()));
    simMotor.setInput(control.getValue());
    simMotor.update(0.020);
    simEncoder.setRate(simMotor.getAngularVelocityRPM());
    RoboRioSim.setVInVoltage(
        BatterySim.calculateDefaultBatteryLoadedVoltage(simMotor.getCurrentDrawAmps()));
  }
}
