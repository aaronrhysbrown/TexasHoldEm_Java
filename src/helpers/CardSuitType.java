package helpers;

public enum CardSuitType
{
    Clubs(0),
    Diamonds(1),
    Hearts(2),
    Spades(3),
    Shirt(999);

	private final int id;
    CardSuitType(int id){ this.id = id; }
    public int getValue() { return id; }
}