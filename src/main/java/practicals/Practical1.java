package practicals;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Practical1 {

        public static void main(String[] args) throws Exception {
            String fullBook = readBooks("src/main/resources");
            String formattedBook = removeSpecialSymbols(fullBook);
            Map<String, Integer> vocabulary = buildVocabulary(formattedBook);
            writeToFile(vocabulary, "src/main/resources/output.txt");
        }

        private static String readBooks(String books) throws IOException {
            StringBuilder fullBook = new StringBuilder();

            try (Stream<Path> paths = Files.walk(Paths.get(books))) {
                paths
                        .filter(Files::isRegularFile)
                        .map(Path::toFile)
                        .map(Practical1::readLines)
                        .forEach(fullBook::append);
            }
            return fullBook.toString();
        }

        private static String readLines(File file)  {
            StringBuilder fullBook = new StringBuilder();
            String line;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
                line = br.readLine();
                while( line != null){
                    fullBook.append(line);
                    line = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return fullBook.toString();

        }

        private static String removeSpecialSymbols(String fullBook) {
            return fullBook
                    .replaceAll("\\.|\\]|\\[|[0-9]|,|\\?|:|\\(|\\)|;|-|!", "")
                    .toLowerCase();
        }

        private static void writeToFile(Map<String, Integer> frequencyMap, String file) throws IOException {
            try (BufferedWriter writer = new BufferedWriter
                    (new FileWriter(file))) {

                for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
                    writer.write(entry.getKey() + " = " + entry.getValue());
                    writer.newLine();
                }
            }
        }

        private static Map<String, Integer> buildVocabulary(String formattedBook) {
            Pattern word = Pattern.compile("[\\w]+");

            Matcher m = word.matcher(formattedBook);
            Map<String, Integer> frequencyMap = new TreeMap<>();
            int counter = 0;

            while (m.find())
            {
                counter++;
                frequencyMap.put(m.group(), frequencyMap.getOrDefault(m.group(), 0) + 1);
            }
            System.out.println("Unique words : " + frequencyMap.size());
            System.out.println("Total words : " + counter);
            return frequencyMap;
        }
    }
