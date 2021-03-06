package main.DungeonCharacter;

import java.util.Random;

/**
 * The type Thief.
 */
public class Thief extends Hero {

    /**
     * Instantiates a new Thief.
     *
     * @param theName the name
     */
    public Thief(final String theName) {
        super(theName, 75, 20, 40, 6, 0.8, 0.4, "Surprise Attack","ThiefImage.png");
    }

    /**
     * Special skill for the Thief
     *
     * @param theTarget the target
     */
    @Override
    public void specialSkill(final DungeonCharacter theTarget) {
        // Surprise Attack: 40 percent chance it is successful.
        // If it is successful, Thief gets an attack and another turn (extra attack) in the current round.
        // There is a 20 percent chance the Thief is caught in which case no attack at all is rendered.
        // The other 40 percent is just a normal attack.
        System.out.println(getMyName() + " goes for a surprise attack");
        Random theRandom = new Random();
        float roll = theRandom.nextFloat(1);
        if (roll <= .4){
            basicAttack(theTarget);
            basicAttack(theTarget);
        }else if(.4 < roll && roll <= .8 ){
            basicAttack(theTarget);
        }
        else System.out.println(getMyName() + " missed their attack");
    }

    /**
     * @return string of the class
     */
    public String getHeroClass() {
        return "Thief";
    }
}