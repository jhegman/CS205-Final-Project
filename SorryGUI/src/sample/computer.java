package sample;
import java.util.ArrayList;

public class computer {
	// the color of the computer's pawns
	private String color;
	// is the computer on nice mode?
	private boolean nice;
	// positions of computer pawns on the main game board
	private ArrayList<Integer> pawnBoardPositions = new ArrayList<Integer>();
	// positions of computer pawns in the computer's safe zone
	private ArrayList<Integer> pawnSafePositions = new ArrayList<Integer>();
	// the space from which the computer can move into it's safe zone
	private int nextToSafe = 32;
	// the space which the computer moves to from the start space
	private int startSpace = 34;
	// Pawns that will bump allies on a slide
	private ArrayList<Integer> dontSlide = new ArrayList<Integer>();
	boolean madeMove=true;

	public computer(boolean n) {
		nice = n;
		color = "green";
	}

	// Get the positions of all computer pawns on the board and in the safe zone
	public void getPositions(board gameBoard) {
		pawnBoardPositions = gameBoard.getComputerBoardPositions();
		pawnSafePositions = gameBoard.getComputerSafePositions();
	}

	// increment a position on the game board by a given amount
	public int newPosition(int start, int moveAmount) {
		int end = start + moveAmount;
		if (end > 59) {
			return end - 60;
		}
		if (end < 0) {
			return 60 + end;
		}
		return end;
	}
	// check if a pawn can be moved into the safe zone with a given movement
	// value(not card value)
	public int canMoveToSafe(int movementValue) {
		for (int i : pawnBoardPositions) {
			int moveTo=newPosition(i, movementValue);
			if ((i<3||i>40)&&moveTo>2 && moveTo<9 && !(pawnSafePositions.contains(moveTo-3)))
			{
				return i;
			}
		}
		return (-1);
	}

	// check if a slide contains an ally pawn
	public boolean shouldSlide(board gameBoard, int pawn, int moveAmount) {
		int pawnLocation = pawn;
		int end = (newPosition(pawnLocation, moveAmount));
		if (gameBoard.board[end].getType().equals("slidestart")) {
			if (gameBoard.board[end].getPlayer().equals("computer")) {
				return false;
			}
			end++;
			while (gameBoard.board[end].getType().equals("slide")) {
				if (gameBoard.board[end].getPlayer().equals("computer")) {
					dontSlide.add(pawn);
					return false;
				}
				end++;
			}
			return true;
		}
		return false;
	}
	//find a move for the computer based on current positions. These moves will not prioritize hitting the user or swapping places with the user.
	public void niceMove(board gameBoard, int cardValue) {
		getPositions(gameBoard);
		boolean moved = false;
		
		if (cardValue == 1&&!moved) {
			// System.out.println("tugwos");

			// move a pawn out of the start space if able
			if ((gameBoard.getCompStart() > 0)
					&& !(gameBoard.board[4].getPlayer().equals("computer"))) {
				gameBoard.moveCompStart();
				moved = true;
			}
		}
		if (cardValue == 2&&!moved) {
			// System.out.println("tugwos");

			// move a pawn out of the start space if able
			if ((gameBoard.getCompStart() > 0)
					&& !(gameBoard.board[4].getPlayer().equals("computer"))) {
				gameBoard.moveCompStart();
				moved = true;
			}
		}
		// move a pawn into the safe space if able
		if ((!moved) && (canMoveToSafe(cardValue) != -1)) {
			// System.out.println("tugwos1");
			int start=canMoveToSafe(cardValue);
			int end=newPosition(start,cardValue);
			gameBoard.moveCompSafe(start,end);
			moved = true;
		}
		// move a pawn into the home space from the safezone if able
		if (!moved) {
			// System.out.println("tugwos2");

			for (int pawn : pawnSafePositions) {
				if ((pawn + cardValue) == 5) {
					gameBoard.moveCompHome(pawn);
					moved = true;
					break;
				}
			}
		}
		// move a pawn onto a slide if it doesn't bump allies
		if (!moved) {
			// System.out.println("tugwos3");
			dontSlide.clear();
			for (int pawn : pawnBoardPositions) {
				if (shouldSlide(gameBoard, pawn, cardValue)) {
					gameBoard.move(pawn, newPosition(pawn, cardValue));
					moved = true;
					break;
				}
			}
		}
		// move a pawn within the computer's safezone(not to home space)
		if (!moved) {
			for (int pawn : pawnSafePositions) {
				if(((pawn+cardValue)<5) && ((pawn+cardValue)>=0))
				{
					if (!(gameBoard.compSafe[pawn+cardValue].getPlayer().equals("computer"))) {
						gameBoard.moveInCompSafeZone(pawn, pawn + cardValue);
						moved = true;
						break;
					}
				}
			}
		}
		// move a pawn on the map forward that won't harm allies
		if (!moved) {
			// System.out.println("tugwos");

			for (int pawn : pawnBoardPositions) 
			{
				if (!((pawn>40||pawn<3) && ((newPosition(pawn,cardValue)>3)) && (newPosition(pawn,cardValue)<16)) && (!(gameBoard.board[newPosition(pawn, cardValue)].getPlayer().equals("computer"))) && (!(dontSlide.contains(pawn)))) 
				{
					gameBoard.move(pawn, newPosition(pawn, cardValue));
					moved = true;
					break;
				}
			}
		}
		// move a harmful pawn forward
		if (!moved) {
			// System.out.println("tugwos");

			for (int pawn : pawnBoardPositions) {
				if (!((pawn>40||pawn<3) && ((newPosition(pawn,cardValue)>3)) && (newPosition(pawn,cardValue)<16)) && !(gameBoard.board[newPosition(pawn, cardValue)].getPlayer().equals("computer"))) {
					gameBoard.move(pawn, newPosition(pawn, cardValue));
					moved = true;
					break;
				}
			}
		}
		// no moves area available
		if (!moved) {
			System.out.println("No moves");
			madeMove=false;
		}
	}
	//find a mean move for the computer, prioritizing hitting the the user's pawns in any way possible
	public void hitUser(board gameBoard, int cardValue)
	{
		boolean hitSomething=false;
		for(int start : pawnBoardPositions)
		{
			int end=newPosition(start,cardValue);
			if(gameBoard.board[end].getPlayer().equals("user"))
			{
				gameBoard.move(start, end);
				hitSomething=true;
			}
		}
		if(!hitSomething)
		{
			niceMove(gameBoard, cardValue);
		}
	}
	// find a move for the computer, cards represented as an integer from 1-11
	public int[] makeMove(board gameBoard, int cardValue) {
		int moveValue;
		madeMove=true;
		if(cardValue==4)
		{
			moveValue=-4;
		}
		else if(cardValue==6)
		{
			moveValue=7;
		}
		else if(cardValue==7)
		{
			moveValue=8;
		}
		else if(cardValue==8)
		{
			moveValue=10;
		}
		else if(cardValue==9)
		{
			moveValue=11;
		}
		else if(cardValue==10)
		{
			moveValue=12;
		}
		else if(cardValue==11)
		{
			moveValue=13;
		}
		else
		{
			moveValue=cardValue;
		}
		if(nice==true)
		{
			niceMove(gameBoard,moveValue);
		}
		else
		{
			hitUser(gameBoard, moveValue);
		}
		int[] positions=new int[3];
        int[] p=gameBoard.returnPositions();
        positions[0]=p[0];
        positions[2]=p[1];
		if(madeMove)
		{
			positions[1]=Math.abs(moveValue);
		}
		else{
			positions[0]=0;
			positions[1]=0;
		}
		return positions;
	}
	public void gotBumped(board gameBoard, int location)
	{
		gameBoard.board[location].setOccupied(false,"none");
		gameBoard.compStart++;
	}
	}
