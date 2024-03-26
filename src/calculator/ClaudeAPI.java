package calculator;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ClaudeAPI {
    String uriString = "https://api.anthropic.com/v1/messages";
    String systemPrompt = "Only give the mathematical expression. You don't have to calculate. for example like 250*8*7/100. give me the output string only in the format 'Output: <output string>'";
    String apiKey;
    ClaudeAPI(String apiKey){
        this.apiKey = apiKey;
    }
    public boolean isValidAPIKey(){
        HttpResponse<String> response = call("add 3 and 5");
        int statusCode = response.statusCode();
        if(statusCode == 401){
            return false;
        }else{
            return true;
        }
    }

    public String getResult(String prompt){
        HttpResponse<String> response = call(prompt);
        String hi = response.body();
        
        String output = "";

        try {
            JSONObject obj = (JSONObject) new JSONParser().parse(hi);
            JSONArray arr = (JSONArray) obj.get("content");
            JSONObject obj2 = (JSONObject) arr.get(0);
            output = (String) obj2.get("text");
            output = output.substring(8, output.length());
            // System.out.println(si);
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }

        return output;        
    }

    private HttpResponse<String> call(String userPrompt){
        String prompt = "Problem: ```" + userPrompt + "``` " + systemPrompt;
        String jsonPayload = "{\"model\": \"claude-3-opus-20240229\","+
        "\"max_tokens\": 200,"+
        "\"messages\": [{\"role\": \"user\", \"content\": \""+ prompt +"\"}]}";
        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(uriString))
        .header("x-api-key", apiKey)
        .header("anthropic-version", "2023-06-01")
        .header("Content-Type", "application/json")
        .method("POST", HttpRequest.BodyPublishers.ofString(jsonPayload))
        .build();

        HttpResponse<String> response = null;
        try {
        	response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        // System.out.println("response: "+ response);
        return response;
    }

}
