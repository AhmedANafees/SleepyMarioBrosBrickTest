
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.Timer;

/**
 *
 * @author nafea8846
 */
public class SleepyMarioBros_BrickTest extends JComponent implements ActionListener {

    // Height and Width of our game
    static final int WIDTH = 800;
    static final int HEIGHT = 600;

    //Title of the window
    String title = "Block Test";

    // sets the framerate and delay for our game
    // this calculates the number of milliseconds per frame
    // you just need to select an approproate framerate
    int desiredFPS = 60;
    int desiredTime = Math.round((1000 / desiredFPS));

    // timer used to run the game loop
    // this is what keeps our time running smoothly :)
    Timer gameTimer;

    // YOUR GAME VARIABLES WOULD GO HERE
    Rectangle[] blocks = new Rectangle[5];
    Rectangle player = new Rectangle(40, 500, 50, 100);

    int moveSpeed = 10;
    boolean right = false;
    boolean left = false;
    boolean crouch = false;
    boolean jump = false;
    int gravity = 1;
    int dy = 0;  // change in the y position
    boolean standing = true; // on the ground or not
    final int JUMP_FORCE = -20;
    int ground = 580;
    boolean reactionTop = false;
    boolean reactionLeft = false;
    boolean reactionRight = false;
    boolean reactionBottom = false;
    int blockSpeed = 5;
    boolean dead = false;
    int timer = desiredFPS/15;
    int blockTime = timer;
    int[] deadBlock = new int[5];
    // GAME VARIABLES END HERE    

    // Constructor to create the Frame and place the panel in
    // You will learn more about this in Grade 12 :)
    public SleepyMarioBros_BrickTest() {
        // creates a windows to show my game
        JFrame frame = new JFrame(title);

        // sets the size of my game
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // adds the game to the window
        frame.add(this);

        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);

        // add listeners for keyboard and mouse
        frame.addKeyListener(new Keyboard());
        Mouse m = new Mouse();
        this.addMouseMotionListener(m);
        this.addMouseWheelListener(m);
        this.addMouseListener(m);

        // Set things up for the game at startup
        setup();

        // Start the game loop
        gameTimer = new Timer(desiredTime, this);
        gameTimer.setRepeats(true);
        gameTimer.start();
    }

    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {
        // always clear the screen first!
        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.fillRect(0, ground, WIDTH, 120);
        // GAME DRAWING GOES HERE
        Graphics2D g2d = (Graphics2D) g;
        if (reactionTop) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.GREEN);
        }
        for (int i = 0; i < 5; i++) {
            g2d.fill(blocks[i]);
        }
        // draw the player
        if (reactionRight || reactionLeft) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLACK);
        }
        g2d.fill(player);
        // GAME DRAWING ENDS HERE
    }

    // This method is used to do any pre-setup you might need to do
    // This is run before the game loop begins!
    public void setup() {
        // Any of your pre setup before the loop starts should go here
        blocks[0] = new Rectangle(100, 400, 60, 60);
        blocks[1] = new Rectangle(160, 400, 60, 60);
        blocks[2] = new Rectangle(220, 400, 60, 60);
        blocks[3] = new Rectangle(280, 400, 60, 60);
        blocks[4] = new Rectangle(340, 400, 60, 60);

    }

    // The main game loop
    // In here is where all the logic for my game will go
    public void loop() {
        if (right) {
            player.x = player.x + moveSpeed;
        } else if (left) {
            player.x = player.x - moveSpeed;
        }

        if (jump && standing) {
            // make it move upwards quickly
            dy = JUMP_FORCE;
            standing = false;
            // no longer standing

        }

        // add in gravity
        dy = dy + gravity;
        player.y = player.y + dy;

        if (player.y + player.height > ground) {
            player.y = ground - player.height;
            standing = true;
        }
    }

    public void collisionControl(Rectangle player, Rectangle block) {
        Rectangle collide = player.intersection(block);
        // fix the smaller of height or width
        if (collide.height < collide.width) {
            // fix up or down
            // on top
            if (player.y < block.y) {
                standing = true;
                dy = 0;
                // move player up on top of block
                player.y = player.y - collide.height;
                reactionTop = true;

            } else {
                dy = 0;
                // move player down under the block
                player.y = player.y + collide.height;
                reactionBottom = true;
            }
        } else {
            // fix left or right
            // left
            if (player.x < block.x) {
                // move to the left of the block
                player.x = player.x - collide.width;
                reactionLeft = true;

            } else {
                // move to the right of the block
                player.x = player.x + collide.width;
                reactionRight = true;

            }
        }
    }

    public void blockMovement(Rectangle block) {
        if (reactionBottom) {
            if (block.y == 400) {
                block.y = block.y - 15;
                dead = true;
                reactionBottom = false;
                blockTime = timer;
            }
        }
        if (dead) {
            System.out.println(blockTime);
            if (blockTime == 0) {

                if (block.y != 400) {
                    block.y = block.y + 15;
                    dead = false;

                }
            } else {
                blockTime--;
            }
        }

    }

    // Used to implement any of the Mouse Actions
    private class Mouse extends MouseAdapter {

        // if a mouse button has been pressed down
        @Override
        public void mousePressed(MouseEvent e) {

        }

        // if a mouse button has been released
        @Override
        public void mouseReleased(MouseEvent e) {

        }

        // if the scroll wheel has been moved
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {

        }

        // if the mouse has moved positions
        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    // Used to implements any of the Keyboard Actions
    private class Keyboard extends KeyAdapter {

        // if a key has been pressed down
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP || key == KeyEvent.VK_SPACE) {
                jump = true;
            }
            if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
                crouch = true;
            }
            if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
                right = true;
            }
            if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
                left = true;
            }
        }

        // if a key has been released
        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP || key == KeyEvent.VK_SPACE) {
                jump = false;
            }
            if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
                crouch = false;
            }
            if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
                right = false;
            }
            if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
                left = false;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        loop();
        repaint();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creates an instance of my game
        SleepyMarioBros_BrickTest game = new SleepyMarioBros_BrickTest();
    }
}
