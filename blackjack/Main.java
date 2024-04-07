import java.util.*;

// Card class
class Card {
    private final String suit;
    private final String value;

    public Card(String suit, String value) {
        this.suit = suit;
        this.value = value;
    }

    public String getCardInfo() {
        return value + " of " + suit;
    }
}

// Deck class
class Deck {
    private ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};

        for (String suit : suits) {
            for (String value : values) {
                cards.add(new Card(suit, value));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card dealCard() {
        if (cards.isEmpty()) {
            System.out.println("Deck is empty. Reshuffling...");
            shuffle();
        }
        return cards.remove(0);
    }
}

// Player class
class Player {
    protected ArrayList<Card> hand;

    public Player() {
        hand = new ArrayList<>();
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }

    public int calculateHandValue() {
        int value = 0;
        int numAces = 0;

        for (Card card : hand) {
            String cardValue = card.getCardInfo().split(" ")[0];
            if (cardValue.equals("Ace")) {
                numAces++;
                value += 11; // Ace initially counts as 11
            } else if (cardValue.equals("King") || cardValue.equals("Queen") || cardValue.equals("Jack")) {
                value += 10;
            } else {
                value += Integer.parseInt(cardValue);
            }
        }

        // Adjust value for aces if necessary
        while (value > 21 && numAces > 0) {
            value -= 10; // Change ace value from 11 to 1
            numAces--;
        }

        return value;
    }

    public void displayHand(boolean showAllCards) {
        System.out.println("Player's Hand:");
        for (int i = 0; i < hand.size(); i++) {
            if (i == 0 && !showAllCards) {
                System.out.println("Card Hidden");
            } else {
                System.out.println(hand.get(i).getCardInfo());
            }
        }
        System.out.println("Hand Value: " + calculateHandValue());
    }
}

// Dealer class
class Dealer extends Player {
    public void displayFirstCard() {
        System.out.println("Dealer's Hand:");
        System.out.println(hand.get(0).getCardInfo());
        System.out.println("Second card hidden");
    }

    public boolean shouldHit() {
        return calculateHandValue() < 17;
    }
}

// Game class
class Game {
    private Deck deck;
    private Player player;
    private Dealer dealer;
    private Scanner scanner;

    public Game() {
        deck = new Deck();
        player = new Player();
        dealer = new Dealer();
        scanner = new Scanner(System.in);
    }

    public void start() {
        deck.shuffle();
        dealInitialCards();
        playPlayerTurn();
        playDealerTurn();
        displayResults();
    }

    private void dealInitialCards() {
        player.addCardToHand(deck.dealCard());
        dealer.addCardToHand(deck.dealCard());
        player.addCardToHand(deck.dealCard());
        dealer.addCardToHand(deck.dealCard());

        player.displayHand(true);
        dealer.displayFirstCard();
    }

    private void playPlayerTurn() {
        while (true) {
            System.out.println("Hit or Stand? (h/s): ");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("h")) {
                player.addCardToHand(deck.dealCard());
                player.displayHand(true);
                if (player.calculateHandValue() > 21) {
                    System.out.println("Player busts. Dealer wins!");
                    break;
                }
            } else if (choice.equals("s")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'h' to hit or 's' to stand.");
            }
        }
    }

    private void playDealerTurn() {
        while (dealer.shouldHit()) {
            dealer.addCardToHand(deck.dealCard());
        }
        dealer.displayHand(true);
    }

    private void displayResults() {
        int playerScore = player.calculateHandValue();
        int dealerScore = dealer.calculateHandValue();

        System.out.println("Player's hand value: " + playerScore);
        System.out.println("Dealer's hand value: " + dealerScore);

        if (playerScore > 21 || (dealerScore <= 21 && dealerScore > playerScore)) {
            System.out.println("Dealer wins!");
        } else if (dealerScore > 21 || playerScore > dealerScore) {
            System.out.println("Player wins!");
        } else {
            System.out.println("It's a tie!");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
