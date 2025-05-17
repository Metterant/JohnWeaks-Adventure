package util.pathfinding;

public class Node {
    public final int row;
    public final int col;

    public double h;
    public double g;
    public double f;

    public Node parent;

    public boolean isBlocked, isPath;

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public String toString() {
        String format = "[N: %d, %d]";
        return String.format(format, row, col);
    }
}
