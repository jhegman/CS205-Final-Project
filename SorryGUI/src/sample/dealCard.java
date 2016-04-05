package sample;
public class dealCard
{

   public static void main(String[] args)
   {
   
      Deck deck = new Deck();
      
      deck.shuffle();
      System.out.println(deck.getArray());
   
  
      for(int i = 0;i < 50;i++)
      {
      
         System.out.println(i + " " + deck.drawCard());
      
      }
      
      
      System.out.println(deck.getArray());
      
      
   }


}