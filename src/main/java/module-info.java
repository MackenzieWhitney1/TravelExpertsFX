module org.example.travelexpertsfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires kernel;
    requires layout;

    opens org.example.travelexpertsfx.models to javafx.base;
    exports org.example.travelexpertsfx;
    exports org.example.travelexpertsfx.controllers;
    opens org.example.travelexpertsfx.controllers to javafx.fxml;
    exports org.example.travelexpertsfx.contexts;
    opens org.example.travelexpertsfx.contexts to javafx.fxml;
    opens org.example.travelexpertsfx to javafx.base, javafx.fxml;
}