import edu.princeton.cs.algs4.Stack;

public class Board {

    private int[] tiles;
    private int N;

    public Board(int[][] blocks)           // construct a board from an N-by-N array of tiles
    // (where tiles[i][j] = block in row i, column j)
    {
        N = blocks.length;
        this.tiles = new int[N * N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.tiles[index(i, j)] = blocks[i][j];
            }
        }
    }

    public Board(int[] blocks)           // construct a board from an N-by-N array of tiles
    // (where tiles[i][j] = block in row i, column j)
    {
        N = (int) Math.sqrt(blocks.length);
        this.tiles = new int[blocks.length];
        System.arraycopy(blocks, 0, this.tiles, 0, N * N);
    }

    public int dimension()                 // board dimension N
    {
        return N;
    }

    private int index(int i, int j) {
        return i * N + j;
    }

    public int hamming()                   // number of tiles out of place
    {
        int distance = 0;
        for (int i = 0; i < N * N; i++) {
            if (tiles[i] != 0 && tiles[i] != i + 1) {
                distance++;
            }
        }
        return distance;
    }

    public int manhattan()                 // sum of Manhattan distances between tiles and goal
    {
        int distance = 0;
        for (int i = 0; i < N * N; i++) {
            if(this.tiles[i] != 0) {
                int x = Math.abs(i % N - (tiles[i] - 1) % N);
                int y = Math.abs(i / N - (tiles[i] - 1) / N);
                distance += x + y;
            }
        }
        return distance;
    }

    public boolean isGoal()                // is this board the goal board?
    {
        for (int i = 0; i < N * N - 1; i++) {
            if (tiles[i] != i + 1)
                return false;
        }
        return true;
    }

//    private void exch(int ix, int iy, int jx, int jy) {
//        int swap = tiles[index(ix, iy)];
//        tiles[index(ix, iy)] = tiles[index(jx, jy)];
//        tiles[index(jx, jy)] = swap;
//    }

    private void exch(int i, int j) {
        int swap = tiles[i];
        tiles[i] = tiles[j];
        tiles[j] = swap;
    }

    public Board twin()                    // a board that is obtained by exchanging any pair of tiles
    {
        Board twin = new Board(tiles);
        for (int i = 0; i < N; i++) {
            if (tiles[i] != 0) {
                for (int j = i + 1; j < N; j++) {
                    if (tiles[j] != 0) {
                        twin.exch(i, j);
                        break;
                    }
                }
                break;
            }
        }
        return twin;
    }

    public boolean equals(Object y)        // does this board equal y?
    {
        if (y == this)
            return true;
        if (y == null)
            return false;
        if (y.getClass() != this.getClass())
            return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension())
            return false;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[index(i, j)] != that.tiles[index(i, j)]) {
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
                if (tiles[index(i, j)] == 0) {
                    Board neighbor;
                    if (i > 0) {
                        neighbor = new Board(tiles);
                        neighbor.exch(index(i, j), index(i - 1, j));
                        neighbors.push(neighbor);
                    }

                    if (j > 0) {
                        neighbor = new Board(tiles);
                        neighbor.exch(index(i, j), index(i, j - 1));
                        neighbors.push(neighbor);
                    }

                    if (i < N - 1) {
                        neighbor = new Board(tiles);
                        neighbor.exch(index(i, j), index(i + 1, j));
                        neighbors.push(neighbor);
                    }

                    if (j < N - 1) {
                        neighbor = new Board(tiles);
                        neighbor.exch(index(i, j), index(i, j + 1));
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
                s.append(String.format("%2d ", tiles[index(i, j)]));
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