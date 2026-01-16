package com.matheusadriel.pomodorotimer;

import com.matheusadriel.pomodorotimer.model.TimerListener;
import com.matheusadriel.pomodorotimer.model.PomodoroTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

public class MiniTimerController implements TimerListener {
    
    private static final String PLAY_ICON = "M8 5v14l11-7z";
    private static final String PAUSE_ICON = "M6 19h4V5H6v14zm8-14v14h4V5h-4z";

    @FXML
    private VBox root;
    
    @FXML
    private Label timeLabel;
    
    @FXML
    private Button playPauseButton;
    
    private PomodoroTimer timer;
    private double xOffset = 0;
    private double yOffset = 0;
    
    @FXML
    public void initialize() {
        
        root.setOnMousePressed(this::onMousePressed);
        root.setOnMouseDragged(this::onMouseDragged);
    }
    
    public void setTimer(PomodoroTimer timer) {
        this.timer = timer;
        timer.addListener(this);
        updateDisplay();
    }
    
    private void onMousePressed(MouseEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }
    
    private void onMouseDragged(MouseEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }
    
    @FXML
    protected void onPlayPauseClick() {
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
    protected void onResetClick() {
        if (timer != null) {
            timer.stop();
            timer.reset();
            updateDisplay();
        }
    }
    
    @FXML
    protected void onExpandClick() {
        PomodoroApplication.switchToMainWindow();
    }
    
    @FXML
    protected void onCloseClick() {
        Platform.exit();
        System.exit(0);
    }
    
    @Override
    public void onTick(int remaining) {
        updateTimeDisplay(remaining);
    }
    
    @Override
    public void onFinish() {
        updatePlayButtonState(false);
    }
    
    private void updateDisplay() {
        if (timer != null) {
            updateTimeDisplay(timer.getTime());
            updatePlayButtonState(timer.isRunning());
        }
    }
    
    private void updatePlayButtonState(boolean isRunning) {
         if (isRunning) {
             playPauseButton.getStyleClass().add("running");
             setButtonIcon(PAUSE_ICON);
         } else {
             playPauseButton.getStyleClass().remove("running");
             setButtonIcon(PLAY_ICON);
         }
    }
    
    private void setButtonIcon(String content) {
        if (playPauseButton.getGraphic() instanceof SVGPath) {
            ((SVGPath) playPauseButton.getGraphic()).setContent(content);
        }
    }
    
    private void updateTimeDisplay(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        timeLabel.setText(String.format("%02d:%02d", minutes, secs));
    }
    
    public void cleanup() {
        if (timer != null) {
            timer.removeListener(this);
        }
    }
}


