package battleship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class Main {

    public static byte[] XY = new byte[3];
    public static byte[] XY2 = new byte[3];
    public static char[][] fieldP1 = new char[10][10];
    public static char[][] fieldP2 = new char[10][10];
    public static char[][] fieldFog = new char[10][10];
    public static ArrayList<Coordinates[]> ShipsP1 = new ArrayList<>();
    public static ArrayList<Coordinates[]> ShipsP2 = new ArrayList<>();
    public static int deleted;
    private static boolean playerN1 = true;

    public static void main(String[] args) {
        int[] cells = {5,4,3,3,2};
        initArray(fieldP1);
        printField(fieldP1);
        System.out.println("\nPlayer 1, place your ships on the game field\n");
        for (int i = 0; i < 5; i++) {
            askShip(cells[i], i);
            askCoordinates(cells[i], fieldP1);
            setShipCoordinates(fieldP1);
            printField(fieldP1);
            ShipsP1.add(fillShips(cells[i]));
        }

        changePlayerBasic();
        initArray(fieldP2);
        System.out.println("\nPlayer 2, place your ships on the game field\n");
        for (int i = 0; i < 5; i++) {
            askShip(cells[i], i);
            askCoordinates(cells[i], fieldP2);
            setShipCoordinates(fieldP2);
            printField(fieldP2);
            ShipsP2.add(fillShips(cells[i]));
        }

        System.out.println("\nThe game starts!");
        initArray(fieldFog);
/*        changePlayer();*/
        shootGun();
    }


    public static void changePlayerBasic() {
        playerN1 = !playerN1;
        System.out.println("\nPress Enter and pass the move to another player\n");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    public static void changePlayer() {
        playerN1 = !playerN1;
        System.out.println("\nPress Enter and pass the move to another player\n");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        char[][] field;
        String playerName;

        if (playerN1) {
            field = fieldP1;
            playerName = "Player 1";
        } else {
            playerName = "Player 2";
            field = fieldP2;
        }

        printField(fieldFog);
        System.out.println("---------------------");
        printField(field);
        System.out.printf("\n%s, it's your turn\n", playerName);

    }


    public static boolean checkDeadShips(ArrayList<Coordinates[]> Ships) {
        boolean flag = false;
        int k;
        if (playerN1) {
            k = 1;
        } else {
            k = 0;
        }
        for (int i = 0; i < Ships.size(); i++) {
            int counter = 0;
            deleted = -1;
            for (int j = 0; j < Ships.get(i).length; j++) {
                if (Ships.get(i)[j].getX() == -1) {
                    counter++;
                }

                if (counter == Ships.get(i).length) {
                    deleted = i;
                    if (Ships.size() == 1) {
                        System.out.println("You sank the last ship. You won. Congratulations!");
                        System.exit(0);
                    }
                    System.out.println("\nYou sank a ship!\n");
                    return true;
                }
            }
        }

        return flag;
    }

    public static void setShotShips() {
        ArrayList<Coordinates[]> Ships;
        if (playerN1) {
            Ships = ShipsP2;
        } else {
            Ships = ShipsP1;
        }


        for (int i = 0; i < Ships.size(); i++) {
            for (int j = 0; j < Ships.get(i).length; j++) {
                if (Ships.get(i)[j].getX() == XY[0] && Ships.get(i)[j].getY() == XY[1]) {

                    Ships.get(i)[j].setX(-1);

                    if (!checkDeadShips(Ships)) {
                        printField(fieldFog);
                        System.out.println("\nYou hit a ship!\n");
                    }
                }
            }
        }
        if (deleted != -1) {
            Ships.remove(deleted);
        }
    }

    public static Coordinates[] fillShips(int cell) {
        Coordinates first = new Coordinates(XY[0], XY[1]);
        Coordinates last = new Coordinates(XY2[0], XY2[1]);
        return Coordinates.fullShip(cell, first, last);
    }

    public static void shootGun() {
        changePlayer();
        setShootGun();
        char[][] field;
        if (playerN1) {
            field = fieldP2;
        } else {
            field = fieldP1;
        }

        if (field[XY[0]][XY[1]] == 'X') {
            System.out.println("You hit a ship");
            shootGun();
        }
        if (field[XY[0]][XY[1]] == 'O') {
            field[XY[0]][XY[1]] = 'X';
            setShotShips();
            shootGun();
        } else {
            field[XY[0]][XY[1]] = 'M';
            System.out.println("\nYou missed!\n");
            shootGun();
        }
    }

    public static void setShootGun() {
        Scanner scanner = new Scanner(System.in);
        String rawCoordinates = scanner.nextLine();
        XY = rawCoordinates.getBytes();
        XY[0] -= 65;
        XY[1] = (byte) (Character.getNumericValue(XY[1]) - 1);
        if (XY[0] > 9) {
            System.out.println("\nError! You entered the wrong coordinates! Try again:\n");
            setShootGun();
        }
        try {
            if (XY[2] == 48) {
                XY[1] = 9;
            }
            if (XY[2] > 48) {
                System.out.println("\nError! You entered the wrong coordinates! Try again:\n");
                setShootGun();
            }
        }
        catch (Exception ignored) {}

    }

    public static void setShipCoordinates(char[][] field) {

        for (int i = XY[0]; i <= XY2[0]; i++) {
            for (int j = XY[1]; j <= XY2[1]; j++) {
                field[i][j] = 'O';
            }
        }

    }

    public static void askShip (int cell, int i) {
        String[] Ships = {"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};
        System.out.printf("\nEnter the coordinates of the %s (%d cells):\n%n", Ships[i], cell);
    }


    public static void askCoordinates(int cell, char[][] field) {
        Scanner scanner = new Scanner(System.in);
        String rawCoordinates = scanner.nextLine();
        String[] rCoordinates = rawCoordinates.split("\\s+");

        XY = rCoordinates[0].getBytes();
        XY2 = rCoordinates[1].getBytes();

        // первые координаты массивов это юникод буквы, а вторые это цифры.
        XY[0] -= 65;
        XY2[0] -= 65;
        XY[1] = (byte) (Character.getNumericValue(XY[1]) - 1);
        XY2[1] = (byte) (Character.getNumericValue(XY2[1]) - 1);

        try {
            if (XY[2] == 48) {
                XY[1] = 9;
            }

            if (XY2[2] == 48) {
                XY2[1] = 9;
            }
        }
        catch (Exception ignored) {}

        // если координаты наоборот

        if (XY[0] > XY2[0]) {
            byte temp = 0;
            temp = XY[0];
            XY[0] = XY2[0];
            XY2[0] = temp;
        }

        if (XY[1] > XY2[1]) {
            byte temp = 0;
            temp = XY[1];
            XY[1] = XY2[1];
            XY2[1] = temp;
        }

        //проверка на длину по горизонтали или вертикали
        if (!((Math.abs(XY[1]-XY2[1]) + 1 == cell) || (Math.abs(XY[0]-XY2[0]) + 1 == cell) )) {
            System.out.println("\nError! Wrong length of the Submarine! Try again:\n");
            askCoordinates(cell, field);
        }

        //проверка на недиагональность
        if ( !( (XY[0] == XY2[0] && (Math.abs(XY[1]-XY2[1]) == cell - 1)) ||
                (XY[1] == XY2[1] && (Math.abs(XY[0]-XY2[0]) == cell-1)) ) ) {
            System.out.println("\nError! Wrong ship location! Try again:\n");
            askCoordinates(cell, field);
        }

        // проверка на соседей
        int counter = 0;
        try {
            // проверка если корабль горизонтальный
            if (XY[0] == XY2[0]) {
                for (int i = XY[1] - 1; i <= XY2[1] + 1; i++) {
                    if (field[XY[0] - 1][i] == 'O' || field[XY[0] + 1][i] == 'O') {
                        counter++;
                    }
                }
            }
            // проверка если корабль вертикальый
            if (XY[1] == XY2[1]) {
                for (int i = XY[0] - 1; i <= XY2[0] + 1; i++) {
                    if (field[i][XY[1] - 1] == 'O' || field[i][XY[1] + 1] == 'O') {
                        counter++;
                    }
                }
            }
        } catch (Exception ignored) {}

        if (counter != 0) {
            System.out.println("\nError! You placed it too close to another one. Try again:\n");
            askCoordinates(cell, field);
        }

    }

    public static void printField(char[][] table) {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < table.length; i++) {
            System.out.print((char) (65+i));
            for (int j = 0; j < table[i].length; j++) {
                System.out.print(" " + table[i][j]);
            }
            System.out.println();
        }
    }

    public static void initArray(char[][] table) {
        for (char[] chars : table) {
            Arrays.fill(chars, '~');
        }
    }

}
