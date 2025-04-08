module ru.cooper.island {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    opens ru.cooper.island to javafx.fxml;

    exports ru.cooper.island;
}
