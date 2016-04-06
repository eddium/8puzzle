import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class GameTree {

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
            this.priority = board.manhattan() + moves;
        }

        @Override
        public int compareTo(SearchNode that) {
            if (this.priority > that.priority) return 1;
            else if (this.priority < that.priority) return -1;
            else return 0;
        }
    }

    private SearchNode currentNode;
    private MinPQ<SearchNode> pq;

    public GameTree(Board board) {
        SearchNode sentinel = new SearchNode(null, null, 0);
        SearchNode root     = new SearchNode(board, sentinel, 0);
        pq = new MinPQ<>();
        pq.insert(root);
    }

    /**
     * Returns true if a goal node is dequeued.
     * @return <tt>true</tt> if a goal node is dequeued; <tt>false</tt> otherwise
     */
    public boolean step() {
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
            currentNode.children[i++] = neighborNode;
        }

        return false;
    }

    public int currentMoves() {
        return currentNode.moves;
    }

    public Stack<Board> getSolution() {
        Stack<Board> stepBySetp = new Stack<>();

        while (currentNode.board != null) {
            stepBySetp.push(currentNode.board);
            currentNode = currentNode.parent;
        }
        return stepBySetp;
    }

}