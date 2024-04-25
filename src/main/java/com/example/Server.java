package com.example;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

// Webソケットのサーバ側クラスであること表すアノテーション。
// 引数(wSck)はクライアントから接続時、使われるURIを表す。
@ServerEndpoint(value = "/localhost:3306/minhaya/server.html")
public class Server {
    //クライアントのセッションスレッドを作成(クライアント毎にそれぞれのセッションを保存)
    //Set：重複要素のないコレクション
    //CopyOnWriteArrayList：java.util.Setをスレッドセーフにしたもの
    private static Set<Session> user = new CopyOnWriteArraySet<>();

    @OnOpen//クライアントと接続したとき
    public void onOpen(Session mySession) {
        System.out.println("connect ID:"+mySession.getId());//session.getId():セッションIDを取得
        user.add(mySession);//クライアント毎のセッションをリストに追加
    }

    @OnMessage//クライアントからデータが送信されたとき
    public void onMessage(String text , Session mySession) {//引数は送信されたテキストと送信元のセッション
        System.out.println(text);
        //getAsyncRemote()：RemoteEndpointのインスタンスを取得
        //sendText(String)：クライアントにテキストを送信
        for (Session user : user) {
            user.getAsyncRemote().sendText(text);
            System.out.println(user.getId()+"番目に"+mySession.getId()+"番目のメッセージを送りました！");
        }
        if(text.equals("bye")) onClose(mySession);//textが「bye」なら切断する
    }

    @OnClose//クライアントが切断したとき
    public void onClose(Session mySession) {
        System.out.println("disconnect ID:"+mySession.getId());
        user.remove(mySession);//切断したクライアントのセッションをリストから削除
        try {
            mySession.close();//closeメソッドで切断
        } catch (IOException e) {
            System.err.println("エラーが発生しました: " + e);
        }
    }
}

