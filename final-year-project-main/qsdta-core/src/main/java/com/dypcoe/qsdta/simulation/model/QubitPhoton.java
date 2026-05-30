package com.dypcoe.qsdta.simulation.model;

public class QubitPhoton {
    private int state;
    private String basis;

    public QubitPhoton(int state, String basis) {
        this.state = state;
        this.basis = basis;
    }

    public int getState() {
        return this.state;
    }

    public String getBasis() {return this.basis;}
}
