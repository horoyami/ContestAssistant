package org.horoyami.contestassistant;

import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by victor on 2/27/18.
 */

public class Task implements Serializable{

    private int step = 0;
    private final int MAX_STEPS = 7;
    private final ArrayList<Integer> steps_t = new ArrayList<>();
    private final ArrayList<Integer> steps_a = new ArrayList<>();


    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        if (step >= MAX_STEPS)
            this.step = MAX_STEPS - 1;
        else
            this.step = step;
    }

    public void incStep() {
        setStep(step + 1);
    }

    public void add(int time, int step) {
        steps_t.add(time);
        steps_a.add(step);
    }

    public int getTimeStep(int i) {
        return steps_t.get(i);
    }

    public int getActStep(int i) {
        return steps_a.get(i);
    }

    public int sizeStep() {
        return steps_t.size();
    }
}
