package com.example.geometryfly;

import android.graphics.Canvas;

public class PuntosThread extends Thread {
    public static long VPS=1;
    private GameView view;
    private boolean running = false;

    public PuntosThread(GameView view){
        this.view=view;
    }

    public void setRunning(boolean run){
        running = run;
    }

    @Override
    public void run() {
        long tickPS = 1000/VPS;
        long startTime;
        long sleepTime;
        while (running){
            startTime = System.currentTimeMillis();
            view.puntos++;
            sleepTime=tickPS-(System.currentTimeMillis()-startTime);
            try{
                if(sleepTime>0)
                    sleep(sleepTime);
                else
                    sleep(10);
            }catch (Exception e){}
        }
    }
}