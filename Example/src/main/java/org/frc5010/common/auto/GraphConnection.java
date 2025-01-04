// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frc5010.common.auto;


/** Add your docs here. */
public class GraphConnection {
    public AutoPath path;
    public GraphNode start;
    public GraphNode end;
    
    public GraphConnection(AutoPath path, GraphNode start, GraphNode end) {
        this.path = path;
        this.start = start;
        this.end = end;
    }

}
