
public class space {
	/*Occupied tells whether or not the space has a pawn on it. 
	 * adjacentToSafe tells if the given space is next to the safe zone.
	 * types of spaces: blank, slideStart, slide, safe
	 * color: yellow, green, red, blue
	 * player: computer or user
	*/
	private boolean occupied, adjacentToSafe;
	private String type, color, player;
	
	public space(String t, String c, boolean ath)
	{
		type=t.toLowerCase();
		if(type.equals("blank"))
		{
			color="none";
		}
		else color=c.toLowerCase();
		adjacentToSafe=ath;
		occupied=false;
		player="none";
	}
	public void setOccupied(boolean o, String p)
	{
		occupied=o;
		player=p.toLowerCase();
	}
	public String getColor()
	{
		return color;
	}
	public String getType()
	{
		return type;
	}
	public boolean isOccupied()
	{
		return occupied;
	}
	public boolean adjacentToHome()
	{
		return adjacentToSafe;
	}
	public String getPlayer()
	{
		return player;
	}
	public String print()
	{
		String attributes=color+"\t"+type+"\t";	
		if(!type.equals("slidestart"))
		{
			attributes+="\t";
		}
		attributes+=player;
		if(adjacentToSafe==true)
		{
			attributes+="\ttrue";
		}
		return attributes;
	}
}
