package com.matheusadriel.pomodorotimer;

import com.matheusadriel.pomodorotimer.model.TimerSettings;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

public class SettingsController {

    @FXML
    private Spinner<Integer> focusDurationSpinner;

    @FXML
    private Spinner<Integer> shortBreakSpinner;

    @FXML
    private Spinner<Integer> longBreakSpinner;

    @FXML
    private Spinner<Integer> longBreakAfterSpinner;

    private TimerSettings settings;

    @FXML
    public void initialize() {
        
        this.settings = PomodoroApplication.getSharedSettings();

        
        focusDurationSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 60, settings.getFocusDuration())
        );

        shortBreakSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30, settings.getShortBreakDuration())
        );

        longBreakSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 60, settings.getLongBreakDuration())
        );
        
        longBreakAfterSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, settings.getLongBreakAfter())
        );
    }

    @FXML
    protected void onSave() {
        
        settings.setFocusDuration(focusDurationSpinner.getValue());
        settings.setShortBreakDuration(shortBreakSpinner.getValue());
        settings.setLongBreakDuration(longBreakSpinner.getValue());
        settings.setLongBreakAfter(longBreakAfterSpinner.getValue());

        
        closeWindow();
        
        
        
        
    }

    @FXML
    protected void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) focusDurationSpinner.getScene().getWindow();
        stage.close();
    }
}

