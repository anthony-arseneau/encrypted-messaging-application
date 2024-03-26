package src;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChatClientSender {
        private Socket socket; // The socket of the client is using to transfer data
        private BufferedWriter bufferedWriter; // Write messages to the server
        private MessageDigest digest;

        public ChatClientSender(Socket socket, String username, String password) {
            try {
                digest = MessageDigest.getInstance("SHA-256");

                // Hash username and get hex string value
                byte[] usernameHashed = digest.digest(username.getBytes(StandardCharsets.UTF_8));
                String usernameHashedHex = bytesToHex(usernameHashed);

                // Hash password and get hex string value
                byte[] passwordHashed = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                String passwordHashedHex = bytesToHex(passwordHashed);

                this.socket = socket; // Current IP address is local for testing
                this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                

                sendMessage(usernameHashedHex + " " + passwordHashedHex);

            } catch (NoSuchAlgorithmException | IOException e) {
                System.err.println(e);
                closeAll();
            }
        }

        private static String bytesToHex(byte[] hash) {
            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }

        /*
        public void sendUsername(String username) {
            try {
                if (socket.isConnected()) {
                    bufferedWriter.write(username);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            } catch (IOException e) {
                // Error handling
                closeAll();
            }
        }*/

        /**
         * Method to send messages to the server
         */
        public void sendMessage(String message) {
            try {
                if (socket.isConnected()) {
                    bufferedWriter.write(message);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            } catch (IOException e) {
                // Error handling
                closeAll();
            }
        }

        /**
         * Close the socket, buffered reader, and buffered writer
         * @param socket the socket the client-server is connected to
         * @param bufferedReader the buffered reader to read the incomming messages
         * @param bufferedWriter the buffered writer to send messages to the rest of the connected clients
         */
        public void closeAll() {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                // Error handling
                e.printStackTrace();
            }
        }
}
