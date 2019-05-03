package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 70;
    private static final int HEIGHT = 50;
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    private static void addHexagon(int s, int middleX, int middleY, TETile[][] world, TETile type){

        for (int i = s, j = 0; i > 0; i--, j++){
            int x = middleX + j;
            int End = s + (i - 1) * 2;
            for (int start = 0; start < End; start++) {
                world[x + start][middleY + j] = type;
                world[x + start][middleY - 1 - j] = type;
            }
        }
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(3);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.MOUNTAIN;
            default: return Tileset.FLOWER;
        }
    }

    private static void drawHexVertically(int x, int y, int num, int side, TETile[][] world) {

        for (int i = 0; i < num; i++) {
            addHexagon(side, x, y - i * side * 2,  world, randomTile());
        }
    }

    private static void draw19Hex(int middleX, int middleY, int side, int num, TETile[][] world) {
        int xDis = side * 2 - 1;
        drawHexVertically(middleX, middleY, num, side,  world);
        drawHexVertically(middleX - xDis, middleY - side, num - 1, side, world);
        drawHexVertically(middleX + xDis, middleY - side, num - 1, side, world);
        drawHexVertically(middleX - 2 * xDis, middleY - 2 * side, num - 2, side, world);
        drawHexVertically(middleX + 2 * xDis, middleY - 2 * side, num - 2, side, world);

    }

    public static void main(String[] args) {

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
//        addHexagon(4, 20, 10, world, Tileset.FLOWER);
//        drawHexVertically(30, 40, 3, 3, world);
//        drawHexVertically(20, 40, 4, 3, world);
        draw19Hex(30, 40, 4, 5,  world);
        ter.renderFrame(world);


    }



    }



