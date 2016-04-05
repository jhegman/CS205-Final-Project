import java.util.ArrayList;


public class board 
{
	//the board starts on the green side and moves clockwise around the board from the top left
	public space[] board=new space[60];
	public space[] userSafe=new space[6];
	public space[] compSafe=new space[6];
	public int userHome, compHome;
	public int userStart, compStart;
	public int[] compPawns=new int[4];
	public int[] userPawns=new int[4];
	//Create a board with 60 spaces, two start spaces, and two safezones/home spaces
	public board()
	{
		int place=0;
		String color="green";
		for(int i=0;i<4;i++)
		{
			if(i==1)
			{
				color="red";						
			}
			else if(i==2)
			{
				color="blue";
			}
			else if(i==3)
			{
				color="yellow";
			}
			for(int j=0;j<15;j++)
			{
				if(j==1||j==9)
				{
					board[place]=new space("slideStart",color,false);
				}
				else if(j==2)
				{
					board[place]=new space("slide",color,true);
				}
				else if(j==3||j==4||j==10||j==11||j==12||j==13)
				{
					board[place]=new space("slide",color,false);
				}
				else
				{
					board[place]=new space("blank",color,false);
				}
				place++;
			}
		}
		for(int i=0;i<6;i++)
		{
			userSafe[i]=new space("safe", "blue", false);
			compSafe[i]=new space("safe", "green", false);
		}
		userStart=4;
		userHome=0;
		compStart=4;
		compHome=0;
	}
	//get the number of pawns in the computer's start space
	public int getCompStart()
	{
		return compStart;
	}
	//get the number of pawns in the user's start space
	public int getUserStart()
	{
		return userStart;
	}
	//swap the position of two pawns(with SORRY! card)
	public void sorry(String player,int end)
	{
		if(player.equals("user"))
		{
			board[end].setOccupied(true,"user");
			userStart--;
			compStart++;
		}
		else
		{
			board[end].setOccupied(true, "computer");
			compStart--;
			userStart++;
		}
	}
	//bump the pawn in a given position back to it's start and move into that spot
	public void bump(int start, int end)
	{
		if(board[end].getPlayer().equals("user"))
		{
			userStart++;
		}
		else if(board[end].getPlayer().equals("computer"))
		{
			compStart++;
		}
		board[end].setOccupied(true, board[start].getPlayer());
		board[start].setOccupied(false, "none");
	}
	//move a pawn from one space to another on the main board
	public void move(int start, int end)
	{
		space endSpace=board[end];
		space startSpace=board[start];
		if(startSpace.getPlayer().equals(endSpace.getPlayer()))
		{
			System.out.println("Illegal Move!");
		}
		else if(endSpace.getType().equals("slidestart") && (startSpace.getPlayer().equals("user") && !(endSpace.getColor().equals("blue"))))
		{
			bump(start,end);
			end++;
			while(board[end].getType().equals("slide"))
			{
				bump(start,end);
				end++;
			}
			board[end-1].setOccupied(true, "player");
			board[start+1].setOccupied(false, "none");
		}
		else if(endSpace.getType().equals("slidestart") && (startSpace.getPlayer().equals("computer") && !(endSpace.getColor().equals("green"))))
		{
			bump(start,end);
			end++;
			while(board[end].getType().equals("slide"))
			{
				bump(start,end);
				end++;
			}
			board[end-1].setOccupied(true, "computer");
			board[start+1].setOccupied(false, "none");
		}
		else if(endSpace.getPlayer().equals("none"))
		{
			board[end].setOccupied(true, startSpace.getPlayer());
			board[start].setOccupied(false, "none");
		}
		else 
		{
			bump(start, end);
		}
	}
	//move a pawn within the computers safe zone
	public void moveInCompSafeZone(int start, int end)
	{
		compSafe[start].setOccupied(false, "none");
		compSafe[end].setOccupied(true, "computer");
	}
	//move a pawn out of the computer's start space
	public void moveCompStart()
	{
		compStart--;
		board[4].setOccupied(true, "computer");
	}
	//move a pawn out of the user's start space
	public void moveUserStart()
	{
		userStart--;
		board[34].setOccupied(true, "user");
	}
	//move a pawn into the computer's safezone(from the space adjacent to it)
	public void moveCompSafe(int amount)
	{
		board[2].setOccupied(false, "none");
		compSafe[amount-1].setOccupied(true, "computer");
	}
	//move a pawn into the user's safe zone(from the space adjacent to it)
	public void moveUserSafe(int amount)
	{
		board[32].setOccupied(false, "none");
		userSafe[amount-1].setOccupied(true, "user");
	}
	//move a pawn into the computer's home space
	public void moveCompHome(int start)
	{
		compSafe[start].setOccupied(false, "none");
		compHome++;
	}
	//move a pawn into the user's home space
	public void moveUserHome(int start)
	{
		compSafe[start].setOccupied(false, "none");
		userHome++;
	}
	//return the positions of the computer's pawns on the main board
	public ArrayList<Integer> getComputerBoardPositions()
	{
		ArrayList <Integer> boardPositions=new ArrayList <Integer>();
		for(int i=0;i<board.length;i++)
		{
			if(board[i].getPlayer().equals("computer"))
			{
				boardPositions.add(i);
				//tugwos System.out.println(i+" PAWN!!");
			}
			//tugwos else System.out.println(i+" "+board[i].getPlayer());
		}
		return boardPositions;
	}
	//return the positions of the pawns in the computer's safezone
	public ArrayList<Integer> getComputerSafePositions()
	{
		ArrayList <Integer> safePositions=new ArrayList <Integer>();
		for(int i=0;i<compSafe.length;i++)
		{
			if(compSafe[i].getPlayer().equals("computer"))
			{
				safePositions.add(i);
			}
		}
		return safePositions;
	}
	//return the positions of the user's pawns on the main board
	public ArrayList<Integer> getUserBoardPositions()
	{
		ArrayList <Integer> uBoardPositions=new ArrayList <Integer>();
		for(int i=0;i<board.length;i++)
		{
			if(board[i].getPlayer().equals("user"))
			{
				uBoardPositions.add(i);
			}
		}
		return uBoardPositions;
	}
	//return the positions of the user's pawns in the safezone
	public ArrayList<Integer> getUserSafePositions()
	{
		ArrayList <Integer> uSafePositions=new ArrayList <Integer>();
		for(int i=0;i<userSafe.length;i++)
		{
			if(userSafe[i].getPlayer().equals("human"))
			{
				uSafePositions.add(i);
			}
		}
		return uSafePositions;
	}
	//print out the board and safe zones with positions of all pawns and special tiles labeled
	public void printBoard()
	{
		String go;
		System.out.println("\nTHE BOARD CURRENTLY LOOKS LIKE:\n--------------------------------------------\nSpace\tType\tOccupant\t\tATS\n--------------------------------------------");
		for(int k=0;k<board.length;k++)
		{
			System.out.println(k+"\t"+board[k].print());
			go=board[k].getPlayer();
		}
		System.out.println("\nTHE COMP SAFEZONE CURRENTLY LOOKS LIKE:\n--------------------------------------------\nSpace\tType\tOccupant\t\tATS\n--------------------------------------------");
		for(int i=0;i<compSafe.length;i++)
		{
			System.out.println(i+"\t"+compSafe[i].print());
		}
	}
	//print out the location of the computer's pawns
	public void printCompPositions()
	{
		System.out.println("\n-------------------------------------------");
		for(int i:getComputerBoardPositions())
		{
			System.out.println("There is a computer pawn at position: "+i);
		}
		System.out.println("\n");
		for(int i:getComputerSafePositions())
		{
			System.out.println("There is a computer pawn in the safezone at: "+i);
		}
	}	
}
