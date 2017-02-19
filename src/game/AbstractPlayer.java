package game;

/**
 *
 * @author iuri, silla
 */
abstract public class AbstractPlayer {

    private int myBoardMark;
    
    private int opponentBoardMark;
        
    private int idUser;

    private AbstractGame game;

    private int depth;

    public AbstractPlayer() {
    	depth = 0;
    	myBoardMark = 0;
        opponentBoardMark = 0;
    }
    
    public AbstractPlayer(int depth) {
        this.depth = depth;
        myBoardMark = 0;
        opponentBoardMark = 0;
    }

    abstract public BoardSquare play(int[][] tab);

    public int getMyBoardMark() {
        return myBoardMark;
    }

    public int getOpponentBoardMark() {
        return opponentBoardMark;
    }

    public void setMyBoardMark(int marcaTabuleiro) {
        this.myBoardMark = marcaTabuleiro;
    }

    public void setOpponentBoardMark(int marcaTabuleiro) {
        this.opponentBoardMark = marcaTabuleiro;
    }

    public AbstractGame getGame() {
        return game;
    }

    public void setGame(AbstractGame game) {
        this.game = game;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getBoardMark() {
        return myBoardMark;
    }

    public void setBoardMark(int boardMark) {
        this.myBoardMark = boardMark;
    }

	public int getDepth() {
		return depth;
	}



}
