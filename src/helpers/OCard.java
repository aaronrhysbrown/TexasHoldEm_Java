package helpers;

public class OCard
{
    public int Rank; // Rank is 2..14 where 14 is Ace
    public int RankIdx; // For performance this is Rank-2;
    public char Suit; // Suit is H, C, D or S

    public OCard() // Constructor
    {
        Rank = 0;
        Suit = 'X';
    }

    public OCard(int rank, char suit)
    {
        Rank = rank;
        RankIdx = rank - 2;
        Suit = suit;
    }

    public OCard(char rankchar, char suit)
    {
        switch (rankchar)
        {
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                Rank = (int) rankchar - '0';
                break;
            case 'T':
                Rank = 10;
                break;
            case 'J':
                Rank = 11;
                break;
            case 'Q':
                Rank = 12;
                break;
            case 'K':
                Rank = 13;
                break;
            case 'A':
                Rank = 14;
                break;
        }
        RankIdx = Rank - 2;
        Suit = suit;
    }

    public OCard(int BetfairID)
    {
        RankIdx = (BetfairID + 12)%13;
        Rank = RankIdx + 2;
        switch (BetfairID/13)
        {
            case 0:
                Suit = 'C';
                break;
            case 1:
                Suit = 'D';
                break;
            case 2:
                Suit = 'H';
                break;
            case 3:
                Suit = 'S';
                break;
        }
    }


    public char RankChar()
    {
        switch (Rank)
        {
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return (char) ((int) '0' + Rank);
            case 10:
                return 'T';
            case 11:
                return 'J';
            case 12:
                return 'Q';
            case 13:
                return 'K';
            case 14:
                return 'A';
            default:
                return 'X';
        }
    }


    public String Print()
    {
        return Character.toString(RankChar()) + Character.toString(Suit);
    }

    public String ImageName()
    {
        if (Rank == 0)
            return "images.OCard back.jpg";
        String imagestring = "images.OCards_";
        switch (Suit)
        {
            case 'H':
                imagestring += '1';
                break;
            case 'C':
                imagestring += '2';
                break;
            case 'D':
                imagestring += '3';
                break;
            case 'S':
                imagestring += '4';
                break;
            default:
                return null;
        }
        imagestring += "x";
        if (Rank == 14)
            return imagestring + "1.jpg";
        return imagestring + Rank + ".jpg";
    }
}