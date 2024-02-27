package UI;

import Domain.Masina;
import Exceptions.RepoException;
import Service.Service;

import java.text.ParseException;
import java.util.Scanner;

public class UI {
    private final Service service;
    public UI(Service service){
        this.service = service;
    }

    public void printMenu(){
        System.out.println("0.Exit");
        System.out.println("1.Adauga o masina");
        System.out.println("2.Modifica o masina");
        System.out.println("3.Stergeti o masina");
        System.out.println("4.Cititi lista de masini");
        System.out.println("5.Adaugati o inchiriere");
        System.out.println("6.Modificati o inchiriere");
        System.out.println("7.Stergeti o inchiriere");
        System.out.println("8.Cititi lista de inchirieri");
        System.out.println("9.Afisati cele mai inchiriate masini");
        System.out.println("10.Afisati masinile care au fost inchiriate cel mai mult timp");
        System.out.println("11.Afisati inchirierile in functie de luna");
    }

    public void uiAddCar(){
        System.out.println("Introduceti un id:");
        String myBrand;
        String myModel;
        Scanner sc = new Scanner(System.in);
        int myId = sc.nextInt();
        System.out.println("Introduceti un brand:");
        myBrand = sc.next();
        System.out.println("Introduceti un model:");
        myModel= sc.next();
        try{
            service.addCar(myId, myBrand, myModel);
        }
        catch (RepoException repoException){
            System.out.println(repoException.getMessage());
        }
    }

    public void uiPrintMostRentedCars(){
        this.service.mostRentedCars();
    }

    public void uiPrintMostRentedCarsByTime(){
        this.service.printMostRentedCarsReport();
    }
    public void UIprintRentalsPerMonthReport(){
        this.service.printRentalsPerMonthReport();
    }

    public void uiAddARent() {
        Scanner sc = new Scanner(System.in);
        int id;
        String dateIn;
        String dateOut;
        System.out.println("Introduceti id-ul inchirierii:");
        id = sc.nextInt();
        int idCar;
        String myBrand;
        String myModel;
        System.out.println("Introduceti un id:");
        idCar = sc.nextInt();
        System.out.println("Introduceti un brand:");
        myBrand = sc.next();
        System.out.println("Introduceti un model:");
        myModel = sc.next();
        Masina car = new Masina(idCar, myBrand, myModel);
        System.out.println("Introduceti data de inceput a inchirierii:");
        dateIn = sc.next();
        System.out.println("Introduceti data de sfarsit a inchirierii:");
        dateOut = sc.next();
        try {
            service.addInchiriere(id, car, dateIn, dateOut);

        } catch (RepoException | ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    public void uiModifyCar(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduceti id-ul masinii de modificat:");
        int id = sc.nextInt();
        System.out.println("Introduceti noul brand:");
        String myBrand = sc.next();
        System.out.println("Introduceti noul model:");
        String myModel = sc.next();
        try{
            service.modifyCar(id, myBrand, myModel);
        }
        catch (RepoException repoException){
            System.out.println(repoException.getMessage());
        }
    }

    public void uiModifyARent(){
        Scanner sc = new Scanner(System.in);
        int id;
        String dateIn;
        String dateOut;
        System.out.println("Introduceti id-ul inchirierii pe care doriti sa o modificati:");
        id = sc.nextInt();
        int idCar;
        String myBrand;
        String myModel;
        System.out.println("Introduceti id-ul masinii:");
        idCar = sc.nextInt();
        System.out.println("Introduceti brand-ul masinii");
        myBrand = sc.next();
        System.out.println("Introduceti modelul masinii:");
        myModel = sc.next();
        Masina car = new Masina(idCar, myBrand, myModel);
        System.out.println("Introduceti noua data de inceput a inchirierii:");
        dateIn = sc.next();
        System.out.println("Introduceti noua data de sfarsit a inchirierii:");
        dateOut = sc.next();
        try {
            service.modifyInchiriere(id, car, dateIn, dateOut);

        } catch (RepoException e) {
            System.out.println(e);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void uiDeleteCar(){
        System.out.println("Introduceti id-ul masinii pe care doriti sa o stergeti");
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        try{
            service.deleteCar(id);
        }
        catch (RepoException repoException){
            System.out.println(repoException.getMessage());
        }
    }

    public void uiDeleteRent(){
        System.out.println("Introduceti id-ul inchirierii pe care doriti sa o stergeti");
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        try {
            service.deleteInchiriere(id);
        }
        catch (RepoException re){
            System.out.println(re.getMessage());
        }
    }

    public void uiPrintCars(){
        service.printCars();
    }

    public void uiPrintRents(){
        service.printRents();
    }

    public void run(){
        int n = -1;
        Scanner sc = new Scanner(System.in);
        while (n != 0){
            printMenu();
            System.out.println("Introduceti comanda aleasa:");
            n = sc.nextInt();
            if(n == 1){
                uiAddCar();
            }
            if(n == 2){
                uiModifyCar();
            }
            if(n == 3){
                uiDeleteCar();
            }
            if(n == 4){
                uiPrintCars();
            }
            if(n == 5){
                uiAddARent();
            }
            if(n == 6){
                uiModifyARent();
            }
            if(n == 7)
                uiDeleteRent();
            if(n == 8)
                uiPrintRents();
            if(n == 9)
                uiPrintMostRentedCars();
            if(n == 10)
                uiPrintMostRentedCarsByTime();
            if(n == 11)
                UIprintRentalsPerMonthReport();
        }
    }
}