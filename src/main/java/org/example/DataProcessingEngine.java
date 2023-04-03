package org.example;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataProcessingEngine {
    private static void writeArticleToFile(FileWriter fileWriter, String title, String content) throws IOException {
        fileWriter.write("Title: " + title + "\n");
        fileWriter.write("Content: " + content + "\n\n");
    }
    public static void processData(String rawData) {

        Pattern articlesPattern = Pattern.compile("\"articles\":\\s*\\[(.+?)\\]", Pattern.DOTALL);
        Matcher articlesMatcher = articlesPattern.matcher(rawData);
        if (articlesMatcher.find()) {
            String articlesString = articlesMatcher.group(1);

            //Pattern articlePattern = Pattern.compile("(\\{(?:[^{}]|(?1))*\\})", Pattern.DOTALL);
            Pattern articlePattern = Pattern.compile("\\{(.+?)\\}", Pattern.DOTALL);
            Matcher articleMatcher = articlePattern.matcher(articlesString);
            int fileNumber = 0;
            int articleCount = 0;

            File file = new File("news_data_" + (fileNumber++) + ".txt");

            while (articleMatcher.find()) {
                try (FileWriter fileWriter = new FileWriter(file, true)) {
                    String article = articleMatcher.group(1);

                    Pattern titlePattern = Pattern.compile("\"title\":\\s*\"(.+?)\"");
                    Matcher titleMatcher = titlePattern.matcher(articlesString);
                    if(articleCount <= 5){
                        if (titleMatcher.find()) {
                            String title = titleMatcher.group(1);
                            fileWriter.write("Title: " + title + "\n");
                        }

                        Pattern contentPattern = Pattern.compile("\"content\":\\s*\"(.+?)\"");
                        Matcher contentMatcher = contentPattern.matcher(articlesString);
                        if (contentMatcher.find()) {
                            String content = contentMatcher.group(1);

                            fileWriter.write("Content: " + content + "\n\n");
                        }
                        articleCount++;
                    }

                    if (articleCount >= 5) {
                        TransformationEngine.transformData(file);
                        file = new File("news_data_" + (fileNumber++) + ".txt");
                        articleCount = 0;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (articleCount > 0) {
                TransformationEngine.transformData(file);
            }
        }
    }

}
