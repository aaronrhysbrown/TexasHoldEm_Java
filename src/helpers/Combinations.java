package helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import helpers.Card.CardComparatorByIndex;

public class Combinations {
	
    private static void combinationsRec(List<Card> arr, int len, int startPosition, Card[] accum, List<List<Card>> result) {
        if (len == 0){
            result.add(new ArrayList<Card>(Arrays.asList(accum)));
            return;
        }       
        for (int i = startPosition; i <= arr.size()-len; i++){
            accum[accum.length - len] = arr.get(i);
            combinationsRec(arr, len-1, i+1, accum, result);
        }
    }
    
    public static List<List<Card>> combinations(List<Card> arr, int len) {
    	Card[] accum = new Card[len];
    	List<List<Card>> result = new ArrayList<List<Card>>();
    	combinationsRec(arr, len, 0, accum, result);
    	return result;
    }

	public static void main(String[] args) {
		
    	Card c4 = new Card(CardIndexType.Nine, CardSuitType.Spades);
    	Card c3 = new Card(CardIndexType.Nine, CardSuitType.Hearts);
    	Card c1 = new Card(CardIndexType.Jack, CardSuitType.Spades);
    	Card c5 = new Card(CardIndexType.Queen, CardSuitType.Spades);
    	Card c6 = new Card(CardIndexType.King, CardSuitType.Spades);
    	Card c2 = new Card(CardIndexType.Ace, CardSuitType.Spades);   
    	ArrayList<Card> cards = new ArrayList<Card>();
    	cards.add(c1);
    	cards.add(c2);
    	cards.add(c3);
    	cards.add(c4);
    	cards.add(c5);
    	cards.add(c6);
    	
        List<List<Card>> combinations = combinations(cards, 3);
        
    	System.out.println(combinations);
        System.out.println(combinations.size());	
        
        for(List<Card> combination : combinations) {
        	Collections.sort(combination, new Card.CardComparatorByIndex());
        	System.out.println(combination);
            System.out.println(combination.size());	
            System.out.println(combination.get(0).Suit == combination.get(combination.size()-1).Suit);
        }
	}
}