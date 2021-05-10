package logic;

import presentation.Presentation;

import java.util.*;

public class Game {

	//-------------------Players-&-Game-Cycle-Data---
	HashMap<Integer,Player> players = new HashMap<>();
	Player actualPlayer;
	Player winner = null;
	Card playedCard;
	int drawnCardsCount;

	CardSuit suit;

	ArrayList<Card> cards = new ArrayList<>();

	int stockSevens;
	boolean skippingActive = false;

	//-------------------Decks----------------------
	ArrayList<Card> drawingDeck = new ArrayList<>();
	ArrayList<Card> playedCardsDeck = new ArrayList<>();

	//-------------------Presentation----------------
	Presentation presentation = new Presentation();
	ArrayList<Boolean> userInput;


	public Game(){
		userInput = presentation.getPlayers();
		System.out.println("TEST");

		createDeck();
		Collections.shuffle(drawingDeck);
		for(Card card: drawingDeck){
			presentation.drawCard(card);
		}

		int y = 0;
		for (int i =0;i< userInput.size();i++){
			for(int x = 0; x < 4;x++){
				System.out.println(x + " : " + y);
				presentation.drawCard(drawingDeck.get(x+y));
				cards.add(new Card(drawingDeck.get(x+y)));
				drawingDeck.remove(x+y);
			}
			y += 4;
			players.put(i,new Player(userInput.get(i), (ArrayList<Card>)cards.clone()));
			cards.clear();
		}

		initGame();
	}

	public void initGame(){
		int playerOnMove = 0;

		playedCardsDeck.add(new Card(drawingDeck.get(drawingDeck.size()-1)));
		drawingDeck.remove(drawingDeck.size()-1);
		suit = playedCardsDeck.get(playedCardsDeck.size()-1).getSuit();
		System.out.println("barva je: "+suit);


		while (winner == null){
			actualPlayer = players.get(playerOnMove);
			System.out.println("hraje: "+playerOnMove);
			System.out.println(actualPlayer.cards.size());
			System.out.println("barva je: "+suit);
			presentation.drawCard(new Card(playedCardsDeck.get(playedCardsDeck.size() -1).getValue(),suit));
			playedCard = new Card(actualPlayer.play(playerOnMove,new Card(playedCardsDeck.get(playedCardsDeck.size() -1).getValue(),suit), skippingActive, stockSevens));
			if(playedCard.getValue() == null){
				System.out.print("Nemas kartu");
				if ((playedCardsDeck.get(playedCardsDeck.size() - 1).getValue() == CardValue.SEVEN)) {
					drawnCardsCount=stockSevens;
					stockSevens = 0;
				} else {
					drawnCardsCount=1;
				}

				if(skippingActive){
					skippingActive = false;
				}

				for(int x =0;x<drawnCardsCount;x++){
					setDecks();
					System.out.print("LIZNUL SI:");
					presentation.drawCard(drawingDeck.get(drawingDeck.size()-1));
					actualPlayer.drawCard(new Card(drawingDeck.get(drawingDeck.size()-1)));
				}
			} else {
				switch (playedCard.getValue()) {
					case SEVEN -> stockSevens += 2;
					//case ACE -> playerOnMove--;
					case EIGHT -> skippingActive = true;
				}

				playedCardsDeck.add(new Card(playedCard));

				if(playedCard.getValue() == CardValue.JACK){
					suit = actualPlayer.chooseSuit();
				} else {
					suit = playedCardsDeck.get(playedCardsDeck.size()-1).getSuit();
				}
				if(actualPlayer.didWin() && playedCard.getValue() != CardValue.ACE){
					winner = actualPlayer;
				}
			}



			playerOnMove = (playerOnMove+1)%players.size();
		}
		System.out.println("PLAYER " + playerOnMove);
	}

	public void createDeck(){
		for(CardSuit suit: CardSuit.values()){
			for(CardValue value: CardValue.values()){
				drawingDeck.add(new Card(value, suit));
			}
		}
	}

	public void setDecks(){
		if(drawingDeck.isEmpty()){
			for(int x = playedCardsDeck.size() - 2; x > 0; x--){
				drawingDeck.add(new Card(playedCardsDeck.get(x)));
				playedCardsDeck.remove(x);
			}
		}
		Collections.shuffle(drawingDeck);
	}
}
