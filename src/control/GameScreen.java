package control;

import elements.Skull;
import elements.PacMan;
import elements.Brick;
import elements.Element;
import elements.Cherry;
import elements.Strawberry;
import utils.Consts;
import utils.Drawing;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Projeto de POO 2017
 *
 * @author Luiz Eduardo
 * Baseado em material do Prof. Jose Fernando Junior
 */
public class GameScreen extends javax.swing.JFrame implements KeyListener {

    private final PacMan pacMan;
    private final Strawberry strawberry;
    private final Cherry cherry;
    private final ArrayList<Element> elemArray;
    private final GameController controller = new GameController();

    public GameScreen() {
        Drawing.setGameScreen(this);
        initComponents();

        this.addKeyListener(this);   /*teclado*/

        /*Cria a janela do tamanho do tabuleiro + insets (bordas) da janela*/
        this.setSize(Consts.NUM_CELLS * Consts.CELL_SIZE + getInsets().left + getInsets().right,
                     Consts.NUM_CELLS * Consts.CELL_SIZE + getInsets().top + getInsets().bottom);

        elemArray = new ArrayList<Element>();

        /*Cria e adiciona elementos*/
        pacMan = new PacMan();
        pacMan.setPosition(0, 0);
        this.addElement(pacMan);

        strawberry = new Strawberry();
        double x = Math.random()*Consts.NUM_CELLS;
        double y = Math.random()*Consts.NUM_CELLS;
        strawberry.setPosition(x, y);
        this.addElement(strawberry);

        cherry = new Cherry();
        x = Math.random()*Consts.NUM_CELLS;
        y = Math.random()*Consts.NUM_CELLS;
        cherry.setPosition(x, y);
        this.addElement(cherry);
    }

    public final void addElement(Element elem) {
        elemArray.add(elem);
    }

    public void removeElement(Element elem) {
        elemArray.remove(elem);
    }

    @Override
    public void paint(Graphics gOld) {
        Graphics g = getBufferStrategy().getDrawGraphics();

        /*Criamos um contexto grafico*/
        Graphics g2 = g.create(getInsets().right, getInsets().top, getWidth() - getInsets().left, getHeight() - getInsets().bottom);

        /* DESENHA CENARIO
           Trocar essa parte por uma estrutura mais bem organizada
           Utilizando a classe Stage
        */
        for (int i = 0; i < Consts.NUM_CELLS; i++) {
            for (int j = 0; j < Consts.NUM_CELLS; j++) {
                try {
                    Image newImage = Toolkit.getDefaultToolkit().getImage(new java.io.File(".").getCanonicalPath() + Consts.PATH + "fundo.png");
                    g2.drawImage(newImage,
                            j * Consts.CELL_SIZE, i * Consts.CELL_SIZE, Consts.CELL_SIZE, Consts.CELL_SIZE, null);

                } catch (IOException ex) {
                    Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        this.controller.drawAllElements(elemArray, g2);
        this.controller.processAllElements(elemArray);
        this.setTitle("-> Cell: " + pacMan.getStringPosition());

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
    }

    public void go() {
        TimerTask repaint = new TimerTask(){
            public void run() {
                repaint();
            }
        };
        TimerTask eraseStrawberry = new TimerTask(){
            public void run(){
                elemArray.remove(strawberry);
            }
        };
        TimerTask eraseCherry = new TimerTask(){
            public void run(){
                elemArray.remove(cherry);
            }
        };
        Timer timer = new Timer();
        timer.schedule(repaint, 0, Consts.DELAY);
        timer.schedule(eraseStrawberry, Consts.TIMER_STRAWBERRY);
        timer.schedule(eraseCherry, Consts.TIMER_CHERRY);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacMan.setMovDirection(PacMan.MOVE_UP);
            pacMan.changeDirection(3);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacMan.setMovDirection(PacMan.MOVE_DOWN);
            pacMan.changeDirection(1);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacMan.setMovDirection(PacMan.MOVE_LEFT);
            pacMan.changeDirection(2);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacMan.setMovDirection(PacMan.MOVE_RIGHT);
            pacMan.changeDirection(0);
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            pacMan.setMovDirection(PacMan.STOP);
        }

        //repaint(); /*invoca o paint imediatamente, sem aguardar o refresh*/
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SCC0604 - Pacman");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocation(new java.awt.Point(20, 20));
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
