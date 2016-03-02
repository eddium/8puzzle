import edu.princeton.cs.algs4.Stack;

public class Board {

    private int[][] tiles;
    private int N;

    public Board(int[][] blocks)           // construct a board from an N-by-N array of tiles
    // (where tiles[i][j] = block in row i, column j)
    {
        N = blocks.length;
        this.tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.tiles[i][j] = blocks[i][j];
            }
        }
    }

    public int dimension()                 // board dimension N
    {
        return N;
    }

    private int index(int i, int j) {
        return i * N + j + 1;
    }

    public int hamming()                   // number of tiles out of place
    {
        int distance = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] != 0 && tiles[i][j] != index(i, j)) {
                    distance++;
                }
            }
        }
        return distance;
    }

    public int manhattan()                 // sum of Manhattan distances between tiles and goal
    {
        int distance = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int num = tiles[i][j];
                if (num != 0 && num != index(i, j)) {
                    int r, c;
                    int remainder = num % N;
                    if (remainder == 0) {
                        r = num / N - 1;
                        c = N - 1;
                    } else {
                        r = num / N;
                        c = remainder - 1;
                    }
                    distance += Math.abs(r - i) + Math.abs(c - j);
                }
            }
        }
        return distance;
    }

    public boolean isGoal()                // is this board the goal board?
    {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == N - 1 && j == N - 1)
                    return true;
                if (tiles[i][j] != index(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void exch(int ix, int iy, int jx, int jy) {
        int swap = tiles[ix][iy];
        tiles[ix][iy] = tiles[jx][jy];
        tiles[jx][jy] = swap;
    }

    public Board twin()                    // a board that is obtained by exchanging any pair of tiles
    {
        Board twin = new Board(tiles);
        int i = 0;
        if (twin.tiles[0][0] != 0) {
            if (twin.tiles[1][0] != 0) {
                twin.exch(0, 0, 1, 0);
            } else {
                twin.exch(0, 0, 1, 1);
            }
        } else {
            if (twin.tiles[1][0] != 0) {
                twin.exch(0, 1, 1, 0);
            } else {
                twin.exch(0, 1, 1, 1);
            }
        }
        return twin;
    }

    public boolean equals(Object y)        // does this board equal y?
    {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.N != that.N) return false;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] != that.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public Iterable<Board> neighbors()     // all neighboring boards
    {
        Stack<Board> neighbors = new Stack<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] == 0) {
                    Board neighbor;
                    if (i > 0) {
                        neighbor = new Board(tiles);
                        neighbor.exch(i, j, i - 1, j);
                        neighbors.push(neighbor);
                    }

                    if (j > 0) {
                        neighbor = new Board(tiles);
                        neighbor.exch(i, j, i, j - 1);
                        neighbors.push(neighbor);
                    }

                    if (i < N - 1) {
                        neighbor = new Board(tiles);
                        neighbor.exch(i, j, i + 1, j);
                        neighbors.push(neighbor);
                    }

                    if (j < N - 1) {
                        neighbor = new Board(tiles);
                        neighbor.exch(i, j, i, j + 1);
                        neighbors.push(neighbor);
                    }
                    break;
                }
            }
        }
        return neighbors;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        int[][] blocks = {{5, 1, 8}, {2, 7, 3}, {4, 0, 6}};
//        int[][] blocks1 = {{1,3,5},{2,1,7},{4,6,8}};
        Board a = new Board(blocks);
//        Board b = new Board(blocks1);
        System.out.println(a);
//        for(Board k : a.neighbors()) {
//            System.out.println(k);
//        }
        System.out.println(a.manhattan());
//        System.out.println(a.equals(a));
    }


}