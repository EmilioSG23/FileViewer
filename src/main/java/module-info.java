module com.emiliosg23 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.emiliosg23 to javafx.fxml;
    exports com.emiliosg23;
}
