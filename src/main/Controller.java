package main;

import main.DungeonCharacter.*;
import main.DungeonGUI.DungeonUI;
import main.DungeonMain.Dungeon;
import main.DungeonMain.Room;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.util.Random;

/**
 * The type Controller.
 */
public class Controller implements Serializable{
    private Dungeon myDungeon;
    private Room myCurrRoom;
    private Hero myHero;
    private final Random r = new Random();
    private DungeonUI myDungeonUIEXP;
    private final AudioController audioController = new AudioController();
    private static final long serialVersionUID = 13425364675L;

    /**
     * Instantiates a new Controller.
     *
     * @param theDungeonUIEXP the dungeon uiexp
     */
    public Controller(final DungeonUI theDungeonUIEXP){
        myDungeonUIEXP = theDungeonUIEXP;
    }

    /**
     * Used to load saved dungeon uiexp
     *
     * @param theDungeonUIEXP  the dungeon uiexp
     */
    public void setMyDungeonUIEXP(final DungeonUI theDungeonUIEXP){myDungeonUIEXP = theDungeonUIEXP;}

    /**
     * Start game.
     *
     * @param theName        the name
     * @param theClass       the class
     * @param theDifficulty  the difficulty
     * @param theDungeonSize the dungeon size
     */
    public void startGame(final String theName, final String theClass, final String theDifficulty, int theDungeonSize) {
        if (theClass.equals("Warrior")) {
            myHero = new Warrior(theName);
        }
        else if (theClass.equals("Priestess")) {
            myHero = new Priestess(theName);
        }
        else if (theClass.equals("Thief")) {
            myHero = new Thief(theName);
        }
        else if (theClass.equals("Barbarian")) {
            myHero = new Barbarian(theName);
        }
        else if (theClass.equals("Mage")) {
            myHero = new Mage(theName);
        }
        else if (theClass.equals("Swordsman")) {
            myHero = new Swordsman(theName);
        }
        else if (theClass.equals("Monk")) {
            myHero = new Monk(theName);
        }
        else if (theClass.equals("Samurai")) {
            myHero = new Samurai(theName);
        }
        else if (theClass.equals("Occultist")){
            myHero = new Occultist(theName);
        }

        myDungeon = new Dungeon(theDungeonSize, theDungeonSize, theDifficulty);
        myCurrRoom = myDungeon.getMyRoom();
        audioController.playBackgroundAudio();
    }

    /**
     * Check direction.
     *
     * @param theDirection the direction
     */
    public void checkDirection(final String theDirection) {
        if (theDirection.equals("^")) {
            traverseDungeon(-1,0);
        }
        if (theDirection.equals("v")) {
            traverseDungeon(1,0);
        }
        if (theDirection.equals(">")) {
            traverseDungeon(0,1);
        }
        if (theDirection.equals("<")) {
            traverseDungeon(0,-1);
        }
    }

    /**
     * Traverse dungeon.
     *
     * @param rowDir the row dir
     * @param colDir the col dir
     */
    public void traverseDungeon(final int rowDir, final int colDir) {
        myDungeon.moveHero(rowDir,colDir);
        myCurrRoom = myDungeon.getMyRoom();
        checkRoom();
    }

    /**
     * Check room.
     */
    public void checkRoom() {
        Room room = myCurrRoom;
        if(room.isHasAbstractionPillar()) {
            myHero.setMyPillars(myHero.getMyPillars() + 1);
            myDungeonUIEXP.updateAdventureText("You have found the Pillar of Abstraction!");
            room.setHasAbstractionPillar(false);
        }
        if(room.isHasEncapsulationPillar()) {
            myHero.setMyPillars(myHero.getMyPillars() + 1);
            myDungeonUIEXP.updateAdventureText("You have found the Pillar of Encapsulation!");
            room.setHasEncapsulationPillar(false);
        }
        if(room.isHasInheritancePillar()) {
            myHero.setMyPillars(myHero.getMyPillars() + 1);
            myDungeonUIEXP.updateAdventureText("You have found the Pillar of Inheritance!");
            room.setHasInheritancePillar(false);
        }
        if(room.isHasPolymorphismPillar()) {
            myHero.setMyPillars(myHero.getMyPillars() + 1);
            myDungeonUIEXP.updateAdventureText("You have found the Pillar of Polymorphism!");
            room.setHasPolymorphismPillar(false);
        }
        if(room.isHasPit()) {
            int pitDamageTaken = pitDamage();
            double tempBlockChance = myHero.getMyBlockChance();
            myHero.setMyBlockChance(0.0);
            myHero.updateHealth(pitDamageTaken);
            audioController.playAudio("src/playerhurt2.wav");
            myHero.setMyBlockChance(tempBlockChance);
            myDungeonUIEXP.updateAdventureText("You have fallen into a pit and have taken " + pitDamageTaken + " damage!");
            room.setHasPit(false);
        }
        if(myHero.getMyAlive()) {
            if (room.isHasHealPotion()) {
                myHero.setMyHealingPotions(myHero.getMyHealingPotions() + 1);
                myDungeonUIEXP.updateAdventureText("You have found a healing potion!");
                room.setHasHealPotion(false);
            }
            if (room.isHasVisionPotion()) {
                myHero.setMyVisionPotions(myHero.getMyVisionPotions() + 1);
                myDungeonUIEXP.updateAdventureText("You have found a vision potion!");
                room.setHasVisionPotion(false);
            }
            if (room.isHasMonster()) {
                audioController.stopBackgroundAudio();
                audioController.playBattleAudio();
                Monster theMonster = room.getMyMonster();
                myDungeonUIEXP.updateAdventureText("You have encountered a " + theMonster.getMyName() + "!");
                myDungeonUIEXP.buildBattlePanel(myHero, theMonster);
                room.setMyMonster(null);
                room.setHasMonster(false);
            } else {
                myDungeonUIEXP.buildAdventurePanel(getMyCurrRoom());
            }
            if (room.isExit()) {
                if (myHero.getMyPillars() < 4) {
                    myDungeonUIEXP.updateAdventureText("You have not collected all the pillars!");
                } else {
                    audioController.stopBackgroundAudio();
                    myDungeonUIEXP.updateAdventureText("You have collected all the pillars!");
                    myDungeonUIEXP.getMyMainPanel().repaint();
                    myDungeonUIEXP.updateAdventureText("However, one last challenge stands in your way.");
                    myDungeonUIEXP.getMyMainPanel().repaint();
                    Monster theMonster = new MonsterFactory().createMonster("Lord of OO");
                    myDungeonUIEXP.updateAdventureText("You must now face the " + theMonster.getMyName() + "!");
                    myDungeonUIEXP.getMyMainPanel().repaint();
                    audioController.playBossAudio();
                    myDungeonUIEXP.buildBattlePanel(myHero, theMonster);
                }
            }
        }
        else {
            audioController.stopBackgroundAudio();
            audioController.playAudio("src/deathsound.wav");
            myDungeonUIEXP.buildDefeatScreen();
        }
    }

    /**
     * Battle.
     *
     * @param theMonster the monster
     * @param theAction  the action
     */
    public void battle(final Monster theMonster, final String theAction) {
        switch (theAction) {
            case "ATTACK" -> {
                myDungeonUIEXP.updateAdventureText(myHero.getMyName() + " attacks");
                basicAttack(myHero, theMonster);
            }
            case "SKILL" -> {
                myDungeonUIEXP.updateAdventureText(myHero.getMyName() + " uses " + myHero.getMySkillName());
                myHero.useSkill(theMonster);
            }
            case "HEAL" -> useHealPotion();
            default -> myDungeonUIEXP.updateAdventureText("Invalid Choice");
        }
        if (theMonster.getMyAlive()) {
            myDungeonUIEXP.updateAdventureText(theMonster.getMyName() + " attacks");
            basicAttack(theMonster, myHero);
            if(!myHero.getMyAlive() && theMonster.getMyName().equals("Lord of OO")) {
                audioController.stopBossAudio();
                audioController.playAudio("src/deathsound.wav");
                myDungeonUIEXP.buildDefeatScreen();
                return;
            } else if(!myHero.getMyAlive()) {
                audioController.stopBattleAudio();
                audioController.playAudio("src/deathsound.wav");
                myDungeonUIEXP.buildDefeatScreen();
                return;
            }
        }else{
            myDungeonUIEXP.updateAdventureText(theMonster.getMyName() + " has been defeated");
        }
        if (myHero instanceof StateResettable) ((StateResettable) myHero).resetState();

        if(myHero.getMyAlive() && theMonster.getMyAlive()) myDungeonUIEXP.buildBattlePanel(myHero, theMonster);
        else if (!theMonster.getMyAlive() && theMonster.getMyName().equals("Lord of OO")){
            audioController.stopBossAudio();
            audioController.playAudio("src/victorysound.wav");
            myDungeonUIEXP.buildVictoryScreen();
        }
        else {
            audioController.stopBattleAudio();
            audioController.startBackgroundAudio();
            myDungeonUIEXP.buildAdventurePanel(myCurrRoom);
        }

    }

    /**
     * Use heal potion.
     */
    public void useHealPotion(){
        int maxHealth = myHero.getMY_MAX_HEALTH();
        if(myHero.getMyHealingPotions()>0 && myHero.getMyHitPoints() < myHero.getMY_MAX_HEALTH()){
            int heal = r.nextInt((int) (.14*maxHealth),(int) (.28*maxHealth)); // can change later
            myHero.setMyHealingPotions(myHero.getMyHealingPotions()-1);
            //call to add heal from healing potion
            myHero.setMyHitPoints(Math.min((myHero.getMyHitPoints() + heal), myHero.getMY_MAX_HEALTH()));
            myDungeonUIEXP.updateAdventureText("You healed for " + heal + " health");
        }
        else if (myHero.getMyHealingPotions()>0 && myHero.getMyHitPoints() == myHero.getMY_MAX_HEALTH())
            myDungeonUIEXP.updateAdventureText("You are already at full health");
        else myDungeonUIEXP.updateAdventureText("No Healing Potions To Use");
    }

    /**
     * Use vision potion.
     */
    public void useVisionPotion() {
        if (myHero.getMyVisionPotions() > 0) {
            if (myCurrRoom.getMyNorthRoom() != null) {
                myCurrRoom.getMyNorthRoom().setSeen(true);
            }
            if (myCurrRoom.getMySouthRoom() != null) {
                myCurrRoom.getMySouthRoom().setSeen(true);
            }
            if (myCurrRoom.getMyWestRoom() != null) {
                myCurrRoom.getMyWestRoom().setSeen(true);
            }
            if (myCurrRoom.getMyEastRoom() != null) {
                myCurrRoom.getMyEastRoom().setSeen(true);
            }
            myHero.setMyVisionPotions(myHero.getMyVisionPotions() - 1);
            myDungeonUIEXP.updateAdventureText("You can see nearby traversable rooms");
        }
        else myDungeonUIEXP.updateAdventureText("No Vision Potions To Use");
        myDungeonUIEXP.buildAdventurePanel(getMyCurrRoom());
    }

    /**
     * Pit damage int.
     *
     * @return the int
     */
    public int pitDamage() {
        Random r = new Random();
        return r.nextInt(1, 15) + 1;
    }

    /**
     * Basic attack int.
     *
     * @param theAttacker the the attacker
     * @param theTarget   the the target
     * @return the int
     */
    public int basicAttack(final DungeonCharacter theAttacker, final DungeonCharacter theTarget){
        int attacks = 1;
        int tempTargetHealth = theTarget.getMyHitPoints();
        if(theAttacker.getMyAttackSpeed() > theTarget.getMyAttackSpeed()) {
            attacks = theAttacker.getMyAttackSpeed() / theTarget.getMyAttackSpeed();
        }
        for (int i = 0; i < attacks; i++) {
            int damage = theAttacker.attackValue(theTarget);
            if (damage > 0) {
                myDungeonUIEXP.updateAdventureText(theAttacker.getMyName() + " hits for " + damage + " damage");
                if(theTarget.getMyHitPoints() > tempTargetHealth - damage && theTarget instanceof Monster) {
                    myDungeonUIEXP.updateAdventureText(theTarget.getMyName() + " heals for " + ((theTarget.getMyHitPoints())-(tempTargetHealth - damage)) + " damage");
                }
            }
            else myDungeonUIEXP.updateAdventureText(theAttacker.getMyName() + " missed their attack");
        }
        return attacks;
    }

    private Object readResolve() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        audioController.setBackgroundClip();
        return this;
    }

    /**
     * Gets my dungeon.
     *
     * @return the my dungeon
     */
    public Dungeon getMyDungeon() {
        return myDungeon;
    }

    /**
     * Sets my dungeon.
     *
     * @param theDungeon the my dungeon
     */
    public void setMyDungeon(final Dungeon theDungeon) {
        this.myDungeon = theDungeon;
    }

    /**
     * Gets my curr room.
     *
     * @return the my curr room
     */
    public Room getMyCurrRoom() {
        return myCurrRoom;
    }

    /**
     * Sets my curr room.
     *
     * @param theCurrRoom the my curr room
     */
    public void setMyCurrRoom(final Room theCurrRoom) {
        this.myCurrRoom = theCurrRoom;
    }

    /**
     * Gets my hero.
     *
     * @return the my hero
     */
    public Hero getMyHero() {
        return myHero;
    }

    /**
     * Sets my hero.
     *
     * @param theHero the my hero
     */
    public void setMyHero(final Hero theHero) {
        this.myHero = theHero;
    }

    public String getHeroClass() {
        return myHero.getHeroClass();
    }
}