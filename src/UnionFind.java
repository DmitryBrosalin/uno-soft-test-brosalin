import java.util.ArrayList;
import java.util.List;

public class UnionFind {
    private final List<Integer> roots;
    private final List<Integer> ranks;

    public UnionFind() {
        roots = new ArrayList<>();
        ranks = new ArrayList<>();
    }

    public void addNewElement() {
        int index = roots.size();
        roots.add(index);
        ranks.add(0);
    }

    public int findRoot(int indexOfString) {
        if (roots.get(indexOfString) != indexOfString) {
            roots.set(indexOfString, findRoot(roots.get(indexOfString)));
        }
        return roots.get(indexOfString);
    }

    public void union(int indexOfString1, int indexOfString2) {
        int root1 = findRoot(indexOfString1);
        int root2 = findRoot(indexOfString2);
        if (root1 == root2) return;

        int rank1 = ranks.get(indexOfString1);
        int rank2 = ranks.get(indexOfString2);
        if (rank1 > rank2) {
            roots.set(root2, root1);
        } else if (rank2 > rank1) {
            roots.set(root1, root2);
        } else {
            roots.set(root2, root1);
            ranks.set(indexOfString1, rank1 + 1);
        }
    }
}
