package DBconnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class getQuiz {

  public String imagePath;
  public String genre;
  public String question;
  public String answer;
  
  public void GetData(){
    try {
      // PHPファイルのURL
      URL url = new URL("http://localhost/minhaya/quiz_get.php");
      // HTTPリクエストの作成
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      // レスポンスを取得
      BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String inputLine;
      StringBuilder response = new StringBuilder();
      while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
      }
      in.close();

      //JSON解析
      JsonAnalyzer(response);

    } catch (IOException e){
        e.printStackTrace();
    } 
  }

  private void JsonAnalyzer(StringBuilder response){
    System.out.println(response.toString());
    JSONArray jsonArray = new JSONArray(response.toString());
    for (int i = 0; i < jsonArray.length(); i++){
      JSONObject jsonObject = jsonArray.getJSONObject(i);
      this.imagePath = jsonObject.getString("image_path");
      this.genre = jsonObject.getString("genre");
      this.question = jsonObject.getString("question");
      this.answer = jsonObject.getString("answer");

      // ここで取得したデータを利用して何かを行う
      System.out.println("Image Path: " + imagePath);
      System.out.println("Genre: " + genre);
      System.out.println("Question: " + question);
      System.out.println("Answer: " + answer);
    }
  }
}
