package util.pathfinding;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import tile.TileManager;
import util.GameConstants;

public class PathFinder {
    Node[][] nodes;

    ArrayList<Node> openList = new ArrayList<>();
    ArrayList<Node> closedList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    int noOfRows, noOfCols;
    boolean goalReached;

    int[][] gridData;

    /** Reset gridData */
    void resetGridData() {
        gridData = new int[noOfRows][noOfCols];

        for (int i = 0; i < noOfRows; i++) {
            for (int j = 0; j < noOfCols; j++) {
                if (TileManager.getInstance().getIsCollidable(i, j))
                    gridData[i][j] = 1;
            }
        }
    }

    public PathFinder() {
        // Init row and col
        noOfRows = GameConstants.MAX_SCREEN_ROW;
        noOfCols = GameConstants.MAX_SCREEN_COL;
    }
    
    /** Reset all fields */
    private void reset() {
        resetGridData();
        nodes = new Node[noOfRows][noOfCols];

        for (int i = 0; i < noOfRows; i++) {
            for (int j = 0; j < noOfCols; j++) {
                nodes[i][j] = new Node(i, j);

                nodes[i][j].isBlocked = (gridData[i][j] == 1);
                nodes[i][j].parent = null;
            }
        }
        openList.clear();
        closedList.clear();

        goalReached = false;
    }

    /**
     * Create a path for the ControllableEntity to follow 
     * @param startCoords : The starting TileCoords
     * @param goalCoords : The end TileCoords
     */
    public TileCoords search(TileCoords startCoords, TileCoords goalCoords) {
        reset();

        int startRow = startCoords.getRow();
        int startCol = startCoords.getCol();
        int goalRow = goalCoords.getRow();
        int goalCol = goalCoords.getCol();

        // Check start and goal Nodes
        if (gridData[startRow][startCol] == 1)
            throw new InvalidParameterException("Start Node is blocked");
        if (gridData[goalRow][goalCol] == 1)
            throw new InvalidParameterException("Goal Node is blocked");
        
        // Inits
        currentNode = nodes[startRow][startCol];
        startNode = currentNode;
        goalNode = nodes[goalRow][goalCol];

        // Initialize currentNode (startNode)
        currentNode.g = 0;
        currentNode.h = distance(currentNode, goalNode);
        currentNode.f = currentNode.g + currentNode.h;

        // Add the start Node into the open List
        openList.add(currentNode);
        
        int step = 0;
        while (!openList.isEmpty() && !goalReached && step < 500) {
            double bestNodeFCost = 99999;
            int bestNodeIndex = 0;
            for (int i = 0; i < openList.size(); i++) {
                if (bestNodeFCost > openList.get(i).f) {
                    bestNodeIndex = i;
                    bestNodeFCost = openList.get(i).f;
                }
                else if (bestNodeFCost == openList.get(i).f && openList.get(i).g < openList.get(bestNodeIndex).g) {
                    bestNodeIndex = i;
                }
            }
            currentNode = openList.get(bestNodeIndex);

            // The the goalNode has been found
            if (currentNode == goalNode) {
                goalReached = true;
                break;
            }

            closedList.add(currentNode);
            openList.remove(currentNode);

            exploreNearbyNodes();

            step++;
        }

        // End of the search
        if (!goalReached) {
            return null;
        }
        Node nextNode = findNextNode();
        
        return (nextNode != null) ? new TileCoords(nextNode.row, nextNode.col) : null ; 
    }

    /**
     * Get the distance between 2 Nodes in Euclidean distance
     * @param node1 : the first Node
     * @param node2 : the second Node
     * @return Distance between the two Nodes
     */
    private double distance(Node node1, Node node2) {
        return Math.sqrt((node2.col - node1.col) * (node2.col - node1.col) + (node2.row - node1.row) * (node2.row - node1.row));
    }

    /**
     * Explore all nearby Nodes and try adding the them in the openList
     */
    private void exploreNearbyNodes() {
        int[] dirX = {0, 1, 0, -1, 1, 1, -1, -1};
        int[] dirY = {1, 0, -1, 0, 1, -1, 1, -1};

        for (int i = 0; i < 8; i++) {
            int targetRow = currentNode.row + dirY[i];
            int targetCol = currentNode.col + dirX[i];
            // Skip if already in closedList
            if (isValidLocation(targetRow, targetCol) && !closedList.contains(nodes[targetRow][targetCol])) {
                openNode(nodes[targetRow][targetCol]);
            }
        }
    }

    /**
     * Try opening a Node
     * @param targetNode : the Node that is intended to be opened
     */
    private void openNode(Node targetNode) {
        // Do not process if in closedList
        if (closedList.contains(targetNode)) {
            return;
        }

        double gTentative = currentNode.g + distance(currentNode, targetNode);

        if (!openList.contains(targetNode)) {
            openList.add(targetNode);
        }
        else if (gTentative >= targetNode.g) {
            return; // This path is not better
        }
        // This is the best path so far
        targetNode.parent = currentNode;
        targetNode.g = gTentative;
        targetNode.h = distance(targetNode, goalNode);
        targetNode.f = targetNode.g + targetNode.h;
    }

    /**
    * Check if <P>
    * - the target position coordinates is in-bound <P>
    * - the target position is not blocked <P>
    * - the path is the target position is not blocked by either nodes diagonal to each other  <P>
    */
    private boolean isValidLocation(int targetRow, int targetCol) {
        // Check bounds first
        if (targetRow < 0 || targetRow >= noOfRows || targetCol < 0 || targetCol >= noOfCols)
            return false;
        // Check if the cell is blocked
        if (nodes[targetRow][targetCol].isBlocked)
            return false;
        // Check for diagonal movement and prevent corner cutting
        if (targetRow != currentNode.row && targetCol != currentNode.col) {
            if (nodes[currentNode.row][targetCol].isBlocked || nodes[targetRow][currentNode.col].isBlocked)
                return false;
        }
        return true;
    }

    public void constructPath() {
        Node pathNode = goalNode;
        while (pathNode != null) {
            pathNode.isPath = true;
            System.out.print(pathNode.toString() + "<-");
            pathNode = pathNode.parent;
        }
        System.out.println();
    }

    /**
     * Retrieve the next Node in the path
     * @return : The next Node in the path starting from the startNode 
     */
    private Node findNextNode() {
        Node pathNode = goalNode;
        while (pathNode != null) {
            if (pathNode.parent == null) {
                return null;
            }
            if (pathNode.parent.parent == null) {
                return pathNode;
            }
            pathNode = pathNode.parent;
        }
        return null;
    }

    public void printGrid() {
        if (this.nodes == null) {
            System.out.println("Grid has not been initialized.");
            return;
        }
        for (int i = 0; i < this.noOfRows; i++) {
            for (int j = 0; j < this.noOfCols; j++) {
                Node node = this.nodes[i][j];
                if (node.isPath && node != this.startNode && node != this.goalNode) {
                    System.out.print("* ");
                } else if (node.isBlocked) {
                    System.out.print("X ");
                } else if (node == this.startNode) {
                    System.out.print("S ");
                } else if (node == this.goalNode) {
                    System.out.print("G ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }

}
