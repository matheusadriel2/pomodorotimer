package com.matheusadriel.pomodorotimer;

import com.matheusadriel.pomodorotimer.model.PomodoroTimer;
import com.matheusadriel.pomodorotimer.model.TimerSettings;
import com.matheusadriel.pomodorotimer.model.TimerType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import fr.brouillard.oss.cssfx.CSSFX;

import java.io.IOException;

public class PomodoroApplication extends Application {
    
    private static Stage primaryStage;
    private static Stage miniStage;
    private static PomodoroTimer sharedTimer;
    private static TimerSettings sharedSettings;
    private static TimerController mainController;
    private static MiniTimerController miniController;
    
    @Override
    public void start(Stage stage) throws IOException {
        CSSFX.start();
        primaryStage = stage;
        Platform.setImplicitExit(false);
        
        
        sharedSettings = new TimerSettings();
        sharedTimer = new PomodoroTimer(sharedSettings.getFocusDurationSeconds(), TimerType.FOCUS);
        
        
        FXMLLoader fxmlLoader = new FXMLLoader(PomodoroApplication.class.getResource("timer-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 375, 525);
        mainController = fxmlLoader.getController();
        mainController.setSharedTimer(sharedTimer, sharedSettings);
        
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setTitle("Pomodoro Timer");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    public static void switchToMiniTimer() {
        try {
            if (miniStage == null) {
                createMiniStage();
            }
            
            
            javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
            double x = screenBounds.getMaxX() - 220;
            double y = screenBounds.getMaxY() - 120;
            
            
            if (primaryStage != null && primaryStage.isShowing()) {
                x = primaryStage.getX() + primaryStage.getWidth() - 200;
                y = primaryStage.getY();
            }
            
            miniStage.setX(x);
            miniStage.setY(y);
            miniStage.show();
            
            if (primaryStage != null) {
                primaryStage.hide();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void switchToMainWindow() {
        if (primaryStage != null) {
            primaryStage.show();
        }
        
        if (miniStage != null) {
            miniStage.hide();
        }
        
        
        if (mainController != null) {
            mainController.refreshDisplay();
        }
    }
    
    private static void createMiniStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PomodoroApplication.class.getResource("mini-timer.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 180, 180); 
        scene.setFill(Color.TRANSPARENT);
        
        miniController = fxmlLoader.getController();
        miniController.setTimer(sharedTimer);
        
        miniStage = new Stage();
        miniStage.initStyle(StageStyle.TRANSPARENT);
        miniStage.setAlwaysOnTop(true);
        miniStage.setScene(scene);
        miniStage.setTitle("Pomodoro Mini");
        miniStage.setResizable(false);
    }
    
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public static PomodoroTimer getSharedTimer() {
        return sharedTimer;
    }
    
    public static TimerSettings getSharedSettings() {
        return sharedSettings;
    }
}

