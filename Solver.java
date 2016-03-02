import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private int moves = 0;

    private boolean isSolved = false;

    private class GameTree {

        private class SearchNode implements Comparable<SearchNode> {

            private Board board;
            private int priority;
            private SearchNode[] children = new SearchNode[4];
            private SearchNode parent;

            SearchNode(Board board, SearchNode parent, int moves) {
                if (board == null || parent == null) return;
                this.board = board;
                this.parent = parent;
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

        private boolean step(int moves) {
            currentNode = pq.delMin();
            if (currentNode.board.isGoal()) {
                return true;
            }

            int i = 0;
            for (Board neighbor : currentNode.board.neighbors()) {
                SearchNode neighborNode = new SearchNode(neighbor, currentNode, moves);
                if (!neighborNode.board.equals(currentNode.parent.board)) {
                    pq.insert(new SearchNode(neighbor, currentNode, moves));
                }
                currentNode.children[i] = neighborNode;
                i++;
            }

            return false;
        }
    }

    private GameTree initTree;
    private GameTree twinTree;

    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
        if (initial == null)
            throw new java.lang.NullPointerException();

        initTree = new GameTree(initial);
        twinTree = new GameTree(initial.twin());

        while (true) {
            moves++;
            if (initTree.step(moves)) {
                isSolved = true;
                break;
            }
            if (twinTree.step(moves)) {
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
        return moves - 1;
    }

    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
        return initTree.getSolution();
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

