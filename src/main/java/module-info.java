module com.nft.marketplace {
    requires javafx.controls;
    requires javafx.fxml;



    exports com.nft.marketplace.controller;
    opens com.nft.marketplace.controller to javafx.fxml;
    exports com.nft.marketplace.view;
    opens com.nft.marketplace.view to javafx.fxml;
}