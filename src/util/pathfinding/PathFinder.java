package util.pathfinding;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import entity.ControllableEntity;
import entity.Entity;
import tile.TileManager;
import util.GameConstants;

public class PathFinder {
    Node[][] nodes;

    ArrayList<Node> openList = new ArrayList<>();
    ArrayList<Node> closedList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    int noOfRows, noOfCols;
    public boolean goalReached;

    int[][] gridData;

    void setGridData() {
        gridData = new int[noOfRows][noOfCols];

        for (int i = 0; i < noOfRows; i++) {
            for (int j = 0; j < noOfCols; j++) {
                if (TileManager.getInstance().getIsCollidable(i, j))
                    gridData[i][j] = 1;
            }
        }
    }

    public PathFinder() {
        noOfRows = GameConstants.MAX_SCREEN_ROW;
        noOfCols = GameConstants.MAX_SCREEN_COL;
        // reset();
    }
    
    /** Reset all fields */
    private void reset() {
        setGridData();
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
     * @param entity : The entity that calls the method
     * @param entity : The target entity that the initEntity is following to
     */
    public void search(ControllableEntity initEntity, Entity targetEntity) {
        reset();

        int startRow = (int)initEntity.getPositionY() / GameConstants.TILE_SIZE;
        int startCol = (int)initEntity.getPositionX() / GameConstants.TILE_SIZE;
        int goalRow = (int)targetEntity.getPositionY() / GameConstants.TILE_SIZE;
        int goalCol = (int)targetEntity.getPositionX() / GameConstants.TILE_SIZE;

        // Errors Handling
        if (gridData[startRow][startCol] == 1)
            throw new InvalidParameterException("Start Node is blocked");
        if (gridData[goalRow][goalCol] == 1)
            throw new InvalidParameterException("Goal Node is blocked");
        
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

            if (currentNode == goalNode) {
                goalReached = true;
                constructPath();
                break;
            }

            closedList.add(currentNode);
            openList.remove(currentNode);

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

            step++;
        }
        if (!goalReached) {
            System.out.println("No path found to the goal.");
        }
    }

    public double distance(Node node1, Node node2) {
        return Math.sqrt((node2.col - node1.col) * (node2.col - node1.col) + (node2.row - node1.row) * (node2.row - node1.row));
    }

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

    private boolean isValidLocation(int targetRow, int targetCol) {
        /*
         * Check if
         * - the target position coordinates is in-bound
         * - the target position is not blocked
         * - the path is the target position is not blocked by 2 nodes diagonal to each other 
         */
        if ((targetRow < noOfRows && targetCol < noOfCols && targetRow >= 0 && targetCol >= 0) && (!nodes[targetRow][targetCol].isBlocked)) {
            if (targetRow != currentNode.row && targetCol != currentNode.col)
                return (!nodes[currentNode.row][targetCol].isBlocked && !nodes[targetRow][currentNode.row].isBlocked);
            }
            return true;
        }

    public void constructPath() {
        Node pathNode = goalNode;
        while (pathNode != null) {
            pathNode.isPath = true;
            System.out.println(pathNode.toString());
            pathNode = pathNode.parent;
        }
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
