package minhaya;

import org.glassfish.tyrus.server.Server;

import DBconnet.getQuiz;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ServerEndpoint("/websocket")
public class WebSocketServer {

    private int currentQuestionIndex = -1;
    getQuiz quiz = new getQuiz();

    public WebSocketServer() {
        // データベースから問題を取得するメソッドを呼び出して、questionsリストに問題を格納する
        quiz.GetData();
        // ここではダミーデータを追加しています
        // questions.add("問題1: 1 + 1 は？");
        // questions.add("問題2: 東京の首都は？");
        // 他の問題も追加する...
    }
    
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("Received message: " + message);
        // session.getBasicRemote().sendText("Server received your message: " + message);
        if (message.equals("request_question")) {
            sendNextQuestion(session);
        } else if (message.startsWith("回答:")) {
            // クライアントからの回答を受け取り、正誤判定を行うメソッドを呼び出す
            String answer = message;
            checkAnswerAndSendResult(answer, session);
        }
    }

    private void sendNextQuestion(Session session) throws IOException {
        currentQuestionIndex++;
        System.out.println("次の問題を表示します");
        if (currentQuestionIndex < quiz.questions.size()) {
            System.out.println("currentQuestion: " + quiz.questions.get(currentQuestionIndex));
            String question = quiz.questions.get(currentQuestionIndex);
            session.getBasicRemote().sendText("問題:"+question);
        } else {
            session.getBasicRemote().sendText("問題はこれで終了です。");
            // ここで必要な後処理を行う（例：最終結果を表示するなど）
        }
    }

    private void checkAnswerAndSendResult(String answer, Session session) throws IOException {
        // 回答を正誤判定する処理を実装し、結果をクライアントに送信する
        // ここでは単純化して、正解は"2"とする
        String result;
        System.out.println("currentAnswer: " + quiz.answers.get(currentQuestionIndex));
        if (answer.equals("回答:"+quiz.answers.get(currentQuestionIndex))) {
            result = "正解です！次の問題を送信します。";
            System.out.println("正解です");
        } else {
            result = "不正解です。";
            System.out.println("不正解です");
        }
        session.getBasicRemote().sendText(result);
        // if (!result.equals("不正解です。")) {
        //     sendNextQuestion(session);
        // }
    }

    public void StartServer() {
        Server server = new Server("192.168.11.14", 8080, "", null, WebSocketServer.class);
        try {
            server.start();
            System.out.println("WebSocket server started...");
            Thread.sleep(Long.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}