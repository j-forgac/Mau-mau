package logic;

import presentation.Presentation;

import java.util.ArrayList;

public class Player {
	boolean type;
	ArrayList<Card> cards;
	Card lastCard;
	Card playCard;


	int stocking;
	boolean skipping;

	Presentation presentation = new Presentation();


	public Player(boolean type, ArrayList<Card> cards) {
		this.type = type;
		this.cards = cards;
	}

	public Card play(int id, Card lastCard, boolean skipping, int stocking) {
		this.lastCard = lastCard;
		this.skipping = skipping;
		this.stocking = stocking;
		if (type) {
			Card chosenCard = aiTurn();
			if (chosenCard.getSuit() != null) {
				presentation.aiDraw(id, chosenCard);
			}
			return chosenCard;
		} else {
			return humanTurn();
		}
	}

	public boolean didWin() {
		return cards.size() == 0;
	}

	public Card aiTurn() {
		return switch (lastCard.getValue()) {
			case SEVEN -> {
				if (stocking == 0) {
					yield chooseCardAi(CardValue.SEVEN, lastCard.getSuit());
				} else {
					yield chooseCardAi(CardValue.SEVEN, null);
				}
			}
			case EIGHT -> {
				if (skipping) {
					yield chooseCardAi(CardValue.EIGHT, null);
				} else {
					yield chooseCardAi(CardValue.EIGHT, lastCard.getSuit());
				}
			}
			default -> chooseCardAi(lastCard.getValue(), lastCard.getSuit());
		};
	}

	public boolean valid(Card checkedCard){
		boolean suitSame = checkedCard.getSuit() == lastCard.getSuit();
		boolean valueSame = checkedCard.getValue() == lastCard.getValue();
		if(checkedCard.getValue() == CardValue.JACK){
			return true;
		}
		return switch (lastCard.getValue()) {
			case SEVEN -> {
				if (stocking == 0) {
					yield suitSame || valueSame;
				} else {
					yield valueSame;
				}
			}
			case EIGHT -> {
				if (skipping) {
					yield valueSame;
				} else {
					yield suitSame || valueSame;
				}
			}
			default -> suitSame || valueSame;
		};
	}

	public Card chooseCardAi(CardValue value, CardSuit suit) {
		for (int x = 0; x < cards.size(); x++) {
			if (cards.get(x).getValue() == value || cards.get(x).getSuit() == suit || cards.get(x).getValue() == CardValue.JACK) {
				playCard = new Card(cards.get(x));
				if (type) {
					cards.remove(x);
				}
				return playCard;
			}
		}
		playCard = new Card(null, null);
		return playCard;
	}

	public CardSuit chooseSuit() {
		if (type) {
			return aiSuit();
		} else {
			return humanSuit();
		}
	}

	public CardSuit aiSuit() {
		int[] cardsPerSuit = new int[4];
		for (CardSuit suit : CardSuit.values()) {
			cardsPerSuit[suit.ordinal()]++;
		}
		int order = 0;
		int val = 0;
		for (int x = 0; x < cardsPerSuit.length; x++) {
			if (cardsPerSuit[x] > val) {
				order = x;
				val = cardsPerSuit[x];
			}
		}
		return CardSuit.values()[order];
	}

	public CardSuit humanSuit() {
		return CardSuit.values()[presentation.getSuit()];
	}

	public void drawCard(Card card) {
		cards.add(card);
	}

	public Card humanTurn() {
		presentation.drawPlayerView(cards);
		boolean validPos = false;
		if (aiTurn().getSuit() == null) {
			playCard = new Card(null, null);
		} else {
			int pos = -1;
			while (!validPos){
				pos = presentation.getCardFromUser(cards);
				playCard = new Card(cards.get(pos));
				if(valid(playCard)){
					validPos = true;
				}
			}
			cards.remove(pos);
		}
		return playCard;
	}
}
