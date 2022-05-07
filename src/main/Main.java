package main;

import main.DungeonCharacter.*;
import main.DungeonMain.Dungeon;
import main.DungeonMain.Room;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Dungeon d = new Dungeon();
        d.generateDungeon();
        Room[][] dungeon = d.getDungeon();
        for (int i = 0; i < 5; i++) {
            System.out.println();
            for (int j = 0; j < 5; j++) {
                System.out.print(dungeon[i][j].getMyStringToken());
                System.out.println(dungeon[i][j].toString());
            }

        }

        //Thief hero = new Thief("Name");
       // Skeleton enemy = new Skeleton();
        /*
        System.out.println(hero.getMyName());
        System.out.println(hero.getMyHitPoints());
        System.out.println(hero.getMyMinDam());
        System.out.println(hero.getMyMaxDam());
        System.out.println(hero.getMyAttackSpeed());
        System.out.println(hero.getMyHitChance());
        System.out.println(hero.getMyAlive());
        */

        //System.out.println(enemy.getMyHitPoints());
        //hero.specialSkill(enemy);
        //System.out.println(enemy.getMyHitPoints());
/*
        System.out.println(hero.getMyHitPoints());
        enemy.basicAttack(hero);
        System.out.println(hero.getMyHitPoints());
        hero.specialSkill(hero);
        System.out.println(hero.getMyHitPoints());
*/
        /*
        System.out.println(hero.getMyHitPoints());
        enemy.basicAttack(hero);
        System.out.println(hero.getMyHitPoints());
        */
    }

}
