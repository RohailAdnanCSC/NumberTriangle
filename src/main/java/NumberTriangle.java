import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the provided NumberTriangle class to be used in this coding task.
 *
 * Note: This is like a tree, but some nodes in the structure have two parents.
 *
 * The structure is shown below. Observe that the parents of e are b and c, whereas
 * d and f each only have one parent. Each row is complete and will never be missing
 * a node. So each row has one more NumberTriangle object than the row above it.
 *
 *                  a
 *                b   c
 *              d   e   f
 *            h   i   j   k
 *
 * Also note that this data structure is minimally defined and is only intended to
 * be constructed using the loadTriangle method, which you will implement
 * in this file. We have not included any code to enforce the structure noted above,
 * and you don't have to write any either.
 *
 *
 * See NumberTriangleTest.java for a few basic test cases.
 *
 * Extra: If you decide to solve the Project Euler problems (see main),
 *        feel free to add extra methods to this class. Just make sure that your
 *        code still compiles and runs so that we can run the tests on your code.
 *
 */
public class NumberTriangle {

    private int root;
    private NumberTriangle left;
    private NumberTriangle right;

    public NumberTriangle(int root) {
        this.root = root;
    }

    public void setLeft(NumberTriangle left) {
        this.left = left;
    }

    public void setRight(NumberTriangle right) {
        this.right = right;
    }

    public int getRoot() {
        return root;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }

    public int retrieve(String path) {
        if (path.isEmpty()) return root;

        char dir = path.charAt(0);
        String rest = path.substring(1);

        if (dir == 'l' && left != null) {
            return left.retrieve(rest);
        } else if (dir == 'r' && right != null) {
            return right.retrieve(rest);
        }

        throw new IllegalArgumentException("Invalid path: " + path);
    }

    public void maxSumPath() {
        if (isLeaf()) return;

        if (left != null) left.maxSumPath();
        if (right != null) right.maxSumPath();

        int leftVal = left != null ? left.root : 0;
        int rightVal = right != null ? right.root : 0;

        root += Math.max(leftVal, rightVal);

        left = null;
        right = null;
    }

    public static NumberTriangle loadTriangle(String fname) throws IOException {
        InputStream inputStream = NumberTriangle.class.getClassLoader().getResourceAsStream(fname);
        if (inputStream == null) {
            throw new FileNotFoundException("File not found: " + fname);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        List<List<NumberTriangle>> rows = new ArrayList<>();

        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");
            List<NumberTriangle> currentRow = new ArrayList<>();
            for (String num : parts) {
                currentRow.add(new NumberTriangle(Integer.parseInt(num)));
            }
            rows.add(currentRow);
        }
        br.close();

        for (int r = 0; r < rows.size() - 1; r++) {
            List<NumberTriangle> current = rows.get(r);
            List<NumberTriangle> next = rows.get(r + 1);
            for (int c = 0; c < current.size(); c++) {
                NumberTriangle node = current.get(c);
                node.setLeft(next.get(c));
                node.setRight(next.get(c + 1));
            }
        }

        return rows.get(0).get(0);
    }

    public static void main(String[] args) throws IOException {
        NumberTriangle mt = NumberTriangle.loadTriangle("input_tree.txt");

        System.out.println("Root before maxSumPath(): " + mt.getRoot());

        mt.maxSumPath();
        System.out.println("Root after maxSumPath(): " + mt.getRoot());
    }
}
