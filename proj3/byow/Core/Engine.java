package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;


public class Engine {
    /* Feel free to change the width and height. */
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;
    private Point locOfAvatar;
    private TETile[][] world;
    private List<Point> doorLocations;
    private String command;
    private boolean day = false;
    private String player;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        menu();
        while (true) {
            char c = getNextKey();
            if (c == 'N' || c == 'n') {
                command = "";
                command += c;
                Random rand = getSeed();
                player = getName();
                command = command + player + ' ';
                buildWorld(rand);
                break;
            }
            if (c == 'Q' || c == 'q') {
                System.exit(1);
            }
            if (c == 'L' || c == 'l') {
//                Engine e = new Engine();
                if (loadGame() == null) {
                    System.exit(1);
                }
                world = this.interactWithInputString(loadGame());
                command = loadGame();
                doorLocations = new ArrayList<>();
                for (int i = 0; i < world.length; i++) {
                    for (int j = 0; j < world[0].length; j++) {
                        if (world[i][j] == Tileset.LOCKED_DOOR) {
                            doorLocations.add(new Point(i, j));
                        }
                        if (world[i][j] == Tileset.AVATAR) {
                            locOfAvatar = new Point(i, j);
                        }
                    }
                }
                break;
            }
        }




        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world);
        char q;
        boolean quit = false;
        while (!quit) {

            while(!StdDraw.hasNextKeyTyped()) {
                ter.renderFrame(world);
                Point mouse = new Point((int) StdDraw.mouseX(), (int) StdDraw.mouseY());
                if (!outOfBound(mouse.x - 3, mouse.y - 3, 0, 0)) {
                    if (!day) {
                        StdDraw.setPenColor(Color.GREEN);
                        StdDraw.textLeft(1, HEIGHT - 1, "Tile:" +  world[mouse.x][mouse.y].description());
                        StdDraw.setPenColor(Color.yellow);
                        StdDraw.textLeft(11, HEIGHT - 1, "Player Name:" + player);
                        StdDraw.setPenColor(Color.gray);
                        StdDraw.textLeft(24, HEIGHT - 1, "Press B to LIGHT the world!!");
                    } else {
                        StdDraw.setPenColor(Color.PINK);
                        StdDraw.textLeft(1, HEIGHT - 1, "Tile:" +  world[mouse.x][mouse.y].description());
                        StdDraw.setPenColor(Color.CYAN);
                        StdDraw.textLeft(11, HEIGHT - 1, "Player Name:" + player);
                        StdDraw.setPenColor(Color.blue);
                        StdDraw.textLeft(24, HEIGHT - 1, "Press B to DARKEN the world!!");
                    }
                    StdDraw.show();
                }
            }
            q = getNextKey();
            quit = move(q);
            if (reachDoor()) {
                command = command.substring(0, command.length() - 1);
                StdDraw.clear(Color.BLACK);
                StdDraw.setPenColor(Color.YELLOW);
                Font font = new Font("Arial", Font.BOLD, 45);
                StdDraw.setFont(font);
                StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2, player + " YOU WON!!!");
                StdDraw.show();
                StdDraw.pause(10000);
                break;
            }


            ter.renderFrame(world);
        }
        saveGame(command);
        System.exit(1);


    }

    private Random getSeed() {
        StdDraw.clear(Color.black);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2,
                "Please Enter a seed MASTER!");
        String s = "seed:";
        StdDraw.textLeft((double) WIDTH / 2, (double) HEIGHT / 2 - 1, s);
        String seed = "";
        char cur = 'a';
        boolean start = false;
        while (!start) {
            cur = getNextKey();
            if (Character.isDigit(cur)) {
                start = true;
            }
        }
        while (cur != 's' && cur != 'S') {
            if (Character.isDigit(cur)) {
                seed += cur;
                s = s + cur;
                StdDraw.clear(Color.black);
                StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2,
                        "Please Enter a seed MASTER!");
                StdDraw.textLeft((double) WIDTH / 2, (double) HEIGHT / 2 - 1, s);
                StdDraw.show();
            }
            cur = getNextKey();
        }
        command += seed + 's';
        long seeD = Long.parseLong(seed);
        return new Random(seeD);

    }

    private String getName() {
        StdDraw.clear(Color.black);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2,
                "Please Enter a name MASTER!");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 1,
                "Finish it with a space");
        String s = "name:";
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 2, s);
        String name = "";
        char cur = getNextKey();
        while (cur != ' ') {
            name += cur;
            s = s + cur;
            StdDraw.clear(Color.black);
            StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2,
                    "Please Enter a name MASTER!");
            StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 1,
                    "Finish it with a space");
            StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 2, s);
            StdDraw.show();
            cur = getNextKey();
        }
        return name;

    }



    private boolean reachDoor() {
        return doorLocations.contains(locOfAvatar);
    }


    private boolean hitWall(Point p) {
        return world[p.x][p.y] == Tileset.WALL;
    }

    private char getNextKey() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                System.out.println(c);
                return c;
            }
        }
    }




    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        String seed = "";
        int index = 0;
        boolean start = false;
        input = input.toUpperCase();
        while (index < input.length()) {
            if (start) {
                if (Character.isDigit(input.charAt(index))) {
                    seed = seed + input.charAt(index);
                } else if (input.charAt(index) == 'S') {
                    index++;
                    break;
                }

            }
            if (input.charAt(index) == 'N') {
                start = true;
            }
            if (input.charAt(index) == 'L') {
                Engine e = new Engine();
                return e.interactWithInputString(loadGame() + input.substring(index + 1));
            }

            index++;
        }
        if (index == input.length() && seed.length() == 0) {
            return null;
        }
        long seeD = Long.parseLong(seed);
        Random rand = new Random(seeD);
        String name = "";
        while(index < input.length() && input.charAt(index) != ' ') {
            name += input.charAt(index);
            index++;
        }
        player = name;
        buildWorld(rand);
        boolean ready = false;
        boolean saved = false;
        while (index < input.length()) {
            if (!ready) {
                if (input.charAt(index) == ':') {
                    ready = true;
                } else {
                    move(input.charAt(index));
                }
            } else {
                if (input.charAt(index) == 'Q') {
                    saveGame(input.substring(0, index - 1));
                    saved = true;
                    break;
                }
            }
            index++;
        }
        if (!saved) {
            saveGame(input);
        }
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world);

        return world;


    }

    private void menu() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.clear(Color.black);
        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.setPenColor(Color.YELLOW);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 + 5, "Welcome to MyWorld");
        font = new Font("Arial", Font.BOLD, 15);
        StdDraw.setFont(font);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 + 1, "New Game(N)");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2, "Load GaMme(L)");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 1, "Quit(Q)");
    }

    private static String loadGame() {
        File f = new File("./Game_data.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                return (String) os.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }

        /* In the case no Editor has been saved yet, we return a new one. */
        return null;
    }

    private static void saveGame(String s) {
        File f = new File("./Game_data.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(s);
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private boolean move(char c) {
        Point cur;
        boolean quit = false;
        switch (c) {
            case 'A':
            case 'a':
                cur = new Point(locOfAvatar.x - 1, locOfAvatar.y);
                if (!hitWall(cur)) {
                    if (!day) {
                        world[locOfAvatar.x][locOfAvatar.y] = Tileset.FLOOR;
                    } else {
                        world[locOfAvatar.x][locOfAvatar.y] = Tileset.FLOWER;
                    }
                    world[cur.x][cur.y] = Tileset.AVATAR;
                    locOfAvatar = cur;
                    command += c;
                }
                break;
            case 'D':
            case 'd':
                cur = new Point(locOfAvatar.x + 1, locOfAvatar.y);
                if (!hitWall(cur)) {
                    if (!day) {
                        world[locOfAvatar.x][locOfAvatar.y] = Tileset.FLOOR;
                    } else {
                        world[locOfAvatar.x][locOfAvatar.y] = Tileset.FLOWER;
                    }
                    world[cur.x][cur.y] = Tileset.AVATAR;
                    locOfAvatar = cur;
                    command += c;
                }
                break;
            case 's':
            case 'S':
                cur = new Point(locOfAvatar.x, locOfAvatar.y - 1);
                if (!hitWall(cur)) {
                    if (!day) {
                        world[locOfAvatar.x][locOfAvatar.y] = Tileset.FLOOR;
                    } else {
                        world[locOfAvatar.x][locOfAvatar.y] = Tileset.FLOWER;
                    }
                    world[cur.x][cur.y] = Tileset.AVATAR;
                    locOfAvatar = cur;
                    command += c;
                }
                break;
            case 'w':
            case 'W':
                cur = new Point(locOfAvatar.x, locOfAvatar.y + 1);
                if (!hitWall(cur)) {
                    if (!day) {
                        world[locOfAvatar.x][locOfAvatar.y] = Tileset.FLOOR;
                    } else {
                        world[locOfAvatar.x][locOfAvatar.y] = Tileset.FLOWER;
                    }
                    world[cur.x][cur.y] = Tileset.AVATAR;
                    locOfAvatar = cur;
                    command += c;
                }
                break;
            case 'b':
            case 'B':
                command += c;
                if (!day) {
                    for (int i = 0; i < world.length; i++) {
                        for (int j = 0; j < world[0].length; j++) {
                            if (world[i][j] == Tileset.NOTHING) {
                                world[i][j] = Tileset.NOTHING1;
                            }
                            if (world[i][j] == Tileset.FLOOR) {
                                world[i][j] = Tileset.FLOWER;
                            }

                        }
                    }
                    day = true;
                } else if (day) {
                    for (int i = 0; i < world.length; i++) {
                        for (int j = 0; j < world[0].length; j++) {
                            if (world[i][j] == Tileset.NOTHING1) {
                                world[i][j] = Tileset.NOTHING;
                            }
                            if (world[i][j] == Tileset.FLOWER) {
                                world[i][j] = Tileset.FLOOR;
                            }

                        }
                    }
                    day = false;
                }


            case ':':
                c = getNextKey();
                if (c == 'Q' || c == 'q') {
                    quit = true;
                }
                break;
            default:
                break;
        }
        return quit;

    }

    private void buildWorld(Random rand) {
        world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        int numOfRoom = 15;
        int i = 0;
        Room lastRoom, currentRoom;
        lastRoom = null;
        List<Room> rooms = new ArrayList<>();

        while (i < numOfRoom) {
            int x = rand.nextInt(WIDTH);
            int y = rand.nextInt(HEIGHT);
            int length = rand.nextInt(3) + 4;
            int width = rand.nextInt(3) + 4;
            if (!outOfBound(x, y, width, length)) {
                if (i == 0) {
                    lastRoom = buildRoom(x, y, width, length);
                    rooms.add(lastRoom);
                    i++;
                } else {
                    currentRoom = buildRoom(x, y, width, length);
                    rooms.add(currentRoom);
                    connectRoom(lastRoom, currentRoom, rand);
                    lastRoom = currentRoom;
                    i++;

                }

            }

        }
        int roomToPlace = rand.nextInt(numOfRoom);
        int placeDoor = rand.nextInt(5) + 1;
        locOfAvatar = placeAvatar(rooms.get(roomToPlace), rand);
        world[locOfAvatar.x][locOfAvatar.y] = Tileset.AVATAR;
        int k = 0;
        int j;
        List<Point> points;
        doorLocations = new ArrayList<>();
        while (k < placeDoor) {
            j = rand.nextInt(numOfRoom);
            points = rooms.get(j).boarderPoints();
            j = rand.nextInt(points.size());
            Point p = points.get(j);
            if (isWall(world[p.x][p.y])) {
                doorLocations.add(p);
                world[p.x][p.y] = Tileset.LOCKED_DOOR;
                k++;
            }
        }


    }


    private Point placeAvatar(Room room, Random rand) {
        int x = rand.nextInt(room.width) + room.p.x + 1;
        int y = rand.nextInt(room.height) + room.p.y + 1;
        return new Point(x, y);
    }


    private Room buildRoom(int x, int y, int width, int height) {
        for (int i = 0; i < width + 2; i++) {
            for (int j = 0; j < height + 2; j++) {
                if (j == 0 || j == height + 1 || i == 0 || i == width + 1) {
                    if (!isFloor(world[x + i][y + j])) {
                        world[x + i][y + j] = Tileset.WALL;
                    }
                } else {
                    if (!world[x + i][y + j].equals(Tileset.AVATAR)) {
                        world[x + i][y + j] = Tileset.FLOOR;
                    }
                }
            }

        }
        return new Room(new Point(x, y), width, height);

    }

    private boolean isFloor(TETile tile) {
        return tile.equals(Tileset.FLOOR) || tile.equals(Tileset.AVATAR);
    }

    private boolean isWall(TETile tile) {
        return tile.equals(Tileset.WALL);
    }

    private void makeHallwayHorizon(int x, int y, int length) {
        for (int i = 0; i <= length; i++) {
            if (!isFloor(world[x + i][y + 1])) {
                world[x + i][y + 1] = Tileset.WALL;
            }
            if (!isFloor(world[x + i][y - 1])) {
                world[x + i][y - 1] = Tileset.WALL;
            }
            if (!isFloor(world[x + i][y])) {
                world[x + i][y] = Tileset.FLOOR;
            }

        }


    }

    private void makeHallwayVertical(int x, int y, int length) {
        for (int i = 0; i <= length; i++) {
            if (!isFloor(world[x + 1][y + i])) {
                world[x + 1][y + i] = Tileset.WALL;
            }
            if (!isFloor(world[x - 1][y + i])) {
                world[x - 1][y + i] = Tileset.WALL;
            }
            if (!isFloor(world[x][y + i])) {
                world[x][y + i] = Tileset.FLOOR;
            }
        }
    }

    private void connectRoom(Room R1, Room R2, Random rand) {
        //get a random point from room 1
        Point p1 = R1.pointsToBeExtended().get(rand.nextInt(R1.pointsToBeExtended().size()));
        Point p2 = R2.pointsToBeExtended().get(rand.nextInt(R2.pointsToBeExtended().size()));
        if (p1.x < p2.x) {
            makeHallwayHorizon(p1.x, p1.y, p2.x - p1.x);
            if (p1.y < p2.y) {
                makeHallwayVertical(p2.x, p1.y, p2.y - p1.y);
            } else {
                makeHallwayVertical(p2.x, p2.y, p1.y - p2.y);
            }
        } else {
            makeHallwayHorizon(p2.x, p2.y, p1.x - p2.x);
            if (p1.y < p2.y) {
                makeHallwayVertical(p1.x, p1.y, p2.y - p1.y);
            } else {
                makeHallwayVertical(p1.x, p2.y, p1.y - p2.y);
            }
        }

    }

    private boolean outOfBound(int currentX, int currentY, int width, int height) {
        return currentX >= WIDTH || currentX + width + 1 >= WIDTH
                || currentY >= HEIGHT - 2 || currentY + height + 1 >= HEIGHT - 2;
    }


    private class Room {
        Point p;
        int height;
        int width;


        private Room(Point p, int width, int height) {
            this.p = p;
            this.height = height;
            this.width = width;

        }

        private List<Point> pointsToBeExtended() {
            List<Point> list = new LinkedList<>();
            for (int i = 1; i <= width; i++) {
                for (int j = 1; j <= height; j++) {
                    list.add(new Point(p.x + i, p.y + j));
                }
            }

            return list;
        }

        private List<Point> boarderPoints() {
            List<Point> list = new LinkedList<>();
            for (int i = 0; i <= width + 1; i++) {
                for (int j = 0; j <= height + 1; j++) {
                    if (((i == 0 || i == width + 1) && (j > 0 && j < height + 1))
                            || ((j == 0 || j == height + 1) && (i > 0 && i < width + 1))) {
                        list.add(new Point(p.x + i, p.y + j));
                    }
                }
            }
            return list;
        }

        private boolean isAttached(int x, int y) {
            return x == 0 || x == WIDTH - 1 || y == 0 || y == HEIGHT - 1;
        }
    }


}

