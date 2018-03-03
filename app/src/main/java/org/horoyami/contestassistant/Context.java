package org.horoyami.contestassistant;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by victor on 3/3/18.
 */

public class Context implements Serializable{

    private final String name;
    private final ArrayList<Task> tasks;
    private final int time;

    Context(String name, ArrayList<Task> tasks, int time) {
        this.name = name;
        this.tasks = tasks;
        this.time = time;
    }

    public String getName() {
        return name;
    }
}
