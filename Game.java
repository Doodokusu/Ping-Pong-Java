import java.awt.*;
import java.awt.event.*;
import java.util.Random;

class Ball {
    public int x; private int orgX;
    public int y; private int orgY;
    public int radius;

    public Ball(int x, int y, int radius) {
        this.x = this.orgX = x;
        this.y = this.orgY = y;
        this.radius = radius;
    }

    public void draw(Graphics p) {
        p.setColor(Color.WHITE);
        p.fillOval(this.x, this.y, this.radius, this.radius);
    }

    public void resetBall() {
        this.x = this.orgX;
        this.y = this.orgY;
    }

}

class Racket {
    public int x; private int orgX;
    public int y; private int orgY;
    public int width; public int height;
    public Racket(int x, int y, int width, int height) {
        this.x = this.orgX = x;
        this.y = this.orgY = y;
        this.width = width;
        this.height = height;
    }

    public void drawRacket(Graphics p) {
        p.setColor(Color.WHITE);
        p.fillRect(this.x, this.y, width, height);
    }

    public void resetRacket() {
        this.x = this.orgX;
        this.y = this.orgY; 
    }
}

class kanvas02 extends Canvas implements KeyListener, MouseListener
{

    private Image offScreenImage;
    private Graphics offScreenGraphics;

    public boolean isMenu = true, isGame = false, isRestart = false, isBeginning = false, isSettings = false;
    private int p1Score = 0, p2Score = 0;
    private int width = 1000, height = 700;
    Racket p1, p2;
    Ball ball;
    Random r1 = new Random(), r2 = new Random();
    int r1I = r1.nextInt(2), r2I = r2.nextInt(2);
    int s1 = (r1I == 0) ? -1 : 1, s2 = (r2I == 0) ? -1 : 1;
    private int ballSpeedX = 8 * s1;
    private int ballSpeedY = 8 * s2;
    private int maxBallSpeed = 12;
    private int orgBallSpeed = ballSpeedX;
    private int racketSpeed = 24;
    private  boolean isWon = false, isGoal = false;
    Color backColor = new Color(30, 30, 30);
    boolean isSinglePlayer = false;
	
	public kanvas02()
	{
		p1 = new Racket(180, height/2-60, 10, 120);
        p2 = new Racket(810, height/2-60, 10, 120);
        ball = new Ball(width/2-10, height/2-10, 20);
		addKeyListener(this);
        addMouseListener(this);
	}

    public void init() {
        offScreenImage = createImage(1000, 700);
        offScreenGraphics = offScreenImage.getGraphics();
    }

    @Override
	public void update(Graphics g) {
		paint(g);
	}

    public void paint(Graphics p) {

        if (isMenu){
            menu();
            p.drawImage(offScreenImage, 0, 0, null);
        }
    
        if (isGame) {
            game();
            p.drawImage(offScreenImage, 0, 0, null);
        }
    
        if (isRestart) {
            ballSpeedX = ballSpeedY = orgBallSpeed;
            isWon = false;
            p1Score = 0;
            p2Score = 0;
            isRestart = false;
            isGame = true;
            setupGameScreen();
            p.drawImage(offScreenImage, 0, 0, null);
        }
        try { Thread.sleep(25); } 
		catch (Exception e) {}
        
		repaint();
    }

    public void menu() {
        Menu.drawMenu(offScreenGraphics, width, height);    
    }

    public void crush() {
        if(ballSpeedX < 0) {
            if((ball.y+ball.radius > p1.y && ball.y < p1.y+p1.height) && (ball.x < p1.x+p1.width && ball.x+20>p1.x)) {
                ballSpeedX *= -1;
                int middleY = p1.y + p1.height/2;
                int differenceY = middleY - ball.y;
                int decreasae = (p1.height/2) / maxBallSpeed;
                int ySpeed = differenceY / decreasae;
                ballSpeedY = -1 * ySpeed;
            }
        }

        if(ballSpeedX > 0) { 
            if((ball.y+ball.radius > p2.y && ball.y < p2.y+p2.height) && (ball.x+ball.radius > p2.x && ball.x < p2.x+p2.width)) {
                ballSpeedX *= -1;
                int middleY = p2.y + p2.height/2;
                int differenceY = middleY - ball.y;
                int decreasae = (p2.height/2) / maxBallSpeed;
                int ySpeed = differenceY / decreasae;
                ballSpeedY = -1 * ySpeed;
            }
        }

        if(ball.y < 0 || ball.y + 60 > height) {
            ballSpeedY *= -1;
        }
    }

    public void restart() {
        ball.resetBall();
        p1.resetRacket();
        p2.resetRacket();
    }

    public void goal() {
        if(ball.x<100) {
            r1 = new Random(); r2 = new Random();
            r1I = r1.nextInt(2); r2I = r2.nextInt(2);
            s1 = (r1I == 0) ? -1 : 1; s2 = (r2I == 0) ? -1 : 1;
            ballSpeedX = 8 * s1;
            ballSpeedY = 8 * s2;
            p2Score++;
            isGoal = true;
        }
        if(ball.x>875) {
            r1 = new Random(); r2 = new Random();
            r1I = r1.nextInt(2); r2I = r2.nextInt(2);
            s1 = (r1I == 0) ? -1 : 1; s2 = (r2I == 0) ? -1 : 1;
            ballSpeedX = 8 * s1;
            ballSpeedY = 8 * s2;
            p1Score++;
            isGoal = true;
        }
    }

    public int won() {
        if(p1Score>2) {
            isWon = true;
            ballSpeedX = ballSpeedY = 0;
            return 1;
        }
        if(p2Score>2) {
            isWon = true;
            ballSpeedX = ballSpeedY = 0;
            return 2;
        }
        return 0;
    }

    boolean p1UpPressed = false;
    boolean p1DownPressed = false;
    boolean p2UpPressed = false;
    boolean p2DownPressed = false;

    public void pressed() {
        if (p1UpPressed && p1.y > 10)                       p1.y -= racketSpeed;
        if (p1DownPressed && p1.y < height-160)             p1.y += racketSpeed;
        if (p2UpPressed && p2.y > 10)                       p2.y -= racketSpeed;
        if (p2DownPressed && p2.y < height-160)             p2.y += racketSpeed;
    }

    public void begin(){
        if(isGoal){
            restart();
            isGoal=false;
            try{
                Thread.sleep(1000);
                }
            catch(Exception e) {}
        }
        isGoal = false;
        isBeginning = false;
    }

    public void backGround(){
        offScreenGraphics.setColor(new Color(0, 25, 0));
        offScreenGraphics.fillRect(0, 0, 1000, 700);
        offScreenGraphics.setColor(new Color(0, 45, 0));
        for(int i=0; i<=7; i+=2){
            offScreenGraphics.fillRect(125*i, 0, 125, 700);
        }
    }

    public void setupGameScreen() {
        backGround();
        offScreenGraphics.setColor(Color.WHITE);
        offScreenGraphics.fillRect(width / 2 - 3, 0, 6, height);
        offScreenGraphics.fillRect(120, 0, 5, 700);
        offScreenGraphics.fillRect(875, 0, 5, 700);
        offScreenGraphics.fillOval(width / 2 - 9, height / 2 - 9, 18, 18);
        offScreenGraphics.drawOval(width / 2 - 112, height / 2 - 112, 224, 224);
        Font f1 = new Font("Arial", Font.PLAIN, 50);
        FontMetrics f1Metrics = offScreenGraphics.getFontMetrics(f1);
        offScreenGraphics.setFont(f1);
        String p1String = Integer.toString(p1Score);
        String p2String = Integer.toString(p2Score);
        offScreenGraphics.drawString(p1String, 50, 60);
        offScreenGraphics.drawString(p2String, 950 - f1Metrics.stringWidth(p2String), 60);
        begin();
    }
    
    public void game() {
        setupGameScreen();
    
        ball.x += ballSpeedX;
        ball.y += ballSpeedY;
        ball.draw(offScreenGraphics);
        p1.drawRacket(offScreenGraphics);
        p2.drawRacket(offScreenGraphics);
        crush();
        goal();
        pressed();

        Font f1 = new Font("Arial", Font.PLAIN, 50);
        FontMetrics f1Metrics = offScreenGraphics.getFontMetrics(f1);

        //offScreenGraphics.setColor(Color.WHITE);

        if (isSinglePlayer) {
            int ballCenterY = ball.y + ball.radius / 2;
            int racketCenterY = p1.y + p1.height / 2;
        
            if (ball.x < width / 2 && ballSpeedX < 0) {
                if (ballCenterY < racketCenterY && p1.y > 10) {
                    p1.y -= racketSpeed;
                } else if (ballCenterY > racketCenterY && p1.y < height - 160) {
                    p1.y += racketSpeed;
                }
            }
        }
        
        int winner = won();
        if (isWon) {
            restart();
            offScreenGraphics.setColor(Color.BLACK);
            offScreenGraphics.setFont(f1);
            if((!isSinglePlayer)){
                offScreenGraphics.drawString("Player "+winner+" Won" , width/2 - f1Metrics.stringWidth("Player "+winner+" Won")/2, height / 2);
            }
            if(isSinglePlayer){
                if(winner == 1) offScreenGraphics.drawString("AI Won" , width/2 - f1Metrics.stringWidth("AI Won")/2, height / 2);
                if(winner == 2) offScreenGraphics.drawString("Player Won" , width/2 - f1Metrics.stringWidth("Player Won")/2, height / 2);
            }
            Font f2 = new Font("Arial", Font.PLAIN, 20);
            FontMetrics f2Metrics = offScreenGraphics.getFontMetrics(f2);
            offScreenGraphics.setFont(f2);
            offScreenGraphics.drawString("Press R to Restart", width/2 - f2Metrics.stringWidth("Press R to Restart")/2, height/2+80);
            try {
                Thread.sleep(250);
            } catch (Exception e) {}
        }
    }

    
    
	public void keyPressed(KeyEvent e) 
    {
        int key = e.getKeyCode();
        
        if(isGame && !(isWon))
        {
            if (!isSinglePlayer) {
                if (key == KeyEvent.VK_W)           p1UpPressed = true;
                if (key == KeyEvent.VK_S)           p1DownPressed = true;
            }
            if (key == KeyEvent.VK_UP)          p2UpPressed = true;
            if (key == KeyEvent.VK_DOWN)        p2DownPressed = true;
        }

        if(isGame && isWon && key == KeyEvent.VK_R)
        {
            isGame = false;
            isRestart = true;
        }
    }
 		
    public void keyReleased(KeyEvent e) 
    {
        int tus = e.getKeyCode();

        if (!isSinglePlayer) {
            if (tus == KeyEvent.VK_W)               p1UpPressed = false;
            if (tus == KeyEvent.VK_S)               p1DownPressed = false;
        }

        if (tus == KeyEvent.VK_UP)              p2UpPressed = false;
        if (tus == KeyEvent.VK_DOWN)            p2DownPressed = false;
    }
 	public void keyTyped(KeyEvent e) {}	
    


    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        
        if(isMenu){
            if(x>width/2-65 && x<width/2+68){
                if(y>height/2+25 && y<height/2+75){
                    isMenu = false;
                    isGame = true;
                    isBeginning = true;
                    isSinglePlayer = true;
                }
            }
            if(x>width/2-65 && x<width/2+68){
                if(y>height/2+85 && y<height/2+135){
                    isMenu = false;
                    isGame = true;
                    isBeginning = true;
                    isSinglePlayer = false;
                }
            }
            if(x>width/2+65 && x<width/2+68){
                if(y>height/2+145 && y<height/2+195){
                    isMenu = false;
                    isSettings = true;
                }
            }
        }
        
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
}

public class Game implements WindowListener
{
	private Frame a;
	private kanvas02 k;

	public Game()
	{
		a = new Frame();
		k = new kanvas02();
		a.addWindowListener(this);
		a.add(k);
		a.setSize(1000,700);
		a.setResizable(false);
		a.setVisible(true);
        k.init();
	}
	
	public static void main(String args[])
	{
		new Game();
	}

	public void windowOpened(WindowEvent e) {}
	public void windowClosing(WindowEvent e) 
	{
		System.exit(0);
	}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
}
