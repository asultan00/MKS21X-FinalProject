import java.util.*; //scanner, ArrayList, Map, Random
import java.io.*; //file, filenotfoundexception

public class Battle{
  public static void main(String[] args) {

    ArrayList<Pokemon> team = new ArrayList<Pokemon>(3);
    System.out.println("here1");


    Pokemon bulb = new Pokemon("Bulbasaur");
    Pokemon chari = new Pokemon("Charizard");
    Pokemon mew2 = new Pokemon("Mewtwo");
    Pokemon mew = new Pokemon("Mew");
    System.out.println("here2");

    team.add(bulb);
    team.add(chari);
    team.add(mew2);
    System.out.println("here3");

    ArrayList<Pokemon> team1 = new ArrayList<Pokemon>(1);
    System.out.println("here4");
    team1.add(mew);


System.out.println("here5");
    Player one = new Trainer("Al", team);
    System.out.println("here6");
    Player enemy = new Enemy("Jo", team1);
    System.out.println("here7");
    Battle battle = new Battle(one, enemy);
    System.out.println("here8");


    Scanner user_input = new Scanner( System.in );
    String firstname;
    System.out.println("Here");
    while (!battle.isOver()){
      System.out.println("Choose next move");
      firstname = user_input.next();
      battle.move(firstname, "fire-punch");

    }
    System.out.println("The Battle is over! " + battle.getWinner().getName() + " has won!");
  }

  private Player one, two, winner;
  private Pokemon active1, active2;
  private boolean over;

  public Battle(Player one1, Player two2){
    one = one1;
    two = two2;

    active1 = one.getMon(0);
    active2 = two.getMon(0);
    over = false;
  }

  // public boolean run(){
  //
  //   }
  // }
  //   while (!run){

  public void escape(){
    if (one.allDead()){
      over = true;
      winner = two;
    }
    if (two.allDead()){
      over = true;
      winner = one;
    }
  }

  public Player getWinner(){
    return winner;
  }

  public void chooseSwitch(int x, int index){
    if (x == 1){
      active1 = one.getMon(index);
    }
    else{
      active2 = two.getMon(index);
    }
  }

  public void ifDead(){
    Console console = System.console();

    if (active1.getHP() <= 0){
      String input = console.readLine("Your pokemon has fainted! Choose a number from 1-6 corresponding to the next Pokemon you wish to use");
      active1 = one.getMon(Integer.parseInt(input));
    }

    if (active2.getHP() <= 0){
      int len = two.getParty().size();
      for (int x = 0; x < len; x++){     // COULD EDIT THIS SO THAT IT'S RANDOM
        if (!(two.getMon(x).isDead())){
          active2 = two.getMon(x);
        }
      }
    }
  }

  public void move(String a, String b){
    if (!active1.isDead() && !active2.isDead()){
      if (active1.getSpeed() > active2.getSpeed()){
        active1.attack(active2, a);
      }
      else{
        active2.attack(active1, b);
      }
    }
  }

  public boolean isOver(){
    return over;
  }

  // public void

  public boolean canCatch(){
    Random rand = new Random();
    if (two.canCatch()){
      if (rand.nextInt(100) >= 70){
        if (one.getParty().size() < 6){
          System.out.println("Congratulations! You have caught " + active2.getName() + "!");
          one.getParty().add(active2);
          over = true;
          return true;
        }
      }
      else{
        System.out.println("Aw! You have not caught it! Try again!");
        return false;
      }
    }
    return false;
  }

}
