package ru.eaglorn.cs

import javafx.fxml.FXML
import javafx.scene.control.Label

class ApplicationController {
    @FXML
    private lateinit var welcomeText: Label

    @FXML
    private fun onHelloButtonClick() {
        welcomeText.text = "Welcome to JavaFX Application!"
    }
}