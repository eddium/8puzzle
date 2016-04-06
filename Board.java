import edu.princeton.cs.algs4.Stack;

public class Board {

    private char[] tiles;
    private int N;

    public Board(int[][] blocks)           // construct a board from an N-by-N array of tiles
    {
        N = blocks.length;
        this.tiles = new char[N * N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.tiles[index(i, j)] = (char) blocks[i][j];
            }
        }
    }

    private Board(char[] blocks) {
        N = (int) Math.sqrt(blocks.length);
        this.tiles = new char[blocks.length];
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
//        distance += 2 * conflictDetection(tiles);
        return distance;
    }

    public int manhattan() {     // sum of Manhattan distances between tiles and goal
        int distance = 0;
        for (int i = 0; i < N * N; i++) {
            if (this.tiles[i] != 0) {
                int x = Math.abs(i % N - (tiles[i] - 1) % N);
                int y = Math.abs(i / N - (tiles[i] - 1) / N);
                distance += x + y;
            }
        }
//        distance += 2 * conflictDetection(tiles);
        return distance;
    }


    //  conflictDetection() returns the number of linear conflicts
    //  count(), sort(), merge() are helper functions for conflictDetection()
    private int conflictDetection(char[] a) {
        int cnt = 0;
        int[] conflict;
        for (int i = 0, k = 0; i < N; i++) {
            conflict = new int[N];
            for (int j = i; j < N * N; j += N) {
                if (j % N == (a[j] - 1) % N) {
                    conflict[k++] = tiles[j];
                }
            }
            cnt += count(conflict, 0, k - 1);
            k = 0;
        }
        for (int i = 0, k = 0; i < N * N; i += N) {
            conflict = new int[N];
            for (int j = i; j < i + N; j++) {
                if (j / N == (a[j] - 1) / N) {
                    conflict[k++] = a[j];
                }
            }
            cnt += count(conflict, 0, k - 1);
            k = 0;
        }
        return cnt;
    }

    private int count(int[] a, int lo, int hi) {
        int[] aux = new int[hi - lo + 1];
        return sort(a, aux, lo, hi);
    }

    private int sort(int[] a, int[] aux, int lo, int hi) {
        if (lo >= hi)
            return 0;
        int cnt = 0;
        int mid = (lo + hi) / 2;
        cnt += sort(a, aux, lo, mid);
        cnt += sort(a, aux, mid + 1, hi);
        cnt += merge(a, aux, lo, mid, hi);
        return cnt;
    }

    private int merge(int[] a, int[] aux, int lo, int mid, int hi) {
        int cnt = 0;
        System.arraycopy(a, lo, aux, lo, hi + 1 - lo);

        int i = lo;
        int j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                a[k] = aux[j++];
            } else if (j > hi) {
                a[k] = aux[i++];
            } else if (aux[i] > aux[j]) {
                a[k] = aux[j++];
                cnt += mid - i + 1;
            } else {
                a[k] = aux[i++];
            }
        }
        return cnt;
    }

    public boolean isGoal()                // is this board the goal board?
    {
        for (int i = 0; i < N * N - 1; i++) {
            if (tiles[i] != i + 1)
                return false;
        }
        return true;
    }

    private void exch(int i, int j) {
        char swap = tiles[i];
        tiles[i] = tiles[j];
        tiles[j] = swap;
    }

    public Board twin()                    // a board that is obtained by exchanging any pair of tiles
    {
        Board twin = new Board(tiles);
        for (int i = 0; i < N * N; i++) {
            if (tiles[i] != 0) {
                for (int j = i + 1; j < N * N; j++) {
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
        for (int i = 0; i < N * N; i++) {
            if (tiles[i] != that.tiles[i]) {
                return false;
            }
        }
        return true;
    }

    private Iterable<Board> getNeighbors(Stack<Board> neighbors, int i, int j) {
        for (int m = -1; m < 2; m++) {
            for (int n = -1; n < 2; n++) {
                if ((m + n) == -1 || (m + n) == 1) {
                    int r = i + m;
                    int c = j + n;
                    if (r > -1 && r < N && c > -1 && c < N) {
                        Board neighbor = new Board(tiles);
                        neighbor.exch(index(i, j), index(r, c));
                        neighbors.push(neighbor);
                    }
                }
            }
        }
        return neighbors;
    }

    public Iterable<Board> neighbors()     // all neighboring boards
    {
        Stack<Board> neighbors = new Stack<>();
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (tiles[index(i, j)] == 0)
                    return getNeighbors(neighbors, i, j);
        return neighbors;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N).append("\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", (int) tiles[index(i, j)]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        int[][] blocks = {{1, 0}, {2, 3}};
        Board a = new Board(blocks);
        System.out.println(a.manhattan());
    }
}