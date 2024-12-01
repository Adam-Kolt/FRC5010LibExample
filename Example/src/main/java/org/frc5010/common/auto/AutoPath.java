// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frc5010.common.auto;

import java.util.Optional;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;

/** Add your docs here. */
public class AutoPath {
    PathPlannerPath pathplannerPath;

    public AutoPath(PathPlannerPath pathplannerPath) {
        this.pathplannerPath = pathplannerPath;
    }

    /**
     * Construct a path from a pathplanner file
     * @param pathPlannerName The name of the pathplanner file
     * @return The path object
     */
    public static AutoPath PP(String pathPlannerName) {
        try {
            return new AutoPath(PathPlannerPath.fromPathFile(pathPlannerName));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("PathPlannerPath file " + pathPlannerName + " not found");
        }
    }

    /**
     * Construct a path from a choreo file
     * @param choreoTrajectory The name of the choreo file
     * @return The path object
     */
    public static AutoPath Choreo(String choreoTrajectory) {
        try {
            return new AutoPath(PathPlannerPath.fromChoreoTrajectory(choreoTrajectory));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Choreo trajectory file " + choreoTrajectory + " not found");
        }
    }

    /**
     * Construct a path from a choreo file with a split
     * @param choreoTrajectory The name of the choreo file
     * @param splitIndex The index of the split in the choreo file
     * @return The path object
     */
    public static AutoPath Choreo(String choreoTrajectory, int splitIndex) {
        try {
            return new AutoPath(PathPlannerPath.fromChoreoTrajectory(choreoTrajectory, splitIndex));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Choreo trajectory file " + choreoTrajectory + " not found");
        }
    }

    /**
     * gets a PathPlannerPath object from the AutoPath
     * 
     * @return the PathPlannerPath object
     */
    public PathPlannerPath getPathPlannerPath() {
        return pathplannerPath;
    }

    /**
     * gets a command which can be used to follow the path
     * 
     * @return the command that follows the path
     */
    public Command follow() {
        Command followingCommand = AutoBuilder.followPath(pathplannerPath);
        followingCommand.setName("Follow Path " + pathplannerPath.name);
        return followingCommand;
    }

    /**
     * gets a command which can be used to reset the robot's odometry to the start of the path
     * 
     * @return the command that resets the odometry
     */
    public Command resetOdometryToStart() {
        Optional<Pose2d> startPose = pathplannerPath.getStartingHolonomicPose();
        if (startPose.isPresent()) {
            Command poseResetCommand = AutoBuilder.resetOdom(startPose.get());
            poseResetCommand.setName("Reset Odometry to Start of Path " + pathplannerPath.name);
            return poseResetCommand;
        }
        return null;
    }

}
