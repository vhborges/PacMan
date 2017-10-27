/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene;

import elements.Ball;
import java.awt.Graphics;
import java.util.Iterator;
import utils.Consts;

public class Scene1 extends Scene {

    public Scene1(final String imageName) {
        super(imageName);
        this.drawSceneFinal();
    }

    @Override
    public void paintScene(Graphics g) {
        // Desenha cenario
        for (int i = 0; i < Consts.NUM_CELLS; i++) {
            for (int j = 0; j < Consts.NUM_CELLS; j++) {
                if (map[i][j] == 1) {
                    g.drawImage(brick, j * Consts.CELL_SIZE, i * Consts.CELL_SIZE,
                            Consts.CELL_SIZE, Consts.CELL_SIZE, null);
                } else {
                    g.fillRect(j * Consts.CELL_SIZE, i * Consts.CELL_SIZE,
                            Consts.CELL_SIZE, Consts.CELL_SIZE);
                }
            }
        }

        // Desenhar bolinhas da tela
        Iterator<Ball> it = balls.listIterator();
        while (it.hasNext()) {
            it.next().autoDraw(g);
        }
    }

    @Override
    protected void drawSceneFinal() {
        // Terminar de desenhar cenario
        map[3][3] = 1;
        map[4][8] = 1;
        map[10][11] = 1;

        // Criar bolinhas
        map[1][1] = 1;
        for (int x = 0; x < Consts.NUM_CELLS; x++) {
            for (int y = 0; y < Consts.NUM_CELLS; y++) {
                if (map[x][y] == 0) {
                    this.balls.add(new Ball("ball.png", 100, x, y));
                }
            }
        }
        map[1][1] = 0;

    }

}