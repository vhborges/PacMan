package control;

import elements.Ball;
import elements.PowerPellet;
import elements.Blinky;
import elements.Cherry;
import elements.Clyde;
import elements.Element;
import elements.Enemy;
import elements.Fruit;
import elements.Inky;
import elements.PacMan;
import elements.Pinky;
import elements.Strawberry;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.Math;
import scene.Scene;
import utils.Consts;
import utils.Position;

public class GameController {

    private int mult = 0;

    // Desenhar todos os elementos do jogo
    public void drawAllElements(Scene scene, ArrayList<Element> elemArray, Graphics g, int control) {
        // Desenha cenario e bolinhas
        scene.paintScene(g);

        // Desenha outros elementos
        if (control != 0 && control != 4) {
            Iterator<Element> it = elemArray.listIterator();
            while (it.hasNext()) {
                it.next().autoDraw(g);
            }
        }
    }

    public boolean processAllElements(Scene scene, ArrayList<Element> e, ArrayList<Enemy> enemys) {
        if (e.isEmpty()) {
            return false;
        }

        // Pacmen
        PacMan pPacMan = (PacMan) e.get(0);

        // Inimigos
        Blinky blinky = (Blinky) enemys.get(0);
        Inky inky = (Inky) enemys.get(1);
        Pinky pinky = (Pinky) enemys.get(2);
        Clyde clyde = (Clyde) enemys.get(3);

        // Verifica colisao entre pacman e o cenario
        if (pPacMan.overlap(scene.getWalls())) {
            pPacMan.backToLastPosition();
            pPacMan.setMovBefDirection(pPacMan.getMovDirection());
            pPacMan.setMovDirection(PacMan.STOP);
            return false;
        }

        // Verifica colisão entre blinky e cenario
        if (blinky.overlap(scene.getWalls())) {

            // Setar movimento do blinky quando ocorre uma colisão
            blinky.backToLastPosition();
            setInvtMovDirectionBlinky(blinky, pPacMan);
        }

        // Verifica colisão entre pinky e cenario
        if (pinky.overlap(scene.getWalls())) {

            // Setar movimento do pinky quando ocorre uma colisão
            pinky.backToLastPosition();
            setInvtMovDirectionPinky(pinky);

            // Se houve colisão, entao pinky passa para estado aleatorio
            pinky.setStateDirection(Pinky.MOVE_ALEAT);
        }

        // Verifica colisão entre inky e cenario
        if (inky.overlap(scene.getWalls())) {

            // Setar movimento do pinky quando ocorre uma colisão
            inky.backToLastPosition();
            setInvtMovDirectionInky(inky);
        }

        if (!isValidPositionScene(inky, scene)) {
            inky.backToLastPosition();
            setInkyMovDirection(inky);
        }

        // Verifica colisao entre as bolinhas e pacman
        Iterator<Ball> it = scene.getBalls().listIterator();
        while (it.hasNext()) {
            if (pPacMan.overlapBall(it.next())) {
                it.remove();
                pPacMan.scorePoints(500);
                break;
            }
        }

        Iterator<PowerPellet> it2 = scene.getPowerPellet().listIterator();
        while (it2.hasNext()) {
            if (pPacMan.overlapBall(it2.next())) {
                mult = 1;
                pPacMan.scorePoints(50);
                it2.remove();
                blinky.setState(2); //coloca em vulnerável
                inky.setState(2);
                pinky.setState(2);
                clyde.setState(2);
                TimerTask vulnerable = new TimerTask(){
                    @Override
                    public void run(){ //coloca em mortal de novo
                        if(blinky.getState() == 2)
                            blinky.setState(1);
                        if(inky.getState() == 2)
                            inky.setState(1);
                        if(pinky.getState() == 2)
                            pinky.setState(1);
                        if(clyde.getState() == 2)
                            clyde.setState(1);
                    }
                };
                Timer timer = new Timer();
                timer.schedule(vulnerable, 7000);
                break;
            }
        }

        Iterator<PowerPellet> it2 = scene.getPowerPellet().listIterator();
        while (it2.hasNext()) {
            if (pPacMan.overlapBall(it2.next())) {
                it2.remove();
                blinky.setState(2); //coloca em vulnerável
                inky.setState(2);
                pinky.setState(2);
                clyde.setState(2);
                TimerTask vulnerable = new TimerTask() {
                    @Override
                    public void run() { //coloca em mortal de novo
                        if (blinky.getState() == 2) {
                            blinky.setState(1);
                        }
                        if (inky.getState() == 2) {
                            inky.setState(1);
                        }
                        if (pinky.getState() == 2) {
                            pinky.setState(1);
                        }
                        if (clyde.getState() == 2) {
                            clyde.setState(1);
                        }
                    }
                };
                Timer timer = new Timer();
                timer.schedule(vulnerable, 7000);
                break;
            }
        }

        // Variavel que detecta que se houve uma colisão entre pacman e inimigo
        boolean aux = false;

        Element eTemp;

        // Verifica colisao entre PacMan e outros elementos
        for (int i = 1; i < e.size(); i++) {
            eTemp = e.get(i);
            if (!eTemp.isTransposable() && pPacMan.overlap(eTemp)) {

                // Se elemento for um inimigo
                if (eTemp instanceof Enemy) {
                    switch (((Enemy) eTemp).getState()) {
                        case 1:
                            aux = true;
                            pPacMan.backToLastPosition();
                            pPacMan.setMovDirection(PacMan.STOP);
                            break;
                        case 2:
                            pPacMan.scorePoints(200*mult);
                            if(eTemp instanceof Blinky)
                                blinky.setState(3);
                            if(eTemp instanceof Inky)
                                inky.setState(3);
                            if(eTemp instanceof Pinky)
                                pinky.setState(3);
                            if(eTemp instanceof Clyde)
                                clyde.setState(3);
                            mult *= 2;
                            break;
                        default:
                            break;
                    }
                }
                else if(eTemp instanceof Fruit){ //verifica se o elemento é fruta e dá os pontos
                    if(eTemp instanceof Cherry)
                        pPacMan.scorePoints(100);
                    if(eTemp instanceof Strawberry)
                        pPacMan.scorePoints(300);
                    e.remove(eTemp);
                }
                else{
                    e.remove(eTemp);
                }
            }
        }
        if(pPacMan.getScore() >= 10000){
            pPacMan.resetScore();
            pPacMan.addLife();
        }

        //Print Teste
        System.out.println(pPacMan.getScore());
        // Movimenta o pacman
        pPacMan.move();

        // Movimentar inimigos
        blinky.move();
        pinky.move();
        inky.move();

        return aux;
    }

    // Setar movimento do blinky
    private void setInvtMovDirectionBlinky(Enemy enemy, PacMan pPacMan) {
        // Definir uma nova direção para o blinky
        switch (enemy.getMovDirection()) {
            case Enemy.MOVE_LEFT:
                if (pPacMan.getPos().getX() > enemy.getPos().getX()) {
                    enemy.setMoveDirection(Enemy.MOVE_DOWN);
                } else {
                    enemy.setMoveDirection(Enemy.MOVE_UP);
                }
                break;

            case Enemy.MOVE_RIGHT:
                if (pPacMan.getPos().getX() > enemy.getPos().getX()) {
                    enemy.setMoveDirection(Enemy.MOVE_DOWN);
                } else {
                    enemy.setMoveDirection(Enemy.MOVE_UP);
                }
                break;

            case Enemy.MOVE_DOWN:
                if (pPacMan.getPos().getY() > enemy.getPos().getY()) {
                    enemy.setMoveDirection(Enemy.MOVE_RIGHT);
                } else {
                    enemy.setMoveDirection(Enemy.MOVE_LEFT);
                }
                break;

            case Enemy.MOVE_UP:
                if (pPacMan.getPos().getY() > enemy.getPos().getY()) {
                    enemy.setMoveDirection(Enemy.MOVE_RIGHT);
                } else {
                    enemy.setMoveDirection(Enemy.MOVE_LEFT);
                }
                break;
        }
    }

    // Setar movimento do pinky
    private void setInvtMovDirectionPinky(Enemy enemy) {

        int aux = Math.round((float) Math.random() % 10);

        // Definir uma nova direção para o pinky
        switch (enemy.getMovDirection()) {
            case Enemy.MOVE_LEFT:
                if (pPacMan.getPos().getX() > enemy.getPos().getX()) {
                    enemy.setMoveDirection(Enemy.MOVE_DOWN);
                } else {
                    enemy.setMoveDirection(Enemy.MOVE_UP);
                }
                break;

            case Enemy.MOVE_RIGHT:
                if (pPacMan.getPos().getX() > enemy.getPos().getX()) {
                    enemy.setMoveDirection(Enemy.MOVE_DOWN);
                } else {
                    enemy.setMoveDirection(Enemy.MOVE_UP);
                }
                break;

            case Enemy.MOVE_DOWN:
                if (pPacMan.getPos().getY() > enemy.getPos().getY()) {
                    enemy.setMoveDirection(Enemy.MOVE_RIGHT);
                } else {
                    enemy.setMoveDirection(Enemy.MOVE_LEFT);
                }
                break;

            case Enemy.MOVE_UP:
                if (pPacMan.getPos().getY() > enemy.getPos().getY()) {
                    enemy.setMoveDirection(Enemy.MOVE_RIGHT);
                } else {
                    enemy.setMoveDirection(Enemy.MOVE_LEFT);
                }
                break;

            default:
                break;
        }
    }

    private void setInkyMovDirection(Inky inky){

        double rand = (Math.random()*10)%2;

        switch(inky.getMovDirection()){
            case Inky.MOVE_LEFT:
                if(rand == 0)
                    inky.setMoveDirection(Inky.MOVE_UP);
                else
                    inky.setMoveDirection(Inky.MOVE_DOWN);
                break;

            case Inky.MOVE_RIGHT:
                if(rand == 0)
                    inky.setMoveDirection(Inky.MOVE_UP);
                else
                    inky.setMoveDirection(Inky.MOVE_DOWN);
                break;

            case Inky.MOVE_UP:
                if(rand == 0)
                    inky.setMoveDirection(Inky.MOVE_LEFT);
                else
                    inky.setMoveDirection(Inky.MOVE_RIGHT);
                break;

            case Inky.MOVE_DOWN:
                if(rand == 0)
                    inky.setMoveDirection(Inky.MOVE_LEFT);
                else
                    inky.setMoveDirection(Inky.MOVE_RIGHT);
                break;
        }
    }
}
