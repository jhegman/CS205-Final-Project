
public class Card
{

   //CONSTANTS 
   public static final int ONE = 1;
   public static final int TWO = 2;
   public static final int THREE = 3;
   public static final int FOUR = 4;
   public static final int FIVE = 5;
   public static final int SEVEN = 6;
   public static final int EIGHT = 7;
   public static final int TEN = 8;
   public static final int ELEVEN = 9; 
   public static final int TWELVE = 10;
   public static final int SORRY = 11;  


   //Variables
   private int number;
   private String description;
   

   //Set card
   public Card(int aNumber)
   {
   
      number = aNumber;
   
   }
   
   //Get card number
   public int getNumber()
   {
   
      switch(number)
      {
      
         case ONE:
         number = 1;
         break;
         
         case TWO:
         number = 2;
         break;
         
         case THREE:
         number = 3;
         break;
         
         case FOUR:
         number = 4;
         break;
         
         case FIVE:
         number = 5;
         break;
         
         case SEVEN:
         number = 6;
         break;
         
         case EIGHT:
         number = 7;
         break;
         
         case TEN:
         number = 8;
         break;
         
         case ELEVEN:
         number = 9;
         break;
         
         case TWELVE:
         number = 10;
         break;
         
         case SORRY:
         number = 11;
         break;
      
      }
      
      
      return number;
      
      }
      
      
      
      //Get description on card
      public String getDescription()
      {
      
         switch(number)
         {
         
            case ONE:
            description = "Move pawn on board forward 1 space or move pawn out of start space.";
            break;
            
            case TWO:
            description = "Move pawn on board forward 2 spaces or move pawn out of start space. Draw another card and repeat your turn.";
            break;
            
            case THREE:
            description = "Move pawn on board forward 3 spaces.";
            break;
            
            case FOUR:
            description = "Move pawn on board backward 4 spaces.";
            break;
            
            case FIVE:
            description = "Move pawn on board forward 5 spaces.";
            break;
            
            case SEVEN:
            description = "Move pawn on board forward 7 spaces, or move two pawns a total of 7 spaces between them (3 and 4).";
            break;
            
            case EIGHT:
            description = "Move pawn on board forward 8 spaces.";
            break;
            
            case TEN:
            description = "Move pawn on board forward 10 spaces, otherwise move pawn backwards 1 space";
            break;
            
            case ELEVEN:
            description = "Move pawn on board forward 11 spaces, or switch position of any pawn with that of an opponent's pawn, or forfeit.";
            break;
            
            case TWELVE:
            description = "Move pawn on board forward 12 spaces.";
            break;
            
            case SORRY:
            description = "Take pawn from own start space, \nplace on space of opponent's pawn, \nand put opponent's pawn in its respective start space.";
            break;
         
         }
         
         return description;
   
      
      }
      
      
      //Inoformation on each card in string form
      public String toString()
      {
      
         String str = "\nNumber: " + getNumber() + "\nDescription: " + getDescription();
         
         return str;
      
      
      }
      
   
   }

