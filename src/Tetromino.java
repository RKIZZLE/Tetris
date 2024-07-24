import java.awt.Color;

public class Tetromino {
    private int[][] cells; // Array of {y, x} pairs
    private final Color color;

    public Tetromino(int[][] cells, Color color) {
        this.cells = cells;
        this.color = color;
    }

    public boolean canMoveDown(Block[][] gameMatrix, Color backGround) {
        for (int[] cell : cells) {
            int nextY = cell[0] + 1;

            if (nextY >= gameMatrix.length) {
                return false;
            }
            if (!(gameMatrix[nextY][cell[1]].getBackground().equals(backGround) ||
                    gameMatrix[nextY][cell[1]].getBackground().equals(this.color))) {
                return false;
            }

        }
        return true;
    }
    public boolean canMoveLeft(Block[][] gameMatrix, Color backGround) {
        for (int[] cell : cells) {
            int nextX = cell[1] - 1;

            if (nextX < 0) {
                return false;
            }
            if (!(gameMatrix[cell[0]][nextX].getBackground().equals(backGround) ||
                    gameMatrix[cell[0]][nextX].getBackground().equals(this.color))) {
                return false;
            }

        }
        return true;
    }
    public boolean canMoveRight(Block[][] gameMatrix, Color backGround) {
        for (int[] cell : cells) {
            int nextX = cell[1] + 1;

            if (nextX >= gameMatrix[0].length) {
                return false;
            }
            if (!(gameMatrix[cell[0]][nextX].getBackground().equals(backGround) ||
                    gameMatrix[cell[0]][nextX].getBackground().equals(this.color))) {
                return false;
            }

        }
        return true;
    }

    public boolean canRotate(Block[][] gameMatrix, Color backGround) {
        int[] center = calculateCenter();
        int[][] rotatedCells = new int[cells.length][2];

        for (int i = 0; i < cells.length; i++) {
            int y = cells[i][0] - center[0];
            int x = cells[i][1] - center[1];

            rotatedCells[i][0] = -x + center[0];
            rotatedCells[i][1] = y + center[1];
        }

        // Check if the rotated position is within bounds and does not collide
        for (int[] cell : rotatedCells) {
            int x = cell[1];
            int y = cell[0];

            // Check bounds
            if (x < 0 || x >= gameMatrix[0].length || y < 0 || y >= gameMatrix.length) {
                return false;
            }

            // Check collision
            if (!(gameMatrix[y][x].getBackground().equals(backGround) ||
                    gameMatrix[y][x].getBackground().equals(this.color))) {
                return false;
            }
        }

        return true;
    }

    public void erase(Block[][] gameMatrix, Color backGround) {
        for (int[] cell : cells) {
            gameMatrix[cell[0]][cell[1]].setBackground(backGround);
        }
    }

    public void moveDown() {
        for (int[] cell : cells) {
            cell[0] += 1;
        }
    }
    public void moveLeft(){
        for (int[] cell : cells) {
            cell[1] -= 1;
        }

    }
    public void moveRight(){
        for (int[] cell : cells) {
            cell[1] += 1;
        }
    }

    public void draw(Block[][] gameMatrix) {
        for (int[] cell : cells) {
            gameMatrix[cell[0]][cell[1]].setBackground(color);
        }
    }

    private int[] calculateCenter() {
        int sumX = 0, sumY = 0, count = cells.length;
        for (int[] cell : cells) {
            sumX += cell[1];
            sumY += cell[0];
        }
        return new int[] { sumY / count, sumX / count };
    }

    public void rotate() {
        int[] center = calculateCenter();
        int[][] newCells = new int[cells.length][2];

        for (int i = 0; i < cells.length; i++) {
            int y = cells[i][0] - center[0];
            int x = cells[i][1] - center[1];

            newCells[i][0] = -x + center[0];
            newCells[i][1] = y + center[1];
        }

        cells = newCells;
    }

    public boolean canBePlaced(Block[][] gameMatrix, Color background, Color currentColor) {
        for (int[] cell : cells) {
            int x = cell[1];
            int y = cell[0];
            Color cellColor = gameMatrix[y][x].getBackground();
        if (!(cellColor.equals(background) || cellColor.equals(currentColor))) {
                return false;
            }
        }
        return true;
    }
}