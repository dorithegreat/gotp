module com.gotp {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.gotp to javafx.fxml;
    exports com.gotp;
}
