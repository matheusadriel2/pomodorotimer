package com.matheusadriel.pomodorotimer;

import com.matheusadriel.pomodorotimer.model.TimerListener;
import com.matheusadriel.pomodorotimer.model.PomodoroTimer;
import com.matheusadriel.pomodorotimer.model.TimerSettings;
import com.matheusadriel.pomodorotimer.model.TimerType;
import com.matheusadriel.pomodorotimer.ui.CircularProgress;
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
    
    // Status and Type labels removed from FXML but keeping fields if simpler, 
    // or better yet, removing them if I remove from FXML.
    // The user asked to remove them. I already removed them from FXML in Step 203.
    // So I should remove them here too.
    
    // @FXML private Label statusLabel; // Removed
    // @FXML private Label typeLabel;   // Removed
    
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
        javafx.application.Platform.exit();
        System.exit(0);
    }
    
    @FXML
    public void initialize() {
        // Criar anel de progresso programaticamente
        progressRing = new CircularProgress(114.0);
        progressContainer.getChildren().add(0, progressRing);
        
        // Timer will be set by PomodoroApplication
        setupTimeButtons();
        updateProgressColor();
    }
    
    public void setSharedTimer(PomodoroTimer sharedTimer, TimerSettings sharedSettings) {
        this.timer = sharedTimer;
        this.settings = sharedSettings;
        this.currentType = timer.getTimerType();
        
        timer.addListener(this);
        
        updateTimerDisplay();
        // updateTypeLabel(); // Removed
        updateProgressColor();
    }
    
    public void refreshDisplay() {
        if (timer != null) {
            currentType = timer.getTimerType();
            updateTimerDisplay();
            // updateTypeLabel(); // Removed
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
        // Reset button state to Start (Play icon)
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
            
            // Refresh display to reflect any changes
            refreshDisplay();
            
        } catch (IOException e) {
            e.printStackTrace();
            // updateStatus("Erro ao abrir settings"); // Removed
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
        
        // Transição automática para break após focus
        if (currentType == TimerType.FOCUS) {
            completedSessions++;
            
            // Determinar tipo de break
            TimerType nextType = (completedSessions % settings.getLongBreakAfter() == 0) 
                ? TimerType.LONG_BREAK 
                : TimerType.SHORT_BREAK;
            
            switchToTimerType(nextType);
        } else {
            // Voltar para focus após break
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
        // updateTypeLabel(); // Removed
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
    
    // Status label logic removed
    
    // Type label logic removed
    
    private void updateProgressColor() {
        // Cor baseada no tipo de timer
        Color color = switch (currentType) {
            case FOCUS -> Color.web("#ffb900"); // Amber matching mini-timer
            case SHORT_BREAK, LONG_BREAK -> Color.web("#4cc2ff"); // Blue matching mini-timer
        };
        progressRing.setProgressColor(color);
    }
    
    private void setupTimeButtons() {
        timeButtonsContainer.getChildren().clear();
        int[] predefinedTimes = {50, 25, 10, 5}; // minutos
        
        for (int minutes : predefinedTimes) {
            Button timeButton = new Button();
            // timeButton.setText(String.valueOf(minutes)); // Removed text
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
