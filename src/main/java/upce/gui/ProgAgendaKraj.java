package upce.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import upce.ads.KolekceException;
import upce.obec.Obec;
import upce.obec.SpravaObec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ProgAgendaKraj extends Application {

    private ListView<Obec> listView;
    private SpravaObec spravaObec;

    @Override
    public void start(Stage primaryStage) {
        spravaObec = new SpravaObec();

        listView = new ListView<>();
        listView.setPrefSize(800, 600);

        // Кнопки
        Button btnNovyObec = new Button("NovyObec");
        btnNovyObec.setOnAction(e -> handleNovyObec());

        Button btnUlozData = new Button("UlozData");
        btnUlozData.setOnAction(e -> handleUlozData());

        Button btnImportData = new Button("ImportData");
        btnImportData.setOnAction(e -> handleImportData());



        Button btnGenerujObec = new Button("GenerujObec");
        btnGenerujObec.setOnAction(e -> {
            try {
                handleGenerujObec();
            } catch (KolekceException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button btnNajdiObec = new Button("NajdiObec");
        btnNajdiObec.setOnAction(e -> handleNajdiObec());

        Button btnOdeberObec = new Button("OdeberObec");
        btnOdeberObec.setOnAction(e -> {
            try {
                handleOdeberObec();
            } catch (KolekceException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button btnClear = new Button("Clear");
        btnClear.setOnAction(e -> {
            listView.getItems().clear();
        });

        VBox vbox = new VBox(10, btnNovyObec, btnUlozData, btnImportData, btnGenerujObec, btnNajdiObec, btnOdeberObec, btnClear);
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

    private void handleNovyObec() {
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
            try {
                spravaObec.vloz(temp);
            } catch (KolekceException e) {
                throw new RuntimeException(e);
            }
            listView.getItems().add(temp);
        });
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
                spravaObec.ulozData(selectedFile.getAbsolutePath());
                showAlert1("Data Saved", "Data was successfully saved to " + selectedFile.getName(), Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert1("Error", "Error occurred while saving data: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }

    }

    private void showAlert1(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
        spravaObec.zrus();
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
                spravaObec.vloz(novaObec);
                listView.getItems().add(novaObec);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Chyba pri cteni souboru: " + e.getMessage());
            errorAlert.showAndWait();
        } catch (KolekceException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void handleGenerujObec() throws KolekceException {
        listView.getItems().clear();
        spravaObec.zrus();

        Random rand = new Random();
        int count = rand.nextInt(100) + 1;

        for (int i = 0; i < count; i++) {
            Obec generated = spravaObec.generuj();
            listView.getItems().add(generated);
        }
    }

    private void handleNajdiObec() {
        // Логика для поиска общины
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Najdi Obec");
        dialog.setHeaderText("Vyhledání Obce");
        dialog.setContentText("Zadejte název obce:");

        dialog.showAndWait().ifPresent(name -> {
            Obec found = spravaObec.najdi(name);
            listView.getSelectionModel().select(found);
        });
    }

    private void handleOdeberObec() throws KolekceException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Odeber Obec");
        dialog.setHeaderText("Odstranění obce");
        dialog.setContentText("Zadejte název obce k odstranění:");

        dialog.showAndWait().ifPresent(name -> {
            try {
                Obec toRemove = spravaObec.najdi(name);
                if (toRemove != null) {
                    spravaObec.odeber(name);
                    listView.getItems().remove(toRemove);
                    Alert info = new Alert(Alert.AlertType.INFORMATION, "Obec " + name + " byla odstraněna.");
                    info.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Obec " + name + " nebyla nalezena.");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Chyba při odstraňování obce: " + e.getMessage());
                alert.showAndWait();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
