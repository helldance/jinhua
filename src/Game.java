import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author yangw
 * @version $Id: Main.java, v 0.1 19 Feb, 2015 1:41:23 pm yangw Exp $
 */
public class Game {
    enum CardType {
        SPADE, HEART, DIAMOND, CLUB
    }

    static CardType[]   cardType          = new CardType[4];

    static List<Player> players           = new ArrayList<Player>();
    static int[]        cardsWeight       = new int[10];
    static List<Card>   cards             = new ArrayList<Card>();
    static int          maxCardsPerPerson = 5;

    static {
        // players jon
        players.add(new Player("Jack"));
        players.add(new Player("Bill"));
        players.add(new Player("May"));
        players.add(new Player("Raymond"));

        // card type map
        cardType[0] = CardType.CLUB;
        cardType[1] = CardType.SPADE;
        cardType[2] = CardType.HEART;
        cardType[3] = CardType.DIAMOND;

        for (int i = 0; i < 10; i++) {
            cardsWeight[i] = i + 1;
        }

        for (int k = 1; k <= 10; k++) {
            cards.add(new Card(CardType.HEART, k));
        }

        for (int k = 1; k <= 10; k++) {
            cards.add(new Card(CardType.DIAMOND, k));
        }

        for (int k = 1; k <= 10; k++) {
            cards.add(new Card(CardType.SPADE, k));
        }

        for (int k = 1; k <= 10; k++) {
            cards.add(new Card(CardType.CLUB, k));
        }
    }

    public static void main(String[] args) {
        System.out.println(cards.size());

        Iterator<?> iterator = cards.iterator();

        // distribute cards
        for (Player p : players) {
            while (p.cards.size() < maxCardsPerPerson) {
                p.draw();
            }
        }

        System.out.println("Cards distribute complete..");

        for (Player p : players) {
            System.out.print(p.printCards());
            p.calcMaxRemain();
        }

        // compare cards

    }

    public void reset() {
        for (Player p : players) {
            p.cards.clear();
            p.points = 0;
        }
    }

    static class Player {
        List<Card> cards;
        String     nickName;
        int        points;

        public Player(String name) {
            this.nickName = name;
            this.points = 0;
            cards = new ArrayList<Card>();
        }

        public void draw() {
            System.out.println(this.nickName + " draw a card");

            cards.add(Card.randomACard());
        }

        public int calcMaxRemain() {
            // brute force way 
            int totalWeight = 0;
            int curSum = 0;

            for (int i = 0; i < cards.size(); i++) {
                for (int j = i + 1; j <= i + 3 && j < cards.size(); j++) {
                    for (int k = j + 1; k <= j + 3 && k < cards.size(); k++) {
                        totalWeight += cards.get(k).value + cards.get(j).value + cards.get(i).value;

                        if (totalWeight % 10 == 0) {
                            // find a combination & calculate the rest sum
                            int sum = 0;

                            for (int m = 0; m < cards.size(); m++) {
                                if (m != i && m != j && m != k) {
                                    sum += cards.get(m).value;
                                }
                            }

                            sum = sum > 10 ? sum % 10 : sum;

                            if (sum > curSum) {
                                curSum = sum;
                            }

                            // print best cards permutation
                            System.out.println(i + ", " + j + ", " + k + " : " + cards.get(i).value + ", "
                                               + cards.get(j).value + ", " + cards.get(k).value + " = " + curSum);
                        }
                        else {
                            totalWeight = 0;
                        }
                    }
                }
            }

            return curSum;
        }

        public String printCards() {
            StringBuilder builder = new StringBuilder();

            for (Card c : cards) {
                builder.append(c.toString()).append("\n");
            }

            return builder.toString();
        }
    }

    static class Card {
        CardType      type;
        int           value;
        static Random r = new Random();

        public Card(CardType type, int k) {
            this.type = type;
            this.value = k;
        }

        public static Card randomACard() {
            int cardSeq = r.nextInt(4);
            int weightSeq = r.nextInt(10);

            return new Card(cardType[cardSeq], cardsWeight[weightSeq]);
        }

        public String toString() {
            return type + " " + value;
        }
    }
}
