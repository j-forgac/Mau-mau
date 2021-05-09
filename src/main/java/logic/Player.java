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
		System.out.println(type);
		if (type) {
			Card chosenCard = aiTurn();
			if(chosenCard.getSuit() == null){
				System.out.println("Líže si");
			} else {
				presentation.aiDraw(id,chosenCard);
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
				if(stocking == 0){
					yield chooseCard(CardValue.SEVEN, lastCard.getSuit());
				} else {
					yield chooseCard(CardValue.SEVEN, null);
				}
			}
			case EIGHT -> {
				if(skipping){
					yield chooseCard(CardValue.EIGHT, null);
				} else {
					yield chooseCard(CardValue.EIGHT, lastCard.getSuit());
				}
			}
			default -> chooseCard(lastCard.getValue(), lastCard.getSuit());
		};
	}

	public Card chooseCard(CardValue value, CardSuit suit){
		System.out.println("value: " + value +": suit: " + suit);
		for(int x= 0;x< cards.size();x++){
			System.out.println("1value: " + cards.get(x).getValue() +": 1suit: " + cards.get(x).getSuit());
			if(cards.get(x).getValue() == value || cards.get(x).getSuit() == suit){
				playCard = new Card(cards.get(x));
				if(type){
					cards.remove(x);
				}
				return playCard;
			}
		}
		playCard = new Card(null,null);
		return playCard;
	}

	public CardSuit chooseSuit(){
		if(type){
			return aiSuit();
		} else {
			return humanSuit();
		}
	}

	public CardSuit aiSuit(){
		int[] cardsPerSuit = new int[4];
		for(CardSuit suit: CardSuit.values()){
			cardsPerSuit[suit.ordinal()]++;
		}
		int order = 0;
		int val = 0;
		for(int x = 0; x<cardsPerSuit.length;x++){
			if(cardsPerSuit[x] > val){
				order = x;
				val = cardsPerSuit[x];
			}
		}
		System.out.println(order);
		return CardSuit.values()[order];
	}

	public CardSuit humanSuit(){
		return CardSuit.values()[presentation.getSuit()];
	}

	public void drawCard(Card card){
		cards.add(card);
	}

	public Card humanTurn() {
		presentation.drawPlayerView(cards);
		if(aiTurn() == null){
			int pos = presentation.getCardFromUser(cards);
			playCard = new Card(cards.get(pos));
			cards.remove(pos);
		} else {
			System.out.println("Lížeš si");
			playCard = new Card(null,null);
		}
		return playCard;
	}
}
