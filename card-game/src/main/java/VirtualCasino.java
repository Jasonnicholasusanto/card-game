import com.github.javafaker.Faker;

import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class VirtualCasino {
    private static final String FILENAME = "res/Cards.csv";
    private static final String USER_FILENAME = "res/Users.csv";
    private static final int INITIAL = 100;
    private static final int BLACKJACK_CARDS = 416;
    private static final int BJ_DUPLICATES = 8;

    private final Scanner scanner = new Scanner(System.in);
    private final Faker faker = new Faker();
    private final HashMap<String, Integer> cardDict = new HashMap<>();
    private final String[] deck = new String[416];
    private Player mainPlayer;
    private final HashMap<String, Player> users = new HashMap<>();
    private final ProgressBar progressBarObj = new ProgressBar();
    private int tableNum;
    private AI[] comp;
    private BlackJack blackJack;

    public static void main(String[] args) throws InterruptedException {
        VirtualCasino game = new VirtualCasino();
        game.gamble();
    }

    public void gamble() throws InterruptedException {
        boolean validity=false;

        System.out.println("OPERATOR: Welcome to The Virtual Casino!");
        this.loadUsers(USER_FILENAME);

        while(!validity) {
            System.out.println("OPERATOR: [sign up] [log in]");
            System.out.print("PLAYER: ");
            String choice = scanner.nextLine();

            if (choice.equals("<")) {
                this.signup();
                validity=true;
            } else if (choice.equals(">")) {
                this.login();
                validity=true;
            } else {
                System.out.println("OPERATOR: Please try again.");
            }
        }

        validity=false;
        while(!validity){
            System.out.println("OPERATOR: Hey "+this.mainPlayer.getName()+"! Please choose a game to play below.");
            System.out.println("    1. Black Jack\n    2. Poker\n    3. 100\n" +
                    "    4. Roulette");
            System.out.print("PLAYER: ");
            String choice = scanner.nextLine();

            if(choice.equals("1")){
                blackJack = new BlackJack();
                this.readCards(FILENAME, BLACKJACK_CARDS, BJ_DUPLICATES);
                this.shuffleCards();
                this.findTable();
                this.assignTable();
                blackJack.run();
                validity=true;
            } else if(choice.equals("2")){
                System.out.println("OPERATOR: Sorry but the Poker table is unavailable at the moment.");
            } else if(choice.equals("3")){
                System.out.println("OPERATOR: Sorry but the 100 table is unavailable at the moment.");
            } else if(choice.equals("4")){
                System.out.println("OPERATOR: Sorry but the roulette table is unavailable at the moment.");
            } else {
                System.out.println("OPERATOR: I am sorry but I did not catch that.");
            }
        }

        this.playerGenerator();


    }

    public void assignTable(){
        tableNum = (int)(Math.random() * (100-1))+1;
        System.out.println("OPERATOR: Table found! You are allocated to TABLE: "+ tableNum);
    }

    public void findTable() throws InterruptedException {
        System.out.println("OPERATOR: Please wait while I allocate you to an available table.");

        System.out.print("\nFinding an available table ... ");
        int waitTime = (int)(Math.random() * (120 - 50)) + 50;

        System.out.println();
        progressBarObj.loading(waitTime);

        this.playerGenerator();
    }

    public void login(){
        boolean validity=false;
        String username;
        String password;

        while(!validity) {
            System.out.println("OPERATOR: Please log in your username and password below.");
            System.out.print("    USERNAME: ");
            username = scanner.nextLine();
            System.out.print("    PASSWORD: ");
            password = scanner.nextLine();

            if(this.checkUser(username, password)){
                validity=true;
            } else {
                System.out.println("OPERATOR: It seems your details are wrong. Please try again.");
            }
        }
    }

    public void signup() throws InterruptedException {
        boolean validity=false;
        boolean valid=false;
        boolean forgotPass=false;
        String firstName="";
        String lastName="";
        String username="";
        String password="";

        while(!validity){
            System.out.println("OPERATOR: Please enter your details below.");
            System.out.print("    FIRST NAME: ");
            firstName = scanner.nextLine();

            System.out.print("    LAST NAME: ");
            lastName = scanner.nextLine();

            System.out.print("    EMAIL: ");
            username = scanner.nextLine();

            System.out.print("    PASSWORD: ");
            password = scanner.nextLine();

            if (this.checkUser(username, password)) {
                while(!valid) {
                    System.out.println("OPERATOR: It seems that you already have an account registered with us. Would you " +
                            "want to log in instead? [yes] [no]");
                    System.out.print("PLAYER: ");
                    String playerLogin = scanner.nextLine();

                    if(playerLogin.equals("<")){
                        this.login();
                        valid=true;
                    } else if(">"){
                        forgotPass=true;
                        valid=true;
                    } else {
                        System.out.println("OPERATOR: Sorry I could not catch that. Please try again.");
                    }
                }
            } else {
                valid = false;
                while (!valid) {
                    System.out.println("\nOPERATOR: [restart] [confirm]");
                    System.out.print("PLAYER: ");
                    String choice = scanner.nextLine();
                    System.out.println();

                    if (choice.equals("<")) {
                        System.out.println("OPERATOR: Restarting Sign up process.");
                        progressBarObj.loading(10);
                        valid = true;
                    } else if (choice.equals(">")) {
                        System.out.println("OPERATOR: Thank you for signing up.");
                        valid = true;
                        validity = true;
                    } else {
                        System.out.println("OPERATOR: Sorry I could not catch that. Please try again.");
                    }
                }
            }

            if(forgotPass){

            }

        }

        mainPlayer = new Player(firstName+" "+lastName, username, password, INITIAL);
        this.writeUser(USER_FILENAME);

    }

    public String resetPassword(){

    }

    public boolean checkUser(String username, String password){
        if(users.containsKey(username)){
            this.mainPlayer = users.get(username);
            return users.get(username).getPassword().equals(password);
        } else {
            return false;
        }
    }

    public void writeUser(String filename){
        try(PrintWriter pw = new PrintWriter((new FileWriter(filename, true)))){
            pw.println(mainPlayer.getUsername()+","+mainPlayer.getName()+","+mainPlayer.getPassword()+","+mainPlayer.getMoney());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadUsers(String filename){

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] info = line.split(",");
                String username_in = info[0];
                String name = info[1];
                String password_in = info[2];
                int cash = Integer.parseInt(info[3]);

                this.users.put(username_in, new Player(name, username_in, password_in, cash));
                this.mainPlayer = new Player(name, username_in, password_in, cash);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void playerGenerator(){
        int playerCount = (int)(Math.random() * (7-2)) + 2;
        String name;

        this.comp = new AI[playerCount];

        for(int i=0; i<playerCount; i++){
            name = faker.name().fullName();
            int cash = (int)(Math.random() * (2000-60)) + 60;
            this.comp[i] = new AI(name, cash);
        }
    }

    public void readCards(String filename, int totalCards, int duplicate) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String card;
            int i=1;
            int j;

            while ((card = reader.readLine()) != null) {

                j=0;
                while(i<=totalCards && j<duplicate){
                    deck[i-1] = card;
                    i+=1;
                    j+=1;
                }

                if(card.contains("K")||card.contains("Q")||card.contains("J")||card.contains("10")){
                    cardDict.put(card, 10);
                } else if(card.contains("A")){
                    cardDict.put(card,11);
                } else {
                    cardDict.put(card, Character.getNumericValue(card.charAt(0)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void shuffleCards() {
        for (int i = 0; i < deck.length; i++) {
            int j = (int)(Math.random() * deck.length); // Get a random index out of the deck size
            String temp = deck[i]; // Swapping the cards
            deck[i] = deck[j];
            deck[j] = temp;
        }
    }

}

