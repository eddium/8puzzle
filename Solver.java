import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private boolean isSolved = false;
    private Stack<Board> solution = new Stack<>();
    private int moves;

    private class GameTree {

        private class SearchNode implements Comparable<SearchNode> {

            private Board board;
            private int priority;
            private int moves;
            private SearchNode[] children = new SearchNode[4];
            private SearchNode parent;

            SearchNode(Board board, SearchNode parent, int moves) {
                if (board == null || parent == null) return;
                this.board = board;
                this.parent = parent;
                this.moves = moves;
                this.priority = board.hamming() + moves;
            }

            @Override
            public int compareTo(SearchNode that) {
                if (this.priority > that.priority) return 1;
                else if (this.priority < that.priority) return -1;
                else return 0;
            }
        }

        private SearchNode root;
        private SearchNode currentNode;
        private MinPQ<SearchNode> pq;

        private GameTree(Board board) {
            SearchNode sentinel = new SearchNode(null, null, 0);
            root = new SearchNode(board, sentinel, 0);
            pq = new MinPQ<>();
            pq.insert(root);
        }

        private Stack<Board> getSolution() {
            Stack<Board> solution = new Stack<>();
            while (currentNode.board != null) {
                solution.push(currentNode.board);
                currentNode = currentNode.parent;
            }
            return solution;
        }

        private boolean step() {
            currentNode = pq.delMin();
            if (currentNode.board.isGoal()) {
                return true;
            }

            int i = 0;
            for (Board neighbor : currentNode.board.neighbors()) {
                SearchNode neighborNode = new SearchNode(neighbor, currentNode, currentNode.moves + 1);
                if (!neighborNode.board.equals(currentNode.parent.board)) {
                    pq.insert(neighborNode);
                }
                currentNode.children[i] = neighborNode;
                i++;
            }

            return false;
        }
    }

    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
        if (initial == null)
            throw new java.lang.NullPointerException();

        GameTree initTree = new GameTree(initial);
        GameTree twinTree = new GameTree(initial.twin());

        while (true) {
            if (initTree.step()) {
                moves = initTree.currentNode.moves;
                solution = initTree.getSolution();
                isSolved = true;
                break;
            }
            if (twinTree.step()) {
                break;
            }
        }
    }

    public boolean isSolvable()            // is the initial board solvable?
    {
        return isSolved;
    }

    public int moves()                     // min number of moves to solution initial board; -1 if unsolvable
    {
        return moves;
    }

    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
        return solution;
    }

    public static void main(String[] args) // solution a slider puzzle (given below)
    {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solution the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}

