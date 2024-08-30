import java.io.*;
import java.util.*;

public class StringGroupFinder {
    private final List<double[]> linesAsArrays;
    private final Map<Key, List<Integer>> positionsMap;
    private final UnionFind unionFind;
    private final Map<Integer, Set<String>> finalGroups;

    public StringGroupFinder() {
        linesAsArrays = new ArrayList<>();
        positionsMap = new HashMap<>();
        unionFind = new UnionFind();
        finalGroups = new HashMap<>();
    }

    public void readFile(String inputFileName) {
        try (BufferedReader bufferReader = new BufferedReader(new FileReader(inputFileName))) {
            int currentIndex = 0;
            while (bufferReader.ready()) {
                String stringOfNumbers = bufferReader.readLine();
                unionFind.addNewElement();
                String[] arrayOfStringNumbers = stringOfNumbers.split(";");
                double[] arrayOfNumbers = new double[arrayOfStringNumbers.length];

                for (int i = 0; i < arrayOfStringNumbers.length; i++) {
                    if (arrayOfStringNumbers[i].startsWith("\"") && arrayOfStringNumbers[i].endsWith("\"")) {
                        arrayOfStringNumbers[i] = arrayOfStringNumbers[i].substring(1, arrayOfStringNumbers[i].length() - 1);
                        if (arrayOfStringNumbers[i].contains("\"")) {
                            break;
                        }
                    }

                    if (!arrayOfStringNumbers[i].isEmpty()) {
                        arrayOfNumbers[i] = Double.parseDouble(arrayOfStringNumbers[i]);
                        Key key = new Key(arrayOfNumbers[i], i);
                        if (positionsMap.containsKey(key)) {
                            unionFind.union(currentIndex, positionsMap.get(key).get(0));
                        } else {
                            positionsMap.put(key, new ArrayList<>());
                        }
                        positionsMap.get(key).add(currentIndex);
                    }
                }
                linesAsArrays.add(arrayOfNumbers);
                currentIndex++;
            }
            positionsMap.clear();


            for (int i = 0; i < linesAsArrays.size(); i++) {
                int root = unionFind.findRoot(i);
                finalGroups.putIfAbsent(root, new HashSet<>());
                String s = getLineFromArray(linesAsArrays.get(i));
                finalGroups.get(root).add(s);
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static class Key {
        private final double number;
        private final int position;

        public Key(double number, int position) {
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

    private String getLineFromArray(double[] lineAsArray) {
        StringBuilder stringOfNumbers = new StringBuilder();
        for (double number : lineAsArray) {
            stringOfNumbers.append("\"");
            if (number != 0) stringOfNumbers.append(String.format("%.17f", number).replaceAll("0+$", ""));
            if (stringOfNumbers.substring(stringOfNumbers.length() - 1, stringOfNumbers.length()).equals(",")) {
                stringOfNumbers.deleteCharAt(stringOfNumbers.length() - 1);
            }
            stringOfNumbers.append("\";");
        }
        return stringOfNumbers.toString();
    }
}