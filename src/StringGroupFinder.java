import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class StringGroupFinder {
    private List<long[]> linesAsArrays;
    private Map<Key, List<Integer>> positionsMap;
    UnionFind unionFind;

    public StringGroupFinder() {
        linesAsArrays = new ArrayList<>();
        positionsMap = new HashMap<>();
        unionFind = new UnionFind();
    }

    public void readFile(String inputFileName) {
        try (BufferedReader bufferReader = new BufferedReader(new FileReader(inputFileName))) {
            int currentIndex = 0;
            while (bufferReader.ready()) {
                String stringOfNumbers = bufferReader.readLine();
                unionFind.addNewElement();
                String[] arrayOfStringNumbers = stringOfNumbers.split(";");
                long[] arrayOfNumbers = new long[arrayOfStringNumbers.length];

                for (int i = 0; i < arrayOfStringNumbers.length; i++) {
                    if (arrayOfStringNumbers[i].startsWith("\"") && arrayOfStringNumbers[i].endsWith("\"")) {
                        arrayOfStringNumbers[i] = arrayOfStringNumbers[i].substring(1, arrayOfStringNumbers[i].length() - 1);
                        if (arrayOfStringNumbers[i].contains("\"")) {
                            break;
                        }
                    }

                    if (!arrayOfStringNumbers[i].isEmpty()) {
                        arrayOfNumbers[i] = Long.parseLong(arrayOfStringNumbers[i]);
                        Key key = new Key (arrayOfNumbers[i], i);
                        if (positionsMap.containsKey(key)) {
                            for (int index : positionsMap.get(key)) {
                                unionFind.union(currentIndex, index);
                            }
                        }
                        positionsMap.computeIfAbsent(key, k -> new ArrayList<>()).add(currentIndex);
                    }
                }
                linesAsArrays.add(arrayOfNumbers);
                currentIndex++;

            }
            positionsMap.clear();

            Map<Integer, List<String>> finalGroups = new HashMap<>();
            for (int i = 0; i < linesAsArrays.size(); i++) {
                int root = unionFind.findRoot(i);
                finalGroups.putIfAbsent(root, new ArrayList<>());
                finalGroups.get(root).add(getLineFromArray(linesAsArrays.get(i)));
            }

            List<List<String>> sortedGroups = new ArrayList<>(finalGroups.values());
            sortedGroups.removeIf(group -> group.size() <= 1);
            sortedGroups.sort((g1, g2) -> Integer.compare(g2.size(), g1.size()));

            System.out.println("Всего групп с более чем одним элементом: " + sortedGroups.size());
            int groupId = 1;
            for (List<String> entry : sortedGroups) {
                System.out.println("Группа " + groupId++);
                for (String string : entry) {
                    System.out.println(string);
                }
                System.out.println();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    class Key {
        private final long number;
        private final int position;

        public Key(long number, int position) {
            this.number = number;
            this.position = position;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return number == key.number && position == key.position;
        }

        @Override
        public int hashCode() {
            return Objects.hash(number, position);
        }
    }

    private String getLineFromArray(long[] lineAsArray) {
        StringBuilder stringOfNumbers = new StringBuilder();
        for (long number : lineAsArray) {
            stringOfNumbers.append("\"");
            if (number != 0) stringOfNumbers.append(number);
            stringOfNumbers.append("\";");
        }
        stringOfNumbers.deleteCharAt(stringOfNumbers.length() - 1);
        return stringOfNumbers.toString();
    }
}