package com.twochicken.multiplayertictactoe;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    Player player1;

    private String buttonIndex;

    private int iButtonIndex;
    private int jButtonIndex;
    private int roundCount;

    private Button buttons [][] = new Button[3][3];

    private Socket socket;
    HashMap<String, Player> allPlayers;
    //private boolean isMyTurn = true;

    {
        try {
            socket = IO.socket("http://192.168.1.67:3000");
            Log.v(TAG, "Fine!");
        } catch (URISyntaxException e) {
            Log.v(TAG, "Error Connecting to IP!" + e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        roundCount = 0;
        //connectSocket();

        //player1 = new Player();
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++){
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }
        socket.connect();
        configSocketEvents();
    }

/*
    public void connectSocket(){
        try{
            socket = IO.socket("http://192.168.1.67:3000");
            socket.connect();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
*/



    public void configSocketEvents() {
        socket.on(socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Hello!");
                        socket.emit("message", "hi");
                        //socket.disconnect();
                    }
                });
            }

        }).on("socket id", new Emitter.Listener(){
            @Override
            public void call(final Object... args) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            String id = data.getString("id");
                            player1 = new Player(id,"X", false);
                            System.out.println("My ID: " + id);

                        } catch (JSONException e){
                            System.out.println("Error getting ID");
                        }
                    }
                });
            }
        }).on("new player", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            String playerId = data.getString("id");
                            System.out.println("New Player ID: " + playerId);
                            player1.setMyTurn(true);
                            player1.setType("O");
                        } catch (JSONException e) {
                            System.out.println("Error getting ID");
                        }
                    }
                });
            }
        }).on("player turn", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            String playerId = data.getString("id");
                            int i = data.getInt("iButtonIndex");
                            int j = data.getInt("jButtonIndex");
                            String playerType = data.getString("playerType");
                            //get the button index and set the text for that index.
                            if (playerId != null) {
                                buttons[i][j].setText(playerType);
                            }
                            player1.setMyTurn(true);
                        } catch (JSONException e) {
                            System.out.println("Error getting ID");
                        }
                    }
                });
            }
        }).on("player won", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            String message = data.getString("message");
                            Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
                            alertDialog("We have a winner!", "Would you like to play agian?");
                        } catch (JSONException e) {
                            System.out.println("Error!");
                        }
                    }
                });
            }
        }).on("draw game", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            String message = data.getString("message");
                            Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
                            alertDialog(message, "Would you like to play again?");
                        } catch (JSONException e) {
                            System.out.println("Error!");
                        }
                    }
                });
            }
        });
    }


    @Override
    public void onClick(View v) {
        if(!((Button) v).getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"That has been played already!",Toast.LENGTH_LONG).show();
            return;
        }
        else if (!(player1.isMyTurn)) {
            Toast.makeText(getApplicationContext(),"It's not your turn!",Toast.LENGTH_LONG).show();
            return;
        }
        switch (v.getId()) {
            case R.id.button_00:
                // do something

                iButtonIndex = 0;
                jButtonIndex = 0;
                //Toast.makeText(this, "Clicked! button_00", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_01:
                // do something else
                //buttonIndex = "01";
                iButtonIndex = 0;
                jButtonIndex = 1;
                //Toast.makeText(this, "Clicked! button_01", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_02:
                // i'm lazy, do nothing
                //buttonIndex = "02";
                iButtonIndex = 0;
                jButtonIndex = 2;
               // Toast.makeText(this, "Clicked! button_02", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_10:
                // do something
                //buttonIndex = "10";
                iButtonIndex = 1;
                jButtonIndex = 0;
                //Toast.makeText(this, "Clicked! button_10", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_11:
                // do something else
                //buttonIndex = "11";
                iButtonIndex = 1;
                jButtonIndex = 1;
                //Toast.makeText(this, "Clicked! button_11", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_12:
                // i'm lazy, do nothing
                //buttonIndex = "12";
                iButtonIndex = 1;
                jButtonIndex = 2;
                //Toast.makeText(this, "Clicked! button_12", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_20:
                // do something
                //buttonIndex = "20";
                iButtonIndex = 2;
                jButtonIndex = 0;
                //Toast.makeText(this, "Clicked! button_20", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_21:
                // do something else
                //buttonIndex = "21";
                iButtonIndex = 2;
                jButtonIndex = 1;
                //Toast.makeText(this, "Clicked! button_21", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_22:
                // i'm lazy, do nothing
                //buttonIndex = "22";
                iButtonIndex = 2;
                jButtonIndex = 2;
                //Toast.makeText(this, "Clicked! button_22", Toast.LENGTH_SHORT).show();
                break;
        }

        if(player1.isMyTurn()){
            ((Button) v).setText(player1.getType());
            ++roundCount;
            //emit the data
            //buttonIndex.put("x", buttonIndex);
            JSONObject data = new JSONObject();
            try {
                data.put("iButtonIndex", iButtonIndex);
                data.put("jButtonIndex", jButtonIndex);
                data.put("playerType", player1.getType());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit("player turn", data);

            player1.setMyTurn(false);

            if(checkForWin()){
                //emit the data to the server telling everyone that player won
                Toast.makeText(getApplicationContext(),"You win!",Toast.LENGTH_LONG).show();
                String message = player1.getName() + " wins!";
                socket.emit("player won", message);
                alertDialog("We have a winner!", "Would you like to play again?");
            }
            else if(draw()){
                String message = "Draw game!";
                socket.emit("draw game", message);
                alertDialog("Draw!", "Would you like you play again?");
            }
        }
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if(field[i][0].equals(field[i][1]) &&
                    field[i][0].equals(field[i][2]) &&
                    !field[i][0].equals("")){
                return true;
            }
        }

        for (int j = 0; j < 3; j++) {
            if(field[0][j].equals(field[1][j]) &&
                    field[0][j].equals(field[2][j]) &&
                    !field[0][j].equals("")){
                return true;
            }
        }


        if(field[0][0].equals(field[1][1]) &&
                field[0][0].equals(field[2][2]) &&
                !field[0][0].equals("")){
            return true;
        }

        if(field[0][2].equals(field[1][1]) &&
                field[0][2].equals(field[2][0]) &&
                !field[0][2].equals("")){
            return true;
        }

        return false;

    }

    private void resetBoard(){
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++){
                buttons[i][j].setText("");
            }
        }
        roundCount = 0;
        if(player1.isMyTurn()) {
            Toast.makeText(getApplicationContext(), "It's your move!", Toast.LENGTH_LONG).show();
        }

    }

    private boolean draw(){
        String[][] field = new String[3][3];
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++){
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
        if(!field[0][0].equals("") && !field[0][1].equals("") && !field[0][2].equals("")
                && !field[1][0].equals("") && !field[1][1].equals("") && !field[1][2].equals("")
                && !field[2][0].equals("") && !field[2][1].equals("") && !field[2][2].equals("")){
            return true;
        }
        return false;
        //alertDialog("Draw!", "Would you like yo play again?");
    }

    private void alertDialog(String title, String message){
        new AlertDialog.Builder(this).setTitle(title)
                                            .setMessage(message)
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    resetBoard();
                                            }}) .setNegativeButton(android.R.string.no, null).show();

    }
/*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("playerTurn", player1.isMyTurn());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        player1.setMyTurn(savedInstanceState.getBoolean("playerTurn"));
    }
  */
}
