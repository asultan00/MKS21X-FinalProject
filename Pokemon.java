import java.util.*; //scanner, ArrayList, Map
import java.io.*; //file, filenotfoundexception

public class Pokemon {

  public static void main(String[] args) {
    Pokemon bulb = new Pokemon("Bulbasaur");
    Pokemon ivy = new Pokemon("Ivysaur");

/*
    System.out.println("Testing Bulbasaur properties");
    System.out.println(bulb.getHP());
    System.out.println(bulb.getAttack());
    System.out.println(bulb.getDefense());
    System.out.println(bulb.getSpeed());
    System.out.println(bulb.getType1());
    System.out.println(bulb.getTypeID1());
    System.out.println(bulb.getType2());
    System.out.println(bulb.getTypeID2());
    System.out.println(ivy.getTypeWeakness());
    System.out.println(ivy.getTypeResistance());
*/
    System.out.println();
    System.out.println(ivy.getHP());
    bulb.attack(ivy, "absorb");
    System.out.println(ivy.getHP());

    bulb.attack(ivy, "flamethrower");
    System.out.println(ivy.getHP());
  }

  private String name, type1, type2;
  private int attack, speed, defense, typeID1, typeID2;
  private double hp;
  private ArrayList<Move> attacks;
  private ArrayList<String> typeWeakness, typeResistance;
  private String[] types =
  {"Normal", "Fighting", "Flying", "Poison", "Ground",
   "Rock", "Bug", "Ghost", "Steel", "Fire", "Water",    //Have to convert this way
   "Grass", "Electric", "Psychic", "Ice", "Dragon", "Dark", "Fairy"};

  public Pokemon(String name1){
    name = name1;

    String[] data = organizeData(name1);

    type1 = data[2];
    type2 = data[3];

    for (int x = 0; x < types.length; x++){
      if (types[x].equals(type1)) typeID1 = x+1;
      if (types[x].equals(type2)) typeID2 = x+1;
    }

    setWeakandRes();

    hp = Integer.parseInt(data[5]);
    attack = Integer.parseInt(data[6]);
    defense = Integer.parseInt(data[7]);
    speed = Integer.parseInt(data[10]);
  }

  private String[] organizeData(String name1){
    try{

      File f = new File("Pokemon.csv");
      Scanner in = new Scanner(f);

      while (in.hasNext()){
        String line = in.nextLine();
        String[] stats = line.split(",");

        // for (int x = 0; x < stats.length; x++){
          // System.out.println(stats[1]);
        // }

        if (stats[1].equals(name)) {
          return stats;
        }
      }
    }
    catch (FileNotFoundException e){
      System.out.println("Error");
    }

    throw new Error();
  }

  public boolean isDead(){
    return hp <= 0;
  }

  //Accessor Methods///////////////////
  public String getName(){
    return name;
  }

  public String getType1(){
    return type1;
  }

  public String getType2(){
    return type2;
  }

  public double getHP(){
    return hp;
  }

  public int getAttack(){
    return attack;
  }

  public int getDefense(){
    return defense;
  }

  public ArrayList<Move> getAttacks(){
    return attacks;
  }

  public ArrayList<String> getTypeWeakness(){
    return typeWeakness;
  }

  public ArrayList<String> getTypeResistance(){
    return typeResistance;
  }

  public int getSpeed(){
    return speed;
  }

  public int getTypeID1(){
    return typeID1;
  }

  public int getTypeID2(){
    return typeID2;
  }

  /////////////////////////////////

  // Mutator Methods

  private void setHP(double num){
    hp = num;
  }

  private void checkFile(int ID){

  }

  private void setWeakandRes(){
    typeWeakness = new ArrayList<String>(10);
    typeResistance = new ArrayList<String>(10);

    try{
      File f = new File("type_efficacy");
      Scanner in = new Scanner(f);
      String line = in.nextLine(); // To skip the first row that just has labels

      while (in.hasNext()){
        line = in.nextLine();
        String[] stats = line.split(",");


        if (typeID1 == Integer.parseInt(stats[1])){
          if (Integer.parseInt(stats[2]) == 200){
            typeWeakness.add(stats[0]);
          }
          if (Integer.parseInt(stats[2]) == 50){
            typeResistance.add(stats[0]);
          }
        }

        if (typeID2 == Integer.parseInt(stats[1])){
          if (Integer.parseInt(stats[2]) == 200){
            typeWeakness.add(stats[0]);
          }
          if (Integer.parseInt(stats[2]) == 50){
            typeResistance.add(stats[0]);
          }
        }
      }
    }
      catch(FileNotFoundException e){
        System.out.println("error");
      }

      for (int x = 0; x < typeWeakness.size() && x < typeResistance.size(); x++){
        if (typeResistance.contains(typeWeakness.get(x))){
          typeResistance.remove(typeWeakness.get(x));
          typeWeakness.remove(x);
        }
      }

      removeRepeats();

    }

    private void removeRepeats(){
      int count = 0;
      for (int x = 0; x < typeResistance.size(); x++){
        if (typeWeakness.contains(typeResistance.get(x))){
          typeWeakness.remove(typeResistance.get(x));
          typeResistance.remove(x);
        }
      }
    }

  ///////////////////////////////

  private double modifier(Move move, Pokemon enemy){
    double x = 1;

    int typeID = move.getTypeID();

    if (typeWeakness.contains(typeID + "")){
      int count = 0;
      for (int i = 0; i < typeWeakness.size(); i++){
        if (Integer.parseInt(typeWeakness.get(i)) == typeID){
          count++;
        }
      }
      x += count;
      return x;
    }

    else if (typeResistance.contains(typeID + "")){
      int count = 0;
      for (int i = 0; i < typeResistance.size(); i++){
        if (Integer.parseInt(typeResistance.get(i)) == typeID){
          count++;
        }
      }
      if (count == 2) x = .25;
      if (count == 1) x = .5;
      return x;
    }

    else{
      return 1;
    }
  }

  public void attack(Pokemon enemy, String move1){
    Move move = new Move(move1);
    double mod = modifier(move, enemy);

    double dmg = ((42 * move.getPower()) *
           (attack / enemy.getDefense()+2) // Formula found online - it's the actual formula used to calculate damage )
           / 50 * mod);

    System.out.println("Attack was " + mod + "x effective. " + enemy.getName() + " took " + dmg + " damage!");

    if (enemy.getHP() - dmg > 0) enemy.setHP(enemy.getHP() - dmg);
    else enemy.setHP(0);
  }
}
