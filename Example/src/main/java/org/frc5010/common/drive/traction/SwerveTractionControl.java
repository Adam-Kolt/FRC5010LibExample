// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frc5010.common.drive.traction;

import java.util.function.Supplier;

import org.frc5010.common.arch.GenericSubsystem;
import org.frc5010.common.telemetry.DisplayDouble;

import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/** Add your docs here. */
public class SwerveTractionControl extends GenericSubsystem {
    // Drivetrain Wheel Measurements
    private Supplier<SwerveModuleState[]> swerveModuleStatesSupplier;
    private Supplier<ChassisSpeeds> externallyMeasuredSpeedsSupplier;
    private SwerveDriveKinematics swerveDriveKinematics;
    
    private DisplayDouble[] wheelSlipDisplays = new DisplayDouble[4];

    private LinearFilter[] wheelSlipFilters = new LinearFilter[4];



    public SwerveTractionControl(Supplier<SwerveModuleState[]> swerveModuleStatesSupplier, SwerveDriveKinematics swerveDriveKinematics,
            Supplier<ChassisSpeeds> externallyMeasuredSpeedsSupplier) {
        super();
        setupDisplays();
        this.swerveModuleStatesSupplier = swerveModuleStatesSupplier;
        this.externallyMeasuredSpeedsSupplier = externallyMeasuredSpeedsSupplier;
        this.swerveDriveKinematics = swerveDriveKinematics;

        for (int i = 0; i < wheelSlipFilters.length; i++) {
            wheelSlipFilters[i] = LinearFilter.movingAverage(5);
            wheelSlipFilters[i].reset();
        }
        
    }

    private void setupDisplays() {
        for (int i = 0; i < wheelSlipDisplays.length; i++) {
            wheelSlipDisplays[i] = displayValues.makeDisplayDouble("Wheel Slip " + i);
        }
    }

    private double calculateSlippage(double measuredSpeed, double predictedSpeed) {
        return Math.max(Math.abs(measuredSpeed / predictedSpeed) - 1, 0);
    }

    public double[] getWheelSlip() {
        ChassisSpeeds externallyMeasuredSpeeds = externallyMeasuredSpeedsSupplier.get();
        SwerveModuleState[] swerveModuleStates = swerveModuleStatesSupplier.get();
        SwerveModuleState[] predictedModuleStates = swerveDriveKinematics.toSwerveModuleStates(externallyMeasuredSpeeds);
        double[] wheelSlips = new double[swerveModuleStates.length];

        for (int i = 0; i < swerveModuleStates.length; i++) {
            wheelSlips[i] = wheelSlipFilters[i].calculate(calculateSlippage(swerveModuleStates[i].speedMetersPerSecond, predictedModuleStates[i].speedMetersPerSecond));
        }
        return wheelSlips;
    }

    @Override
    public void periodic() {
        double[] wheelSlips = getWheelSlip();
        for (int i = 0; i < wheelSlips.length; i++) {
            wheelSlipDisplays[i].setValue(wheelSlips[i]);
        }

        SmartDashboard.putNumberArray("Wheel Slips", wheelSlips);
    }

}
