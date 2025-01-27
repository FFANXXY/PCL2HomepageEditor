module com.ffanxxy.phe.phe {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.ffanxxy.phe.phe to javafx.fxml;
    exports com.ffanxxy.phe.phe;
}