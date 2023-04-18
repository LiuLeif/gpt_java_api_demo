import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.*;


public class ChatGPTClient {
	private static final String API_URL = "https://api.openai.com/v1/chat/completions";

	private static String getPrompt(int number) {
		System.out.println(number + ". Please input your question:");
		Scanner sc=new Scanner(System.in);  
		return sc.nextLine();
	}

    public static void main(String[] args) throws Exception {
        String apiKey = "YOUR_API_KEY";
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("172.16.10.8", 7890));
        OkHttpClient client = new OkHttpClient.Builder().proxy(proxy).build();

		JSONObject json = new JSONObject();
		json.put("model", "gpt-3.5-turbo");
		json.put("temperature", 0.4f);
		JSONArray messages = new JSONArray();
		JSONObject sysMsg = new JSONObject();
		sysMsg.put("role", "system");
		sysMsg.put("content", "You are a helpful assistant.");
		messages.put(sysMsg);


		for(int i = 1; i < 4; i++) {
			String prompt = getPrompt(i);
			JSONObject promptMsg = new JSONObject();
			promptMsg.put("role", "user");
			promptMsg.put("content", prompt);
			messages.put(promptMsg);
			json.put("messages", messages);

			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(json.toString(), mediaType);
			Request request = new Request.Builder()
					.url(API_URL)
					.post(body)
					.addHeader("Authorization", "Bearer " + apiKey)
					.addHeader("Content-Type", "application/json")
					.build();
			Response response = client.newCall(request).execute();

			String responseBody = response.body().string();
			JSONObject jsonObject = new JSONObject(responseBody);
			String content = jsonObject.getJSONArray("choices")
					.getJSONObject(0)
					.getJSONObject("message")
					.getString("content");

			System.out.println(content);
		}
		System.out.println("Finish demo!");
    }
}