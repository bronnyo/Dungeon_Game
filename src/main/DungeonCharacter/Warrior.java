package main.DungeonCharacter;

/**
 * The type Warrior.
 */
public class Warrior extends Hero {

    /**
     * Instantiates a new Warrior.
     *
     * @param theName the name
     */
    public Warrior(final String theName) {
        //super(theName, 125, 400, 500, 4, 1.0, 0.2, "WarriorImage.png");
        super(theName, 125, 35, 60, 4, 0.8, 0.2, "Crushing Blow","WarriorImage.png");
    }

    /**
     * Special Skill for the Warrior
     *
     * @param theTarget the target
     */
    @Override
    public void specialSkill(final DungeonCharacter theTarget) {
        // Crushing Blow: does 75 to 175 points of damage but only has a 40% chance of succeeding
        //save current hit chance, min damage, max damage
        System.out.println(getMyName() + " goes for a crushing blow");
        double currHitChance = this.getMyHitChance();
        int currMinDam = this.getMyMinDam();
        int currMaxDam = this.getMyMaxDam();

        //update with special skill changes
        this.setMyHitChance(0.4);
        this.setMyMinDam(75);
        this.setMyMaxDam(175);

        //call attack - attacks only once
        attackValue(theTarget);

        //set values back
        this.setMyHitChance(currHitChance);
        this.setMyMinDam(currMinDam);
        this.setMyMaxDam(currMaxDam);
    }

    /**
     * @return string of the class
     */
    public String getHeroClass(){
        return "Warrior";
    }
}
