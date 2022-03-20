package com.example.letscheck;

public class Trial {
    private String name;
    private int id;
    private double correct;
    private double wrong;
    private double net;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public double getCorrect() {
        return correct;
    }

    public double getWrong() {
        return wrong;
    }

    public double getNet() {
        return net;
    }

    public Trial(String name, int id, double correct, double wrong) {
        this.name = name;
        this.id = id;
        this.correct = correct;
        this.wrong = wrong;
    }

    public Trial(String name, double correct, double wrong) {
        this.name = name;
        this.correct = correct;
        this.wrong = wrong;
    }

    public Trial(String name, int id) {
        this.name = name;
        this.id = id;
    }
}
