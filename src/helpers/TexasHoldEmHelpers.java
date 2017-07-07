package helpers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import helpers.Card.CardComparatorBySuit;

import java.util.ArrayList;

public class TexasHoldEmHelpers {

    public static List<Card> ToList(Card self)
    {
        return new ArrayList<Card>(Arrays.asList(self));
    }

    public static Card FirstOrDefault(List<Card> cards)
    {
        return AtIndexOrDefault(cards, 0);
    }

    public static Card SecondOrDefault(List<Card> cards)
    {
        return AtIndexOrDefault(cards, 1);
    }

    public static Card ThirdOrDefault(List<Card> cards)
    {
        return AtIndexOrDefault(cards, 2);
    }

    public static Card FourthOrDefault(List<Card> cards)
    {
        return AtIndexOrDefault(cards, 3);
    }

    public static Card FifthOrDefault(List<Card> cards)
    {
        return AtIndexOrDefault(cards, 4);
    }

    public static Card AtIndexOrDefault(List<Card> cards, int index)
    {
        if(cards.size() > index)
        {
            return cards.get(index);
        }
        return null;
    }
/*
    private static T Is<T>(Function<List<Card>, T> action, Card self, Card[] cards)
    {
        if (self == null || cards == null || cards.Length == 0)
        {
            return default(T);
        }
        var list = cards.ToList();
        list.Insert(0, self);
        return action(list);
    }

    public static bool IsPair(Card self, params Card[] cards)
    {
        return Is(IsPair, self, cards);
    }

    public static bool IsTwoPair(Card self, params Card[] cards)
    {
        return Is(IsTwoPair, self, cards);
    }

    public static bool IsThreeOfAKind(Card self, params Card[] cards)
    {
        return Is(IsThreeOfAKind, self, cards);
    }

    public static bool IsStraight(Card self, params Card[] cards)
    {
        return Is(IsStraight, self, cards);
    }

    public static bool IsFlush(Card self, params Card[] cards)
    {
        return Is(IsFlush, self, cards);
    }

    public static bool IsFullHouse(Card self, params Card[] cards)
    {
        return Is(IsFullHouse, self, cards);
    }

    public static bool IsFourOfAKind(Card self, params Card[] cards)
    {
        return Is(IsFourOfAKind, self, cards);
    }

    public static bool IsStraightFlush(Card self, params Card[] cards)
    {
        return Is(IsStraightFlush, self, cards);
    }

    public static bool IsFourCardFlush(Card self, params Card[] cards)
    {
        return Is(IsFourCardFlush, self, cards);
    }

    public static bool IsFourCardStraight(Card self, params Card[] cards)
    {
        return Is(IsFourCardStraight, self, cards);
    }

    public static bool IsHiddenPair(Card self, out int pairRank, params Card[] cards)
    {
        return IsHiddenPair(new List<Card> { self }, cards, out pairRank);
    }

    public static int HandRank(Card self, params Card[] cards)
    {
        return Is(HandRank, self, cards);
    }
    #endregion
*/
//    #region Private
    private static Boolean _isInitialiseTactics = false;
    private static void InitAnalysis()
    {
        if(!_isInitialiseTactics)
        {
            Analysis.InitialiseTactics();
            _isInitialiseTactics = true;
        }
    }

    public static OCard OpCard(Card self)
    {
        return self == null ? null : new OCard(self.toString().toUpperCase().charAt(0), self.Suit.toString().charAt(0));
    }

    private static Boolean _IsFlush(List<Card> self, int combinationCount)
    {
        if(self == null) return false;
        self.removeAll(Collections.singleton(null));
        
        if(self.size() < combinationCount) return false;
        
        List<List<Card>> cardCombinations = Combinations.combinations(self, combinationCount);
        
        for(List<Card> cards : cardCombinations) {
        	Collections.sort(cards, new Card.CardComparatorBySuit());
        	if(cards.get(0).Suit == cards.get(cards.size()-1).Suit) return true;
        }
        return false;
    }

    private static Boolean _IsStraight(List<Card> self, int combinationCount)
    {
        if(self == null) return false;
        self.removeAll(Collections.singleton(null));
        
        if(self.size() < combinationCount) return false;
        
        List<List<Card>> cardCombinations = Combinations.combinations(self, combinationCount);        
        
        Boolean flag = false;
        for(List<Card> cards : cardCombinations) {
        	
        	Collections.sort(cards, new Card.CardComparatorByIndex());
        	int count = cards.size()-1;
        	
        	for(int i = 0; i < count; i++) {
        		Card card = cards.get(i);
        		Card nextCard = cards.get(i+1);
        		// If we have reached the second to last card we have to check whether the last is consecutive
        		// and if so, we have a straight.
        		if(i+1 == count && card.Index.getValue()+1 == nextCard.Index.getValue()) flag = true;
        		// Also, we need to consider the special case of the straight A2345 since when we sort
        		// it becomes 2345A.
        		if(i+1 == count && card.Index == CardIndexType.Five && nextCard.Index == CardIndexType.Ace) flag = true;
        		// If at any point the cards are not consecutive, break, so we never set the flag to true. 
        		if(card.Index.getValue()+1 != nextCard.Index.getValue()) break;
        	}
        	if(flag) return flag;
        }
        return flag;
    }

    private static final String[] Ranks = { "32", "42", "62", "52", "72", "43", "32s", "63", "53", "73", "82", "42s", "83", "62s", "52s", "64", "54", "72s", "74", "43s", "92", "84", "63s", "53s", "65", "93", "73s", "82s", "75", "94", "83s", "64s", "85", "54s", "T2", "74s", "76", "92s", "T3", "95", "84s", "65s", "86", "93s", "T4", "75s", "94s", "T5", "J2", "96", "85s", "T2s", "87", "J3", "76s", "T3s", "95s", "T6", "J4", "86s", "97", "T4s", "J5", "T5s", "Q2", "J2s", "96s", "J6", "T7", "87s", "98", "Q3", "J3s", "T6s", "J4s", "97s", "Q4", "J7", "T8", "J5s", "Q5", "Q2s", "22", "K2", "J6s", "T7s", "98s", "Q3s", "Q6", "K3", "J8", "T9", "Q7", "Q4s", "J7s", "K4", "T8s", "Q5s", "K2s", "J9", "K5", "Q8", "Q6s", "33", "J8s", "T9s", "K3s", "K6", "Q7s", "K4s", "A2", "K7", "JT", "Q9", "J9s", "K5s", "A3", "Q8s", "K8", "K6s", "A4", "44", "QT", "A2s", "JTs", "K7s", "Q9s", "A6", "A5", "K9", "QJ", "A3s", "K8s", "A7", "A4s", "QTs", "KT", "A8", "A6s", "A5s", "K9s", "QJs", "55", "KJ", "A9", "A7s", "KQ", "KTs", "A8s", "KJs", "AT", "A9s", "66", "KQs", "AJ", "AQ", "ATs", "AK", "AJs", "AQs", "77", "AKs", "88", "99", "TT", "JJ", "QQ", "KK", "AA" };

    
    public static Boolean IsRoyalFlush(List<Card> self)
    {
        if(self == null) return false;
        self.removeAll(Collections.singleton(null));
        
        if(self.size() < 5) return false;
        
        List<List<Card>> cardCombinations = Combinations.combinations(self, 5);
        
        for(List<Card> cards : cardCombinations) {
        	Collections.sort(cards, new Card.CardComparatorByIndex());
			if(IsStraightFlush(cards) && cards.get(0).Index == CardIndexType.Ten) return true;
        }
        return false;
    }
    public static Boolean IsStraightFlush(List<Card> self)
    {
        if(self == null) return false;
        self.removeAll(Collections.singleton(null));
        
        if(self.size() < 5) return false;
        
        List<List<Card>> cardCombinations = Combinations.combinations(self, 5);
        
        for(List<Card> cards : cardCombinations) {
        	if(IsStraight(cards) && IsFlush(cards)) return true;
        }
        return false;
    }
    public static Boolean IsFourOfAKind(List<Card> self)
    {
        if(self == null) return false;
        self.removeAll(Collections.singleton(null));
        
        if(self.size() < 4) return false;
        
        List<List<Card>> cardCombinations = Combinations.combinations(self, 4);
        
        for(List<Card> cards : cardCombinations) {
        	if(cards.get(0).Index == cards.get(1).Index &&
        			cards.get(1).Index == cards.get(2).Index &&
        			cards.get(2).Index == cards.get(3).Index) return true;
        }
        return false;
    }
    //+
    public static Boolean IsFullHouse(List<Card> self)
    {
        if(self == null) return false;
        self.removeAll(Collections.singleton(null));
        
        if(self.size() < 5) return false;
        
        List<List<Card>> cardCombinations = Combinations.combinations(self, 5);
        
        for(List<Card> cards : cardCombinations) {
        	Collections.sort(cards, new Card.CardComparatorByIndex());
        	// First line ensures the first two cards and the last two cards form two pairs
        	// and the second line ensures that the middle card forms three of a kind with either of the pairs.
        	if((cards.get(0).Index == cards.get(1).Index && cards.get(3).Index == cards.get(4).Index) &&
        			(cards.get(2).Index == cards.get(1).Index || cards.get(2).Index == cards.get(3).Index)) return true;
        }
        return false;
    }
    //+
    public static Boolean IsFourCardFlush(List<Card> self)
    {
        return _IsFlush(self, 4);
    }
    //+
    public static Boolean IsFlush(List<Card> self)
    {
        return _IsFlush(self, 5);
    }
    //+
    public static Boolean IsFourCardStraight(List<Card> self)
    {
        return _IsStraight(self, 4);
    }
    //+
    public static Boolean IsStraight(List<Card> self)
    {
        return _IsStraight(self, 5);
    }
    //+
    public static Boolean IsThreeOfAKind(List<Card> self)
    {
        if(self == null) return false;
        self.removeAll(Collections.singleton(null));
        
        if(self.size() < 3) return false;
        
        List<List<Card>> cardCombinations = Combinations.combinations(self, 3);
        
        for(List<Card> cards : cardCombinations) {
        	if(cards.get(0).Index == cards.get(1).Index && cards.get(1).Index == cards.get(2).Index) return true;
        }
        return false;
    }
    //+
    public static Boolean IsTwoPair(List<Card> self)
    {
        if(self == null) return false;
        self.removeAll(Collections.singleton(null));
        
        if(self.size() < 4) return false;
        
        List<List<Card>> cardCombinations = Combinations.combinations(self, 4);
        
        for(List<Card> cards : cardCombinations) {
        	Collections.sort(cards, new Card.CardComparatorByIndex());
        	if(cards.get(0).Index == cards.get(1).Index && cards.get(2).Index == cards.get(3).Index) return true;
        }
        return false;
    }
    //+
    public static Boolean IsPair(List<Card> self)
    {
        if(self == null) return false;
        self.removeAll(Collections.singleton(null));
        
        if(self.size() < 2) return false;
        
        List<List<Card>> cardCombinations = Combinations.combinations(self, 2);
        
        for(List<Card> cards : cardCombinations) {
        	if(cards.get(0).Index == cards.get(1).Index) return true;
        }
        return false;
    }
    //+
    public static Boolean IsJacksOrBetter(List<Card> self)
    {
        if(self == null) return false;
        
        for(Card card : self) {
        	if(card != null && IsPictureOrAce(card)) return true;
        }
        return false;
    }
    //+
    public static Boolean IsPictureOrAceOrTen(Card self)
    {
        return self != null && (IsPictureOrAce(self) || self.Index == CardIndexType.Ten);
    }
    //+
    public static Boolean IsPictureOrAce(Card self)
    {
        return self != null && (self.Index.getValue() >= CardIndexType.Jack.getValue() && self.Index.getValue() <= CardIndexType.Ace.getValue());
    }
    public static Boolean IsSuitedConnector(Card self, Card card)
    {
        if (self.Suit == card.Suit)
        {
            if (self.Index.getValue() == card.Index.getValue() + 1 || self.Index.getValue() + 1 == card.Index.getValue())
            {
                return true;
            }
        }
        return false;
    }
    public static Boolean IsSuitedConnector(List<Card> self)
    {
        if (self == null || self.size() != 2)
        {
            return false;
        }
        return IsSuitedConnector(self.get(0), self.get(1));
    }
    public static String ToShorthandString(Card self, Card card)
    {
        if (self == null || card == null)
        {
            return null;
        }

        StringBuilder sb = new StringBuilder(3);
        if (self.Index.getValue() <= card.Index.getValue())
        {
            Card temp = self;
            self = card;
            card = temp;
        }
        sb.append(Character.toUpperCase(self.toString().charAt(0)));
        sb.append(Character.toUpperCase(card.toString().charAt(0)));
        if (self.Suit == card.Suit)
        {
            sb.append('s');
        }
        return sb.toString();
    }
    public static String ToShorthandString(List<Card> self)
    {
        return self.size() != 2 ? null : ToShorthandString(self.get(0), self.get(1));
    }
    public static Boolean IsHiddenPair(List<Card> self, List<Card> boardCards)
    {
        if(boardCards.size() != 3 && self.size() != 2)
        {
            return false;
        }

    	Collections.sort(boardCards, new Card.CardComparatorByIndex());
        for(int i = 0; i < self.size(); i++)
        {
        	Card card = self.get(i);
        	for(Card boardCard : boardCards) {
                if(boardCard.Index == card.Index) {
                    return true;
                }
        	}
        }
        return false;
    }
    public static int HoleRank(Card self, Card card)
    {
        String shorthand = ToShorthandString(self, card);
        if(shorthand == null || shorthand.isEmpty())
        {
            return -1;
        }
        return Arrays.asList(Ranks).indexOf(shorthand);
    }
    public static int HoleRank(List<Card> self)
    {
        return self.size() != 2 ? 0 : HoleRank(self.get(0), self.get(1));
    }

    public static int HandRank(List<Card> self)
    {
        if(self == null) return 0;
        self.removeAll(Collections.singleton(null));
        
        if(self.size() < 5) return 0;
        
        InitAnalysis();
        for(int i = self.size(); i < 7; i++)
        {
            self.add(null);
        }
        return Analysis.AssessHand(
            OpCard(self.get(0)),
            OpCard(self.get(1)), 
            OpCard(self.get(2)), 
            OpCard(self.get(3)), 
            OpCard(self.get(4)), 
            OpCard(self.get(5)), 
            OpCard(self.get(6)));
    }
    public static int HandRank(List<Card> player, List<Card> board)
    {
    	List<Card> newList = new ArrayList<Card>();
    	newList.addAll(player);
    	newList.addAll(board);
    	return HandRank(newList);
    }
    public static String HandRankDesc(int rank)
    {
        InitAnalysis();
        return rank >= 1 && rank <= 7462 
            ? Analysis.HandRankInfo[rank].Description 
            : "Invalid hand rank";
    }
    
    public static void main(String[] args) {
    	Card c7 = new Card(CardIndexType.Six, CardSuitType.Hearts);
    	Card c4 = new Card(CardIndexType.Ten, CardSuitType.Spades);
    	Card c3 = new Card(CardIndexType.Jack, CardSuitType.Spades);
    	Card c1 = new Card(CardIndexType.Queen, CardSuitType.Spades);
    	Card c5 = new Card(CardIndexType.King, CardSuitType.Spades);
    	Card c6 = new Card(CardIndexType.Ace, CardSuitType.Clubs);
    	Card c2 = new Card(CardIndexType.Ace, CardSuitType.Spades);
    	ArrayList<Card> cards = new ArrayList<Card>();
    	ArrayList<Card> hand = new ArrayList<Card>();
    	ArrayList<Card> board = new ArrayList<Card>();
    	cards.add(c1);
    	cards.add(c2);
    	cards.add(c3);
    	cards.add(c4);
    	cards.add(c5);
    	cards.add(c6);
    	cards.add(c7);
    	hand.add(c7);
    	hand.add(c4);
    	board.add(c2);
    	board.add(c1);
    	board.add(c5);
    	
    	System.out.println(IsStraightFlush(cards));
    	System.out.println(IsRoyalFlush(cards));
    	System.out.println(IsFourOfAKind(cards));
    	System.out.println(IsFullHouse(cards));
    	System.out.println(IsThreeOfAKind(cards));
    	System.out.println(IsTwoPair(cards));
    	System.out.println(IsPair(cards));
    	System.out.println(IsJacksOrBetter(cards));
    	System.out.println(IsPictureOrAceOrTen(c7));
    	System.out.println(IsPictureOrAce(c7));
    	System.out.println(IsSuitedConnector(c2, c1));
    	System.out.println(IsSuitedConnector(hand));
    	System.out.println(ToShorthandString(c6, c2));
    	System.out.println(ToShorthandString(hand));
    	System.out.println(IsHiddenPair(hand, board));
    	System.out.println(HoleRank(c6, c2));
    	System.out.println(HoleRank(hand));
    	System.out.println(Ranks[49]);
    	System.out.println(HandRank(cards));
    	
    }
}