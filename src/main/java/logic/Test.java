package logic;

import presentation.Presentation;

import java.util.ArrayList;

public class Test {
	public static void main(String[] args){
		Presentation io = new Presentation();
		ArrayList<Card> drawingDeck = new ArrayList<>();
		for(CardSuit suit: CardSuit.values()){
			for(CardValue value: CardValue.values()){
				drawingDeck.add(new Card(value, suit));
			}
		}

		Player a1 = new Player(false,drawingDeck);
		io.drawCard(drawingDeck.get(0));
		Card x1 = a1.play(1,drawingDeck.get(0),false,0);
		System.out.println(x1.suit + " xD " + x1.value);
	}
}
