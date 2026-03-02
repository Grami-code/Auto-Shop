package com.serviceauto.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.serviceauto.models.*;

/**
 * Clasa principala pentru gestionarea service-ului auto.
 *
 * Aceasta clasa gestioneaza listele de clienti, masini,
 * programari si reparatii. De asemenea, ofera meniul
 * interactv pentru a adauga clienti, masini, programari
 * si a afisa detalii despre clienti si masinile lor.
 */

public class ServiceAuto {

    Scanner scanner = new Scanner(System.in);
    private List<Client> clienti;
    private List<Masina> masini;
    private List<Programare> programari;
    private List<Reparatie> reparatii;

    public ServiceAuto() {
        // GRESEALA ANTERIOARA:
        // clienti = ClientService.getTotiClientii();
        // masini = MasinaService.getToateMasinile();
        // Daca functiile nu returnau corect ID-urile sau proprietarId,
        // "clientul nu exista" aparea.

        // VARIANTA CORECTA: citim toti clientii si masinile din DB la start
        clienti = ClientService.getTotiClientii();
        masini = MasinaService.getToateMasinile();
        programari = new ArrayList<>();
        reparatii = new ArrayList<>();
    }

    public void adaugaClient(Client c) {
        clienti.add(c);
    }

    public void adaugaMasina(Masina m) {
        masini.add(m);
    }

    public void adaugaProgramare(Programare p) {
        programari.add(p);
    }

    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("Introdu optiunea dorita");
            System.out.println("1. Introdu client nou");
            System.out.println("2. Introdu masina clientului");
            System.out.println("3. Creeaza o programare");
            System.out.println("4. Afiseaza detalii");
            System.out.println("5. Afiseaza reparatiile");
            System.out.println("0. Inchide programul");

            int optiune = Integer.parseInt(scanner.nextLine());
            System.out.println("\n");

            switch (optiune) {
                case 1:
                    System.out.println("Introdu numele");
                    String nume = scanner.nextLine();
                    System.out.println("Introdu prenume");
                    String prenume = scanner.nextLine();
                    System.out.println("Introdu email");
                    String email = scanner.nextLine();

                    Client c = new Client(nume, prenume, email);
                    int idClientDB = ClientService.adaugaClientInDB(c);
                    if(idClientDB > 0) {
                        c.setId(idClientDB);
                        clienti.add(c); // actualizam lista locala
                        System.out.println("Client adaugat cu ID: " + idClientDB);
                    } else {
                        System.out.println("Eroare la adaugarea clientului in DB!");
                    }
                    break;

                case 2:

                    System.out.println("Introdu marca masinii");
                    String marca = scanner.nextLine();
                    System.out.println("Introdu modelul masinii");
                    String model = scanner.nextLine();
                    System.out.println("Introdu id-ul proprietarului masinii");
                    int proprietarId = Integer.parseInt(scanner.nextLine());

                    // VERIFICARE: clientul exista in lista locala?
                    boolean clientExista = false;
                    for(Client cl : clienti) {
                        if(cl.getId() == proprietarId) {
                            clientExista = true;
                            break;
                        }
                    }

                    if(!clientExista) {
                        System.out.println("Nu exista client cu acest ID!");
                        break;
                    }

                    System.out.println("Introdu numarul de inmatriculare");
                    String numar = scanner.nextLine();

                    Masina m = new Masina(marca, model, proprietarId, numar);
                    int idMasinaDB = MasinaService.adaugaMasinaInDB(m);
                    if(idMasinaDB > 0) {
                        m.setId(idMasinaDB); // daca clasa Masina are camp id
                        masini.add(m); // actualizam lista locala
                        System.out.println("Masina adaugata cu ID: " + idMasinaDB);
                    } else {
                        System.out.println("Eroare la adaugarea masinii in DB!");
                    }
                    break;

                case 3:
                    System.out.println("Introdu ziua");
                    int ziua = Integer.parseInt(scanner.nextLine());
                    System.out.println("Introdu luna");
                    int luna = Integer.parseInt(scanner.nextLine());
                    System.out.println("Introdu anul");
                    int anul = Integer.parseInt(scanner.nextLine());
                    System.out.println("Introdu ora");
                    int ora = Integer.parseInt(scanner.nextLine());
                    System.out.println("Introdu minutele");
                    int minute = Integer.parseInt(scanner.nextLine());

                    Programare p = new Programare(anul, luna, ziua, ora, minute);
                    programari.add(p);
                    break;

                case 4:
                    System.out.println("Introdu id-ul clientului");
                    int clientId = Integer.parseInt(scanner.nextLine().trim());
                    boolean gasit = false;

                    for(Client cl : clienti) {
                        if(cl.getId() == clientId) {
                            gasit = true;
                            System.out.println("Detalii client: " + cl);
                            System.out.println("Masini detinute:");


                            for(Masina mas : masini) {
                                if(mas.getProprietarId() == clientId) {
                                    System.out.println(mas);
                                }
                            }
                            break;
                        }
                    }

                    if(!gasit) {
                        System.out.println("Acest client nu exista!");
                    }
                    break;

                case (5):
                    System.out.println("Titlul reparatiei:");
                    String titlu = scanner.nextLine();

                    System.out.println("Piese folosite:");
                    String piese = scanner.nextLine();

                    System.out.println("Cost total:");
                    double cost = Double.parseDouble(scanner.nextLine());

                    System.out.println("Status:");
                    String status = scanner.nextLine();

                    Reparatie rep = new Reparatie(titlu, piese, cost, status);

                    //introducere ai
                    String prompt = "Genereaza o descriere profesionala pentru reparatia: " + titlu +
                            ". Piesele folosite: " + piese +
                            ". Pret: " + cost + " lei";

                    String raspuns = OpenAIService.cereAI(prompt);

                    rep.setDescriereAI(raspuns);

                    reparatii.add(rep);

                    System.out.println("Descriere AI generata:");
                    System.out.println(raspuns);
                    break;

                case 0:
                    running = false;
                    break;

                default:
                    System.out.println("Optiune invalida!");
            }
        }
    }
}
