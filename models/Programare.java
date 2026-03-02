package com.serviceauto.models;

import java.time.LocalTime;
import java.time.LocalDate;

public class Programare {

    private LocalTime time ;
    private LocalDate data ;

    public Programare (int An , int Luna , int Zi , int OraT , int MinT){
        this.time = LocalTime.of(OraT,MinT);
        this.data = LocalDate.of(An,Luna,Zi);
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Detaliile programarii tale :\n" +
                "-Data: " + data + "\n" +
                "-Ora: " + time + "\n";
    }
}
