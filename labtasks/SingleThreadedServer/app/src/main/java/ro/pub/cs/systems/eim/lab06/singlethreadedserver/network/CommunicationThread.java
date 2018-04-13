package ro.pub.cs.systems.eim.lab06.singlethreadedserver.network;

import android.util.Log;
import android.widget.EditText;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.lab06.singlethreadedserver.general.Constants;
import ro.pub.cs.systems.eim.lab06.singlethreadedserver.general.Utilities;

/**
 * Created by student on 13.04.2018.
 */

public class CommunicationThread extends Thread {

    private Socket socket;
    private EditText serverTextEditText;

    public CommunicationThread(Socket socket, EditText serverTextEditText) {
        this.socket = socket;
        this.serverTextEditText = serverTextEditText;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException interruptedException) {
            Log.e(Constants.TAG, interruptedException.getMessage());
            if (Constants.DEBUG) {
                interruptedException.printStackTrace();
            }
        }

        PrintWriter printWriter = null;
        try {
            printWriter = Utilities.getWriter(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        printWriter.println(serverTextEditText.getText().toString());
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v(Constants.TAG, "Connection closed");
    }
}
