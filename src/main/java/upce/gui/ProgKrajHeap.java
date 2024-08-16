package upce.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import upce.abstrHeap.eTypComp;
import upce.abstrTreeEnum.eTypProhl;
import upce.ads.KolekceException;
import upce.obec.FrontaObec;
import upce.obec.Obec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

public class ProgKrajHeap extends Application {

    private ListView<Obec> listView;
    private FrontaObec frontaObec;

    private final ComboBox<eTypComp> comparator = new ComboBox<>();

    private final ComboBox<eTypProhl> typeProhl = new ComboBox<>();


    @Override
    public void start(Stage primaryStage) throws Exception {

        comparator.getItems().addAll(eTypComp.values());
        comparator.setValue(eTypComp.BY_NAME);
        comparator.setOnAction(e -> frontaObec.setComparator(comparator.getValue()));

        typeProhl.getItems().addAll(eTypProhl.values());
        typeProhl.setValue(eTypProhl.DO_SIRZKY);

        frontaObec = new FrontaObec();

        listView = new ListView<>();
        listView.setPrefSize(800, 600);

        Button btnGenerujObec = new Button("GenerujObec");
        btnGenerujObec.setOnAction(e -> {
            try {
                handleGenerujObec();
            } catch (KolekceException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button btnUlozData = new Button("UlozData");
        btnUlozData.setOnAction(e -> handleUlozData());

        Button btnImportData = new Button("ImportData");
        btnImportData.setOnAction(e -> handleImportData());

        Button btnVloz = new Button("Vloz");
        btnVloz.setOnAction(e -> handleVloz());

        Button btnReorganizace = new Button("Reorganizace");
        btnReorganizace.setOnAction(e -> handleReorganizace());

        Button btnOdeberMax = new Button("OdeberMax");
        btnOdeberMax.setOnAction(e -> {
            try {
                handleOdeberMax();
            } catch (KolekceException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button btnZpristupniMax = new Button("ZpristupniMax");
        btnZpristupniMax.setOnAction(e -> {
            try {
                handleZpristupniMax();
            } catch (KolekceException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button btnZrus = new Button("Zrus");
        btnZrus.setOnAction(e -> {
            frontaObec.zrus();
            listView.getItems().clear();
        });

        VBox vbox = new VBox(10, btnGenerujObec, btnVloz, comparator, typeProhl, btnReorganizace, btnOdeberMax, btnZpristupniMax, btnZrus, btnUlozData, btnImportData);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.TOP_LEFT);

        HBox root = new HBox(10, vbox, listView);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER_LEFT);

        Scene scene = new Scene(root, 1000, 600);

        primaryStage.setTitle("Sprava Obec");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void handleUlozData() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Data");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) {
            try {
                frontaObec.ulozData(selectedFile.getAbsolutePath());
                showAlert1("Data Saved", "Data was successfully saved to " + selectedFile.getName(), Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert1("Error", "Error occurred while saving data: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }

    }

    private void handleImportData() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Vyberte soubor pro import");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            importDataFromFile(file);
        }

    }

    private void importDataFromFile(File file) {
        listView.getItems().clear();
        frontaObec.zrus();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int id = Integer.parseInt(values[0]);
                int psc = Integer.parseInt(values[1]);
                String obec = values[2];
                int pocetMuzu = Integer.parseInt(values[3]);
                int pocetZen = Integer.parseInt(values[4]);
                int celkem = Integer.parseInt(values[5]);

                Obec novaObec = new Obec(id, psc, obec, pocetMuzu, pocetZen, celkem);
                frontaObec.vloz(novaObec);
                listView.getItems().add(novaObec);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Chyba pri cteni souboru: " + e.getMessage());
            errorAlert.showAndWait();
        }
    }

    private void showAlert1(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void handleZpristupniMax() throws KolekceException {
        Obec temp = frontaObec.zpristupniMax();
        //listViewUdpate();
        listView.getSelectionModel().select(temp);
    }

    private void handleReorganizace() {
            frontaObec.reorganizace();
            updateListView(typeProhl.getValue());
            if (typeProhl.getValue() == eTypProhl.DO_SIRZKY) {
                listViewUdpate();
            }
    }

    private void updateListView(eTypProhl typProhl) {
        listView.getItems().clear();
        Iterator<Obec> frontaIterator;
        frontaIterator = frontaObec.vytvorIterator(typProhl);
        while (frontaIterator.hasNext()) {
            listView.getItems().add(frontaIterator.next());
        }
    }

    private void handleOdeberMax() throws KolekceException {
        //Obec temp = frontaObec.odeberMax();
        //listView.getItems().remove(temp);

        Obec temp = frontaObec.odeberMax();
        listView.getItems().remove(temp);
        //listViewUdpate();

    }



    private void handleVloz() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nový Kraj");
        dialog.setHeaderText("Zadejte údaje pro nový kraj");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField pscField = new TextField();
        TextField obecField = new TextField();
        TextField pocetMuzuField = new TextField();
        TextField pocetZenField = new TextField();

        grid.add(new Label("PSC:"), 0, 0);
        grid.add(pscField, 1, 0);
        grid.add(new Label("Obec:"), 0, 1);
        grid.add(obecField, 1, 1);
        grid.add(new Label("Počet mužů:"), 0, 2);
        grid.add(pocetMuzuField, 1, 2);
        grid.add(new Label("Počet žen:"), 0, 3);
        grid.add(pocetZenField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(response -> {
            int psc = Integer.parseInt(pscField.getText());
            String obec = obecField.getText();
            int pocetMuzu = Integer.parseInt(pocetMuzuField.getText());
            int pocetZen = Integer.parseInt(pocetZenField.getText());

            Obec temp = new Obec(psc, psc, obec, pocetMuzu, pocetZen, pocetMuzu + pocetZen);
            frontaObec.vloz(temp);
            listView.getItems().add(temp);
        });
    }

    private void handleGenerujObec() throws KolekceException{
        TextInputDialog dialog = new TextInputDialog("10");
        dialog.setTitle("Generování obcí");
        dialog.setHeaderText("Zadejte počet obcí k vygenerování");
        dialog.setContentText("Počet obcí:");

        dialog.showAndWait().ifPresent(response -> {
            int count;
            try {
                count = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                showAlert("Chyba", "Zadali jste neplatné číslo!");
                return;
            }

            listView.getItems().clear();
            frontaObec.zrus();

            for (int i = 0; i < count; i++) {
                Obec generated = null;
                try {
                    generated = frontaObec.generuj();
                } catch (KolekceException e) {
                    throw new RuntimeException(e);
                }
                listView.getItems().add(generated);
            }
            frontaObec.reorganizace();
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void listViewUdpate () {
        listView.getItems().clear();
        Iterator<Obec> iterator = frontaObec.vytvorIterator(eTypProhl.DO_SIRZKY);
        while(iterator.hasNext()) {
            listView.getItems().add(iterator.next());
        }
    }

}
