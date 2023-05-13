package net.naumen.springbackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

@Service
public class NameService {

    private static HashMap<String, Integer> userMap = new HashMap<>();

    @PostConstruct
    public void init() {
        try(BufferedReader reader = new BufferedReader(new FileReader("src/main/Name.txt"))){
            String line;
            while((line = reader.readLine()) != null){
                String[] str = line.split("_");
                addNameAge(str[0], Integer.parseInt(str[1]));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addNameAge(String name, int age){
        userMap.put(name,age);
    }

    public static HashMap<String, Integer> getNameMap() {
        return userMap;
    }

    public int getAgeFromApi(String username) {
        try{
            URL url = new URL("https://api.agify.io/?name=" + username);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }

                scanner.close();

                ObjectMapper objectMapper = new ObjectMapper();

                JsonNode root = objectMapper.readTree(String.valueOf(informationString));

                return root.get("age").asInt();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
