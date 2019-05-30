# GuessThatMess
AP Comp Sci Final Project 2019: Nikita, Simona, Harshini, Aishni 

Guess that Mess: An Android App using Machine Learning for Classifying Doodles With QuickDraw TensorFlow Lite Model

Problem: Trying to get the user to draw a doodle and get the computer to try (using machine learning) to guess what is drawn

Instructions:

Download Android Studio 

Create a new project with the language option Java (make sure it is an empty activity)

Name your project (Guess that mess)

Go to App -> Src -> Main

Go to Android Mainfest xml (main top level file that holds all information about the project)

Activity_main xml = defines layout of app, under resources and layout (res -> layout)


Under Java are the three following files and a classifier folder:

MainActivity = main class, where onClick methods are executed 

DoodleCanvas = canvas class for drawing images

Stroke = set size for drawing path made by user

Under the Classifier folder:

ImageClassifier - load the model (located in assets directory under res) and labels file

Model Input - model takes the image and converts it into a bitmap 

Result -  displays result from tensorflow lite in percentages of what the machine has guessed

Assets Folder - two files (labels and model) under res

Main UI = has canvas for drawing (defined in Activity Main); Has three buttons(guess, clear, next) and two Textfields for displaying the result and prompting the user to draw a random labelled doodle


Res -> values, colors.xml (change and set up primary colors)

Create file dimension.xml (can be used to define margins) 

Strings.xml to define variable names

Styles.xml

Create menu folder (menu item for home, dashboard, and notifications)

import tflite and other dependencies into build.grade file

# Preview:

![Correct Guess](/Correct.png?raw=true "Correct Guess")

![Incorrect Guess](/Incorrect.png?raw=true "Incorrect Guess")




Possible Extensions: add timer, score, add more categories

Tools Used: Java, Android Studio, Tensorflow Lite, Quick Draw Data Set (labels), Github (inspired by many repositories), Google documentation, Stack Overflow, YouTube

