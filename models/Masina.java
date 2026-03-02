package com.serviceauto.models;

public class Masina {

    private int id;
    private String nr;
    private String marca;
    private String model;
    int proprietarId;

    public Masina(String marca , String model , int proprietarId , String numar){
        this.nr=numar;
        this.marca=marca;
        this.model=model;
        this.proprietarId=proprietarId;
    }

    // Constructor pentru citire din DB
    public Masina(int id, String marca, String model, int proprietarId, String numar){
        this.id = id;
        this.nr = numar;
        this.marca = marca;
        this.model = model;
        this.proprietarId = proprietarId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNR() {
        return nr;
    }
    public void setNR(String NR) {
        this.nr = NR;
    }

    public String getMarca() {
        return marca;
    }
    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }

    public int getProprietarId()
    {
        return proprietarId;
    }

    public void setProprietarId(int proprietarId){
        this.proprietarId = proprietarId;
    }

    @Override
    public String toString() {
        return "Detaliile masinii :\n" +
                "-Marca: " + marca + "\n" +
                "-Prenume: " + model + "\n" +
                "-Proprietarul (ID): " + proprietarId + "\n" +
                "-Numarul de inmatriculare: '" + nr + '\'';
    }
}
