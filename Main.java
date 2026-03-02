package com.serviceauto;

import com.serviceauto.models.Client;
import com.serviceauto.models.Masina;
import com.serviceauto.models.Programare;
import com.serviceauto.models.Reparatie;
import com.serviceauto.services.ServiceAuto;

public class Main {
    public static void main(String[] args){

        /*
        Programare p = new Programare(2025, 4, 20, 12,30);
        System.out.println(p);
        */


        /*
        Masina bmw = new Masina("BMW","M5","MH29GRM");
        System.out.println(bmw);
        */

        /*
        Client alex = new Client("Popescu","Alexandru","alex.popescu@gmail.com", "0771539156");
        System.out.println(alex);
        */
        /*
        Reparatie rep1 = new Reparatie("Schimb ulei","Filtru ulei , 5L ulei",560.6,"Noi");
        System.out.println(rep1);
        */

        ServiceAuto serviceAuto = new ServiceAuto();
        serviceAuto.start();


    }
}
