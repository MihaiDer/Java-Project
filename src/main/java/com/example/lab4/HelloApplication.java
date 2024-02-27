package com.example.lab4;
import Domain.Inchiriere;
import Domain.Masina;
import Exceptions.RepoException;
import Repository.*;
import Service.Service;
import UI.UI;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws RepoException {
        TextField idTextField = new TextField();
        TextField brandTextField = new TextField();
        TextField modelTextField = new TextField();
        SQLRepositoryMasina sqlRepositoryMasina = new SQLRepositoryMasina();
        SQLRepositoryInchiriere sqlRepositoryInchiriere = new SQLRepositoryInchiriere();
        Service service = new Service(sqlRepositoryMasina, sqlRepositoryInchiriere);
        VBox carsVerticalBox = new VBox();
        carsVerticalBox.setPadding(new Insets(10));
        ObservableList<Masina> cars = FXCollections.observableArrayList(service.getAllCars());
        ListView<Masina> listView = new ListView<>(cars);
        listView.setOnMouseClicked(mouseEvent -> {
            Masina car = listView.getSelectionModel().getSelectedItem();
            idTextField.setText(Integer.toString(car.getId()));
            brandTextField.setText(car.getMarca());
            modelTextField.setText(car.getModel());
        });
        carsVerticalBox.getChildren().add(listView);
        GridPane carsGridPane = new GridPane();
        Label idLabel = new Label();
        idLabel.setText("Id: ");
        idLabel.setPadding(new Insets(10, 5, 10, 0));
        Label brandLabel = new Label();
        brandLabel.setText("Brand:");
        brandLabel.setPadding(new Insets(10, 5, 10, 0));
        Label modelLabel = new Label();
        modelLabel.setText("Model:");
        modelLabel.setPadding(new Insets(10, 5, 10, 0));
        carsGridPane.add(idLabel, 0, 0);
        carsGridPane.add(idTextField, 1 , 0);
        carsGridPane.add(brandLabel, 0, 1);
        carsGridPane.add(brandTextField, 1, 1);
        carsGridPane.add(modelLabel,0,2);
        carsGridPane.add(modelTextField, 1, 2);
        carsVerticalBox.getChildren().add(carsGridPane);
        HBox buttonsHorizontalBox = new HBox();
        carsVerticalBox.getChildren().add(buttonsHorizontalBox);
        Button addButton = new Button("Add car");
        addButton.setOnMouseClicked(mouseEvent -> {
            try {
                int id = Integer.parseInt(idTextField.getText());
                String brand = brandTextField.getText();
                String model = modelTextField.getText();
                service.addCar(id, brand, model);
            } catch (Exception e) {
                Alert errorPopUp = new Alert(Alert.AlertType.ERROR);
                errorPopUp.setTitle("Error");
                errorPopUp.setContentText(e.getMessage());
                errorPopUp.show();
            }
            cars.setAll(service.getAllCars());
        });
        buttonsHorizontalBox.getChildren().add(addButton);
        Button updateButton = new Button("Update car");
        updateButton.setOnMouseClicked(mouseEvent -> {
            try {
                int id = Integer.parseInt(idTextField.getText());
                String brand = brandTextField.getText();
                String model = modelTextField.getText();
                service.modifyCar(id, brand, model);
                cars.setAll(service.getAllCars());
            } catch (Exception e) {
                Alert errorPopUp = new Alert(Alert.AlertType.ERROR);
                errorPopUp.setTitle("Error");
                errorPopUp.setContentText(e.getMessage());
                errorPopUp.show();
            }
        });
        buttonsHorizontalBox.getChildren().add(updateButton);
        Button deleteButton = new Button("Delete car");
        deleteButton.setOnMouseClicked(mouseEvent -> {
            try {
                int id = Integer.parseInt(idTextField.getText());
                service.deleteCar(id);
                cars.setAll(service.getAllCars());
            } catch (Exception e) {
                Alert errorPopUp = new Alert(Alert.AlertType.ERROR);
                errorPopUp.setTitle("Error");
                errorPopUp.setContentText(e.getMessage());
                errorPopUp.show();
            }
        });
        buttonsHorizontalBox.getChildren().add(deleteButton);
        TextField rentalIdTextField = new TextField();
        TextField carIdTextField = new TextField();
        TextField marcaTextField = new TextField();
        TextField modellTextField = new TextField();
        TextField startDateTextField = new TextField();
        TextField endDateTextField = new TextField();

        VBox rentalsVerticalBox = new VBox();
        rentalsVerticalBox.setPadding(new Insets(10));

        ObservableList<Inchiriere> rentals = FXCollections.observableArrayList(service.getAllRentals());
        ListView<Inchiriere> rentalListView = new ListView<>(rentals);
        rentalListView.setOnMouseClicked(mouseEvent -> {
            Inchiriere rental = rentalListView.getSelectionModel().getSelectedItem();
            rentalIdTextField.setText(Integer.toString(rental.getId()));
            carIdTextField.setText(Integer.toString(rental.getCar().getId()));
            marcaTextField.setText(rental.getCar().getMarca());
            modellTextField.setText(rental.getCar().getModel());
            startDateTextField.setText(rental.getDateIn());
            endDateTextField.setText(rental.getDateOut());
        });
        rentalsVerticalBox.getChildren().add(rentalListView);
        GridPane rentalsGridPane = new GridPane();
        Label rentalIdLabel = new Label("Rental Id:");
        rentalIdLabel.setPadding(new Insets(10, 5, 10, 0));
        Label carIdLabel = new Label("Car Id:");
        carIdLabel.setPadding(new Insets(10, 5, 10, 0));
        Label carMarcaLabel = new Label("Car brand:");
        carMarcaLabel.setPadding(new Insets(10, 5, 10, 0));
        Label carModelLabel = new Label("Car model:");
        carModelLabel.setPadding(new Insets(10, 5, 10, 0));
        Label startDateLabel = new Label("Start Date:");
        startDateLabel.setPadding(new Insets(10, 5, 10, 0));
        Label endDateLabel = new Label("End Date:");
        endDateLabel.setPadding(new Insets(10, 5, 10, 0));

        rentalsGridPane.add(rentalIdLabel, 0, 0);
        rentalsGridPane.add(rentalIdTextField, 1, 0);
        rentalsGridPane.add(carIdLabel, 0, 1);
        rentalsGridPane.add(carIdTextField, 1, 1);
        rentalsGridPane.add(carMarcaLabel, 0, 2);
        rentalsGridPane.add(marcaTextField, 1, 2);
        rentalsGridPane.add(carModelLabel, 0, 3);
        rentalsGridPane.add(modellTextField, 1, 3);
        rentalsGridPane.add(startDateLabel, 0, 4);
        rentalsGridPane.add(startDateTextField, 1, 4);
        rentalsGridPane.add(endDateLabel, 0, 5);
        rentalsGridPane.add(endDateTextField, 1, 5);

        rentalsVerticalBox.getChildren().add(rentalsGridPane);

        HBox rentalButtonsHorizontalBox = new HBox();
        rentalsVerticalBox.getChildren().add(rentalButtonsHorizontalBox);

        Button addRentalButton = new Button("Add Rental");
        addRentalButton.setOnMouseClicked(mouseEvent -> {
            try {
                int rentalId = Integer.parseInt(rentalIdTextField.getText());
                int carId = Integer.parseInt(carIdTextField.getText());
                String brand = marcaTextField.getText();
                String model = modellTextField.getText();
                String startDate = startDateTextField.getText();
                String endDate = endDateTextField.getText();
                Masina m = new Masina(carId, brand, model);
                service.addInchiriere(rentalId, m, startDate, endDate);
            } catch (Exception e) {
                Alert errorPopUp = new Alert(Alert.AlertType.ERROR);
                errorPopUp.setTitle("Error");
                errorPopUp.setContentText(e.getMessage());
                errorPopUp.show();
            }
            rentals.setAll(service.getAllRentals());
        });
        rentalButtonsHorizontalBox.getChildren().add(addRentalButton);
        Button updateRentalButton = new Button("Update Rental");
        updateRentalButton.setOnMouseClicked(mouseEvent -> {
            try {
                int rentalId = Integer.parseInt(rentalIdTextField.getText());
                int carId = Integer.parseInt(carIdTextField.getText());
                String brand = marcaTextField.getText();
                String model = modellTextField.getText();
                String startDate = startDateTextField.getText();
                String endDate = endDateTextField.getText();
                Masina m = new Masina(carId, brand, model);
                service.modifyInchiriere(rentalId, m, startDate, endDate);
                rentals.setAll(service.getAllRentals());
            } catch (Exception e) {
                Alert errorPopUp = new Alert(Alert.AlertType.ERROR);
                errorPopUp.setTitle("Error");
                errorPopUp.setContentText(e.getMessage());
                errorPopUp.show();
            }
        });

        rentalButtonsHorizontalBox.getChildren().add(updateRentalButton);

        Button deleteRentalButton = new Button("Delete Rental");
        deleteRentalButton.setOnMouseClicked(mouseEvent -> {
            try {
                int rentalId = Integer.parseInt(rentalIdTextField.getText());
                service.deleteInchiriere(rentalId);
                rentals.setAll(service.getAllRentals());
            } catch (Exception e) {
                Alert errorPopUp = new Alert(Alert.AlertType.ERROR);
                errorPopUp.setTitle("Error");
                errorPopUp.setContentText(e.getMessage());
                errorPopUp.show();
            }
        });
        rentalButtonsHorizontalBox.getChildren().add(deleteRentalButton);
        TextArea reportsTextArea = new TextArea();
        reportsTextArea.setEditable(false);
        Button mostRentedCarsReportButton = new Button("Most Rented Cars Report");
        mostRentedCarsReportButton.setOnMouseClicked(mouseEvent -> service.generateMostRentedCarsReportToUI(reportsTextArea));
        Button printMostRentedCarsReportButton = new Button("Afisati masinile care au fost inchiriate cel mai mult timp");
        printMostRentedCarsReportButton.setOnMouseClicked((MouseEvent mouseEvent) -> service.generatePrintMostRentedCarsReportToUI(reportsTextArea));
        Button printRentalsPerMonthReportButton = new Button("Print Rentals Per Month Report");
        printRentalsPerMonthReportButton.setOnMouseClicked(mouseEvent -> service.generateRentalsPerMonthReportToUI(reportsTextArea));
        VBox reportsVBox = new VBox(
                mostRentedCarsReportButton,
                printMostRentedCarsReportButton,
                printRentalsPerMonthReportButton,
                reportsTextArea
        );
        VBox carsAndRentalsVBox = new VBox();
        carsAndRentalsVBox.getChildren().addAll(carsVerticalBox, rentalsVerticalBox);
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(carsAndRentalsVBox, reportsVBox);
        Scene scene = new Scene(splitPane, 900, 700);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws RepoException {
        Service service = null;
        String repositoryType = Settings.getRepositoryType();
        if ("binary".equals(repositoryType)) {
            IRepository<Masina> masinaRepository = new BinaryFileRepositoryMasina("masini.bin");
            IRepository<Inchiriere> inchiriereRepository = new BinaryFileRepositoryInchiriere("inchirieri.bin");
            service = new Service(masinaRepository, inchiriereRepository);
        } else if ("text".equals(repositoryType)) {
            IRepository<Masina> masinaRepository = new TextFileRepositoryMasina("masini.txt");
            IRepository<Inchiriere> inchiriereRepository = new TextFileRepositoryInchiriere("inchirieri.txt");
            service = new Service(masinaRepository, inchiriereRepository);
        }
        else if("SQL".equals(repositoryType)){
            SQLRepositoryMasina sqlRepositoryMasina = new SQLRepositoryMasina();
            SQLRepositoryInchiriere sqlRepositoryInchiriere = new SQLRepositoryInchiriere();
            service = new Service(sqlRepositoryMasina, sqlRepositoryInchiriere);
        }
        UI ui = new UI(service);
//        ui.run();
        launch();
    }
}