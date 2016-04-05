import java.util.ArrayList;

public class computer 
{
	//the color of the computer's pawns
	private String color;
	//is the computer on nice mode?
	private boolean nice;
	//positions of computer pawns on the main game board
	private ArrayList <Integer> pawnBoardPositions=new ArrayList <Integer>();
	//positions of computer pawns in the computer's safe zone
	private ArrayList <Integer> pawnSafePositions=new ArrayList <Integer>();
	//the space from which the computer can move into it's safe zone
	private int nextToSafe=32;
	//the space which the computer moves to from the start space
	private int startSpace=34;
	//Pawns that will bump allies on a slide
	private ArrayList <Integer> dontSlide = new ArrayList <Integer>();
	
	public computer(boolean n)
	{
		nice=n;
		color="red";
	}
	//Get the positions of all computer pawns on the board and in the safe zone
	public void getPositions(board gameBoard)
	{
		pawnBoardPositions=gameBoard.getComputerBoardPositions();
		pawnSafePositions=gameBoard.getComputerSafePositions();
	}
	//check if a pawn can be moved into the safe zone with a given movement value(not card value)
	public int canMoveToSafe(int movementValue)
	{
		for(int i:pawnBoardPositions)
		{
			if(i>0 && i<=32 && movementValue+i>32 && movementValue+i<39 && !(pawnSafePositions.contains((movementValue+i)-33)))
			{
				return i;
			}
		}
		return (-1);
	}
	//increment a position on the game board by a given amount
	public int newPosition(int start, int moveAmount)
	{
		int end=start+moveAmount;
		if(end>59)
		{
			return end-60;
		}
		if(end<0)
		{
			return 60+end;
		}
		return end;
	}
	//check if a slide contains an ally pawn
	public boolean shouldSlide(board gameBoard, int pawn, int moveAmount)
	{
		int pawnLocation=pawn;
		int end=(newPosition(pawnLocation, moveAmount));
		if(gameBoard.board[end].getType().equals("slidestart"))
		{
			if(gameBoard.board[end].getPlayer().equals("computer"))
			{
				return false;
			}
			end++;
			while(gameBoard.board[end].getType().equals("slide"))
			{
				if(gameBoard.board[end].getPlayer().equals("computer"))
				{
					dontSlide.add(pawn);
					return false;
				}
				end++;
			}
			return true;
		}
		return false;	
	}
	//find a move for the computer
	public void moveNice(board gameBoard,int cardValue)
	{
		getPositions(gameBoard);
		if(cardValue==1)
		{
			boolean moved=false;
			//System.out.println("tugwos");

			//move a pawn out of the start space if able
			if((gameBoard.getCompStart()>0) && !(gameBoard.board[34].getPlayer().equals("computer")))
			{
				gameBoard.moveCompStart();
				moved=true;
			}
			//move a pawn into the safe space if able
			if((!moved) && (canMoveToSafe(1)!=-1))
			{
				//System.out.println("tugwos");

				gameBoard.moveCompSafe(1);
				moved=true;
			}
			//move a pawn into the home space if able
			if(!moved)
			{
				//System.out.println("tugwos");

				for(int pawn:pawnSafePositions)
				{
					if((pawn+1)==5)
					{
						gameBoard.moveCompHome(pawn);
						moved=true;
						break;
					}
				}
			}
			//move a pawn onto a slide if it doesn't bump allies
			if(!moved) 
			{
				//System.out.println("tugwos");
				dontSlide.clear();
				for(int pawn:pawnBoardPositions)
				{
					if(shouldSlide(gameBoard, pawn, 1))
					{
						gameBoard.move(pawn, newPosition(pawn,1));
						moved=true;
						break;
					}
				}
			}
			//move a pawn within the computer's safezone(not to home space)
			if(!moved)
			{
				for(int pawn:pawnSafePositions)
				{
					if(!(gameBoard.compSafe[pawn+1].getPlayer().equals("computer")))
					{
						gameBoard.moveInCompSafeZone(pawn, pawn+1);
						moved=true;
						break;
					}
				}
			}
			//move a pawn on the map forward that won't harm allies
			if(!moved)
			{
				//System.out.println("tugwos");

				for(int pawn:pawnBoardPositions)
				{
					if((!(gameBoard.board[newPosition(pawn,1)].getPlayer().equals("computer"))) && (!(dontSlide.contains(pawn))))
					{
						gameBoard.move(pawn, newPosition(pawn,1));
						moved=true;
						break;
					}
				}
			}
			//move a harmful pawn forward
			if(!moved)
			{
				//System.out.println("tugwos");

				for(int pawn:pawnBoardPositions)
				{
					if(!(gameBoard.board[newPosition(pawn,1)].getPlayer().equals("computer")))
					{
						gameBoard.move(pawn, newPosition(pawn,1));
						moved=true;
						break;
					}
				}
			}
			//no moves area available
			if(!moved)
			{
				System.out.println("No moves");
			}
		}
		else if(cardValue==2)
		{
			if((gameBoard.getCompStart()>0) && !(gameBoard.board[34].getPlayer().equals("computer")))
			{
				gameBoard.moveCompStart();
			}
		}
		else if(cardValue==3)
		{
			
		}
		else if(cardValue==4)
		{
			
		}
		else if(cardValue==5)
		{
			
		}
		else if(cardValue==7)
		{
			
		}
		else if(cardValue==8)
		{
			
		}
		else if(cardValue==10)
		{
			
		}
		else if(cardValue==11)
		{
			
		}
		else if(cardValue==12)
		{
			
		}
		else
		{
			if(gameBoard.compStart>0 && gameBoard.getUserBoardPositions().size()>0)
			{
				
			}
		}
	}
}
