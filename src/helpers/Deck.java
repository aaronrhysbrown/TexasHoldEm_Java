package helpers;

import java.util.Random;

public class Deck
{
    public OCard[] OCards = new OCard[52];
    public int Dealt = 0;

    public Deck() // Constructor
    {
        for (int rank = 2; rank <= 14; rank++)
        {
            OCards[(rank - 2)*4 + 0] = new OCard(rank, 'H');
            OCards[(rank - 2)*4 + 1] = new OCard(rank, 'C');
            OCards[(rank - 2)*4 + 2] = new OCard(rank, 'D');
            OCards[(rank - 2)*4 + 3] = new OCard(rank, 'S');
        }
        Shuffle();
    }

    public void Shuffle()
    {
        Random autoRand = new Random();

        for (int i = 0; i < 52; i++)
        {
            int SwitchWith = autoRand.nextInt(52);
            OCard tempc = OCards[i];
            OCards[i] = OCards[SwitchWith];
            OCards[SwitchWith] = tempc;
        }
        Dealt = 0;
    }

    public OCard DealOCard()
    {
        Dealt++;
        return OCards[Dealt - 1];
    }

    public void ForceOCard(int Index, OCard OCard)
    {
        int i;
        for (i = 0; i < 52; i++) // Find desired OCard
            if ((OCards[i].Rank == OCard.Rank) && (OCards[i].Suit == OCard.Suit))
                break;
        OCard tempc = OCards[i]; // Swap it with the index OCard
        OCards[i] = OCards[Index];
        OCards[Index] = tempc;
    }

    public void RemoveOCard(OCard OCard)
    {
        ForceOCard(Dealt, OCard);
        DealOCard();
    }
}