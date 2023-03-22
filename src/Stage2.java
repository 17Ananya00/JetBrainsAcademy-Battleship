import java.util.Arrays;
import java.util.Scanner;

public class Stage2 {
    public class Main {

        public void main(String[] args) {
            Game game = new Game();
            game.start();
        }
    }
    public class Battlefield {
        final int SIZE = 10;
        final int AIRCRAFT_SIZE = 5;
        final int BATTLESHIP_SIZE = 4;
        final int SUBMARINE_SIZE = 3;
        final int CRUISER_SIZE = 3;
        final int DESTROYER_SIZE = 2;
        final char EMPTY = '~';
        char[][] filed = new char[SIZE][SIZE];
        Ship[] ships;
        Scanner scanner = new Scanner(System.in);


        public Battlefield() {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    filed[i][j] = EMPTY;
                }
            }
        }


        public void initfield() {
            System.out.println(this.toString());
            ships = new Ship[5];
            ships[0] = new Ship(AIRCRAFT_SIZE, "Aircraft Carrier");
            ships[1] = new Ship(BATTLESHIP_SIZE, "Battleship");
            ships[2] = new Ship(SUBMARINE_SIZE, "Submarine");
            ships[3] = new Ship(CRUISER_SIZE, "Cruiser");
            ships[4] = new Ship(DESTROYER_SIZE, "Destroyer");
            for (Ship ship : ships) {
                System.out.printf("Enter the coordinates of the %s (%d cells):\n", ship.getName(), ship.getSize());
                while (true) {
                    String[] coordinates = scanner.nextLine().split(" ");
                    int rowBegin = coordinates[0].charAt(0) - 65;
                    int columnBegin = Integer.parseInt(coordinates[0].substring(1));
                    int rowEnd = coordinates[1].charAt(0) - 65;
                    int columnEnd = Integer.parseInt(coordinates[1].substring(1));
                    if (rowBegin > rowEnd) {
                        int tmp = rowEnd;
                        rowEnd = rowBegin;
                        rowBegin = tmp;
                    }
                    if (columnBegin > columnEnd) {
                        int tmp = columnEnd;
                        columnEnd = columnBegin;
                        columnBegin = tmp;
                    }
                    if (ship.setCoordinates(rowBegin, columnBegin, rowEnd, columnEnd)) {
                        if (putShipOnField(rowBegin, columnBegin, rowEnd, columnEnd, ship)) {
                            System.out.println(this.toString());
                            break;
                        }
                    }
                }
            }
        }


        public boolean putShipOnField(int _rowBegin, int _columnBegin, int _rowEnd, int _columnEnd, Ship _ship) {
            //for each ships
            for (Ship ship : ships) {
                //if the ship being compared is not an installable ship and the ship isn't on the field yet
                if (ship != _ship && ship.isPlaced()) {
                    //find out if there are any coordinates of other ships near the one being placed
                    for (int i = _rowBegin - 1; i <= _rowEnd + 1; i++) {
                        for (int j = _columnBegin - 1; j <= _columnEnd + 1; j++) {
                            if ((i == ship.getRowBegin() && j == ship.getColumnBegin()) ||
                                    (i == ship.getRowEnd() && j == ship.getColumnEnd())) {
                                System.out.println("Error! You placed it too close to another one. Try again:");
                                return false;
                            }
                        }
                    }
                }
            }

            //put the ship symbols in the game field according to its coordinates
            if (_rowBegin == _rowEnd) {
                for (int i = _columnBegin; i <= _columnEnd; i++) {
                    this.filed[_rowBegin][i - 1] = _ship.getCells()[i - _columnBegin];
                }
            } else {
                for (int i = _rowBegin; i <= _rowEnd; i++) {
                    this.filed[i][_columnBegin - 1] = _ship.getCells()[i - _rowBegin];
                }
            }
            return true;
        }

        /**
         * Simplified method of shooting. For now just to pass the test.
         */
        public void makeShot() {
            System.out.println("The game starts!\n");
            System.out.println(this.toString());
            System.out.println("Take a shot!");
            while (true) {
                String shotCell = scanner.nextLine();
                int shotRow = shotCell.charAt(0) - 65;
                int shotColumn = Integer.parseInt(shotCell.substring(1)) - 1;
                if (shotRow < 0 || shotRow > 9 || shotColumn < 0 || shotColumn > 9) {
                    System.out.println("Error! You entered the wrong coordinates! Try again:");
                } else {
                    if (this.filed[shotRow][shotColumn] == EMPTY) {
                        this.filed[shotRow][shotColumn] = 'M';
                        System.out.println(this.toString());
                        System.out.println("You missed!");
                    } else {
                        this.filed[shotRow][shotColumn] = 'X';
                        System.out.println(this.toString());
                        System.out.println("You hit a ship!");
                    }
                    break;
                }
            }
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder("  1 2 3 4 5 6 7 8 9 10\n");
            for (int i = 0; i < SIZE; i++) {
                result.append(Character.toChars(65 + i));
                for (int j = 0; j < SIZE; j++) {
                    result.append(" ").append(filed[i][j]);
                }
                result.append("\n");
            }
            return String.valueOf(result);
        }
    }

    public class Game {

        public void start() {
            Battlefield battlefield = new Battlefield();
            battlefield.initfield();
            battlefield.makeShot();
        }
    }

    public class Ship {
        final char SHIP_SYMBOL = 'O';
        int size;
        String name;
        char[] cells;
        boolean isPlaced = false;
        int rowBegin;
        int rowEnd;
        int columnBegin;
        int columnEnd;


        public Ship(int _size, String _name) {
            this.size = _size;
            this.name = _name;
            this.cells = new char[_size];
            Arrays.fill(this.cells, SHIP_SYMBOL);
        }

        public String getName() {
            return this.name;
        }

        public int getSize() {
            return this.size;
        }


        public boolean setCoordinates(int _rowBegin, int _columnBegin, int _rowEnd, int _columnEnd) {
            if(_rowBegin == _rowEnd || _columnBegin == _columnEnd) {
                if(_rowEnd - _rowBegin != this.size - 1 && _columnEnd - _columnBegin != this.size - 1) {
                    System.out.printf("Error! Wrong length of the %s! Try again:\n", this.name);
                    return false;
                }
            } else {
                System.out.println("Error! Wrong ship location! Try again:");
                return false;
            }
            this.rowBegin = _rowBegin;
            this.rowEnd = _rowEnd;
            this.columnBegin = _columnBegin;
            this.columnEnd = _columnEnd;
            this.isPlaced = true;

            return true;
        }

        public char[] getCells() {
            return cells;
        }

        public int getRowBegin() {
            return rowBegin;
        }

        public int getRowEnd() {
            return rowEnd;
        }

        public int getColumnBegin() {
            return columnBegin;
        }

        public int getColumnEnd() {
            return columnEnd;
        }

        public boolean isPlaced(){
            return isPlaced;
        }
    }



}
