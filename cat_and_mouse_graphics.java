/* 
//****************************************************************************************************************************
//Program name: "The Great Cat Chase". This program simulates a mouse ball moving in any direction, it can bounce off of the * 
//walls and corners of the UI; the user is welcome to input mouse/cat speed and mouse's direction. Additionally, there is a  * 
//cat ball that chases the mouse ball and moves in a direction which has curved motion.    Copyright (C) 2021 Bryant Nguyen  *
//This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License  *
//version 3 as published by the Free Software Foundation.                                                                    *
//This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied         *
//warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.     *
//A copy of the GNU General Public License v3 is available here:  <https://www.gnu.org/licenses/>.                           *
//****************************************************************************************************************************
//Author information:
  //Author: Bryant Nguyen
  //Mail: bryantdnguyen@csu.fullerton.edu

//Program information:
  //Program name: The Great Cat Chase, 1.0
  //Programming language: Java
  //Files: cat_and_mouse_driver.java, cat_and_mouse_UI.java, cat_and_mouse_graphics.java, run.sh
  //Date project began: 2021-March-31 (version 1.0).
  //Date of last update: 2021-May-09 (version 1.0)
  //Status: Complete
  //Purpose: This program demonstrate the design of a simple UI (user interface) and the use of 2D motion through 
  //         an animated ball. Implemented functions are: Start/Pause/Resume, Clear, and Quit. The user is prompted
  //         to input the Mouse and Cat Speed (pix/sec), and the Mouse Direction. 
  //Also, this program demonstrates the use of multiple source files as one program. 
  //Nice feature: User is allowed to START/RESUME and PAUSE the movement of the ball. The Start button can be hit once, it changes to Pause
  //              when the user hits it and once Pause is hit the button changes to Resume; When the user hits clear, both the mouse and cat  
  //              ball position is reset to default and the Start button can be hit again.
  //              Additionally, the distance between both balls' closest edges is constantly updated and displayed.
  //Base test system: Linux system with Bash shell and openjdk-14-jdk
  //How to write informative messages to the terminal window.

//This module
  //File name: cat_and_mouse_graphics.java
  //Compile: javac cat_and_mouse_graphics.java
  //This module is invoked from the cat_and_mouse_UI class
  //Purpose: This class maintains the panel with the Ball's motion: motionPane.
  //Educational purpose: Demonstrate how detailed operations unrelated to the definition of the UI can be and should be 
  //off-loaded to a separate file, which is then invoked by the cat_and_mouse_UI class. This is to add clarity to both files.
*/
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.Timer;
import javax.swing.JTextField;

public class cat_and_mouse_graphics extends JPanel
{
    //For the Mouse
    private double mouse_defaultxPos = 600.0;
    private double mouse_defaultyPos = 300.0;
    private final double mouse_ballradius = 25.0;
    private double mouse_balldiameter = 2.0*mouse_ballradius;
    private double mouse_ball_center_x = mouse_defaultxPos;
    private double mouse_ball_center_y = mouse_defaultyPos;
    private double mouse_ball_upper_corner_x = mouse_ball_center_x - mouse_ballradius;
    private double mouse_ball_upper_corner_y = mouse_ball_center_y - mouse_ballradius;
    
    private int mouse_ball_upper_corner_integer_x;
    private int mouse_ball_upper_corner_integer_y;
    private double mouse_deltaX;
    private double mouse_deltaY;
    private double mouse_distanceMoved_oneTic;

    private double mouse_a1, mouse_a2, mouse_b1, mouse_b2;

    //For the Cat
    private double cat_defaultxPos = 50.0; //50.0
    private double cat_defaultyPos = 50.0; //50.0
    private final double cat_ballradius = 35.0;
    private double cat_balldiameter = 0.0; //2.0*cat_ballradius
    private double cat_ball_center_x = cat_defaultxPos;
    private double cat_ball_center_y = cat_defaultyPos;
    private double cat_ball_upper_corner_x = cat_ball_center_x - cat_ballradius;
    private double cat_ball_upper_corner_y = cat_ball_center_y - cat_ballradius;
    
    private int cat_ball_upper_corner_integer_x;
    private int cat_ball_upper_corner_integer_y;
    private double cat_deltaX;
    private double cat_deltaY;
    private double cat_distanceMoved_oneTic;
    private double cat_New_deltaX;
    private double cat_New_deltaY;

    private double cat_a1, cat_a2, cat_b1, cat_b2;


    private double tempDouble;
    private double gap_catmouse = 1.0;
    private final int motionPanel_width = 1200; //1200
    private final int motionPanel_height = 575;//not the actual height, temp height to account for ball bounce visibility

    public cat_and_mouse_graphics()
    {
        setVisible(true);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        this.setBackground(new Color(245, 240, 225)); //tan color
        //Draw Mouse
        g.setColor(new Color(192,129,41));//brownish color
            g.fillOval((int)Math.round(mouse_ball_upper_corner_x),(int)Math.round(mouse_ball_upper_corner_y),
                                   (int)Math.round(mouse_balldiameter),(int)Math.round(mouse_balldiameter));
            g.setColor(Color.BLACK);//Black Border
            g.drawOval((int)Math.round(mouse_ball_upper_corner_x),(int)Math.round(mouse_ball_upper_corner_y),
                                   (int)Math.round(mouse_balldiameter),(int)Math.round(mouse_balldiameter));
        //Draw Cat
        g.setColor(new Color(192,192,192));//silver
            g.fillOval((int)Math.round(cat_ball_upper_corner_x),(int)Math.round(cat_ball_upper_corner_y),
                                   (int)Math.round(cat_balldiameter),(int)Math.round(cat_balldiameter));
            g.setColor(Color.BLACK);//Black Border
            g.drawOval((int)Math.round(cat_ball_upper_corner_x),(int)Math.round(cat_ball_upper_corner_y),
                                   (int)Math.round(cat_balldiameter),(int)Math.round(cat_balldiameter));
        //color for cat: 192, 192, 192 (silver) | 128, 137, 144 grey
    }//end of paintComponent
//****************************************************************************************************************************************** 
     public void initializeObjects_Mouse(double startx, double starty, double dx, double dy)
     { 
          mouse_a1 = startx;
          mouse_b1 = starty;
          mouse_deltaX = dx;
          mouse_deltaY = dy;
          mouse_distanceMoved_oneTic = Math.sqrt(mouse_deltaX*mouse_deltaX + mouse_deltaY*mouse_deltaY); //Hypotenuse in right triangle with legs dx and dy.
          mouse_ball_upper_corner_integer_x = (int)Math.round(mouse_ball_upper_corner_x);
          mouse_ball_upper_corner_integer_y = (int)Math.round(mouse_ball_upper_corner_y); 
          //System.out.println("Coordinates of the center of the ball are ("+mouse_ball_center_x+','+mouse_ball_center_y+')');  //Debug purposes
     }//End of initializeObjects_Mouse
//****************************************************************************************************************************************** 
public void initializeObjects_Cat(double startx, double starty, double dx, double dy)
     { 
          cat_a1 = startx;
          cat_b1 = starty;
          cat_deltaX = dx;
          cat_deltaY = dy;
          cat_distanceMoved_oneTic = Math.sqrt(cat_deltaX*cat_deltaX + cat_deltaY*cat_deltaY); //Hypotenuse in right triangle with legs dx and dy.
          cat_ball_upper_corner_integer_x = (int)Math.round(cat_ball_upper_corner_x);
          cat_ball_upper_corner_integer_y = (int)Math.round(cat_ball_upper_corner_y); 
          //System.out.println("Coordinates of the center of the ball are ("+mouse_ball_center_x+','+mouse_ball_center_y+')');  //Debug purposes
     }//End of initializeObjects_Cat
//******************************************************************************************************************************************
     public void moveMouseBall(double mouse_current_position_x, double mouse_current_position_y)//move the ball when called | conditions for each wall and corner
     {    
       mouse_ball_center_x = mouse_current_position_x;
       mouse_ball_center_y = mouse_current_position_y;
         if(((mouse_ball_center_y - mouse_ballradius) < mouse_distanceMoved_oneTic) && ((motionPanel_width - (mouse_ball_center_x + mouse_ballradius)) < mouse_distanceMoved_oneTic))
          {                                       //Ball hits North and East Walls
            System.out.println("North & East Wall Hit");
            mouse_deltaY = (mouse_deltaY * -1);
            mouse_deltaX = (mouse_deltaX * -1);
            //continue moving ball
            mouse_ball_center_x += mouse_deltaX;
            mouse_ball_center_y += mouse_deltaY;
          }
          else if(((mouse_ball_center_y - mouse_ballradius) < mouse_distanceMoved_oneTic) && ((mouse_ball_center_x - mouse_ballradius) < mouse_distanceMoved_oneTic))
          {                                      //Ball hits North and West Walls
            System.out.println("North & West Wall Hit");
            mouse_deltaY = (mouse_deltaY * -1);
            mouse_deltaX = (mouse_deltaX * -1);
            //continue moving ball
            mouse_ball_center_x += mouse_deltaX;
            mouse_ball_center_y += mouse_deltaY;
          }
          else if(((motionPanel_height - (mouse_ball_center_y + mouse_ballradius)) < mouse_distanceMoved_oneTic) && 
          ((motionPanel_width - (mouse_ball_center_x + mouse_ballradius)) < mouse_distanceMoved_oneTic))
          {                                     //Ball hits South and East Walls
            System.out.println("South & East Wall Hit");
            mouse_deltaY = (mouse_deltaY * -1);
            mouse_deltaX = (mouse_deltaX * -1);
            //continue moving ball
            mouse_ball_center_x += mouse_deltaX;
            mouse_ball_center_y += mouse_deltaY;
          }
          else if(((motionPanel_height - (mouse_ball_center_y + mouse_ballradius)) < mouse_distanceMoved_oneTic) && 
          ((mouse_ball_center_x - mouse_ballradius) < mouse_distanceMoved_oneTic))
          {                                     //Ball hits South and West Walls
            System.out.println("South & West Wall Hit");
            mouse_deltaY = (mouse_deltaY * -1);
            mouse_deltaX = (mouse_deltaX * -1);
            //continue moving ball
            mouse_ball_center_x += mouse_deltaX;
            mouse_ball_center_y += mouse_deltaY;
          } 
     //***************************************************************************************** ////////
          else if((mouse_ball_center_y - mouse_ballradius) < mouse_distanceMoved_oneTic)
           {                                                   //This is the case where the ball has hit the North Wall
            System.out.println("North wall hit");//**DEBUG Purposes
            mouse_deltaY = (mouse_deltaY * -1);
            //continue moving ball
            mouse_ball_center_x += mouse_deltaX;
            mouse_ball_center_y += mouse_deltaY;
           }
          else if((motionPanel_width - (mouse_ball_center_x + mouse_ballradius)) < mouse_distanceMoved_oneTic)
          {                                                   //This is the case where the ball has hit the East Wall
            System.out.println("East wall hit");//**DEBUG Purposes
            mouse_deltaX = (mouse_deltaX * -1);
            //continue moving ball
            mouse_ball_center_x += mouse_deltaX;
            mouse_ball_center_y += mouse_deltaY;
          }
          else if((motionPanel_height - (mouse_ball_center_y + mouse_ballradius)) < mouse_distanceMoved_oneTic)
          {                                                   //This is the case where the ball has hit the South Wall
            System.out.println("South wall hit");//**DEBUG Purposes
            mouse_deltaY = (mouse_deltaY * -1);
            //continue moving ball
            mouse_ball_center_x += mouse_deltaX;
            mouse_ball_center_y += mouse_deltaY;
          }
          else if((mouse_ball_center_x - mouse_ballradius) < mouse_distanceMoved_oneTic)
          {                                                    //This is the case where the ball has hit the West Wall
            System.out.println("West wall hit");//**DEBUG Purposes
            mouse_deltaX = (mouse_deltaX * -1);
            //continue moving ball
            mouse_ball_center_x += mouse_deltaX;
            mouse_ball_center_y += mouse_deltaY;
          } 
          //*********************************************************************
          else                                                          //      *
              {//This is the case where the ball has not hit any of the walls   *
               mouse_ball_center_x += mouse_deltaX;                     //      *
               mouse_ball_center_y += mouse_deltaY;                     //      *
              }                                                         //      *
          //********************************************************************* 
          //update coords
          mouse_ball_upper_corner_x = mouse_ball_center_x - mouse_ballradius;
          mouse_ball_upper_corner_y = mouse_ball_center_y - mouse_ballradius;
          mouse_ball_upper_corner_integer_x = (int)Math.round(mouse_ball_upper_corner_x);
          mouse_ball_upper_corner_integer_y = (int)Math.round(mouse_ball_upper_corner_y);
          //System.out.println("Coordinates of the center of the ball are ("+mouse_ball_center_x+','+mouse_ball_center_y+')');  //Debug
     }//End of moveMouseball
//******************************************************************************************************************************************
//******************************************************************************************************************************************
     public void moveCatBall(double cat_current_position_x, double cat_current_position_y, double cat_speed_PixPerTic, double dist_between_catmouse, 
                              Timer refresh, Timer catmotion, Timer mousemotion, JTextField distance_betweenTxtF)//move the ball when called  
    {
        //System.out.println("Cat Delta X : " + cat_deltaX);// DEBUG PURPOSES
        //System.out.println("Cat Delta Y : " + cat_deltaY);// DEBUG PURPOSES
      //calculate gap distance from nearest 2 EDGES
      gap_catmouse = dist_between_catmouse - mouse_ballradius - cat_ballradius;
      cat_distanceMoved_oneTic = Math.sqrt(cat_deltaX*cat_deltaX + cat_deltaY*cat_deltaY);//update in accordance to changing deltas
      if(gap_catmouse <= 0.0)
      {
        System.out.println("MoveCatBall If statement: Stop Clocks => INITIATED");// DEBUG PURPOSES
        catmotion.stop();
        mousemotion.stop();
        refresh.stop();
        distance_betweenTxtF.setText("0.00");
      }
      else if (gap_catmouse >= cat_distanceMoved_oneTic)
      {
        //System.out.println("MoveCatBall normal");//DEBUG PURPOSES
        //update cat's center's current position
        cat_ball_center_x = cat_current_position_x;
        cat_ball_center_y = cat_current_position_y;
        //recompute deltas
        cat_deltaX = (mouse_ball_center_x - cat_ball_center_x) * (cat_speed_PixPerTic/dist_between_catmouse);
        cat_deltaY = (mouse_ball_center_y - cat_ball_center_y) * (cat_speed_PixPerTic/dist_between_catmouse);
                //System.out.println("Cat Delta X : " + cat_deltaX);// DEBUG PURPOSES
                //System.out.println("Cat Delta Y : " + cat_deltaY);// DEBUG PURPOSES
        //Move the cat ball
        cat_ball_center_x += cat_deltaX;
        cat_ball_center_y += cat_deltaY;
      }
      else //gap_catmouse < cat_distanceMoved_oneTic
      {
        System.out.println("MoveCatBall NEW SMALLER DELTAS");// DEBUG PURPOSES
        //update cat's center's current position
        cat_ball_center_x = cat_current_position_x;
        cat_ball_center_y = cat_current_position_y;
        //Calculate new deltas for SHORTER movement
                //System.out.println("Cat Delta X : " + cat_deltaX);// DEBUG PURPOSES
                //System.out.println("Cat Delta Y : " + cat_deltaY);// DEBUG PURPOSES
        cat_deltaX = (((mouse_ball_center_x - cat_ball_center_x)*gap_catmouse)/dist_between_catmouse);
        cat_deltaY = (((mouse_ball_center_y - cat_ball_center_y)*gap_catmouse)/dist_between_catmouse);
                //System.out.println("NewCat Delta X : " + cat_deltaX);// DEBUG PURPOSES
                //System.out.println("NewCat Delta Y : " + cat_deltaY);// DEBUG PURPOSES
        //Move the cat ball
        cat_ball_center_x += cat_deltaX;
        cat_ball_center_y += cat_deltaY;
      }
        //Update for repaint function
        cat_ball_upper_corner_x = cat_ball_center_x - cat_ballradius;
        cat_ball_upper_corner_y = cat_ball_center_y - cat_ballradius;
        cat_ball_upper_corner_integer_x = (int)Math.round(cat_ball_upper_corner_x);
        cat_ball_upper_corner_integer_y = (int)Math.round(cat_ball_upper_corner_y);
    }//end of moveCatBall
//******************************************************************************************************************************************
//Getters
  public double get_distance_between_catmouse()
  {
    tempDouble = Math.sqrt(Math.pow((mouse_ball_center_x - cat_ball_center_x),2) + Math.pow((mouse_ball_center_y - cat_ball_center_y),2));
    //tempDouble = tempDouble - cat_ballradius - mouse_ballradius;// gap = center_Distance - catballradius - mouseballradius
    return tempDouble;
  }

  public double get_gap_catmouse()
  {
    return gap_catmouse;
  }
    //For Mouse
    public double mouse_getX_center_of_ball()
    {
      return mouse_ball_center_x;
    }

    public double mouse_getY_center_of_ball()
    {
      return mouse_ball_center_y;
    }
    //For Cat
    public double cat_getX_center_of_ball()
    {
      return cat_ball_center_x;
    }

    public double cat_getY_center_of_ball()
    {
      return cat_ball_center_y;
    }

    public double cat_get_distanceMoved_OneTic()
    {
      return cat_distanceMoved_oneTic;
    }
//Setters
    public void reset_gap_catmouse()
    {
      gap_catmouse = 1.0;
    }
    //For Mouse
    public void mouse_setX_center_of_ball()
     {
        mouse_ball_center_x = mouse_defaultxPos;
     }

    public void mouse_setY_center_of_ball()
    {
        mouse_ball_center_y = mouse_defaultyPos;
    }
    public void mouse_reset_ball_upper_corners()
     {
       mouse_ball_upper_corner_x = mouse_ball_center_x - mouse_ballradius;
       mouse_ball_upper_corner_y = mouse_ball_center_y - mouse_ballradius;
     }

    //For Cat
    public void cat_setX_center_of_ball()
    {
      cat_ball_center_x = cat_defaultxPos;
    }
    public void cat_setY_center_of_ball()
    {
      cat_ball_center_y = cat_defaultyPos;
    }
    public void cat_reset_ball_upper_corners()
    {
      cat_ball_upper_corner_x = cat_ball_center_x - cat_ballradius;
      cat_ball_upper_corner_y = cat_ball_center_y - cat_ballradius;
    }
    public void cat_setDiameter()
    {
      cat_balldiameter = 2.0*cat_ballradius;
    }



}//end of cat_and_mouse_graphics class

