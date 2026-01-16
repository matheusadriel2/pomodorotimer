package com.matheusadriel.pomodorotimer.ui;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;

public class CircularProgress extends Pane {
    private static final double STROKE_WIDTH = 7.0;
    private Circle backgroundCircle;
    private Arc progressArc;
    private double progress = 0.0;
    private DropShadow glowEffect;
    
    public CircularProgress(double radius) {
        double size = radius * 2 + STROKE_WIDTH * 2;
        double center = size / 2;
        
        setPrefSize(size, size);
        setMinSize(size, size);
        setMaxSize(size, size);
        
        // Círculo de fundo (sutil)
        backgroundCircle = new Circle(center, center, radius);
        backgroundCircle.setFill(Color.TRANSPARENT);
        backgroundCircle.setStroke(Color.rgb(255, 255, 255, 0.08));
        backgroundCircle.setStrokeWidth(STROKE_WIDTH);
        
        // Arco de progresso - começa do topo (90 graus)
        progressArc = new Arc(center, center, radius, radius, 90, 0);
        progressArc.setType(ArcType.OPEN);
        progressArc.setFill(Color.TRANSPARENT);
        progressArc.setStroke(Color.rgb(255, 107, 107));
        progressArc.setStrokeWidth(STROKE_WIDTH);
        progressArc.setStrokeLineCap(StrokeLineCap.ROUND);
        
        // Efeito de glow sutil
        glowEffect = new DropShadow();
        glowEffect.setColor(Color.rgb(255, 107, 107, 0.5));
        glowEffect.setRadius(14);
        glowEffect.setSpread(0.15);
        progressArc.setEffect(glowEffect);
        
        getChildren().addAll(backgroundCircle, progressArc);
    }
    
    public void setProgress(double progress) {
        this.progress = Math.max(0.0, Math.min(1.0, progress));
        
        if (this.progress == 0.0) {
            progressArc.setLength(0);
            progressArc.setVisible(false);
        } else {
            progressArc.setVisible(true);
            // Começar do topo (90 graus) e preencher no sentido horário (ângulo negativo)
            double angle = -360 * this.progress;
            progressArc.setLength(angle);
        }
    }
    
    public double getProgress() {
        return progress;
    }
    
    public void setProgressColor(Color color) {
        progressArc.setStroke(color);
        // Atualizar cor do glow
        glowEffect.setColor(Color.color(
            color.getRed(), 
            color.getGreen(), 
            color.getBlue(), 
            0.5
        ));
    }
}
