package util.pathfinding;

public class TileCoords {
    private final int row;
    private final int col;
    
    public TileCoords(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return : The row in the tileMap
     */
    public int getRow() { return row; }
    /**
     * @return : The column in the tileMap
     */
    public int getCol() { return col; }

    public String toString() {
        String format = "[TileCoords=[Row: %d, Col: %d]]";
        return String.format(format, row, col);
    }
}
