// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frc5010.common.auto;

import static edu.wpi.first.units.Units.Meters;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.units.measure.Distance;

/** Add your docs here. */
public class GraphNode {
    private int id;
    private String name;
    private Translation2d position;
    private Map<Integer, GraphConnection> connections = new HashMap<>();

    public GraphNode(int id, String name, Translation2d position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Translation2d getPosition() {
        return position;
    }

    public Distance distanceTo(Translation2d other) {
        return Meters.of(position.getDistance(other));
    }

    public void addConnection(GraphConnection connection) {
        connections.put(connection.end.id, connection);
    }

    public GraphConnection getConnection(int endId) {
        return connections.get(endId);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<Integer, GraphConnection> getConnections() {
        return connections;
    }
}
