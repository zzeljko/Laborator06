package ro.pub.cs.systems.eim.lab06.pheasantgame.network;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Random;

import ro.pub.cs.systems.eim.lab06.pheasantgame.general.Constants;
import ro.pub.cs.systems.eim.lab06.pheasantgame.general.Utilities;

public class ServerCommunicationThread extends Thread {

    private Socket socket;
    private TextView serverHistoryTextView;

    private Random random = new Random();

    private String expectedWordPrefix = new String();

    public ServerCommunicationThread(Socket socket, TextView serverHistoryTextView) {
        if (socket != null) {
            this.socket = socket;
            Log.d(Constants.TAG, "[SERVER] Created communication thread with: " + socket.getInetAddress() + ":" + socket.getLocalPort());
        }
        this.serverHistoryTextView = serverHistoryTextView;
    }

    public void run() {
        try {
            if (socket == null) {
                return;
            }
            boolean isRunning = true;
            BufferedReader requestReader = Utilities.getReader(socket);
            PrintWriter responsePrintWriter = Utilities.getWriter(socket);

            String lastWord = requestReader.readLine();
            while (isRunning) {

                // TODO exercise 7a
                String currentWord = requestReader.readLine();
                Log.d("bla", currentWord);

                serverHistoryTextView.setText(currentWord);

                if (Utilities.wordValidation(currentWord)) {

                    if (currentWord.substring(0, 2).equals(lastWord.substring(lastWord.length() - 2))) {

                        expectedWordPrefix = currentWord.substring(lastWord.length() - 2);
                        List<String> possibleWords = Utilities.getWordListStartingWith(expectedWordPrefix);
                        if (possibleWords == null || possibleWords.isEmpty()) {
                            responsePrintWriter.println(Constants.END_GAME);
                            isRunning = false;
                        } else {

                            int index = random.nextInt(possibleWords.size());
                            responsePrintWriter.println(possibleWords.get(index));
                        }
                    } else responsePrintWriter.println(currentWord);
                } else responsePrintWriter.println(currentWord);

                if (currentWord.equals(Constants.END_GAME))
                    isRunning = false;

                lastWord = currentWord;
            }
            socket.close();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }
}
