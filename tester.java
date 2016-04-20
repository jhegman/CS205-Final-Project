import java.util.ArrayList;
import java.util.Random;


public class tester {
	public static void main(String[]args)
	{
		board b=new board();
		computer c=new computer(true);
		ArrayList userPawns=b.getUserBoardPositions();
		b.printBoard();
		
		//c.moveNice(b, 1);
		//b.board[2].setOccupied(true, "computer");
		//b.compStart--;
		//b.printBoard();
		Random rand=new Random();
		for(int i=1;i<51;i++)
		{
			System.out.println("TURN: "+i);
			int move=rand.nextInt(10)+1;
			System.out.println("Move Value for this turn= "+move);
			c.makeMove(b, move);
			//.printBoard();
			b.printCompPositions();
		}		
	}

}
