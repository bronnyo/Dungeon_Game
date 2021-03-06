package main.DungeonCharacter;

/**
 * The type Mage.
 */
public class Mage extends Hero {
    /**
     * Instantiates a new Mage.
     *
     * @param theName the  name
     */
    public Mage(final String theName) {
        super(theName, 60, 55, 75, 5, 0.7, 0.35, "Fireball","MageImage.png");
    }

    /**
     * Special skill for the Mage
     *
     * @param theTarget the target
     */
    @Override
    public void specialSkill(final DungeonCharacter theTarget) {
        // Fireball: Has a guaranteed chance to hit, but with the cost of reduced damage
        System.out.println(getMyName() + " shoots out a fireball");
        double currHitChance = this.getMyHitChance();
        int currMinDam = this.getMyMinDam();
        int currMaxDam = this.getMyMaxDam();

        //update with special skill changes
        this.setMyHitChance(1.0);
        this.setMyMinDam(20);
        this.setMyMaxDam(25);

        //call attack - attacks only once
        basicAttack(theTarget);

        //set values back
        this.setMyHitChance(currHitChance);
        this.setMyMinDam(currMinDam);
        this.setMyMaxDam(currMaxDam);
    }
    /**
     * @return string of the class
     */
    public String getHeroClass() {
        return "Mage";
    }
}
