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
  //File name: cat_and_mouse_UI.java
  //Compile : javac cat_and_mouse_UI.java
  //Purpose: This class maintains the user interface
  //This module (class) is called from the cat_and_mouse_driver class.
  //Educational purpose of this cat_and_mouse_UI class:
    //   1.  Demonstrate the common technique of partitioning a software solution into 3 or more source files.
    //   2.  Demonstrate the use of multiple panels with multiple colors in the UI (user interface).
    //   3.  Demonstrate the functionality of the start/pause/resume, clear, and quit buttons.
    //   4.  Demonstrate the use of the ClockHandler class to maintain the refresh and motion clocks.
    //   5.  Demonstrate the use of two different motion clocks, cat motion and mouse motion clocks.
*/

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;

import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.Timer;

import java.text.DecimalFormat;

public class cat_and_mouse_UI extends JFrame
{
    private final int UI_width = 1200; 
    private final int UI_height = 800; 

    //For the Title Panel
    private final int titlePane_width = UI_width;
    private final int titlePane_height = 75;
    private JPanel titlePane;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JLabel authorLabel;

    //For User Control Panel
    private final int userPanel_width = UI_width;
    private final int userPanel_height = 125;  //125
    private JPanel userPane;
    private JLabel catSpeed_Label;
    private JLabel mouseSpeed_Label;
    private JLabel mouseDirection_Label;
    private JLabel distanceBetween_Label;
    private JLabel empty_label;
    private JTextField catSpeed_TxtF;
    private JTextField mouseSpeed_TxtF;
    private JTextField mouseDirection_TxtF;
    private JTextField distanceBetween_TxtF;
    private JButton clearB;
    private JButton startB;
    private JButton quitB;

    //For the Graphic Panel
    private cat_and_mouse_graphics motionPane;
    private final int motionPane_width = UI_width; //1200.0
    private final int motionPane_height = UI_height - titlePane_height - userPanel_height;
                                        // 800 - 75 - 125  = 600 (motionPane_height)
    
    //For the Refresh/Motion Clocks
    private Timer refreshclock;
    private Timer cat_motionclock;
    private Timer mouse_motionclock;
    private Clockhandlerclass clockhandler;
    private double refresh_clock_rate = 100.45;   //Hz.  limited by the speed of hardware (monitor)
    private int refresh_clock_delay_interval;   

    private double mouse_motion_clock_rate = 100.0   ;   //60Hz.   For math operations, updating position
    private int mouse_motion_clock_delay_interval;
    private double mouse_speed_pix_per_tic;

    private double cat_motion_clock_rate = 90.0;        //50Hz. 
    private int cat_motion_clock_delay_interval; 
    private double cat_speed_pix_per_tic;

    private final double millisecondpersecond = 1000.0;
    

    private int started = 0;

    //For calculations and processing user inputs
    private double mouse_delta_x;
    private double mouse_delta_y;
    private double cat_delta_x;
    private double cat_delta_y;
    private double mouse_default_x = motionPane_width/2; //600
    private double mouse_default_y = motionPane_height/2;//300
    private double cat_default_x = 50.0;
    private double cat_default_y = 50.0;
    private double mouse_current_xPos = mouse_default_x;
    private double mouse_current_yPos = mouse_default_y;
    private double cat_current_xPos = cat_default_x;
    private double cat_current_yPos = cat_default_y;
    private double distance_between_catmouse;
    private double user_doubleCatSpeed;
    private double user_doubleMouseSpeed;
    private double user_doubleMouseDirection;

    private String user_strCatSpeed;
    private String user_strMouseSpeed;
    private String user_strMouseDirection;


    public cat_and_mouse_UI()
    {
        setTitle("The Great Cat Chase");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(UI_width, UI_height));
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //Panel 1: Title Panel
        titlePane = new JPanel();
        titlePane.setLayout(new GridLayout(3,1));
        titlePane.setPreferredSize(new Dimension(titlePane_width, titlePane_height)); 
        titlePane.setBackground(new Color(30,61,89)); //prev 230,241,140
        titleLabel = new JLabel("The Great Cat Chase");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.PLAIN, 30));
        titleLabel.setForeground(Color.white);
        subtitleLabel = new JLabel("Cat and Mouse Animation");
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Verdana", Font.PLAIN, 25));
        subtitleLabel.setForeground(Color.white);
        authorLabel = new JLabel("by Bryant Nguyen");
        authorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        authorLabel.setFont(new Font("Verdana", Font.PLAIN, 25));
        authorLabel.setForeground(Color.white);
        //Adding to titlePane
        titlePane.add(titleLabel);
        titlePane.add(subtitleLabel);
        titlePane.add(authorLabel);

//****************************************************************************** 
    //Panel 2: Motion Panel
     motionPane = new cat_and_mouse_graphics();
     motionPane.setLayout(new GridLayout(1,1));
     motionPane.setPreferredSize(new Dimension (motionPane_width, motionPane_height)); //panel size: (1800,800)

//*****************************************************************************
    //Panel 3: User Panel
    userPane = new JPanel();
    userPane.setLayout(new GridLayout(3,4));
    userPane.setPreferredSize(new Dimension (userPanel_width, userPanel_height));
    userPane.setBackground(new Color(255,110,64)); // prev 67,121,117
    buttonhandler myhandler = new buttonhandler();
    //Buttons
    clearB = new JButton("Clear");
    clearB.setFocusable(false);
    clearB.addActionListener(myhandler);
    startB = new JButton("Start");
    startB.setFocusable(false);
    startB.addActionListener(myhandler);
    quitB = new JButton("Quit");
    quitB.setFocusable(false);
    quitB.addActionListener(myhandler);
    //Labels
    catSpeed_Label = new JLabel("Cat Speed (Pix/sec)");
        catSpeed_Label.setFont(new Font("Verdana", Font.PLAIN, 25));
        catSpeed_Label.setForeground(Color.white);
    mouseSpeed_Label = new JLabel("Mouse Speed (Pix/sec)");
        mouseSpeed_Label.setFont(new Font("Verdana", Font.PLAIN, 25));
        mouseSpeed_Label.setForeground(Color.white);
    mouseDirection_Label = new JLabel("Mouse Direction");
        mouseDirection_Label.setFont(new Font("Verdana", Font.PLAIN, 25));
        mouseDirection_Label.setForeground(Color.white);
    distanceBetween_Label = new JLabel("Distance Between");
        distanceBetween_Label.setFont(new Font("Verdana", Font.PLAIN, 25));
        distanceBetween_Label.setForeground(Color.white);
    empty_label = new JLabel(""); //fills in empty space in panel
    //Textfields
    catSpeed_TxtF = new JTextField(10);
        catSpeed_TxtF.setFont(new Font("Verdana", Font.PLAIN, 30));
    mouseSpeed_TxtF = new JTextField(10);
        mouseSpeed_TxtF.setFont(new Font("Verdana", Font.PLAIN, 30));
    mouseDirection_TxtF = new JTextField(10);
        mouseDirection_TxtF.setFont(new Font("Verdana", Font.PLAIN, 30));
    distanceBetween_TxtF = new JTextField(10);
        distanceBetween_TxtF.setFont(new Font("Verdana", Font.PLAIN, 35));
    //Adding Buttons/Labels/Textfields to User Panel
    userPane.add(clearB);
    userPane.add(startB);
    userPane.add(quitB);
    userPane.add(distanceBetween_Label);
    userPane.add(catSpeed_Label);
    userPane.add(mouseSpeed_Label);
    userPane.add(mouseDirection_Label);
    userPane.add(distanceBetween_TxtF);
    userPane.add(catSpeed_TxtF);
    userPane.add(mouseSpeed_TxtF);
    userPane.add(mouseDirection_TxtF);
    userPane.add(empty_label);
//************************************************************************************************
    //Adding to BallUI Frame
        add(titlePane, BorderLayout.NORTH);
        add(motionPane, BorderLayout.CENTER);
        add(userPane, BorderLayout.SOUTH);


        setVisible(true);
    }//end of cat_and_mouse_UI constructor
//************************************************************************************************

private class buttonhandler implements ActionListener
{
    public void actionPerformed(ActionEvent event)
    {
        if(event.getSource() == startB)
        {
            if(started == 0)
            {
                startB.setText("Pause");
                started++;
                //Retrieve and Process User's Inputs
                user_strCatSpeed = catSpeed_TxtF.getText();
                user_strMouseSpeed = mouseSpeed_TxtF.getText();
                user_strMouseDirection = mouseDirection_TxtF.getText();
                process_UserInfo(user_strCatSpeed, user_strMouseSpeed, user_strMouseDirection);//process and converts user's input to double
                    //****************************************************************************************************************
                    //Initialization of Refresh & Mouse/Cat Clocks
                    clockhandler = new Clockhandlerclass();
                    //Create Refresh Clock
                    refresh_clock_delay_interval = (int)Math.round(millisecondpersecond/refresh_clock_rate);
                    refreshclock = new Timer(refresh_clock_delay_interval,clockhandler);
                    //Create Mouse Clock
                    mouse_speed_pix_per_tic = user_doubleMouseSpeed/mouse_motion_clock_rate; //will be used to calculate deltas
                        //System.out.println("Mouse Speed (pix/tic) : " + mouse_speed_pix_per_tic);// DEBUG PURPOSES
                    mouse_motion_clock_delay_interval = (int)Math.round(millisecondpersecond/mouse_motion_clock_rate); 
                        mouse_motionclock = new Timer(mouse_motion_clock_delay_interval,clockhandler);
                    //Create Cat Clock
                    cat_speed_pix_per_tic = user_doubleCatSpeed/cat_motion_clock_rate; //will be used to calculate deltas
                        //System.out.println("Cat Speed (pix/tic) : " + cat_speed_pix_per_tic);// DEBUG PURPOSES
                    cat_motion_clock_delay_interval = (int)Math.round(millisecondpersecond/cat_motion_clock_rate); 
                       cat_motionclock = new Timer(cat_motion_clock_delay_interval,clockhandler);
                    //****************************************************************************************************************
                //Compute Distance between Cat and Mouse (for cat's deltas)
                distance_between_catmouse = motionPane.get_distance_between_catmouse();
                //Compute Mouse and Cat's deltas
                mouse_delta_x = (mouse_speed_pix_per_tic) * Math.cos(user_doubleMouseDirection);//Math function uses RADIANS
                mouse_delta_y = -((mouse_speed_pix_per_tic) * Math.sin(user_doubleMouseDirection));//Negative so that the ball moves in correlation to a regular graph's x,y plots,
                                                                                                   //not based off the one used in computer science. 
                                                                                                   //Ex. WITHOUT the "-" sign
                                                                                                   //Direction: 45 would move South East 
                                                                                                   //WITH the "-" sign
                                                                                                   //Direction: 45 would move North East
                    System.out.println("Mouse Delta X : " + mouse_delta_x);// DEBUG PURPOSES
                    System.out.println("Mouse Delta Y : " + mouse_delta_y);// DEBUG PURPOSES
                cat_delta_x = (mouse_default_x - cat_default_x) * (cat_speed_pix_per_tic/distance_between_catmouse);
                cat_delta_y = (mouse_default_y - cat_default_y) * (cat_speed_pix_per_tic/distance_between_catmouse);
                //Initialize User's inputs for Movement
                motionPane.initializeObjects_Mouse(mouse_default_x, mouse_default_y, mouse_delta_x, mouse_delta_y);
                motionPane.initializeObjects_Cat(cat_default_x, cat_default_y, cat_delta_x, cat_delta_y);
                motionPane.cat_setDiameter();
                //start refresh, Mouse/Cat clocks
                refreshclock.start();
                mouse_motionclock.start();
                cat_motionclock.start();
            }//end of IF(STARTED == 0)
            else if (started == 1)
            {//Pause Button 
                startB.setText("Resume");
                started++;
                //stop clocks
                refreshclock.stop();
                mouse_motionclock.stop();
                cat_motionclock.stop();
            }
            else
            {//Resume Button
                startB.setText("Pause");
                started = 1; 
                //resume clocks
                refreshclock.start();
                mouse_motionclock.start();
                cat_motionclock.start();
            }
        }//END OF STARTB
        else if(event.getSource() == clearB)
        {
            //Stop Motion & Mouse/Cat Clocks
            refreshclock.stop();
            mouse_motionclock.stop();
            cat_motionclock.stop();
            //Set Ball position to default starting position
                //Mouse ball
            motionPane.mouse_setX_center_of_ball();
            motionPane.mouse_setY_center_of_ball();
            motionPane.mouse_reset_ball_upper_corners();//recalculate ball's upper corners for the repaint function
            mouse_current_xPos = motionPane.mouse_getX_center_of_ball();
            mouse_current_yPos = motionPane.mouse_getY_center_of_ball();
                //Cat ball
            motionPane.cat_setX_center_of_ball();
            motionPane.cat_setY_center_of_ball();
            motionPane.cat_reset_ball_upper_corners();
            cat_current_xPos = motionPane.cat_getX_center_of_ball();
            cat_current_yPos = motionPane.cat_getY_center_of_ball();
            
            motionPane.repaint();//repaint to default position
            motionPane.reset_gap_catmouse();//reset gap to be above 0.0
            //CLEAR BUTTON MUST MAKE THE START BUTTON APPEAR AGAIN, this allows user to reinitilize inputs after using Clear button
                startB.setText("Start");
                started = 0;
            //Clear TextFields
            catSpeed_TxtF.setText("");
            mouseSpeed_TxtF.setText("");
            mouseDirection_TxtF.setText("");
            distanceBetween_TxtF.setText("");
        }//END OF CLEARB
        else if(event.getSource() == quitB)
        {
            System.exit(0);
        }
    }//END OF ACTIONPERFORMED
}//END OF BUTTONHANDLER
//******************************************************************************************************* 
    private class Clockhandlerclass implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource() == refreshclock)
            {
                motionPane.repaint();
                //System.out.println("Event: Refresh Clock"); //DEBUG PURPOSES
            }
            else if(e.getSource() == cat_motionclock)
            {
                motionPane.moveCatBall(cat_current_xPos, cat_current_yPos, cat_speed_pix_per_tic, distance_between_catmouse, 
                                        refreshclock, cat_motionclock, mouse_motionclock, distanceBetween_TxtF);
                //update values
                distance_between_catmouse = motionPane.get_distance_between_catmouse();
                cat_current_xPos = motionPane.cat_getX_center_of_ball();
                cat_current_yPos = motionPane.cat_getY_center_of_ball();
            }
            else if(e.getSource() == mouse_motionclock)
            {
                motionPane.moveMouseBall(mouse_current_xPos, mouse_current_yPos);
                mouse_current_xPos = motionPane.mouse_getX_center_of_ball();
                mouse_current_yPos = motionPane.mouse_getY_center_of_ball();
                distanceBetween_TxtF.setText(String.format("%.2f", motionPane.get_gap_catmouse()));
            }//end of ELSE IF
        }//end of actionPerformed
    }//end of Clockhandlerclass
//********************************************************************************************************* 
    public void process_UserInfo(String user_catspeedstr, String user_mousespeedstr, String user_mouseDirectstr)
    {
        //Check User's Cat Speed
        if(user_catspeedstr.length() > 0)
        {
            user_doubleCatSpeed = Double.parseDouble(user_catspeedstr);
        }
        else
        {
            user_doubleCatSpeed = 0.0;
        }
        //Check User's Mouse Speed
        if(user_mousespeedstr.length() > 0)
        {
            user_doubleMouseSpeed = Double.parseDouble(user_mousespeedstr);
        }
        else
        {
            user_doubleMouseSpeed = 0.0;
        }
        //Check User's Mouse Direction
        if(user_mouseDirectstr.length() > 0)
        {
            user_doubleMouseDirection = Double.parseDouble(user_mouseDirectstr);
            //Convert Degrees to Radians
            user_doubleMouseDirection = user_doubleMouseDirection * (Math.PI/180);
            //System.out.println(user_doubleMouseDirection); //DEBUG PURPOSES
        }
        else 
        {
            user_doubleMouseDirection = 0.0;
        }
    }//end of process_UserInfo

}//end of cat_mouse_UI class