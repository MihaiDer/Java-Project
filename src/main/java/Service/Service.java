package Service;

import Domain.*;
import Exceptions.*;
import Repository.IRepository;
import javafx.scene.control.TextArea;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Service{
    private final IRepository<Masina> masinaRepo;
    private final IRepository<Inchiriere> inchiriereRepo;
    public Service(IRepository<Masina> masinaRepo, IRepository<Inchiriere> inchiriereRepo){
        this.inchiriereRepo = inchiriereRepo;
        this.masinaRepo = masinaRepo;
    }
    public void addCar(int id, String marca, String model) throws RepoException {
        Masina m = new Masina(id, marca, model);
        masinaRepo.addEntity(m);
    }

    public void deleteCar(int id) throws RepoException{
        masinaRepo.deleteEntity(id);
        for(Inchiriere in: inchiriereRepo.getAll()){
            if(in.getCar().getId() == id)
                inchiriereRepo.deleteEntity(in.getId());
        }
    }

    public void modifyCar(int id, String marca, String model) throws RepoException{
        Masina m = new Masina(id, marca, model);
        masinaRepo.modifyEntity(id, m);
    }
    public List<Masina> getAllCars(){
        return this.masinaRepo.getAll();
    }
    public List<Inchiriere> getAllRentals(){
        return this.inchiriereRepo.getAll();
    }
    public void printCars(){
        for(int i = 0; i < masinaRepo.getSize(); i++){
            System.out.println(masinaRepo.getAt(i).toString());
        }
    }

    public boolean rentingIntervalCondition(Masina car, Date date1, Date date2) throws ParseException {
        if(inchiriereRepo.getSize() == 0)
            return true;
        for(int i = 0; i < inchiriereRepo.getSize(); i++){
            if(inchiriereRepo.getAt(i).getCar().getId()==car.getId()){
                Date d = new SimpleDateFormat("dd/MM/yyyy").parse(inchiriereRepo.getAt(i).getDateIn());
                Date d1 = new SimpleDateFormat("dd/MM/yyyy").parse(inchiriereRepo.getAt(i).getDateOut());
                if (!(date2.before(d) || date1.after(d1))) {
                    return false;
                }
            }
        }
        return true;
    }
    public void addInchiriere(int id, Masina car, String dateIn, String dateOut) throws ParseException, RepoException {
        Inchiriere in = new Inchiriere(id, car, dateIn, dateOut);
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(in.getDateIn());
        Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(in.getDateOut());
        if(masinaRepo.findById(in.getCar().getId())){
            if(rentingIntervalCondition(car, date1, date2))
                inchiriereRepo.addEntity(in);
            else
                throw new InchiriereDateException("Nu se poate adauga aceasta inchiriere in intervalul ales");
        }
        else{
            throw new RepoException("Masina cu id-ul dat nu exista in colectie");
        }
    }

    public void modifyInchiriere(int id, Masina car, String dateIn, String dateOut) throws ParseException, RepoException {
        if(inchiriereRepo.findById(id)){
            Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(dateIn);
            Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(dateOut);
            if(masinaRepo.findById(car.getId())){
                if(rentingIntervalCondition(car,date1,date2)){
                    Inchiriere in = new Inchiriere(id, car, dateIn, dateOut);
                    inchiriereRepo.modifyEntity(id, in);
                }
                else{
                    throw new InchiriereDateException("Nu se poate modifica aceasta inchiriere in intervalul ales");
                }
            }
            else
                throw new RepoException("Nu exista nici o masina cu id-ul dat");

        }
        else
            throw new RepoException("Nu exista nici o inchiriere cu id-ul dat");
    }

    public void deleteInchiriere(int id) throws RepoException {
        inchiriereRepo.deleteEntity(id);
    }

    public void printRents(){
        for(int i = 0; i < inchiriereRepo.getSize(); i++){
            System.out.println(inchiriereRepo.getAt(i).toString());
        }
    }

    public void mostRentedCars(){
        List<Inchiriere> rentals = inchiriereRepo.getAll();
        Map<Masina, Long> carRentalsCount = rentals.stream()
                .collect(Collectors.groupingBy(Inchiriere::getCar, Collectors.counting()));
        List<String> report = carRentalsCount.entrySet().stream()
                .sorted(Map.Entry.<Masina, Long>comparingByValue().reversed())
                .map(entry -> entry.getKey().toString() + " - " + entry.getValue() + " inchirieri")
                .toList();
        for (String s : report)
            System.out.println(s);
    }
    public void printMostRentedCarsReport() {
        List<Inchiriere> rentals = inchiriereRepo.getAll();
        Map<Masina, Long> carRentalDays = rentals.stream()
                .collect(Collectors.groupingBy(Inchiriere::getCar, Collectors.summingLong(this::calculateRentalDays)));
        List<Map.Entry<Masina, Long>> sortedCars = carRentalDays.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .toList();
        for (Map.Entry<Masina, Long> entry : sortedCars) {
            Masina car = entry.getKey();
            Long rentalDays = entry.getValue();
            System.out.println(car.toString() + " - " + rentalDays + " days rented");
        }
    }

    private long calculateRentalDays(Inchiriere rental) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date startDate = dateFormat.parse(rental.getDateIn());
            Date endDate = dateFormat.parse(rental.getDateOut());
            long diffInMilliseconds = Math.abs(endDate.getTime() - startDate.getTime());
            return diffInMilliseconds / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void printRentalsPerMonthReport() {
        List<Inchiriere> rentals = inchiriereRepo.getAll();

        Map<String, Long> rentalsPerMonth = rentals.stream()
                .collect(Collectors.groupingBy(this::extractMonth, Collectors.counting()));

        List<Map.Entry<String, Long>> sortedRentals = rentalsPerMonth.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .toList();

        for (Map.Entry<String, Long> entry : sortedRentals) {
            String month = entry.getKey();
            Long rentalCount = entry.getValue();
            System.out.println(month + " - " + rentalCount + " rentals");
        }
    }

    private String extractMonth(Inchiriere rental) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            Date startDate = dateFormat.parse(rental.getDateIn());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            String monthName = new SimpleDateFormat("MMMM").format(startDate);

            return monthName;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public void generateMostRentedCarsReportToUI(TextArea reportsTextArea) {
        List<Inchiriere> rentals = inchiriereRepo.getAll();
        Map<Masina, Long> carRentalsCount = rentals.stream()
                .collect(Collectors.groupingBy(Inchiriere::getCar, Collectors.counting()));
        List<String> report = carRentalsCount.entrySet().stream()
                .sorted(Map.Entry.<Masina, Long>comparingByValue().reversed())
                .map(entry -> entry.getKey().toString() + " - " + entry.getValue() + " inchirieri")
                .toList();
        appendReportToUI(report, reportsTextArea);
    }


    private void appendReportToUI(List<String> report, TextArea reportsTextArea) {
        reportsTextArea.clear();
        String formattedReport = String.join("\n", report);
        reportsTextArea.appendText(formattedReport + "\n");
    }

    public void generatePrintMostRentedCarsReportToUI(TextArea reportsTextArea) {
        List<Inchiriere> rentals = inchiriereRepo.getAll();
         Map<Masina, Long> carRentalDays = rentals.stream()
                 .collect(Collectors.groupingBy(Inchiriere::getCar, Collectors.summingLong(this::calculateRentalDays)));
         List<Map.Entry<Masina, Long>> sortedCars = carRentalDays.entrySet().stream()
                 .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                 .toList();
         List<String> report = sortedCars.stream()
                 .map(entry -> entry.getKey().toString() + " - " + entry.getValue() + " days rented")
                 .toList();
         appendReportToUI(report, reportsTextArea);
    }

    public void generateRentalsPerMonthReportToUI(TextArea reportsTextArea) {
        List<Inchiriere> rentals = inchiriereRepo.getAll();
         Map<String, Long> rentalsPerMonth = rentals.stream()
                 .collect(Collectors.groupingBy(this::extractMonth, Collectors.counting()));
         List<Map.Entry<String, Long>> sortedRentals = rentalsPerMonth.entrySet().stream()
                 .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                 .toList();
         List<String> report = sortedRentals.stream()
                 .map(entry -> entry.getKey() + " - " + entry.getValue() + " rentals")
                 .toList();
         appendReportToUI(report, reportsTextArea);
    }
}