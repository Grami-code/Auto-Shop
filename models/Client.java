package com.serviceauto.models;


/**
 * Reprezinta un client al service-ului auto.
 *
 * Clasa Client stocheaza informatii despre un client,
 * precum numele, prenumele, email-ul si ID-ul unic
 * generat automat de baza de date.
 */

public class Client {

    private int id;
    private String nume;
    private String prenume;
    private String email;

    // Pentru client nou, înainte să fie introdus în DB
    public Client(String nume, String prenume, String email){
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
    }

    // Pentru client preluat din DB (cu ID)
    public Client(int id, String nume, String prenume, String email){
        this.id = id;
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Detaliile clientului :\n" +
                "-ID: " + id + "\n" +
                "-Nume: " + nume + "\n" +
                "-Prenume: " + prenume + "\n" +
                "-Email: " + email + "\n";
    }
}
