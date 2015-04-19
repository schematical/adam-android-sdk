package com.schematical.adam.comm.socket;

/**
 * Created by user1a on 10/10/13.
 */

import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;


import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.net.UnknownHostException;

import com.schematical.adam.comm.AdamCommClientBase;
import com.schematical.adam.comm.AdamCommDriver;

import org.json.JSONObject;

public class AdamSocketClient extends AdamCommClientBase {

    private static Socket socket;


    public boolean IsConnected(){
        if(socket != null){
            return true;
        }
        return false;
    }

    public void Connect() throws URISyntaxException {
        socket = IO.socket("http://192.168.0.105:3030");
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.d("Adam", "SOCKET - Connected");
                socket.emit("hello", "hi");
                //socket.disconnect();
            }

        }).on("event", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.d("Adam", "SOCKET - Call?");
            }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.d("Adam", "SOCKET - Disconnected");
            }

        });
        socket.connect();
        Log.d("Adam", "CONNECTING TO SOCKET");
    }

    public void Send(String message_type, JSONObject data){
        Send(message_type, data.toString());
    }
    public void Send(String message_type, String strOut){
        socket.emit(message_type, strOut);
    }


}

