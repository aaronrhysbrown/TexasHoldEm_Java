package helpers;

public class Analysis {

    private static short[][][][][][] HandRanks = new short[2][13][13][13][13][13];

    private static final int SUITED = 1;
    private static final int UNSUITED = 0;

    public static class HANDRANKINFO {
        public String Description;
        public int HandType;
        public int Rank1, Rank2, Rank3, Rank4, Rank5;
    };

    private static final int HighOCard = 0;
    private static final int OnePair = 1;
    private static final int TwoPairs = 2;
    private static final int ThreeOfAKind = 3;
    private static final int Straight = 4;
    private static final int Flush = 5;
    private static final int FullHouse = 6;
    private static final int FourOfAKind = 7;
    private static final int StraightFlush = 8;

    public static HANDRANKINFO[] HandRankInfo = new HANDRANKINFO[8000];

    private static String OCardChar(int cr)
    {
        switch (cr)
        {
            case 0:
                return "2";
            case 1:
                return "3";
            case 2:
                return "4";
            case 3:
                return "5";
            case 4:
                return "6";
            case 5:
                return "7";
            case 6:
                return "8";
            case 7:
                return "9";
            case 8:
                return "T";
            case 9:
                return "J";
            case 10:
                return "Q";
            case 11:
                return "K";
            case 12:
                return "A";
        }
        return "X";
    }



    private static void SetUpCombinations(int c1, int c2, int c3, int c4, int c5, short HandRank,
        int Suited, String DescFormat, int HandType,
        int Rank1, int Rank2, int Rank3, int Rank4, int Rank5)
    {
        int[] OCards = new int[5];
        int i1, i2, i3, i4, i5;

        OCards[0] = c1;
        OCards[1] = c2;
        OCards[2] = c3;
        OCards[3] = c4;
        OCards[4] = c5;

        for (i1 = 0; i1 <= 4; i1++) // i1 Goes from 0 to 4, e.g. 2
            for (i2 = 0; i2 <= 4; i2++)
                if (i2 != i1) // i2 goes from 0 to 4, skipping i1, i.e. 0,1,3,4 e.g. 3
                    for (i3 = 0; i3 <= 4; i3++)
                        if ((i3 != i2) && (i3 != i1)) // i3 goes from 0 to 4, skipping i2 and i1, i.e. 0,1,4, e.g. 0
                            for (i4 = 0; i4 <= 4; i4++)
                                if ((i4 != i3) && (i4 != i2) && (i4 != i1))
                                    // i4 goes from 0 to 4, skipping i3, i2 and i1, i.e. 1,4 e.g. 4
                                    for (i5 = 0; i5 <= 4; i5++)
                                        if ((i5 != i4) && (i5 != i3) && (i5 != i2) && (i5 != i1))
                                            // i5 goes from 0 to 4, skipping i4,i3,i2 and i1, i.e. 1
                                            HandRanks[Suited][OCards[i1]][OCards[i2]][OCards[i3]][OCards[i4]][OCards[i5]]
                                                = HandRank; // In this example, we set up [2][3][0][4][1]

        // Set up textual description
        HandRankInfo[HandRank] = new HANDRANKINFO();
        HandRankInfo[HandRank].Description = DescFormat + OCardChar(c1) + OCardChar(c2) + OCardChar(c3) +
                                             OCardChar(c4) + OCardChar(c5);
        HandRankInfo[HandRank].HandType = HandType;
        HandRankInfo[HandRank].Rank1 = Rank1 + 2;
        HandRankInfo[HandRank].Rank2 = Rank2 + 2;
        HandRankInfo[HandRank].Rank3 = Rank3 + 2;
        HandRankInfo[HandRank].Rank4 = Rank4 + 2;
        HandRankInfo[HandRank].Rank5 = Rank5 + 2;
    }


    public static void InitialiseTactics()
    {
        short Rank = 1; // We start at one for 7 high, then increment all the way up to a Royal Flush
        int i1, i2, i3, i4, i5;
        int res;


        // Handy OCard value reckoner:    Index     OCard
        //									0		2
        //									1	-	3
        //									2		4
        //									3	-	5
        //									4		6
        //									5	-	7
        //									6		8	
        //									7	-	9
        //									8		10
        //									9	-	Jack
        //									10		Queen
        //									11	-	King
        //									12		Ace

        // First set up high OCard ranks - Lowest high OCard is 7 high (OCard Rank 7 is index 5)
        for (i1 = 5; i1 <= 12; i1++)
            for (i2 = 3; i2 < i1; i2++) // Second OCard can go from OCard Rank 5 to one below i1
                for (i3 = 2; i3 < i2; i3++) // Third OCard can go from 4 to one below i2
                    for (i4 = 1; i4 < i3; i4++) // Fourth OCard can go from 3 to one below i3
                        for (i5 = 0; i5 < i4; i5++) // Fifth OCard can go from 2 to one below i4
                            if ((i1-i5 > 4) && ((i1!=12)||(i2!=3)||(i3!=2)||(i4!=1)||(i5!=0)))// However, where the difference between highest and lowest is exactly 4 or in the specific case of A2345, this is a straight
                                SetUpCombinations(i1, i2, i3, i4, i5, Rank++, UNSUITED, "a High OCard ", HighOCard,
                                	i1, i2, i3, i4, i5);
        
        res = Rank;
        // Then its pairs
        for (i1 = 0; i1 <= 12; i1++) // Pair can be anything from 2 to Ace
            for (i2 = 2; i2 <= 12; i2++) // first kicker can be anything from 4 to Ace
                for (i3 = 1; i3 < i2; i3++) // second kicker can be anything from 3 to one below first kicker
                    for (i4 = 0; i4 < i3; i4++) // third kicker can be anything from 2 to one below second kicker
                        if ((i1 != i2) && (i1 != i3) && (i1 != i4))
                            // However, none of the kickers can be equal to the pair OCard
                            SetUpCombinations(i1, i1, i2, i3, i4, Rank++, UNSUITED, "a Pair ", OnePair, i1, i2, i3,
                                i4, 0);
        res = Rank - res;
        // Then two pairs
        for (i1 = 1; i1 <= 12; i1++) // Top pair can be anything from 3 to Ace
            for (i2 = 0; i2 < i1; i2++) // Second pair can be anything from 2 one below top pair
                for (i3 = 0; i3 <= 12; i3++) // Single kicker can be any OCard...
                    if ((i3 != i1) && (i3 != i2)) // .. as long as it does not match either of the pairs
                        SetUpCombinations(i1, i1, i2, i2, i3, Rank++, UNSUITED, "Two Pairs ", TwoPairs, i1, i2, i3,
                            0, 0);

        res = Rank - res;
        // Three of a kind
        for (i1 = 0; i1 <= 12; i1++) // Trips can be anything from 2 to Ace
            for (i2 = 1; i2 <= 12; i2++) // First kicker can be anything from 3 to Ace
                for (i3 = 0; i3 < i2; i3++) // Second kicker can be anything from 2 to one less than first kicker
                    if ((i2 != i1) && (i3 != i1)) // Neither kicker can match the trips OCard
                        SetUpCombinations(i1, i1, i1, i2, i3, Rank++, UNSUITED, "Three Of A Kind ", ThreeOfAKind, i1,
                            i2, i3, 0, 0);

        res = Rank - res;
        // Straight
        SetUpCombinations(3, 2, 1, 0, 12, Rank++, UNSUITED, "a Straight ", Straight, 5, 0, 0, 0, 0);
            // Special case for 5 high straight
        for (i1 = 4; i1 <= 12; i1++) // Top OCard of other straights can be anything from 6 to Ace
            SetUpCombinations(i1, i1 - 1, i1 - 2, i1 - 3, i1 - 4, Rank++, UNSUITED, "a Straight ", Straight, i1, 0,
                0, 0, 0);

        res = Rank - res;
        // Flush - same as high OCard except SUITED
        for (i1 = 5; i1 <= 12; i1++)
            for (i2 = 3; i2 < i1; i2++) // Second OCard can go from OCard Rank 5 to one below i1
                for (i3 = 2; i3 < i2; i3++) // Third OCard can go from 4 to one below i2
                    for (i4 = 1; i4 < i3; i4++) // Fourth OCard can go from 3 to one below i3
                        for (i5 = 0; i5 < i4; i5++) // Fifth OCard can go from 2 to one below i4
                        	if ((i1-i5 > 4) && ((i1!=12)||(i2!=3)||(i3!=2)||(i4!=1)||(i5!=0)))// However, where the difference between highest and lowest is exactly 4 or in the specific case of A2345, this is a straight
                                SetUpCombinations(i1, i2, i3, i4, i5, Rank++, SUITED, "a Flush ", Flush, i1, i2, i3,
                                    i4, i5);

        res = Rank - res;
        // Full House
        for (i1 = 0; i1 <= 12; i1++) // Three of a kind can be anything from 2 to Ace
            for (i2 = 0; i2 <= 12; i2++) // As can pair, ...
                if (i1 != i2) // But they cannot be the same
                    SetUpCombinations(i1, i1, i1, i2, i2, Rank++, UNSUITED, "a Full House ", FullHouse, i1, i2, 0, 0,
                        0);

        res = Rank - res;
        // Four of a kind
        for (i1 = 0; i1 <= 12; i1++) // Four of a kind can be anything from 2 to Ace
            for (i2 = 0; i2 <= 12; i2++) // As can kicker...
                if (i1 != i2) // But they cannot be the same
                    SetUpCombinations(i1, i1, i1, i1, i2, Rank++, UNSUITED, "Four Of A Kind ", FourOfAKind, i1, i2,
                        0, 0, 0);

        res = Rank - res;
        // Straight Flush - same as straight, except SUITED
        SetUpCombinations(3, 2, 1, 0, 12, Rank++, SUITED, "a Straight Flush ", StraightFlush, i1, 0, 0, 0, 0);
            // Special case for 5 high straight
        for (i1 = 4; i1 <= 12; i1++) // Top OCard of other straights can be anything from 6 to Ace
            SetUpCombinations(i1, i1 - 1, i1 - 2, i1 - 3, i1 - 4, Rank++, SUITED, "a Straight Flush ", StraightFlush,
                i1, 0, 0, 0, 0);

        //	for (i1=0; i1<=12; i1++) // In theory there should be no blanks now for UNSUITED, except invalid hands with identical OCards
        //		for (i2=0; i2<=12; i2++)
        //			for (i3=0; i3<=12; i3++)
        //				for (i4=0; i4<=12; i4++)
        //					for (i5=0; i5<=12; i5++)
        //						if ((i1!=i2) && (i1!=i3) && (i1!=i4) && (i1!=i5)
        //							&& (i2!=i3) && (i2!=i4) && (i2!=i5)
        //							&& (i3!=i4) && (i3!=i5)
        //							&& (i4!=i5))
        //						{
        //							if (HandRanks[UNSUITED][i1][i2][i3][i4][i5]==0)
        //								raise(SIGABRT);
        //						}
        //	tactics();
    }

    private static short CheckFiveOCards(OCard c1, OCard c2, OCard c3, OCard c4, OCard c5)
    {
        if ((c2.Suit == c1.Suit) && (c3.Suit == c1.Suit) && (c4.Suit == c1.Suit) && (c5.Suit == c1.Suit)) // Suited
            return HandRanks[SUITED][c1.RankIdx][c2.RankIdx][c3.RankIdx][c4.RankIdx][c5.RankIdx];
        else // Hand is unSuited
            return HandRanks[UNSUITED][c1.RankIdx][c2.RankIdx][c3.RankIdx][c4.RankIdx][c5.RankIdx];
    }


    private static short CheckSixOCards(OCard c1, OCard c2, OCard c3, OCard c4, OCard c5, OCard c6)
    {
        short bestrank, testrank;

        bestrank = CheckFiveOCards(c1, c2, c3, c4, c5);
        testrank = CheckFiveOCards(c1, c2, c3, c4, c6);
        if (testrank > bestrank) bestrank = testrank;
        testrank = CheckFiveOCards(c1, c2, c3, c5, c6);
        if (testrank > bestrank) bestrank = testrank;
        testrank = CheckFiveOCards(c1, c2, c4, c5, c6);
        if (testrank > bestrank) bestrank = testrank;
        testrank = CheckFiveOCards(c1, c3, c4, c5, c6);
        if (testrank > bestrank) bestrank = testrank;
        testrank = CheckFiveOCards(c2, c3, c4, c5, c6);
        if (testrank > bestrank) bestrank = testrank;

        return bestrank;
    }


    private static short CheckSevenOCards(OCard c1, OCard c2, OCard c3, OCard c4, OCard c5, OCard c6, OCard c7)
    {
        short bestrank, testrank;

        bestrank = CheckFiveOCards(c1, c2, c3, c4, c5); // Missing 7, 6
        testrank = CheckFiveOCards(c1, c2, c3, c4, c6);
        if (testrank > bestrank) bestrank = testrank; // 7, 5
        testrank = CheckFiveOCards(c1, c2, c3, c5, c6);
        if (testrank > bestrank) bestrank = testrank; // 7, 4
        testrank = CheckFiveOCards(c1, c2, c4, c5, c6);
        if (testrank > bestrank) bestrank = testrank; // 7, 3
        testrank = CheckFiveOCards(c1, c3, c4, c5, c6);
        if (testrank > bestrank) bestrank = testrank; // 7, 2
        testrank = CheckFiveOCards(c2, c3, c4, c5, c6);
        if (testrank > bestrank) bestrank = testrank; // 7, 1

        testrank = CheckFiveOCards(c1, c2, c3, c4, c7);
        if (testrank > bestrank) bestrank = testrank; // 6, 5
        testrank = CheckFiveOCards(c1, c2, c3, c5, c7);
        if (testrank > bestrank) bestrank = testrank; // 6, 4
        testrank = CheckFiveOCards(c1, c2, c4, c5, c7);
        if (testrank > bestrank) bestrank = testrank; // 6, 3
        testrank = CheckFiveOCards(c1, c3, c4, c5, c7);
        if (testrank > bestrank) bestrank = testrank; // 6, 2
        testrank = CheckFiveOCards(c2, c3, c4, c5, c7);
        if (testrank > bestrank) bestrank = testrank; // 6, 1

        testrank = CheckFiveOCards(c1, c2, c3, c6, c7);
        if (testrank > bestrank) bestrank = testrank; // 5, 4
        testrank = CheckFiveOCards(c1, c2, c4, c6, c7);
        if (testrank > bestrank) bestrank = testrank; // 5, 3
        testrank = CheckFiveOCards(c1, c3, c4, c6, c7);
        if (testrank > bestrank) bestrank = testrank; // 5, 2
        testrank = CheckFiveOCards(c2, c3, c4, c6, c7);
        if (testrank > bestrank) bestrank = testrank; // 5, 1

        testrank = CheckFiveOCards(c1, c2, c5, c6, c7);
        if (testrank > bestrank) bestrank = testrank; // 4, 3
        testrank = CheckFiveOCards(c1, c3, c5, c6, c7);
        if (testrank > bestrank) bestrank = testrank; // 4, 2
        testrank = CheckFiveOCards(c2, c3, c5, c6, c7);
        if (testrank > bestrank) bestrank = testrank; // 4, 1

        testrank = CheckFiveOCards(c1, c4, c5, c6, c7);
        if (testrank > bestrank) bestrank = testrank; // 3, 2
        testrank = CheckFiveOCards(c2, c4, c5, c6, c7);
        if (testrank > bestrank) bestrank = testrank; // 3, 1

        testrank = CheckFiveOCards(c3, c4, c5, c6, c7);
        if (testrank > bestrank) bestrank = testrank; // 2, 1

        return bestrank;
    }


    public static short AssessHand(OCard hole1, OCard hole2, OCard flop1, OCard flop2, OCard flop3,
        OCard turn, OCard river)
    {
        //	HANDRANK hr;
        // This function can be called with either 5, 6 or 7 valid OCards
        if (turn == null) // If we have only five valid OCards:
            return CheckFiveOCards(hole1, hole2, flop1, flop2, flop3);
        else if (river == null) // If we have six valid OCards, we need to find the best hand...
            return CheckSixOCards(hole1, hole2, flop1, flop2, flop3, turn);
        else
            return CheckSevenOCards(hole1, hole2, flop1, flop2, flop3, turn, river);
    }
}