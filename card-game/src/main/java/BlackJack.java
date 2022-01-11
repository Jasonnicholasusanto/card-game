import com.github.javafaker.Faker;

public class BlackJack extends VirtualCasino{
    String dealerName;
    Faker faker = new Faker();

    public void run(){
        dealerName = faker.name().firstName();
        System.out.println(dealerName+" (DEALER): Welcome to Black Jack! Let's play!");

    }

    public void printCards(){

    }
}
