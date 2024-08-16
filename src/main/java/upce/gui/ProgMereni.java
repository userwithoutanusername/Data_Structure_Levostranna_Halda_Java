package upce.gui;

import upce.mereni.*;
import upce.ads.KolekceException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

public class ProgMereni extends Application {
    SpravaMereni spravaMereni = new SpravaMereni();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mereni - Kopytin Makar");

        ListView<Mereni> listView = new ListView<>();
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Mereni actual = spravaMereni.zpristupniMereni(enumPozice.PRVNI);
                Mereni selected = listView.getSelectionModel().getSelectedItem();

                while (actual != selected && actual != null) {
                    actual = spravaMereni.zpristupniMereni(enumPozice.NASLEDNIK);
                }
            } catch (KolekceException e) {
                throw new RuntimeException(e);
            }

        });

        BorderPane root = new BorderPane();

        VBox leftMenu = new VBox();
        leftMenu.setSpacing(10);
        leftMenu.setPadding(new Insets(10, 10, 10, 10));

        Button btnSave = new Button("Uloz");
        btnSave.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                spravaMereni.ulozData("data.bin");
            } catch (KolekceException ex) {
                throw new RuntimeException(ex);
            }
        });
        Button btnLoad = new Button("Nacti");
        btnLoad.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                spravaMereni.nactiData("data.bin");
                listView.getItems().clear();
                for (Iterator<Mereni> it = spravaMereni.iterator(); it.hasNext(); ) {
                    Mereni mereni = it.next();
                    listView.getItems().add(mereni);
                }
            } catch (KolekceException ex) {
                throw new RuntimeException(ex);
            }
        });
        Button btnNext = new Button("Dalsi");
        btnNext.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                spravaMereni.zpristupniMereni(enumPozice.NASLEDNIK);
                listView.getSelectionModel().selectNext();
            } catch (KolekceException ex) {
                showError("Nemame dalsi mereni");
            }
        });
        Button btnPrevious = new Button("Predchozi");
        btnPrevious.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                spravaMereni.zpristupniMereni(enumPozice.PREDCHUDCE);
                listView.getSelectionModel().selectPrevious();
            } catch (KolekceException ex) {
                showError("Nemame predchozi mereni");
            }
        });
        Button btnFirst = new Button("Prvni");
        btnFirst.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                spravaMereni.zpristupniMereni(enumPozice.PRVNI);
                listView.getSelectionModel().selectFirst();
            } catch (KolekceException ex) {
                showError("Nemame zadne mereni");
            }
        });

        Button btnLast = new Button("Posledni");
        btnLast.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                spravaMereni.zpristupniMereni(enumPozice.POSLEDNI);
                listView.getSelectionModel().selectLast();
            } catch (KolekceException ex) {
                showError("Nemame zadne mereni");
            }
        });

        Button btnZrus = new Button("Zrus");
        btnZrus.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            spravaMereni.zrus();
            listView.getItems().clear();
        });

        Button btnGenerate = new Button("Generovat");
        btnGenerate.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            spravaMereni.zrus();
            listView.getItems().clear();
            int pocet = (int) (Math.floor(Math.random() * 100) + 1);

            for (int i = 0; i < pocet; i++) {
                Mereni mereni;
                LocalDateTime date = LocalDate.now().atStartOfDay().minusDays((long) Math.floor(Math.random() * 30));
                if (i % 2 == 0) {
                    mereni = new MereniElektrika(i, date, Math.floor(Math.random() * 100), Math.floor(Math.random() * 100));
                } else {
                    mereni = new MereniVoda(i, date, Math.floor(Math.random() * 100));
                }
                listView.getItems().add(mereni);
                try {
                    spravaMereni.vlozMereni(mereni, enumPozice.POSLEDNI);
                    spravaMereni.zpristupniMereni(enumPozice.POSLEDNI);
                } catch (KolekceException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        Button btnSmazAktualni = new Button("Smaz Aktualni");
        btnSmazAktualni.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                spravaMereni.odeberMereni(enumPozice.AKTUALNI);
                listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
            } catch (KolekceException ex) {
                throw new RuntimeException(ex);
            }
        });

        leftMenu.getChildren().addAll(btnSave, btnLoad, btnNext, btnPrevious, btnFirst, btnLast, btnZrus, btnGenerate, btnSmazAktualni);

        HBox bottomBox = new HBox();
        bottomBox.setSpacing(10);
        bottomBox.setPadding(new Insets(10, 10, 10, 10));
        bottomBox.setAlignment(Pos.CENTER);

        DatePicker datePickerFrom = new DatePicker();
        DatePicker datePickerTo = new DatePicker();
        Button btnMax = new Button("Maximalni");
        TextField txtFieldSearch = new TextField();
        txtFieldSearch.setPromptText("Senzor ID");
        btnMax.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            int id = Integer.parseInt(txtFieldSearch.getText());
            double vysledek = spravaMereni.maxSpotreba(id, datePickerFrom.getValue().atStartOfDay(), datePickerTo.getValue().atStartOfDay());
            showInfo("Max spotreba = " + vysledek);
        });
        Button btnAvg = new Button("Prumer");
        btnAvg.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            int id = Integer.parseInt(txtFieldSearch.getText());
            double vysledek = spravaMereni.prumerSpotreba(id, datePickerFrom.getValue().atStartOfDay(), datePickerTo.getValue().atStartOfDay());
            showInfo("Prumer spotreba = " + vysledek);
        });

        VBox electricityBox = new VBox();
        Label lblElectricity = new Label("Electrina");
        TextField idSenzoruElektrina = new TextField();
        idSenzoruElektrina.setPromptText("Senzor ID");
        TextField txtVT = new TextField();
        txtVT.setPromptText("VT");
        TextField txtNT = new TextField();
        txtNT.setPromptText("NT");
        DatePicker datePickerElectricity = new DatePicker();
        Button btnAddElectricity = new Button("Vytvorit");
        btnAddElectricity.setOnAction(e -> {
            int id = Integer.parseInt(idSenzoruElektrina.getText());
            Double vt = Double.parseDouble(txtVT.getText());
            Double nt = Double.parseDouble(txtNT.getText());
            Mereni mereni = new MereniElektrika(id, datePickerElectricity.getValue().atStartOfDay(), vt, nt);
            listView.getItems().add(mereni);
            try {
                spravaMereni.vlozMereni(mereni, enumPozice.POSLEDNI);
                spravaMereni.zpristupniMereni(enumPozice.POSLEDNI);
            } catch (KolekceException ex) {
                throw new RuntimeException(ex);
            }
        });

        electricityBox.getChildren().addAll(lblElectricity, idSenzoruElektrina, txtVT, txtNT, datePickerElectricity, btnAddElectricity);

        VBox waterBox = new VBox();
        Label lblWater = new Label("Voda Data");
        TextField idSenzoruVoda = new TextField();
        idSenzoruVoda.setPromptText("Senzor ID");
        TextField txtM3 = new TextField();
        txtM3.setPromptText("M3");
        DatePicker datePickerWater = new DatePicker();
        Button btnAddWater = new Button("Vytvorit");
        btnAddWater.setOnAction(e -> {
            int id = Integer.parseInt(idSenzoruVoda.getText());
            Double m3 = Double.parseDouble(txtM3.getText());
            Mereni mereni = new MereniVoda(id, datePickerWater.getValue().atStartOfDay(), m3);
            listView.getItems().add(mereni);
            try {
                spravaMereni.vlozMereni(mereni, enumPozice.POSLEDNI);
                spravaMereni.zpristupniMereni(enumPozice.POSLEDNI);
            } catch (KolekceException ex) {
                throw new RuntimeException(ex);
            }

        });

        waterBox.getChildren().addAll(lblWater, idSenzoruVoda, txtM3, datePickerWater, btnAddWater);

        bottomBox.getChildren().addAll(txtFieldSearch, datePickerFrom, datePickerTo, btnMax, btnAvg, electricityBox, waterBox);

        root.setLeft(leftMenu);
        root.setCenter(listView);
        root.setBottom(bottomBox);

        Scene scene = new Scene(root, 1200, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showError(String zprava) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(zprava);
        alert.showAndWait();
    }

    private void showInfo(String zprava) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("Info");
        alert.setContentText(zprava);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
