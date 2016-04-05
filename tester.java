import java.util.ArrayList;


public class tester {
	public static void main(String[]args)
	{
		board b=new board();
		computer c=new computer(true);
		ArrayList userPawns=b.getUserBoardPositions();
		b.printBoard();
		
		c.moveNice(b, 1);
		//b.board[2].setOccupied(true, "computer");
		b.compStart--;
		b.printBoard();
		for(int i=0;i<2;i++)
		{
			//System.out.println("TURN: "+i);
			c.moveNice(b, 1);
			b.printBoard();
			//b.printCompPositions();
		}		
	}

}
