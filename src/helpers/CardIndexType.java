package helpers;

public enum CardIndexType
{
    Two(2),
    Three(3),
    Four(4),
    Five(5),
    Six(6),
    Seven(7),
    Eight(8),
    Nine(9),
    Ten(10),
    Jack(11),
    Queen(12),
    King(13),
    Ace(14),
    Shirt(999);
    
	private final int id;
    CardIndexType(int id){ this.id = id; }
    public int getValue() { return id; }
}