module com.emiliosg23 {
    requires javafx.controls;
    requires javafx.fxml;
		requires java.desktop;
		requires transitive javafx.graphics;

    opens com.emiliosg23 to javafx.fxml;
    exports com.emiliosg23;
    opens com.emiliosg23.controllers to javafx.fxml;
		exports com.emiliosg23.controllers;
}
