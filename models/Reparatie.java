package com.serviceauto.models;

public class Reparatie {

    private String lucrare;
    private String piese;
    private double cost;
    private String uzura;
    private String descriereAI;


    public Reparatie(String lucrare, String piese, double cost, String uzura) {
        this.lucrare = lucrare;
        this.piese = piese;
        this.cost = cost;
        this.uzura = uzura;
    }

    public String getLucrare() {
        return lucrare;
    }

    public void setLucrare(String lucrare) {
        this.lucrare = lucrare;
    }

    public String getPiese() {
        return piese;
    }

    public void setPiese(String piese) {
        this.piese = piese;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getUzura() {
        return uzura;
    }

    public void setUzura(String uzura) {
        this.uzura = uzura;
    }

    public void setDescriereAI(String descriereAI) {
        this.descriereAI = descriereAI;
    }

    public String getDescriereAI() {
        return descriereAI;
    }

    @Override
    public String toString() {
        return "Detaliile reparatiei  :\n" +
                "-tipul lucrarii este : " + lucrare + "\n" +
                "-piesele necesare sunt : " + piese + "\n" +
                "-costul lucrarii este : " + cost + "\n" +
                "-piesele folosite au fost : " + uzura ;
    }
}
