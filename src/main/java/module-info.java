module upce.semestralni_prace_b_kopytin_makar {
    requires javafx.controls;
    requires javafx.fxml;


    exports upce.gui;
    opens upce.gui to javafx.fxml;
}