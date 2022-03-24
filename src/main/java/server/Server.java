package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import dbconnection.DBConnection;

public class Server {

    private static ArrayList<String> users = new ArrayList<String>();
    private static ArrayList<MessagingThread> clients = new ArrayList<MessagingThread>();

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(8081, 10);
        System.out.println("Now Server Is Running");
        while (true) {
            System.out.println("I'm in");
            Socket client = server.accept();
            System.out.println("added");
            MessagingThread thread = new MessagingThread(client);
            clients.add(thread);
            thread.start();
        }
    }
    public static void sendToAll(String user, String message) {
        clients.forEach(client->{
        	if (!client.getUser().equals(user)) {
        		client.sendMessage(user, message);
            }else{
            	client.sendToMe(user, message);
            }
        });
    }
    static class MessagingThread extends Thread {

        String user = "";
        BufferedReader input;
        PrintWriter output;

        public MessagingThread(Socket client) throws Exception {

            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintWriter(client.getOutputStream(), true);

            user = input.readLine();
            users.add(user);
            DBConnection.addUserInDB(user);
        }

        public void sendMessage(String chatUser, String msg) {
            output.println(chatUser + ": " + msg);
        }

        public void sendToMe(String chatUser, String msg){
            output.println("You: " + msg);
        }

        public String getUser() {
            return user;
        }

        public void saveInDB(String chatUser, String msg) throws SQLException {
            String msg_id = chatUser + "_" + System.currentTimeMillis();
            DBConnection.chatBackUp(user, msg_id, msg);
        }

        @Override
        public void run() {
            String line;
            try {
                while (true) {
                    line = input.readLine();
                    if (line.equals("end")) {
                        clients.remove(this);
                        users.remove(user);
                        break;
                    }else {
                        sendToAll(user, line);
                        saveInDB(user, line);
                    }
                }
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
