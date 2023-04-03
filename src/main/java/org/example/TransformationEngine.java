package org.example;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;
public class TransformationEngine {

    public static void transformData(File file) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("myMongoNews");
        MongoCollection<Document> collection = database.getCollection("newsArticles");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();

            String content = sb.toString();
            System.out.println((content));
            content = cleanData(content);

            String[] articles = content.split("\n\n");

            for (String article : articles) {
                System.out.println((article));
                String[] parts = article.split("\n");
                String title = parts[0].replace("Title: ", "").trim();
                String newsContent = "";
                if(parts.length >= 2)
                    newsContent += parts[1].replace("Content: ", "").trim();

                Document document = new Document();
                document.put("title", title);
                document.put("content", newsContent);
                collection.insertOne(document);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String cleanData(String data) {
        data = data.replaceAll("[^\\x00-\\x7F]", "");
        data = data.replaceAll("http[^\\s]+", "");
        data = data.replaceAll("[^\\w\\s\\.\\,]", "");
        return data;
    }
}
