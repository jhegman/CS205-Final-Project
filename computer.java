import java.util.ArrayList;
import java.util.Collections;

public class computer {
	// the color of the computer's pawns
	private String color;
	// is the computer on nice mode?
	private boolean nice;
	// positions of computer pawns on the main game board
	private ArrayList<Integer> pawnBoardPositions = new ArrayList<Integer>();
	// positions of computer pawns in the computer's safe zone
	private ArrayList<Integer> pawnSafePositions = new ArrayList<Integer>();
	// positions of the user's pawns on the game board
	private ArrayList<Integer> userPawnPositions= new ArrayList<Integer>();
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
		userPawnPositions = gameBoard.getUserPawnPositions();
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
	// check if a pawn can be moved into the safe zone with a given movement value(not card value)
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
	// check if a given move will bump a user pawn
	public boolean willBump(board gameBoard, int start, int amount)
	{
		for(int p:userPawnPositions)
		{
			int endPosition=newPosition(start, amount);
			if(endPosition==p)
			{
				return true;
			}
			else if(gameBoard.board[endPosition].getType().equals("slidestart"))
			{
				int current=endPosition+1;
				while((gameBoard.board[current].getType().equals("slide")))
				{
					if(p==current)
					{
						return true;
					}
					current++;
				}
			}
		}
		return false;
	}
	//swap the position of two pawns on the board
	public void swap(board gameBoard, int userPawn, int compPawn)
	{
		gameBoard.board[userPawn].setOccupied(true, "computer");
		gameBoard.board[compPawn].setOccupied(true, "user");
	}
	//find a move for the computer based on current positions. These moves will not prioritize hitting the user or swapping places with the user.
	public void niceMove(board gameBoard, int cardValue) {
		getPositions(gameBoard);
		boolean moved = false;
		
		//use the sorry card to switch a pawn from the computer's start
		if (cardValue == 13 && !moved)
		{
			if(userPawnPositions.isEmpty() || gameBoard.compStart==0 ){}
			else{
				Collections.sort(userPawnPositions);
				if(userPawnPositions.get(0)<3)
				{
					gameBoard.sorry(userPawnPositions.get(0));
				}
				else
				{
					gameBoard.sorry(userPawnPositions.get(userPawnPositions.size()-1));
				}
			}
			moved = true;
		}
		if (cardValue == 1 && !moved) {
			// System.out.println("tugwos");

			// move a pawn out of the start space if able
			if ((gameBoard.getCompStart() > 0) && !(gameBoard.board[4].getPlayer().equals("computer"))) {
				boolean hit=willBump(gameBoard,2,0);
				if(nice && !hit)
				{
					gameBoard.moveCompStart();
					moved = true;
				}
				else if(!nice && hit)
				{
					gameBoard.moveCompStart();
					moved = true;
				}
			}
		}
		if (cardValue == 2 && !moved) {
			// System.out.println("tugwos");

			// move a pawn out of the start space if able
			if ((gameBoard.getCompStart() > 0) && !(gameBoard.board[4].getPlayer().equals("computer"))) {
				boolean hit=willBump(gameBoard,2,0);
				if(nice && !hit)
				{
					gameBoard.moveCompStart();
					moved = true;
				}
				else if(!nice && hit)
				{
					gameBoard.moveCompStart();
					moved = true;
				}
			}
		}
		//if computer is nice, try not to swap with an 11
		if (cardValue == 11 && !nice)
		{
			if(userPawnPositions.isEmpty() || pawnBoardPositions.isEmpty()){}
			else{
				int switchTo;
				int switchWith;
				Collections.sort(userPawnPositions);
				Collections.sort(pawnBoardPositions);
				if(userPawnPositions.get(0)<3)
				{
					switchTo = userPawnPositions.get(0);
				}
				else
				{
					switchTo = userPawnPositions.get(userPawnPositions.size()-1);
				}
				for(int i=0; i < pawnBoardPositions.size(); i++)
				{
					switchWith=pawnBoardPositions.get(i);
					if(switchWith > 2 || i == pawnBoardPositions.size()-1)
					{
						swap(gameBoard, switchTo, switchWith);
					}
				}
				moved = true;
			}
		}
		// move a pawn into the safe space if able
		if (nice && (!moved) && (canMoveToSafe(cardValue) != -1)) {
			// System.out.println("tugwos1");
			int start=canMoveToSafe(cardValue);
			int end=newPosition(start,cardValue);
			gameBoard.moveCompSafe(start,end);
			moved = true;
		}
		// move a pawn into the home space from the safezone if able
		if (!moved && nice) {
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
					boolean hit=willBump(gameBoard,pawn,cardValue);
					if(nice && !hit)
					{
						gameBoard.move(pawn, newPosition(pawn, cardValue));
						moved = true;
						break;
					}
					else if(!nice && hit)
					{
						gameBoard.move(pawn, newPosition(pawn, cardValue));
						moved = true;
						break;
					}
				}
			}
		}
		// move a pawn within the computer's safezone(not to home space)
		if (!moved && nice) {
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
					boolean hit=willBump(gameBoard,pawn,cardValue);
					if(nice && !hit)
					{
						gameBoard.move(pawn, newPosition(pawn, cardValue));
						moved = true;
						break;
					}
					else if(!nice && hit)
					{
						gameBoard.move(pawn, newPosition(pawn, cardValue));
						moved = true;
						break;
					}
				}
			}
		}
		// move a harmful pawn forward
		if (!moved) {
			// System.out.println("tugwos");

			for (int pawn : pawnBoardPositions) {
				if (!((pawn>40||pawn<3) && ((newPosition(pawn,cardValue)>3)) && (newPosition(pawn,cardValue)<16)) && !(gameBoard.board[newPosition(pawn, cardValue)].getPlayer().equals("computer"))) {
					boolean hit=willBump(gameBoard,pawn,cardValue);
					if(nice && !hit)
					{
						gameBoard.move(pawn, newPosition(pawn, cardValue));
						moved = true;
						break;
					}
					else if(!nice && hit)
					{
						gameBoard.move(pawn, newPosition(pawn, cardValue));
						moved = true;
						break;
					}
				}
			}
		}
		// no moves area available
		if (!moved) {
			System.out.println("No moves");
			madeMove=false;
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
		niceMove(gameBoard, moveValue);
		if(!madeMove)
		{
			 nice = !nice;
			 madeMove=true;
			 niceMove(gameBoard, moveValue);
			 nice = !nice;
		}
		int[] positions=gameBoard.returnPositions();
		if(madeMove)
		{
			//positions[1]=Math.abs(moveValue);
		}
		else{
			positions[0]=0;
			positions[1]=0;
		}
		return positions;
	}
	public void gotBumped(board gameBoard, int location)
	{
		gameBoard.board[location].setOccupied(false, "none");
		gameBoard.compStart++;
	}
	}
