package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author David Rambo
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.
        if (side != Side.NORTH) {
            board.setViewingPerspective(side);
        }
        // Iterate over columns and call processColumn() to move.
        for (int c = 0; c < board.size(); c++) {

        }

        checkGameOver();
        if (changed) {
            setChanged();
        }

        board.setViewingPerspective(Side.NORTH);
        return changed;
    }

    /** Iterate over a column's rows and make necessary moves.
     * It begins at row = 2 because "Northern"-row tiles cannot move.
     * It uses an array to track whether a merge has been made.
     */
    private boolean processColumn(int col) {
        boolean changed;
        changed = false;
        boolean update_score;
        // Array of booleans corresponding to rows 0, 1, and 2. These indicate whether
        // a merge has been made already.
        boolean[] merge_status = new boolean[] {false, false, false};

        // Iterate through the rows from second to the top down to 0.
        for (int row = board.size() - 2; row >= 0; row--) {
            Tile t = board.tile(col, row);
            // Check status of tiles above Tile t.
            for (int row_above = row + 1; row < board.size() - 1; row_above++) {
                Tile t_above = board.tile(col, row_above);
                if (is_available(t.value(), t_above, merge_status[row_above])) {
                    // check for rows further above before committing to a move
                    if (!keep_checking(col, row_above, t.value(), merge_status)) {
                        // no other options, so move:
                        update_score = board.move(col, row_above, t);
                        if (update_score) {
                            // Move resulted in merge, so increment score by
                            // the value of tile at new location.
                            this.score += board.tile(col, row_above).value();
                            // reset update_score
                            update_score = false;
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

   /** Checks whether tile is available for a move. */
    private boolean is_available(int current_t_val, Tile possible_t, boolean merge_status) {
        return possible_t == null || possible_t.value() == current_t_val && merge_status;
    }

    /** Checks whether there are more rows available for a move. */
    private boolean keep_checking(int col, int row_above, int t_val, boolean[] merge_status) {
        int r = row_above + 1;
        while (r < board.size() - 1) {
            if (is_available(t_val, board.tile(col, row_above), merge_status[row_above])) {
                return true;
            }
//            if (board.tile(col, r) == null) {
//                return true;
//            } else if (board.tile(col, r).value() == t_val) {
//                // Possible merge, so check whether merge has already occurred.
//               if (!merge_status[r]) {
//                   return true;
//               }
//            }
            r += 1;
        }
        return false;
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        // iterate through the values at each row/col position and return true if any is 0
        int end = b.size();  // get size of board
        /* Iterate over each row. */
        for (int r = 0; r < end; r++) {
            /* Iterate over each column per row. */
            for (int c = 0; c < end; c++) {
                if (b.tile(r, c) == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        int end = b.size();  // get size of board
        /* Iterate over each row. */
        for (int r = 0; r < end; r++) {
            /* Iterate over each column per row. */
            for (int c = 0; c < end; c++) {
                // Since each tile can be null, we need to catch that exception.
                try {
                    int val = b.tile(r, c).value();
                    if (val == MAX_PIECE) {
                        return true;
                    }
                }
                catch (Exception e) {
                    continue;
                }
            }
        }

        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        /* If there is an empty space, then true. */
        if (emptySpaceExists(b)) {
            return true;
        }

        /* If there are two adjacent tiles with the same value, then true.
        * We can assume every value is an int because none are empty at this point.
        * To avoid repeats, we check (r, c) against (r+1, c) and (r, c+1).
        * We also need to avoid cases where r+1 or c+1 are >= b.size().*/
        int end = b.size();

        for (int r = 0; r < end; r++) {
            for (int c = 0; c < end; c++) {
               int above = c + 1;
               if (above < end) {
                   if (b.tile(r, c).value() == b.tile(r, above).value()) {
                       return true;
                   }
               }
               int over = r + 1;
               if (over < end) {
                   if (b.tile(over, c).value() == b.tile(over, c).value()) {
                       return true;
                   }
               }
            }
        }
        return false;
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
