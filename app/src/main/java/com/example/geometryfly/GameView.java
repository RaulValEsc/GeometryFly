package com.example.geometryfly;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.geometryfly.modelo.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameView extends SurfaceView {
    private Bitmap bmp,bmpobstaculo,bmpfondo;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private PuntosThread puntosThread;
    private int posicionNaveY =  500;
    private int pincho1X,pincho1Y=getYpinchoramdom(),pincho2X,pincho2Y=getYpinchoramdom(),pincho3X,pincho3Y=getYpinchoramdom(),pincho4X,pincho4Y=getYpinchoramdom();
    public static int dificultadx=1;
    int tamanoX,tamanoY;
    public static int puntos = 0;
    DatabaseReference database;
    boolean borrado = false, sonando;
    private MediaPlayer mediaPlayer;


    public GameView(Context context, Point pantalla, final MainActivity main){
        super(context);

        sonando = false;

        tamanoX=pantalla.x;
        tamanoY=pantalla.y;

        mediaPlayer = MediaPlayer.create(getContext(), R.raw.musica);
        mediaPlayer.setVolume(1,1);

        puntosThread = new PuntosThread(this);
        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
                puntosThread.setRunning(true);
                puntosThread.start();
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                puntosThread.setRunning(false);
                mediaPlayer.pause();
                while (retry) {
                    try{
                        puntosThread.join();
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e){

                    }
                }
            }
        });
        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.nave);
        bmpobstaculo = BitmapFactory.decodeResource(getResources(),R.drawable.pincho);
        bmpfondo = BitmapFactory.decodeResource(getResources(),R.drawable.fondopartida);

    }

    public void setCoordenadaY(float y){
        posicionNaveY = posicionNaveY + (int) Math.floor(y) *10;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (!sonando){
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
            sonando = true;
        }

        if (posicionNaveY<0){
            posicionNaveY = 0;
        }else{
            if (posicionNaveY + bmp.getHeight() > tamanoY){
                posicionNaveY = tamanoY - bmp.getHeight();
            }
        }
        if (puntos>200){
            dificultadx=48;
        }else{
            if (puntos>175){
                dificultadx=44;
            }else{
                if (puntos>150){
                    dificultadx=40;
                }else{
                    if(puntos>125){
                        dificultadx=36;
                    }else{
                        if (puntos>100){
                            dificultadx=32;
                        }else{
                            if (puntos>75){
                                dificultadx=28;
                            }else{
                                if(puntos>50){
                                    dificultadx=24;
                                }else{
                                    if (puntos>25){
                                        dificultadx=20;
                                    }else{
                                        dificultadx=16;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        avanzarXpincho1();
        avanzarXpincho2();
        avanzarXpincho3();
        avanzarXpincho4();
        choque();
        canvas.drawBitmap(bmpfondo,0,0,null);

        if(pincho1X<0){
            pincho1Y=getYpinchoramdom();
            pincho1X=tamanoX+100;
        }
        if(pincho2X<0){
            pincho2Y=getYpinchoramdom();
            pincho2X=tamanoX+200;
        }
        if(pincho3X<0){
            pincho3Y=getYpinchoramdom();
            pincho3X=tamanoX+300;
        }
        if(pincho4X<0){
            pincho4Y=getYpinchoramdom();
            pincho4X=tamanoX+400;
        }
        //pincho1
        canvas.drawBitmap(bmpobstaculo,pincho1X,pincho1Y,null);
        //pincho2
        canvas.drawBitmap(bmpobstaculo,pincho2X,pincho2Y,null);
        //pincho3
        canvas.drawBitmap(bmpobstaculo,pincho3X,pincho3Y,null);
        //pincho4
        canvas.drawBitmap(bmpobstaculo,pincho4X,pincho4Y,null);
        canvas.drawBitmap(bmp, 60, posicionNaveY,null);
        puntuaje(canvas);
    }

    private void choque(){
        if ((60<=pincho1X&&60+bmp.getWidth()>=pincho1X&&posicionNaveY<=pincho1Y&&posicionNaveY+bmp.getHeight()>=pincho1Y)||(60>=pincho1X+bmpobstaculo.getWidth()&&60+bmp.getWidth()<=pincho1X+bmpobstaculo.getWidth()&&posicionNaveY<=pincho1Y+bmpobstaculo.getHeight()&&posicionNaveY+bmp.getHeight()>=pincho1Y+bmpobstaculo.getHeight())){
            acabar();
        }else{
            if ((60<=pincho2X&&60+bmp.getWidth()>=pincho2X&&posicionNaveY<=pincho2Y&&posicionNaveY+bmp.getHeight()>=pincho2Y)||(60>=pincho2X+bmpobstaculo.getWidth()&&60+bmp.getWidth()<=pincho2X+bmpobstaculo.getWidth()&&posicionNaveY<=pincho2Y+bmpobstaculo.getHeight()&&posicionNaveY+bmp.getHeight()>=pincho2Y+bmpobstaculo.getHeight())){
                acabar();
            }else{
                if ((60<=pincho3X&&60+bmp.getWidth()>=pincho3X&&posicionNaveY<=pincho3Y&&posicionNaveY+bmp.getHeight()>=pincho3Y)||(60>=pincho3X+bmpobstaculo.getWidth()&&60+bmp.getWidth()<=pincho3X+bmpobstaculo.getWidth()&&posicionNaveY<=pincho3Y+bmpobstaculo.getHeight()&&posicionNaveY+bmp.getHeight()>=pincho3Y+bmpobstaculo.getHeight())){
                    acabar();
                }else{
                    if ((60<=pincho4X&&60+bmp.getWidth()>=pincho4X&&posicionNaveY<=pincho4Y&&posicionNaveY+bmp.getHeight()>=pincho4Y)||(60>=pincho4X+bmpobstaculo.getWidth()&&60+bmp.getWidth()<=pincho4X+bmpobstaculo.getWidth()&&posicionNaveY<=pincho4Y+bmpobstaculo.getHeight()&&posicionNaveY+bmp.getHeight()>=pincho4Y+bmpobstaculo.getHeight())){
                        acabar();
                    }
                }
            }
        }
    }

    private void acabar(){
        gameLoopThread.setRunning(false);
        puntosThread.setRunning(false);

        mediaPlayer.stop();
        sonando=false;

        Intent nextActivityIntent = new Intent(getContext(), LoginActivity.class);
        database = FirebaseDatabase.getInstance().getReference().child("usuarios");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!borrado) {
                    for (DataSnapshot child: snapshot.getChildren()) {
                        if (child.child("nombre").getValue().toString().equals(MainActivity.nombreUsuario)) {
                            Usuario newU = child.getValue(Usuario.class);
                            newU.setPuntos(puntos);
                            database = FirebaseDatabase.getInstance().getReference().child("usuarios").child(child.getKey());
                            borrado = true;
                            database.removeValue();
                            database = FirebaseDatabase.getInstance().getReference().child("usuarios").child(child.getKey());
                            database.setValue(newU);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        getContext().startActivity(nextActivityIntent);
    }

    private int getYpinchoramdom() {
        Double a = Math.random()*2;
        int pinchoy= (int)(a*((tamanoY-200)-30))+30;
        return pinchoy;
    }

    private void avanzarXpincho1(){
        pincho1X=pincho1X-dificultadx;
    }
    private void avanzarXpincho2(){
        pincho2X=pincho2X-dificultadx;
    }
    private void avanzarXpincho3(){
        pincho3X=pincho3X-dificultadx;
    }
    private void avanzarXpincho4(){
        pincho4X=pincho4X-dificultadx;
    }

    private void puntuaje(Canvas canvas) {
        TextView text = new TextView(getContext());
        text.setText("Puntos : "+puntos);
        text.setTextSize(15);

        Paint paintText = text.getPaint();

        Rect boundsText = new Rect();

        paintText.getTextBounds(text.getText().toString(),0,text.length(),boundsText);
        paintText.setTextAlign(Paint.Align.RIGHT);
        paintText.setColor(Color.WHITE);

        canvas.drawText(text.getText().toString(),getWidth()*5/6,getHeight()/6,paintText);
    }

}
