package com.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import java.util.Random;

import minhaya.MainGame;

public class MinhayaServer{

    public static final String PROGRAM_TITLE = "Minhaya Server 2024";

    private ServerSocket serverSocket;

    //表示関係
    private SimpleCUIFrame cuiframe;

    //通信関係
    private int waitingPort;
    private boolean waiting = false;
    private int connectedCount;

    private MainGame game;

    public static void main(String[] args) {
        MinhayaServer server = new MinhayaServer();
    }

    public MinhayaServer(){
        this.cuiframe = new SimpleCUIFrame(this);
        this.cuiframe.setTitle(PROGRAM_TITLE);
        this.cuiframe.setVisible(true);
    }

    public static ServerSocket getWatingPort() {
        int PORT = 80; // サーバーの待ち受けポート
        ServerSocket soc = null;
        for (int i = 0; i < 100; i++) {
            try {
                soc = new ServerSocket(PORT);
                return soc;
            } catch (IOException e) {
            }
            PORT++;
        }
        return soc;
    }

    /** NOT DEBUGED */
    public void resetServer(){
        try {
            this.serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(MinhayaServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.cuiframe.setVisible(false);
        this.cuiframe.dispose();
        
                this.cuiframe = new SimpleCUIFrame(this);
        this.cuiframe.setTitle(PROGRAM_TITLE);
        this.cuiframe.setVisible(true);
        long seed = Math.abs(new Random(new Date().getTime()).nextInt());
    }

    private void initServer() {
        this.printMessage("Server 起動完了 MINHAYA");
        this.connectedCount = 0;
        this.serverSocket = MinhayaServer.getWatingPort();
        if (this.serverSocket == null) {
            this.printMessage("待ち受けが開始できません。");
            return;
        }
        this.waitingPort = this.serverSocket.getLocalPort();
        this.printMessage(this.waitingPort + "で待ち受け開始しました。");
        this.waiting = true;
    }

    public void initWithSeed() {
        this.initServer();

        //待ち受け開始
        while (this.waiting) {
            try {
                Socket csoc = this.serverSocket.accept();
                this.printMessage(csoc.getInetAddress() + "が接続しました。");
                //開始ボタンで始めるようにする
                if (connectedCount >= 1) {
                    //ゲームの準備が完了
                    this.gameInit();
                }

                //子スレッドスタート
            } catch (IOException ex) {
                this.waiting = false;
            }
        }
    }

    private void gameInit() {
        this.game = new MainGame();
    }

    public void printMessage(String message) {
        this.cuiframe.addMessage(message);
        System.out.println(message);
    }
}
