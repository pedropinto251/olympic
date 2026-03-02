module com.lp3_grupo5.lp3_grupo5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.dotenv;
    requires java.desktop;
    requires com.jfoenix;
    requires jbcrypt;
    requires org.kordamp.ikonli.javafx;
    requires com.zaxxer.hikari;
    requires com.google.gson;
    requires com.mailjet.api;
    requires org.json;

    opens com.lp3_grupo5.lp3_grupo5 to javafx.fxml;
    
    exports com.lp3_grupo5.lp3_grupo5;
    //opens com.lp3_grupo5.lp3_grupo5.Model to javafx.base;
    opens com.lp3_grupo5.lp3_grupo5.Model to javafx.base, com.google.gson;

    exports com.lp3_grupo5.lp3_grupo5.Controller.Athlete;
    opens com.lp3_grupo5.lp3_grupo5.Controller.Athlete to javafx.fxml;

    exports com.lp3_grupo5.lp3_grupo5.Controller.Event;
    opens com.lp3_grupo5.lp3_grupo5.Controller.Event to javafx.fxml;

    exports com.lp3_grupo5.lp3_grupo5.Controller.Local;
    opens com.lp3_grupo5.lp3_grupo5.Controller.Local to javafx.fxml;

    exports com.lp3_grupo5.lp3_grupo5.Controller.Menus;
    opens com.lp3_grupo5.lp3_grupo5.Controller.Menus to javafx.fxml;

    exports com.lp3_grupo5.lp3_grupo5.Controller.Login;
    opens com.lp3_grupo5.lp3_grupo5.Controller.Login to javafx.fxml;

    exports com.lp3_grupo5.lp3_grupo5.Controller.Sports;
    opens com.lp3_grupo5.lp3_grupo5.Controller.Sports to javafx.fxml;

    exports com.lp3_grupo5.lp3_grupo5.Controller.Team;
    opens com.lp3_grupo5.lp3_grupo5.Controller.Team to javafx.fxml;

    exports com.lp3_grupo5.lp3_grupo5.Controller.XML;
    opens com.lp3_grupo5.lp3_grupo5.Controller.XML to javafx.fxml;

    exports com.lp3_grupo5.lp3_grupo5.Controller.Inscriptions;
    opens com.lp3_grupo5.lp3_grupo5.Controller.Inscriptions to javafx.fxml;

    exports com.lp3_grupo5.lp3_grupo5.Util;
    opens com.lp3_grupo5.lp3_grupo5.Util to javafx.fxml;

    exports com.lp3_grupo5.lp3_grupo5.Controller.API;
    opens com.lp3_grupo5.lp3_grupo5.Controller.API to javafx.fxml;
}
