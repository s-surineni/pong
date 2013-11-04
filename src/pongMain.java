//http://www.dreamincode.net/forums/topic/172211-programing-an-applet-game-of-pong/
/* 
 * This is the main method of my Pong applet. The game is played by a single
 * player who uses the mouse to control the y position of their paddle.
 * The computer controlled paddle(the one on the right) uses the y position
 * of the ball in play to determine it's paddle position. Essetially, it has
 * God-like capabilities that can't be beaten. The object is to see how long 
 * you can hold out before the computer scores 10 points
 */

import java.applet.*; 
import java.awt.event.*; 
import java.awt.*;
import javax.swing.Timer;

//we need the Applet methods and the MouseMotionListener interface
//(used for the human controlled panel
public class pongMain extends Applet implements MouseMotionListener, ActionListener 
{ 
     //we declare an instance of our ball and two paddles
	 Ball ball;
	 PaddleLeft pLeft; 
	 PaddleRight pRight;
	 //a font used to display the score
	 Font newFont = new Font("sansserif", Font.BOLD, 20);
	 //The image I am creating is going to be double buffered
	 //so that there is no flicker when the applet is repainted
	 Graphics bufferGraphics; 
     // The image that will contain everything that has been drawn on 
     // bufferGraphics. 
     Image offscreen; 
     // variables used to set the width and height of the applet. 
     final int WIDTH = 500, HEIGHT = 300; 
     //variable used to record the time that the game has proceeded to
     //because we want to tell the person how long the lasted
     long currentTime;
     
     public void init()  
     { 
    	  //sets the applet to be 500 * 300
          setSize(500, 300);
          //we now instantiate our ball and two paddles
          ball = new Ball();
    	  pLeft = new PaddleLeft();
    	  //this paddle is set to the current ball y position
    	  //minus 35 so the ball will be lined up with the center
    	  //of our 70 pixel long paddle
    	  pRight = new PaddleRight(ball.getY() - 35);
          
    	  //we add our mouseMotionListener
    	  addMouseMotionListener(this);
          //I want the applet to look like a grass court =)
    	  setBackground(Color.green); 
          // Create an offscreen image to draw on 
          offscreen = createImage(WIDTH,HEIGHT); 
          bufferGraphics = offscreen.getGraphics(); 
     }
     
     public void start(){
    	 //this sets the current time in milliseconds
    	 currentTime = System.currentTimeMillis();
    	 
    	 //I am going to use a timer to do a certain list of tasks
    	 //every 15 milliseconds, which is about 67 frames a second
    	 //(there are 1000 milliseconds in a second, so we divide that
    	 //by 15 to set up our game's frame rate
    	 Timer time = new Timer(15, this);     
    	
    	 //this is the "game loop". It won't end until the
    	 //computer has scored 10 points on the board.
    	//we begin our time so that the actionPerformed method will be called
    	 //every 15 milliseconds
    	 time.start(); 
    	 while(pRight.getScore() < 10){
    		 //nothing happens in here, but the timer is still running in the
    		 //background, executing our actionPerformed method and making
    		 //the wheels of this program turn
         }
    	 //after the game needs to end, we stop the timer and calculate
    	 //how long the user has been playing by subtracting the current
    	 //time from the intitial currentTime
    	 time.stop();
    	 currentTime = System.currentTimeMillis() - currentTime;
    	 //we repaint one more time to display how long the player 
    	 //lasted in this helpless cause =)
    	 repaint();
     }
     
     public void stop(){
    	 
     }

      public void paint(Graphics g)  
     { 
          //instead of using the typical graphics, we are going to
    	  //use bufferGraphics (which we declared at the beginning
    	  //of the class) to draw onto our off-screen image
    	  
    	  //first, we clear off whatever was previously on the image
    	  bufferGraphics.clearRect(0,0,WIDTH,HEIGHT); 
         
    	  //we now draw our two paddles in blue
          bufferGraphics.setColor(Color.blue);
          //the XPOS is a final int, so it never changes, but the 
          //yPos does. We make the paddles 10 * 70.
          //Left side
          bufferGraphics.fillRect(pLeft.XPOS,pLeft.getPos(),10,70);
          //Right side
          bufferGraphics.fillRect(pRight.XPOS, pRight.getPos(), 10, 70);
          
          //this draws our mid-court line and our scores in white
          bufferGraphics.setColor(Color.white);
          bufferGraphics.setFont(newFont);
          //we show our player's hopeless circumstances
          bufferGraphics.drawString("Futile", 150, 15);
          //we get the score from our PaddleRight object
          bufferGraphics.drawString(""+ pRight.getScore(),300,15);
          //mid-court divider
          bufferGraphics.fillRect(240,0,20,300);
          
          //Remeber, we painted one last time after the computer's 
          //score reached ten
          if(pRight.getScore() == 10){
        	  //we divide the milliseconds by 1000 to display in seconds how
        	  //long the game lasted
        	  bufferGraphics.drawString("You Lasted: " + (currentTime/ 1000) + "sec.", 40, 150);
          }
          
          //we draw the ball
          bufferGraphics.setColor(Color.red);
          bufferGraphics.fillRect(ball.getX(),ball.getY(),10,10);
          //finally, we draw the offscreen image to the applet
          g.drawImage(offscreen,0,0,this);
          //this line makes sure that all the monitors are up to date before 
          //proceeding
          Toolkit.getDefaultToolkit().sync();
     }
     
     //all good double buffers need the update() method. If
     // you leave this out, the image will still flicker
     public void update(Graphics g)
     { 
          paint(g); 
     } 
 

     //this sets the mouse's y position to the center of our paddle
     public void mouseMoved(MouseEvent evt)  
     { 
          pLeft.setPos(evt.getY()- 35); 
     } 
 

     // The necessary methods, though this one is unused, we still
     //have to put it here. 
     public void mouseDragged(MouseEvent evt)  
     { 
     }
     
     //this is where the program figures out if the ball has hit
     //a paddle, the top or bottom of the applet window, or if the 
     //computer scored
     public void checkCollision(){
 		//remember, our ball is 10*10 and the x and y positions are the
    	//top-left corners of the ball. If the top left corner y position
    	//is 0 or 290, we reverse the y- direction that the ball was
    	//travelling in by multiplying ball.dy by -1
    	if(ball.getY() == 0 || ball.getY() == 290){
 			ball.dy = (ball.dy * -1);
 		}
 		
 		//if the ball is at the right-hand edge of the human paddle's
    	//domain and the boolean method hitPaddle() is true, then we
    	//reverse the dx position of ball by multiplying ball.dx by -1
    	if((ball.getX() == 40) && hitPaddle()){
 			ball.dx = (ball.dx * -1);
 		}
 		
    	//we already know that the computer paddle can't miss, so if
    	//the ball reaches the left-hand edge of the paddle, we can make the
    	//dx switch directions without any additional checks
 		if(ball.getX() == 460){
 			ball.dx = (ball.dx * -1);
 		}
 		
 		//if the ball is missed by the human paddle and reaches the 
 		//left-hand edge of the applet window, then reset the ball
 		//and increment the score
 		if(ball.getX() == 0){
 			pRight.setScore(pRight.getScore() + 1);
 			ball.reset();
 		}
 	 }
     
     public boolean hitPaddle(){
    	 boolean didHit = false;
    	 //this just checks if the ball is lined up between the top and 
    	 //bottom right-hand corners of the human paddle
    	 if((pLeft.getPos() - 10) <= ball.getY() && (pLeft.getPos() + 70) > ball.getY()){
    		 //sets didHit to true
    		 didHit = true;
    	 }
    	 return didHit;
     }

	@Override
	//every 15 milliseconds, the Timer time triggers the 
	//actionPerformed method
	public void actionPerformed(ActionEvent arg0) {
		 //moves the ball 
		 ball.move();
		 //lines the computer paddle up with the ball
		 pRight.setPos(ball.getY() - 35);
		 //checks the ball for a collision
		 checkCollision();
		 //repaints the applet
		 repaint();
	}

 }
