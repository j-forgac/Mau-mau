package logic;

import presentation.Presentation;

import java.util.*;

public class Game {

	//-------------------Players-&-Game-Cycle-Data---
	HashMap<Integer, Player> players = new HashMap<>();
	Player actualPlayer;
	Player winner = null;
	Card playedCard;
	int drawnCardsCount;

	CardSuit suit;

	ArrayList<Card> cards = new ArrayList<>();

	int stackSevens;
	boolean skippingActive = false;

	//-------------------Decks----------------------
	ArrayList<Card> drawingDeck = new ArrayList<>();
	ArrayList<Card> playedCardsDeck = new ArrayList<>();

	//-------------------Presentation----------------
	Presentation presentation = new Presentation();
	ArrayList<Boolean> userInput;


	public Game() {
		userInput = presentation.getPlayers();
		createDeck();
		Collections.shuffle(drawingDeck);

		int y = 0;
		for (int i = 0; i < userInput.size(); i++) {
			for (int x = 0; x < 4; x++) {
				cards.add(new Card(drawingDeck.get(x + y)));
				drawingDeck.remove(x + y);
			}
			y += 4;
			players.put(i, new Player(userInput.get(i), (ArrayList<Card>) cards.clone()));
			cards.clear();
		}

		initGame();
	}

	public void initGame() {
		int playerOnMove = 0;

		playedCardsDeck.add(new Card(drawingDeck.get(drawingDeck.size() - 1)));
		drawingDeck.remove(drawingDeck.size() - 1);
		suit = playedCardsDeck.get(playedCardsDeck.size() - 1).getSuit();

		while (winner == null) {
			actualPlayer = players.get(playerOnMove);
			presentation.print1(playerOnMove, new Card(playedCardsDeck.get(playedCardsDeck.size() - 1).getValue(), suit));
			playedCard = new Card(actualPlayer.play(playerOnMove, new Card(playedCardsDeck.get(playedCardsDeck.size() - 1).getValue(), suit), skippingActive, stackSevens));
			if (playedCard.getValue() == null) {
				if ((playedCardsDeck.get(playedCardsDeck.size() - 1).getValue() == CardValue.SEVEN)) {
					drawnCardsCount = stackSevens;
					stackSevens = 0;
				} else {
					drawnCardsCount = 1;
				}

				if (skippingActive) {
					skippingActive = false;
				}

				for (int x = 0; x < drawnCardsCount; x++) {
					setDecks();
					presentation.print2(drawingDeck.get(drawingDeck.size() - 1));
					actualPlayer.drawCard(new Card(drawingDeck.get(drawingDeck.size() - 1)));
				}
			} else {
				switch (playedCard.getValue()) {
					case SEVEN -> stackSevens += 2;
					case ACE -> playerOnMove--;
					case EIGHT -> skippingActive = true;
				}

				playedCardsDeck.add(new Card(playedCard));

				if (playedCard.getValue() == CardValue.JACK) {
					suit = actualPlayer.chooseSuit();
					stackSevens = 0;
					skippingActive = false;
				} else {
					suit = playedCardsDeck.get(playedCardsDeck.size() - 1).getSuit();
				}
				if (actualPlayer.didWin() && playedCard.getValue() != CardValue.ACE) {
					winner = actualPlayer;
				}
			}


			playerOnMove = (playerOnMove + 1) % players.size();
		}

		presentation.print3(playerOnMove);
	}

	public void createDeck() {
		for (CardSuit suit : CardSuit.values()) {
			for (CardValue value : CardValue.values()) {
				drawingDeck.add(new Card(value, suit));
			}
		}
	}

	public void setDecks() {
		if (drawingDeck.isEmpty()) {
			for (int x = playedCardsDeck.size() - 2; x > 0; x--) {
				drawingDeck.add(new Card(playedCardsDeck.get(x)));
				playedCardsDeck.remove(x);
			}
		}
		Collections.shuffle(drawingDeck);
	}
}
