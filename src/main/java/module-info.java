module com.gotp {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.gotp to javafx.fxml;
    opens com.gotp.GUIcontrollers to javafx.fxml;
    opens com.gotp.server to javafx.fxml;
    exports com.gotp;
}
