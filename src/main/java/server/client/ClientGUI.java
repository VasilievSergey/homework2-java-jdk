package server.client;

import server.server.ServerWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientGUI extends JFrame implements ClientView{
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;

    private Client client;

    JTextArea log;
    JTextField tfIPAddress, tfPort, tfLogin, tfMessage;
    JPasswordField password;
    JButton btnLogin, btnSend;
    JPanel topPanel;

    public ClientGUI(ServerWindow serverWindow){
        setting(serverWindow);
        createPanel();

        setVisible(true);
    }

    private void setting(ServerWindow server) {
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat client");
        setLocation(server.getX() - 500, server.getY());
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        client = new Client(this, server.getConnection());
    }

    private void hideHeaderPanel(boolean visible){
        topPanel.setVisible(visible);
    }

    public void sendMessage(){
        client.sendMessage(tfMessage.getText());
        tfMessage.setText("");
    }

    private void createPanel() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createLog());
        add(createFooter(), BorderLayout.SOUTH);
    }

    private Component createHeaderPanel(){
        topPanel = new JPanel(new GridLayout(2, 3));
        tfIPAddress = new JTextField("127.0.0.1");
        tfPort = new JTextField("8080");
        tfLogin = new JTextField("Ivan Ivanovich");
        password = new JPasswordField("password");
        btnLogin = new JButton("login");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectedToServer();
            }
        });

        topPanel.add(tfIPAddress);
        topPanel.add(tfPort);
        topPanel.add(new JPanel());
        topPanel.add(tfLogin);
        topPanel.add(password);
        topPanel.add(btnLogin);

        return topPanel;
    }

    private Component createLog(){
        log = new JTextArea();
        log.setEditable(false);
        return new JScrollPane(log);
    }

    private Component createFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        tfMessage = new JTextField();
        tfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n'){
                    sendMessage();
                }
            }
        });
        btnSend = new JButton("send");
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        panel.add(tfMessage);
        panel.add(btnSend, BorderLayout.EAST);
        return panel;
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING){
            disconnectedFromServer();
        }
    }

    @Override
    public void sendMessage(String message) {
        log.append(message);
    }

    @Override
    public void connectedToServer() {
        if (client.connectToServer(tfLogin.getText())){
            hideHeaderPanel(false);
        }
    }

    @Override
    public void disconnectedFromServer() {
        hideHeaderPanel(true);
        client.disconnectFromServer();
    }
}