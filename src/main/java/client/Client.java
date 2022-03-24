package client;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


import dbconnection.DBConnection;

public class Client extends JFrame implements ActionListener {

	String username;
    PrintWriter printWriter;
    BufferedReader bufferedReader;
    JTextArea  chatmsg;
    JTextField chatip;
    JButton send, exit;
    Socket chatusers;
    Map<String, String> historyMap = new HashMap<>();
    public Client(String uname,String servername) throws Exception {
        super(uname);
        this.username = uname;
        chatusers  = new Socket(servername,8081);
        bufferedReader = new BufferedReader( new InputStreamReader( chatusers.getInputStream()) ) ;
        printWriter = new PrintWriter(chatusers.getOutputStream(),true);
        printWriter.println(uname);
        buildInterface();
        new MessagesThread().start();
    }

    public void buildInterface() throws SQLException {
        send = new JButton("Send");
        exit = new JButton("Exit");
        chatmsg = new JTextArea();
        chatmsg.setRows(30);
        chatmsg.setColumns(50);
        historyMap = DBConnection.get100Message();
        for (Map.Entry<String,String> entry : historyMap.entrySet()) {
        	chatmsg.append(entry.getKey()+":");
        	chatmsg.append(entry.getValue());
        	chatmsg.append("\n");
        	
        }
       
        chatmsg.setEditable(false);
        chatip  = new JTextField(50);
        JScrollPane sp = new JScrollPane(chatmsg, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(sp,"Center");
        JPanel jPanel = new JPanel( new FlowLayout());
        jPanel.add(chatip);

        jPanel.add(send);
        jPanel.add(exit);
        jPanel.setBackground(Color.RED);
        jPanel.setName("Instant Messenger");
        add(jPanel,"North");
        send.addActionListener(this);
        exit.addActionListener(this);
        setSize(500,300);
        setVisible(true);
        pack();
    }

   
    public void actionPerformed(ActionEvent evt) {
        if ( evt.getSource() == exit ) {
        	printWriter.println("end");
            System.exit(0);
        }else if(evt.getSource() == send){
        	printWriter.println(chatip.getText());
            chatip.setText(null);
        } 
    }

    class  MessagesThread extends Thread {
        @Override
        public void run() {
            String line;
            
            try {
                    while(true) {
                        line = bufferedReader.readLine();
                        chatmsg.append(line + "\n");
                    }
            } catch(Exception ignored) {}
        }
    }

    public static void main(String[] args) {
        String userName = JOptionPane.showInputDialog(null,"Please enter your name to begin:", "Chat Application",
                JOptionPane.PLAIN_MESSAGE);
        String servername = "localhost";
        try {
            new Client( userName ,servername);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
