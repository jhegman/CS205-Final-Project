import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;


public class Deck extends Card
{


      final int CARDS_IN_DECK = 45;

      private ArrayList<Card> deck = new ArrayList<Card>();
      private ArrayList<Card> discard = new ArrayList<Card>();
      
      
      public Deck()
      {        
            super(0);
            freshDeck();
      }
      
      public void freshDeck()
      {
      
         //Offset with 5 1 cards
         deck.add(new Card(1));
      
         for(int i = 1; i < 11+1; i++)
         {
            for(int j = 1; j < 4+1; j++)
            {
               
               deck.add(new Card(i));
               
            }
         }
         
         
          
      }
      
      
       public ArrayList<Card> getArray()
       {
   
            return deck;
      
   
       }
       
       
       public void shuffle()
       {
       
            Collections.shuffle(deck);
       
       }
       
       public Card drawCard()
       {
       
         Card topCard = deck.get(0);
         discard.add(topCard);
         deck.remove(0);
       
         while(deck.size() < 1)
         {
            
           
            for(int i = 0; i < CARDS_IN_DECK; i++)
            {
            
                  deck.add(discard.get(i));
            
            }
   
         
         }
         
                  
        
         return topCard;
       
       }
      
      

}