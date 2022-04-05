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
*/


import javax.swing.JFrame;

public class cat_and_mouse_driver
{
    public static void main(String[] args)
    {
        cat_and_mouse_UI myFrame = new cat_and_mouse_UI();
    }
}