package texasholdem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import helpers.*;

public class TexasHoldEm {
	/** Random number generator for our auto play game strategy. */
    static Random r = new Random();

    /* Use this method to define the bot's strategy .
     * You have to set the fold and betsize variables in order to make a move.
     * By default, if fold is true, the bot will fold regardless of betsize.
     * Feel free to use the helper values and methods available.
     */
    public static TexasHoldEmMove calculateMove(MainWindow.GameState state) {
        // Calculate the bet size and whether we should fold.
        Boolean fold;
        int betsize;
        fold = (r.nextInt(10) == 0);
        betsize = (r.nextInt(10) + 1) * 20;
    	
    	
        
        
    	/* Helper values */
    	
    	// Calculate min/max possible bets.
    	// If you try and bet less than minBet, you will fold.
        int minBet = state.OpponentRoundBetTotal - state.PlayerRoundBetTotal;
        int maxBet = minBet + state.OpponentStack;
        // If you try and bet more than maxBet, you will bet maxBet.
        if (maxBet > state.PlayerStack) {
            maxBet = state.PlayerStack; 
        }

        List<Card> boardCards = Card.toCards(state.BoardCards);
        int boardCardsCount = boardCards.size();

        List<Card> playerHand = Card.toCards(state.PlayerHand);
        
        List<Card> allCards = new ArrayList<Card>();
        allCards.addAll(playerHand);
        allCards.addAll(boardCards);

        Card hole1 = playerHand.get(0);
        Card hole2 = playerHand.get(1);
        Card flop1 = TexasHoldEmHelpers.FirstOrDefault(boardCards);
        Card flop2 = TexasHoldEmHelpers.SecondOrDefault(boardCards);
        Card flop3 = TexasHoldEmHelpers.ThirdOrDefault(boardCards);
        Card turn = TexasHoldEmHelpers.FourthOrDefault(boardCards);
        Card river = TexasHoldEmHelpers.FifthOrDefault(boardCards);

        int round = state.Round;
        Boolean isOurFirstBetOfRound = round == 0
            ? state.PlayerRoundBetTotal <= state.BigBlind 
            : state.PlayerRoundBetTotal == 0;

        int bigBlind = state.BigBlind;
        int smallBlind = state.SmallBlind;
        int dealCount = state.DealCount;
        int dealNumber = state.DealNumber;
        Boolean isDealer = state.IsDealer;
        int opponentRoundBetTotal = state.OpponentRoundBetTotal;
        int opponentStack = state.OpponentStack;
        int playerRoundBetTotal = state.PlayerRoundBetTotal;
        int playerStack = state.PlayerStack;
        int potAfterPreviousRound = state.PotAfterPreviousRound;
        String responseDeadline = state.ResponseDeadline;
        
        
        /* Helper methods */

        // IsPicture Helpers
        Boolean isPictureHole1 = TexasHoldEmHelpers.IsPictureOrAce(hole1);
        Boolean isPictureHole2 = TexasHoldEmHelpers.IsPictureOrAce(hole2);
        Boolean isPictureOrTenHole1 = TexasHoldEmHelpers.IsPictureOrAceOrTen(hole1);
        Boolean isPictureOrTenHole2 = TexasHoldEmHelpers.IsPictureOrAceOrTen(hole2);

        // IsPair Helpers
        // IsPair could check a list of cards with any number of cards
        Boolean isPairPlayerHand = TexasHoldEmHelpers.IsPair(playerHand);
        // IsPair could take any number of parameters
        Boolean isPair = TexasHoldEmHelpers.IsPair(allCards);

        // IsTwoPair Helpers
        // IsTwoPair could check a list of cards with any number of cards
        Boolean isTwoPairBoardCards = TexasHoldEmHelpers.IsTwoPair(boardCards);
        // IsTwoPair could take any number of parameters
        Boolean isTwoPair = TexasHoldEmHelpers.IsTwoPair(allCards);

        // IsThreeOfAKind Helpers
        // IsThreeOfAKind could check a list of cards with any number of cards
        Boolean isThreeOfAKindBoardCards = TexasHoldEmHelpers.IsThreeOfAKind(boardCards);
        // IsThreeOfAKind could take any number of parameters
        Boolean isThreeOfAKind = TexasHoldEmHelpers.IsThreeOfAKind(allCards);

        // IsStraight Helpers
        // IsStraight could check a list of cards with any number of cards
        Boolean isStraightBoardCards = TexasHoldEmHelpers.IsStraight(boardCards);
        // IsStraight could take any number of parameters
        Boolean isStraight = TexasHoldEmHelpers.IsStraight(allCards);

        // IsFlush Helpers
        // IsFlush could check a list of cards with any number of cards
        Boolean isFlushBoardCards = TexasHoldEmHelpers.IsFlush(boardCards);
        // IsFlush could take any number of parameters
        Boolean isFlush = TexasHoldEmHelpers.IsFlush(allCards);

        // IsFullHouse Helpers
        // IsFullHouse could check a list of cards with any number of cards
        Boolean isFullHouseBoardCards = TexasHoldEmHelpers.IsFullHouse(boardCards);
        // IsFullHouse could take any number of parameters
        Boolean isFullHouse = TexasHoldEmHelpers.IsFullHouse(allCards);

        // IsFourOfAKind Helpers
        // IsFourOfAKind could check a list of cards with any number of cards
        Boolean isFourOfAKindBoardCards = TexasHoldEmHelpers.IsFourOfAKind(boardCards);
        // IsFourOfAKind could take any number of parameters
        Boolean isFourOfAKind = TexasHoldEmHelpers.IsFourOfAKind(allCards);

        // IsStraightFlush Helpers
        // IsStraightFlush could check a list of cards with any number of cards
        Boolean isStraightFlushBoardCards = TexasHoldEmHelpers.IsStraightFlush(boardCards);
        // IsStraightFlush could take any number of parameters
        Boolean isStraightFlush = TexasHoldEmHelpers.IsStraightFlush(allCards);

        // IsFourCardFlush Helpers
        // IsFourCardFlush could check a list of cards with any number of cards
        Boolean isFourCardFlushBoardCards = TexasHoldEmHelpers.IsFourCardFlush(boardCards);
        // IsFourCardFlush could take any number of parameters
        Boolean isFourCardFlush = TexasHoldEmHelpers.IsFourCardFlush(allCards);

        // IsFourCardStraight Helpers
        // IsFourCardStraight could check a list of cards with any number of cards
        Boolean isFourCardStraightBoardCards = TexasHoldEmHelpers.IsFourCardStraight(boardCards);
        // IsFourCardStraight could take any number of parameters
        Boolean isFourCardStraight = TexasHoldEmHelpers.IsFourCardStraight(allCards);

        // IsSuitedConnector Helpers
        Boolean isSuitedConnector;
        // IsSuitedConnector checks a list of EXACTLY 2 cards
        isSuitedConnector = TexasHoldEmHelpers.IsSuitedConnector(playerHand);
        isSuitedConnector = TexasHoldEmHelpers.IsSuitedConnector(hole1, hole2);

        // IsHiddenPair Helpers
        Boolean isHiddenPair;
        // IsHiddenPair could check a list of cards with any number of cards
        isHiddenPair = TexasHoldEmHelpers.IsHiddenPair(playerHand, boardCards);

        // ToShorthandString Helpers
        String shorthand;
        // you can use list or card method to get shorthand string
        // see next examples:
        shorthand = TexasHoldEmHelpers.ToShorthandString(playerHand);
        shorthand = TexasHoldEmHelpers.ToShorthandString(hole1, hole2);

        // HoleRank Helpers
        int holeRank;
        // HoleRank checks a list of EXACTLY 2 cards
        holeRank = TexasHoldEmHelpers.HoleRank(playerHand);
        holeRank = TexasHoldEmHelpers.HoleRank(hole1, hole2);

        // HandRank Helpers
        // HandRank could check a list of cards with AT LEAST 5 cards
        int boardCardsRank = TexasHoldEmHelpers.HandRank(boardCards);
        // Or you can call card method with AT LEAST 6 parameters
        int handRank = TexasHoldEmHelpers.HandRank(allCards);

        // HandRankDesc returns English description of the hand
        String handRankDescription = TexasHoldEmHelpers.HandRankDesc(handRank);


        return new TexasHoldEmMove(fold, betsize);
    }
}
