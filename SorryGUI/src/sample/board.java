package sample;
import java.util.ArrayList;


public class board 
{
	//the board starts on the green side and moves clockwise around the board from the top left
	public space[] board=new space[60];
	public space[] userSafe=new space[5];
	public space[] compSafe=new space[5];
	public int userHome, compHome;
	public int userStart, compStart;
	public int[] compPawns=new int[4];
	public int[] userPawns=new int[4];
	private int compStartPosition;
	private int compEndPosition;
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
		for(int i=0;i<5;i++)
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
	public int[] returnPositions()
	{
		int[] positions=new int[2];
		positions[0]=compStartPosition;
		positions[1]=compEndPosition;
		return positions;
	}
	//move a pawn from one space to another on the main board
	public void move(int start, int end)
	{
		compStartPosition=start;
		compEndPosition=end;
		space endSpace=board[end];
		space startSpace=board[start];
		if(startSpace.getPlayer().equals(endSpace.getPlayer()))
		{
			System.out.println("Illegal Move!");
		}
		else if(endSpace.getType().equals("slidestart") && (startSpace.getPlayer().equals("user") && !(endSpace.getColor().equals("blue"))))
		{
			int slidFrom=end;
			bump(start,end);
			end++;
			while(board[end].getType().equals("slide"))
			{
				bump(start,end);
				end++;
			}
			board[end-1].setOccupied(true, "player");
			System.out.println(slidFrom);
			board[slidFrom].setOccupied(false, "none");
		}
		else if(endSpace.getType().equals("slidestart") && (startSpace.getPlayer().equals("computer") && !(endSpace.getColor().equals("green"))))
		{
			int slidFrom=end;
			bump(start,end);
			end++;
			while(board[end].getType().equals("slide"))
			{
				bump(start,end);
				end++;
			}
			board[end-1].setOccupied(true, "computer");
			board[slidFrom].setOccupied(false, "none");
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
		compStartPosition=start;
		compSafe[end].setOccupied(true, "computer");
		compEndPosition=end;
	}
	//move a pawn out of the computer's start space
	public void moveCompStart()
	{
		compStart--;
		board[4].setOccupied(true, "computer");
		compStartPosition=-1;
		compEndPosition=4;
	}
	//move a pawn into the computer's safezone(from the space adjacent to it)
	public void moveCompSafe(int start, int end)
	{
		compStartPosition=start;
		compEndPosition=57+end;
		board[start].setOccupied(false, "none");
		if(end==8)
		{
			compHome++;
		}
		else
		{
			compSafe[end-3].setOccupied(true, "computer");
		}
	}
	//move a pawn into the computer's home space
	public void moveCompHome(int start)
	{
		compStartPosition=start;
		compEndPosition=70;
		compSafe[start].setOccupied(false, "none");
		compHome++;
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
		System.out.println("There are/is "+compStart+" pawn(s) in the computer's start space");
		System.out.println("There are/is "+compHome+" pawn(s) in the computer's home space");
	}	
}
