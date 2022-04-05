#!/bin/bash

#Author: Bryant Nguyen  
#Program: The Great Cat Chase
#Date completed: 2021-May-09 (version 1.0)
#Preconditions:
#   1.  All source files are in one directory
#   2.  This file, run.sh, is in that same directory
#   3.  The shell is currently active in that same directory
#How to execute: Enter "sh run.sh" without the quotes.

echo "Remove old byte-code files if any exist."
rm *.class

echo "Compile the graphical panel cat_and_mouse_graphics.java"
javac cat_and_mouse_graphics.java

echo "Compile the user interface"
javac cat_and_mouse_UI.java

echo "Compile the main method"
javac cat_and_mouse_driver.java

echo "Run the program "
java cat_and_mouse_driver

echo "This bash script file is quiting"