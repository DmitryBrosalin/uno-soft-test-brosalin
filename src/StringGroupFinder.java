import java.io.*;
import java.util.*;

public class StringGroupFinder {
    private final Map<String, List<Integer>> positionsMap;
    private final UnionFind unionFind;
    private final Map<Integer, Set<String>> finalGroups;
    private final List<String> lines;

    public StringGroupFinder() {
        positionsMap = new HashMap<>();
        unionFind = new UnionFind();
        finalGroups = new HashMap<>();
        lines = new ArrayList<>();
    }

    public void readFile(String inputFileName) {
        try (BufferedReader bufferReader = new BufferedReader(new FileReader(inputFileName))) {
            int currentIndex = 0;
            String stringOfNumbers;
            while ((stringOfNumbers = bufferReader.readLine()) != null) {
                unionFind.addNewElement();
                String[] arrayOfStringNumbers = stringOfNumbers.split(";");

                for (int i = 0; i < arrayOfStringNumbers.length; i++) {
                    String number = arrayOfStringNumbers[i];
                    if (number.startsWith("\"") && number.endsWith("\"")) {
                        number = number.substring(1, number.length() - 1);
                        if (number.contains("\"")) {
                            break;
                        }
                    }

                    if (!number.isEmpty()) {
                        String key = number + "_" + i;
                        if (positionsMap.containsKey(key)) {
                            unionFind.union(currentIndex, positionsMap.get(key).get(0));
                        } else {
                            positionsMap.put(key, new ArrayList<>());
                        }
                        positionsMap.get(key).add(currentIndex);
                    }
                }
                lines.add(stringOfNumbers);
                currentIndex++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        positionsMap.clear();

        for (int i = 0; i < lines.size(); i++) {
            int root = unionFind.findRoot(i);
            if (!finalGroups.containsKey(root)) {
                finalGroups.put(root, new HashSet<>());
            }
            finalGroups.get(root).add(lines.get(i));
        }

        List<Set<String>> sortedGroups = new ArrayList<>(finalGroups.values());
        sortedGroups.removeIf(group -> group.size() <= 1);
        sortedGroups.sort((g1, g2) -> Integer.compare(g2.size(), g1.size()));

        try (Writer fileWriter = new FileWriter("outputFile.txt")) {
            fileWriter.write("Всего групп с более чем одним элементом: " + sortedGroups.size() + "\n");
            int groupId = 1;
            for (Set<String> entry : sortedGroups) {
                fileWriter.write("Группа " + groupId++ + "\n");
                for (String string : entry) {
                    fileWriter.write(string + "\n");
                }
                fileWriter.write("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}