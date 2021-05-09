package logic;

public class Card {
	CardValue value;
	CardSuit suit;

	public Card(CardValue value, CardSuit suit){
		this.suit = suit;
		this.value = value;
	}

	public Card(Card card){
		this.suit = card.suit;
		this.value = card.value;
	}

	public static Card newInstance(Card card){
		return newInstance(card);
	}

	public CardValue getValue() {
		return value;
	}

	public CardSuit getSuit() {
		return suit;
	}
}
