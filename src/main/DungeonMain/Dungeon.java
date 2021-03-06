package main.DungeonMain;

import java.io.Serializable;
import java.util.Random;

/**
 * The type Dungeon.
 */
public class Dungeon implements Serializable {
    private final Room[][] myDungeon;
    private int myHeroRow;
    private int myHeroCol;
    private final int myDungeonRows;
    private final int myDungeonCols;
    private int myStartingRow;
    private int myStartingCol;
    private final String myDifficulty;
    private final Random r = new Random();
    private static final long serialVersionUID = 3536060713340084481L;

    /**
     * Instantiates a new Dungeon.
     *
     * @param theDungeonRows the the dungeon rows
     * @param theDungeonCols the the dungeon cols
     * @param theDifficulty  the the difficulty
     */
    public Dungeon(final int theDungeonRows, final int theDungeonCols, final String theDifficulty) {
        myDungeonRows = theDungeonRows;
        myDungeonCols = theDungeonCols;
        myDifficulty = theDifficulty;
        myDungeon = new Room[myDungeonRows][myDungeonCols];
        buildDungeonArray();
        generateDungeon();
    }

    /**
     * Build dungeon array.
     */
    public void buildDungeonArray() {
        for(int row = 0; row < myDungeonRows; row++) {
            for(int col = 0; col < myDungeonCols; col++) {
                myDungeon[row][col] = new Room(myDifficulty);
            }
        }
    }

    /**
     * Generate dungeon.
     */
    public void generateDungeon() {
        int roomsBuilt = 1;
        myStartingRow = r.nextInt(myDungeonRows);
        myStartingCol = r.nextInt(myDungeonCols);
        myHeroRow = myStartingRow;
        myHeroCol = myStartingCol;
        setEntrance(myStartingRow, myStartingCol);
        int currentRow = myStartingRow;
        int currentCol = myStartingCol;
        int nextRow = myStartingRow;
        int nextCol = myStartingCol;
        myDungeon[currentRow][currentCol].setBuilt();
        myDungeon[currentRow][currentCol].setVisited();
        String[] directions = {"NORTH", "SOUTH", "EAST", "WEST"};

        while (roomsBuilt < myDungeonRows*myDungeonCols) {
            String direction = directions[r.nextInt(4)];
            if(currentRow < myDungeonRows - 1 && direction.equals("SOUTH")) {
                nextRow = currentRow + 1;
                myDungeon[currentRow][currentCol].setMySouthRoom(myDungeon[nextRow][nextCol]);
                myDungeon[nextRow][nextCol].setMyNorthRoom(myDungeon[currentRow][currentCol]);
                currentRow = nextRow;
                roomsBuilt += checkBuilt(myDungeon[currentRow][currentCol]);
            }
            else if(currentRow > 0 && direction.equals("NORTH")) {
                nextRow = currentRow - 1;
                myDungeon[currentRow][currentCol].setMyNorthRoom(myDungeon[nextRow][nextCol]);
                myDungeon[nextRow][nextCol].setMySouthRoom(myDungeon[currentRow][currentCol]);
                currentRow = nextRow;
                roomsBuilt += checkBuilt(myDungeon[currentRow][currentCol]);
            }
            else if(currentCol < myDungeonCols - 1 && direction.equals("EAST")) {
                nextCol = currentCol + 1;
                myDungeon[currentRow][currentCol].setMyEastRoom(myDungeon[nextRow][nextCol]);
                myDungeon[nextRow][nextCol].setMyWestRoom(myDungeon[currentRow][currentCol]);
                currentCol = nextCol;
                roomsBuilt += checkBuilt(myDungeon[currentRow][currentCol]);
            }
            else if(currentCol > 0 && direction.equals("WEST")) {
                nextCol = currentCol - 1;
                myDungeon[currentRow][currentCol].setMyWestRoom(myDungeon[nextRow][nextCol]);
                myDungeon[nextRow][nextCol].setMyEastRoom(myDungeon[currentRow][currentCol]);
                currentCol = nextCol;
                roomsBuilt += checkBuilt(myDungeon[currentRow][currentCol]);
            }
        }
        setExit(currentRow, currentCol);
        setAbstractionPillar(r.nextInt(myDungeonRows), r.nextInt(myDungeonCols));
        setEncapsulationPillar(r.nextInt(myDungeonRows), r.nextInt(myDungeonCols));
        setInheritancePillar(r.nextInt(myDungeonRows), r.nextInt(myDungeonCols));
        setPolymorphismPillar(r.nextInt(myDungeonRows), r.nextInt(myDungeonCols));
    }

    /**
     * Sets entrance.
     *
     * @param row the row
     * @param col the col
     */
    public void setEntrance(final int row, final int col) {
        myDungeon[row][col].setEntrance(true);
        disableItems(row, col);
        myDungeon[row][col].setMyStringToken("i");
    }

    /**
     * Sets exit.
     *
     * @param row the row
     * @param col the col
     */
    public void setExit(final int row, final int col) {
        myDungeon[row][col].setExit(true);
        disableItems(row, col);
        myDungeon[row][col].setMyStringToken("O");
    }

    /**
     * Sets abstraction pillar.
     *
     * @param row the row
     * @param col the col
     */
    public void setAbstractionPillar(final int row, final int col) {
        if(myDungeon[row][col].isEntrance() || myDungeon[row][col].isExit() ||
                myDungeon[row][col].isHasEncapsulationPillar() || myDungeon[row][col].isHasInheritancePillar() ||
                myDungeon[row][col].isHasPolymorphismPillar()) {
            setAbstractionPillar(r.nextInt(myDungeonRows), r.nextInt(myDungeonCols));
        } else {
            myDungeon[row][col].setHasAbstractionPillar(true);
            myDungeon[row][col].setMyStringToken("A");
        }
    }

    /**
     * Sets inheritance pillar.
     *
     * @param row the row
     * @param col the col
     */
    public void setInheritancePillar(final int row, final int col) {
        if(myDungeon[row][col].isEntrance() || myDungeon[row][col].isExit() ||
                myDungeon[row][col].isHasEncapsulationPillar() || myDungeon[row][col].isHasAbstractionPillar() ||
                myDungeon[row][col].isHasPolymorphismPillar()) {
            setInheritancePillar(r.nextInt(myDungeonRows), r.nextInt(myDungeonCols));
        } else {
            myDungeon[row][col].setHasInheritancePillar(true);
            myDungeon[row][col].setMyStringToken("I");
        }
    }

    /**
     * Sets encapsulation pillar.
     *
     * @param row the row
     * @param col the col
     */
    public void setEncapsulationPillar(final int row, final int col) {
        if(myDungeon[row][col].isEntrance() || myDungeon[row][col].isExit() ||
                myDungeon[row][col].isHasAbstractionPillar() || myDungeon[row][col].isHasInheritancePillar() ||
                myDungeon[row][col].isHasPolymorphismPillar()) {
            setEncapsulationPillar(r.nextInt(myDungeonRows), r.nextInt(myDungeonCols));
        } else {
            myDungeon[row][col].setHasEncapsulationPillar(true);
            myDungeon[row][col].setMyStringToken("E");
        }
    }

    /**
     * Sets polymorphism pillar.
     *
     * @param row the row
     * @param col the col
     */
    public void setPolymorphismPillar(final int row, final int col) {
        if(myDungeon[row][col].isEntrance() || myDungeon[row][col].isExit() ||
                myDungeon[row][col].isHasEncapsulationPillar() || myDungeon[row][col].isHasInheritancePillar() ||
                myDungeon[row][col].isHasAbstractionPillar()) {
            setPolymorphismPillar(r.nextInt(myDungeonRows), r.nextInt(myDungeonCols));
        } else {
            myDungeon[row][col].setHasPolymorphismPillar(true);
            myDungeon[row][col].setMyStringToken("P");
        }
    }

    /**
     * Check built int.
     *
     * @param theRoom the room
     * @return the int
     */
    public int checkBuilt(final Room theRoom) {
        if (!theRoom.isBuilt()) {
            theRoom.setBuilt();
            return 1;
        }
        else return 0;
    }

    /**
     * Disable items.
     *
     * @param row the row
     * @param col the col
     */
    public void disableItems(final int row, final int col) {
        myDungeon[row][col].setHasHealPotion(false);
        myDungeon[row][col].setHasVisionPotion(false);
        myDungeon[row][col].setHasPit(false);
        myDungeon[row][col].setHasMonster(false);
        myDungeon[row][col].setMyMonster(null);
    }

    /**
     * Move hero.
     *
     * @param rowDirection the row direction
     * @param colDirection the col direction
     */
    public void moveHero(int rowDirection, int colDirection) {
        myDungeon[myHeroRow][myHeroCol].setMyStringToken("v");
        myHeroRow += rowDirection;
        myHeroCol += colDirection;
        myDungeon[myHeroRow][myHeroCol].setMyStringToken("h");
    }

    public String toString() {
        String s = "";
        for (int row = 0; row < myDungeonRows; row++) {
            s += getTopString(row);
            s += getEmptyMidString(row);
        }
        s += "***";
        for (int row = 0; row < myDungeonRows-1; row++) {
            s += "**";
        }
        return s;
    }

    /**
     * Get top string string.
     *
     * @param theRow the row
     * @return the string
     */
    public String getTopString(final int theRow){
        String s = "*";
        for (int i = 0; i < myDungeonRows; i++) {
            if(myDungeon[theRow][i].getMyNorthRoom() == null){
                s += "**";
            } else{
                s += "-*";
            }
        }
        s += "\n";
        return s;
    }

    /**
     * Get empty mid string string.
     *
     * @param theRow the row
     * @return the string
     */
    public String getEmptyMidString(final int theRow){
        String s = "*";
        for (int i = 0; i < myDungeonRows; i++) {
            if (myDungeon[theRow][i].getMyStringToken().equals("i")) {
                s += "h";
            }else if(myDungeon[theRow][i].getMyStringToken().equals("h")) {
                s += "h";
            }else if(myDungeon[theRow][i].getMyStringToken().equals("v")) {
                s += "v";
            }else if(myDungeon[theRow][i].isSeen()) {
                s += myDungeon[theRow][i].getMyStringToken();
            }else {
                s += " ";
            } // empty dungeon
            if(myDungeon[theRow][i].getMyEastRoom() == null){
                s += "*";
            } else{
                s += "|";
            }
        }
        s += "\n";
        return s;
    }

    /**
     * Get mid string string.
     *
     * @param theRow the row
     * @return the string
     */
    public String getMidString(final int theRow){
        String s = "*";
        for (int i = 0; i < myDungeonRows; i++) {
            if (myDungeon[theRow][i].getMyStringToken() == "i") {
                s += "h";
            }else if(myDungeon[theRow][i].getMyStringToken()== "h") {
                s += "h";
            }else if(myDungeon[theRow][i].getMyStringToken()== "v") {
                s += "v";
            }else {
                s += " ";
            } // empty dungeon
            if(myDungeon[theRow][i].getMyEastRoom() == null){
                s += "*";
            } else{
                s += "|";
            }
        }
        s += "\n";
        return s;
    }

    /**
     * Get my dungeon room [ ] [ ].
     *
     * @return the room [ ] [ ]
     */
    public Room[][] getMyDungeon() {
        return myDungeon;
    }

    /**
     * Gets my room.
     *
     * @return the room
     */
    public Room getMyRoom() {
        return myDungeon[myHeroRow][myHeroCol];
    }

    /**
     * Gets my dungeon rows.
     *
     * @return the dungeon rows
     */
    public int getMyDungeonRows() {
        return myDungeon.length;
    }

    /**
     * Gets my dungeon cols.
     *
     * @return the dungeon cols
     */
    public int getMyDungeonCols() {
        return myDungeon.length;
    }

    /**
     * Gets my difficulty.
     *
     * @return the difficulty
     */
    public String getMyDifficulty() {
        return myDifficulty;
    }
}