import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private Stack<Board> solution;
    private boolean isSolved;
    private int moves;

    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
        if (initial == null)
            throw new java.lang.NullPointerException();

        GameTree initTree = new GameTree(initial);
        GameTree twinTree = new GameTree(initial.twin());

        while (true) {
            if (initTree.step()) {
                moves = initTree.currentMoves();
                solution = initTree.getSolution();
                isSolved = true;
                break;
            }
            if (twinTree.step()) {
                moves = -1;
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

