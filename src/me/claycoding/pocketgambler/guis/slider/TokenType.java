package me.claycoding.pocketgambler.guis.slider;

public enum TokenType {

    common(50.0), rare(150.0), epic(500.0);
    private double cost;

    TokenType(double cost) {
        this.cost = cost;
    }

    public double getCost(){
        return this.cost;
    }

}
