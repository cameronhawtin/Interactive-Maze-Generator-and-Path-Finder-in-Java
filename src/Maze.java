import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

public class Maze {
    private static final byte  OPEN = 0;
    private static final byte  WALL = 1;
    private static final byte  VISITED = 2;

    private int         rows, columns;
    private byte[][]    grid;

    // A constructor that makes a maze of the given size
    public Maze(int r, int c) {
        rows = r;
        columns = c;
        grid = new byte[r][c];
        for(r=0; r<rows; r++) {
            for (c = 0; c<columns; c++) {
                grid[r][c] = WALL;
            }
        }
    }

    public int getRows() { return rows; }
    public int getColumns() { return columns; }

    // Return true if a wall is at the given location, otherwise false
    public boolean wallAt(int r, int c) {
        return grid[r][c] == WALL;
    }

    // Return true if this location has been visited, otherwise false
    public boolean visitedAt(int r, int c) {
        return grid[r][c] == VISITED;
    }

    // Put a visit marker at the given location
    public void placeVisitAt(int r, int c) {
        grid[r][c] = VISITED;
    }

    // Remove a visit marker from the given location
    public void removeVisitAt(int r, int c) {
        grid[r][c] = OPEN;
    }

    // Put a wall at the given location
    public void placeWallAt(int r, int c) {
        grid[r][c] = WALL;
    }

    // Remove a wall from the given location
    public void removeWallAt(int r, int c) {
        grid[r][c] = 0;
    }

    // Carve out a maze
    public void carve() {
        int startRow = (int)(Math.random()*(rows-2))+1;
        int startCol = (int)(Math.random()*(columns-2))+1;
        carve(startRow, startCol);
    }

    // Directly recursive method to carve out the maze
    public void carve(int r, int c) {

        if (r==getRows()-1 || r==0 || c==getColumns()-1 || c==0) {
            // dont remove wall
        }
        else if (!wallAt(r, c)) {
            // do nothing
        }
        else {
            int wallsAround = 0;

            if (wallAt(r-1, c)) wallsAround++;
            if (wallAt(r+1, c)) wallsAround++;
            if (wallAt(r, c-1)) wallsAround++;
            if (wallAt(r, c+1)) wallsAround++;

            if (wallsAround >= 3) {

                removeWallAt(r, c);

                ArrayList<Integer> rowOffsets = new ArrayList<Integer>(Arrays.asList(-1, 1, 0, 0));
                ArrayList<Integer> colOffsets = new ArrayList<Integer>(Arrays.asList(0, 0, -1, 1));

                int arraySize = 4;
                while (rowOffsets.size() > 0) {
                    int random = (int)(Math.random() * arraySize);
                    int currentRow = rowOffsets.get(random);
                    int currentColumn = colOffsets.get(random);

                    rowOffsets.remove(random);
                    colOffsets.remove(random);

                    arraySize--;
                    carve(r+currentRow, c+currentColumn);
                }
            }
        }
    }

    // Determine the longest path in the maze from the given start location
    public ArrayList<Point2D> longestPath() {
        ArrayList<Point2D> currentLongest = new ArrayList<Point2D>();
        int Longest = 0;
        for (int i=1; i<getRows()-1; i++) {
            for (int j=1; j<getColumns()-1; j++) {
                if (longestPathFrom(i, j).size() > Longest) {
                    currentLongest = longestPathFrom(i, j);
                    Longest = longestPathFrom(i, j).size();
                }
            }
        }
        return currentLongest;
    }

    // Determine the longest path in the maze from the given start location
    public ArrayList<Point2D> longestPathFrom(int r, int c) {
        ArrayList<Point2D> path = new ArrayList<Point2D>();

        if (grid[r][c] != OPEN) {
            return path;
        }
        else {
            grid[r][c] = VISITED;
            ArrayList<Point2D> Right    = longestPathFrom(r+1, c);
            ArrayList<Point2D> Left     = longestPathFrom(r-1, c);
            ArrayList<Point2D> Up       = longestPathFrom(r, c+1);
            ArrayList<Point2D> Down     = longestPathFrom(r, c-1);

            if (Right.size() >= path.size())
                path = Right;
            if (Left.size() >= path.size())
                path = Left;
            if (Up.size() >= path.size())
                path = Up;
            if (Down.size() >= path.size())
                path = Down;

            grid[r][c] = OPEN;
            path.add(new Point2D(r,c));
            return path;
        }
    }
}
