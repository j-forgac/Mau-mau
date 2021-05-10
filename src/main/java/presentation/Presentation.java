package presentation;

import logic.Card;
import logic.CardSuit;

import java.util.ArrayList;
import java.util.Scanner;

public class Presentation {

	public void drawPlayerView(ArrayList<Card> cards) {
		for (int x = 1; x <= cards.size(); x++) {
			System.out.print(" " + x + ". |");
		}
		System.out.println("");
		for (int x = 0; x < cards.size(); x++) {
			System.out.print("─────");
		}
		System.out.println("");
		for (Card card : cards) {
			drawCard(card);
			System.out.print(" |");
		}
		System.out.println("");
	}

	public void aiDraw(int id, Card card) {
		System.out.println("Player " + id + " played: ");
		drawCard(card);
		System.out.println("");
	}

	public void drawCard(Card card) {
		switch (card.getValue()) {
			case SEVEN -> System.out.print(" 7");
			case EIGHT -> System.out.print(" 8");
			case NINE -> System.out.print(" 9");
			case TEN -> System.out.print("10");
			default -> System.out.print(" " + card.getValue().toString().charAt(0));
		}
		drawSuit(card.getSuit());
	}

	public void drawSuit(CardSuit suit){
		switch (suit) {
			case HEARTS -> System.out.print(Colors.red + "♥" + Colors.res);
			case DIAMONDS -> System.out.print(Colors.red + "♦" + Colors.res);
			case SPADES -> System.out.print(Colors.black + "♠" + Colors.res);
			case CLUBS -> System.out.print(Colors.black + "♣" + Colors.res);
		}
	}

	public ArrayList<Boolean> getPlayers() {
		int playerCount = 0;
		Scanner sc = new Scanner(System.in);
		ArrayList<Boolean> players = new ArrayList<>();
		while (playerCount < 2 || playerCount > 4) {
			System.out.println("How many players? (2-4)");
			try {
				playerCount = Integer.parseInt(sc.nextLine());
			} catch (Exception e) {
				playerCount = 0;
			}

		}
		for (int x = 0; x < playerCount; x++) {
			String type;
			System.out.println("Player " + (x + 1) + ": AI or human? (AI = a, Human = h)");
			type = sc.nextLine();
			if (type.equals("a")) {
				players.add(true);
			} else if (type.equals("h")) {
				players.add(false);
			} else {
				x--;
			}
		}
		return players;
	}

	public Integer getCardFromUser(ArrayList<Card> cards){
		int pos = -1;
		Scanner sc = new Scanner(System.in);
		while (pos > cards.size() || pos < 1) {
			System.out.println("Play card");
			System.out.println("Card position:");
			try {
				pos = Integer.parseInt(sc.nextLine());
			} catch (Exception e) {
				pos = -1;
			}

		}
		return --pos;
	}

	public int getSuit(){
		int x = 0;
		System.out.println("Choose suit which will be next card played on: ");
		for(CardSuit suit: CardSuit.values()){
			x++;
			System.out.print(x+": ");
			drawSuit(suit);
			System.out.println("");
		}
		int suit = 0;
		Scanner sc = new Scanner(System.in);
		while (suit < 1 || suit > 4) {
			System.out.println("Enter number of suit: ");
			try {
				suit = Integer.parseInt(sc.nextLine());
			} catch (Exception e) {
				suit = 0;
			}
		}
		return --suit;
	}
}
