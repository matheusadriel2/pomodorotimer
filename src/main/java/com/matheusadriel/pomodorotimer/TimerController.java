package com.matheusadriel.pomodorotimer;

import com.matheusadriel.pomodorotimer.model.TimerListener;
import com.matheusadriel.pomodorotimer.model.PomodoroTimer;
import com.matheusadriel.pomodorotimer.model.TimerSettings;
import com.matheusadriel.pomodorotimer.model.TimerType;
import com.matheusadriel.pomodorotimer.ui.CircularProgress;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.shape.SVGPath;

public class TimerController implements TimerListener {
    
    private static final String PLAY_ICON = "M8 5v14l11-7z";
    private static final String PAUSE_ICON = "M6 19h4V5H6v14zm8-14v14h4V5h-4z";

    @FXML
    private Label timeLabel;
    
    @FXML
    private Button startPauseButton;
    
    @FXML
    private Button resetButton;
    
    @FXML
    private Button settingsButton;
    
    @FXML
    private Button miniButton;
    
    @FXML
    private StackPane progressContainer;
    
    @FXML
    private HBox timeButtonsContainer;
    
    private CircularProgress progressRing;
    
    private PomodoroTimer timer;
    private TimerSettings settings;
    private TimerType currentType = TimerType.FOCUS;
    private int completedSessions = 0;
    
    private double xOffset = 0;
    private double yOffset = 0;
    
    @FXML
    private void onTitleBarPressed(javafx.scene.input.MouseEvent event) {
        Stage stage = (Stage) timeLabel.getScene().getWindow();
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }
    
    @FXML
    private void onTitleBarDragged(javafx.scene.input.MouseEvent event) {
        Stage stage = (Stage) timeLabel.getScene().getWindow();
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }
    
    @FXML
    private void onMinimizeClick() {
        Stage stage = (Stage) timeLabel.getScene().getWindow();
        stage.setIconified(true);
    }
    
    @FXML
    private void onCloseClick() {
        Platform.exit();
        System.exit(0);
    }
    
    @FXML
    public void initialize() {
        
        progressRing = new CircularProgress(114.0);
        progressContainer.getChildren().add(0, progressRing);
        
        setupTimeButtons();
        updateProgressColor();
    }
    
    public void setSharedTimer(PomodoroTimer sharedTimer, TimerSettings sharedSettings) {
        this.timer = sharedTimer;
        this.settings = sharedSettings;
        this.currentType = timer.getTimerType();
        
        timer.addListener(this);
        
        updateTimerDisplay();
        updateProgressColor();
    }
    
    public void refreshDisplay() {
        if (timer != null) {
            currentType = timer.getTimerType();
            updateTimerDisplay();
            updateProgressColor();
            updatePlayButtonState(timer.isRunning());
        }
    }
    
    @FXML
    protected void onStartPauseButtonClick() {
        if (timer == null) return;
        
        if (timer.isRunning()) {
            timer.stop();
            updatePlayButtonState(false);
        } else {
            timer.start();
            updatePlayButtonState(true);
        }
    }
    
    @FXML
    protected void onResetButtonClick() {
        if (timer == null) return;
        
        timer.reset();
        updatePlayButtonState(false);
        updateTimerDisplay();
    }
    
    @FXML
    protected void onSettingsButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("settings-view.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Settings");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
            
            refreshDisplay();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    protected void onMiniButtonClick() {
        PomodoroApplication.switchToMiniTimer();
    }
    
    @Override
    public void onTick(int remaining) {
        updateTimeDisplay(remaining);
        if (timer != null) {
            double progress = timer.getProgress();
            progressRing.setProgress(progress);
        }
    }
    
    @Override
    public void onFinish() {
        updatePlayButtonState(false);
        
        
        if (currentType == TimerType.FOCUS) {
            completedSessions++;
            
            
            TimerType nextType = (completedSessions % settings.getLongBreakAfter() == 0) 
                ? TimerType.LONG_BREAK 
                : TimerType.SHORT_BREAK;
            
            switchToTimerType(nextType);
        } else {
            
            switchToTimerType(TimerType.FOCUS);
        }
    }
    
    private void switchToTimerType(TimerType type) {
        currentType = type;
        if (timer != null) {
            timer.stop();
            
            int duration = switch (type) {
                case FOCUS -> settings.getFocusDurationSeconds();
                case SHORT_BREAK -> settings.getShortBreakDurationSeconds();
                case LONG_BREAK -> settings.getLongBreakDurationSeconds();
            };
            
            timer.setDuration(duration);
            timer.setTimerType(type);
        }
        updateTimerDisplay();
        updateProgressColor();
    }
    
    private void updateTimeDisplay(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        timeLabel.setText(String.format("%02d:%02d", minutes, secs));
    }
    
    private void updateTimerDisplay() {
        if (timer != null) {
            updateTimeDisplay(timer.getTime());
            progressRing.setProgress(timer.getProgress());
        }
    }
    
    private void updateProgressColor() {
        
        Color color = switch (currentType) {
            case FOCUS -> Color.web("#ffb900"); 
            case SHORT_BREAK, LONG_BREAK -> Color.web("#4cc2ff"); 
        };
        progressRing.setProgressColor(color);
    }
    
    private void setupTimeButtons() {
        timeButtonsContainer.getChildren().clear();
        int[] predefinedTimes = {50, 25, 10, 5}; 
        
        for (int minutes : predefinedTimes) {
            Button timeButton = new Button();
            timeButton.getStyleClass().add("time-preset-button");
            
            timeButton.setOnAction(e -> setTimerDuration(minutes * 60));
            timeButtonsContainer.getChildren().add(timeButton);
        }
    }
    
    private void setTimerDuration(int seconds) {
        if (timer == null) return;
        
        if (timer.isRunning()) {
            timer.stop();
            updatePlayButtonState(false);
        }
        timer.setDuration(seconds);
        updateTimerDisplay();
    }

    private void updatePlayButtonState(boolean isRunning) {
        if (isRunning) {
            startPauseButton.getStyleClass().remove("start-button");
            startPauseButton.getStyleClass().add("pause-button");
            setButtonIcon(PAUSE_ICON);
        } else {
            startPauseButton.getStyleClass().remove("pause-button");
            startPauseButton.getStyleClass().add("start-button");
            setButtonIcon(PLAY_ICON);
        }
    }

    private void setButtonIcon(String content) {
        if (startPauseButton.getGraphic() instanceof SVGPath) {
            ((SVGPath) startPauseButton.getGraphic()).setContent(content);
        }
    }
}


