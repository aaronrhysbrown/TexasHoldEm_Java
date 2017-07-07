package helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Card {
	
    public CardIndexType Index;
    public String Path;
    public CardSuitType Suit;
	
    public Card(CardIndexType index, CardSuitType suit) {
    	this.Index = index;
    	this.Suit = suit;
    }
    
    public Card(String card) {
    	char index = card.charAt(0);
    	char suit = card.charAt(1);
    	
    	switch(index) {
            case '2': this.Index = CardIndexType.Two; break;
            case '3': this.Index = CardIndexType.Three; break;
            case '4': this.Index = CardIndexType.Four; break;
            case '5': this.Index = CardIndexType.Five; break;
            case '6': this.Index = CardIndexType.Six; break;
            case '7': this.Index = CardIndexType.Seven; break;
            case '8': this.Index = CardIndexType.Eight; break;
            case '9': this.Index = CardIndexType.Nine; break;
            case 'T': this.Index = CardIndexType.Ten; break;
            case 'J': this.Index = CardIndexType.Jack; break;
            case 'Q': this.Index = CardIndexType.Queen; break;
            case 'K': this.Index = CardIndexType.King; break;
            case 'A': this.Index = CardIndexType.Ace; break;
            default: this.Index = CardIndexType.Shirt; break;
        }
    	
    	switch(suit) {
	        case 'C': this.Suit = CardSuitType.Clubs; break;
	        case 'D': this.Suit = CardSuitType.Diamonds; break;
	        case 'H': this.Suit = CardSuitType.Hearts; break;
	        case 'S': this.Suit = CardSuitType.Spades; break;
	        default: this.Suit = CardSuitType.Shirt; break;
    	}
    }
    
    public static List<Card> toCards(List<String> cardstrings) {
    	List<Card> cards = new ArrayList<Card>();
    	for(String cardstring : cardstrings) {
    		cards.add(new Card(cardstring));
    	}
    	return cards;
    }

    public String toString() {
    	switch(Index) {
    		case Ten: case Jack: case Queen: case King: case Ace:
    			return "" + Index.toString().charAt(0) + Suit.toString().charAt(0);
    		default:
    			return "" + String.valueOf(Index.getValue()).charAt(0) + Suit.toString().charAt(0);
    	}
    }
    
    public static class CardComparatorByIndex implements Comparator<Card> {
        @Override
        public int compare(Card c1, Card c2) {
//        	if(c1 == null && c2 == null) return 0;
//        	if(c1 == null) return -1;
//        	if(c2 == null) return 1;
            return c1.Index.compareTo(c2.Index);
        }
    }

    public static class CardComparatorBySuit implements Comparator<Card> {
        @Override
        public int compare(Card c1, Card c2) {
//        	if(c1 == null && c2 == null) return 0;
//        	if(c1 == null) return -1;
//        	if(c2 == null) return 1;
            return c1.Suit.compareTo(c2.Suit);
        }
    }
}