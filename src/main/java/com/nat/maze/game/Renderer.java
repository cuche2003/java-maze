package com.nat.maze.game;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.nat.maze.game.entity.Entity;
import com.nat.maze.game.entity.Player;
import com.nat.maze.math.Vector2;

import java.io.IOException;

public final class Renderer {
    private final Screen screen;
    private final TextGraphics textGraphics;
    private final GameState gameState;
    public Renderer(GameState gs, Screen s) {
        gameState = gs;
        screen = s;
        textGraphics = screen.newTextGraphics();
    }

    private void renderFloor() {
        final Player p = gameState.player;

        TerminalSize size = screen.getTerminalSize();

        for (int x = 0; x < size.getColumns(); x++) {
            //floor casting
            for (int y = 0; y < size.getRows(); ++y) {
                // whether this section is floor or ceiling
                int floor_high = (int) (size.getRows() / 2 + p.pitch);
                boolean is_floor = y > floor_high;

                char shade;

                if (is_floor) {
                    // floor
                    double b = (double) (size.getRows() - y) / (size.getRows() - floor_high);
                    if (b < 0.25) shade = '#';
                    else if (b < 0.5) shade = 'x';
                    else if (b < 0.75) shade = '-';
                    else if (b < 0.9) shade = '.';
                    else shade = ' ';
                    textGraphics.setCharacter(x, y, shade);
                }

            }
        }
    }

    public void render(double delta) throws IOException {
        TerminalSize size = screen.getTerminalSize();

        final Map m = gameState.map;
        final Player p = gameState.player;

        final Vector2 pos = p.position;
        final Vector2 dir = p.direction;
        final Vector2 pln = p.plane;

        TerminalSize newSize = screen.doResizeIfNecessary();
        if (newSize != null) size = newSize;

        renderFloor();

        for (int x = 0; x < size.getColumns(); x++) {
            //calculate ray position and direction
            double cameraX = 2 * x / (double) size.getColumns() - 1; //x-coordinate in camera space
            double rayDirX = dir.x + pln.x * cameraX;
            double rayDirY = dir.y + pln.y * cameraX;
            //which box of the map we're in
            int mapX = (int) pos.x;
            int mapY = (int) pos.y;

            //length of ray from current position to next x or y-side
            double sideDistX;
            double sideDistY;

            //length of ray from one x or y-side to next x or y-side
            //these are derived as:
            //deltaDistX = sqrt(1 + (rayDirY * rayDirY) / (rayDirX * rayDirX))
            //deltaDistY = sqrt(1 + (rayDirX * rayDirX) / (rayDirY * rayDirY))
            //which can be simplified to abs(|rayDir| / rayDirX) and abs(|rayDir| / rayDirY)
            //where |rayDir| is the length of the vector (rayDirX, rayDirY). Its length,
            //unlike (dirX, dirY) is not 1, however this does not matter, only the
            //ratio between deltaDistX and deltaDistY matters, due to the way the DDA
            //stepping further below works. So the values can be computed as below.
            // Division through zero is prevented, because Java doesn't implement IEEE 754
            double deltaDistX = (rayDirX == 0) ? 1e30 : Math.abs(1 / rayDirX);
            double deltaDistY = (rayDirY == 0) ? 1e30 : Math.abs(1 / rayDirY);

            double pWallDist;

            //what direction to step in x or y-direction (either +1 or -1)
            int stepX;
            int stepY;

            int hit = 0; //was there a wall hit?
            int side = 0; //was a NS or an EW wall hit?
            //calculate step and initial sideDist
            if (rayDirX < 0) {
                stepX = -1;
                sideDistX = (pos.x - mapX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1.0 - pos.x) * deltaDistX;
            }
            if (rayDirY < 0) {
                stepY = -1;
                sideDistY = (pos.y - mapY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1.0 - pos.y) * deltaDistY;
            }
            //perform DDA
            while (hit == 0) {
                //jump to next map square, either in x-direction, or in y-direction
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }

                //Check if ray has hit a wall
                if (m.getMap()[mapX][mapY].value > 0) hit = 1;
            }
            //Calculate distance projected on camera direction. This is the shortest distance from the point where the wall is
            //hit to the camera plane. Euclidean to center camera point would give fisheye effect!
            //This can be computed as (mapX - posX + (1 - stepX) / 2) / rayDirX for side == 0, or same formula with Y
            //for size == 1, but can be simplified to the code below thanks to how sideDist and deltaDist are computed:
            //because they were left scaled to |rayDir|. sideDist is the entire length of the ray above after the multiple
            //steps, but we subtract deltaDist once because one step more into the wall was taken above.
            if (side == 0) pWallDist = (sideDistX - deltaDistX);
            else pWallDist = (sideDistY - deltaDistY);

            //Calculate height of line to draw on screen
            int lineHeight = (int) (size.getRows() / pWallDist);

            //calculate lowest and highest pixel to fill in current stripe
            int drawStart = (int) (-lineHeight / 2 + p.pitch + size.getRows() / 2);
            if (drawStart < 0) drawStart = 0;
            int drawEnd = (int) (lineHeight / 2 + p.pitch + size.getRows() / 2);
            if (drawEnd >= size.getRows()) drawEnd = size.getRows();

            //choose wall shade
            char shade = ' ';

            if (pWallDist <= p.depth) {
                shade = 0x2591;
            }

            if (pWallDist <= p.depth / 2) {
                shade = 0x2592;
            }

            if (pWallDist <= p.depth / 3) {
                shade = 0x2593;
            }

            if (pWallDist <= p.depth / 4) {
                shade = 0x2588;
            }

            if (m.getMap()[mapX][mapY] == Entity.Type.Flag) {
                shade = '/';
            }

            //clear the ceiling
            textGraphics.drawLine(x, 0, x, drawStart - 1, ' ');

            //draw the pixels of the stripe as a vertical line
            textGraphics.drawLine(x, drawStart, x, drawEnd, shade);

            //clear the floor (not needed because the floor is redrawn above anyways)
            //textGraphics.drawLine(x, drawEnd, x, size.getRows(), ' ');
        }
    }

}
