module com.gotp {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.gotp to javafx.fxml;
    opens com.gotp.GUIcontrollers to javafx.fxml;
    opens com.gotp.server to javafx.fxml;
    
    exports com.gotp;

    exports com.gotp.database;

    exports com.gotp.game_mechanics.board;
    exports com.gotp.game_mechanics.board.move;
    exports com.gotp.game_mechanics.utilities;
    
    exports com.gotp.GUIcontrollers;
    
    exports com.gotp.server;
    exports com.gotp.server.messages;

    requires java.sql;
}
