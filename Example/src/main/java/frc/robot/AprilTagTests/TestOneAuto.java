// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.AprilTagTests;

import org.frc5010.common.auto.AutoPath;
import org.frc5010.common.auto.AutoSequence;

/** Add your docs here. */
public class TestOneAuto extends AutoSequence {

    public TestOneAuto() {

        AutoPath circle = AutoPath.PP("Circle");

        addCommands(
            circle.resetOdometryToStart(),
            circle.follow().repeatedly()
        );
    }
}
