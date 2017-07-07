# TexasHoldEm_Java
Demo client for Texas Hold 'Em for java

## Getting Started with the Java Demo Client
In order to run this project you first have to set up java on your machine. Then you would need to install an integrated development environment on your system. We have used Eclipse, but any such IDE would do - you only need the src folder that contains all the source files of the project, and the lib folder that contains all the additional packages required to run the code.

### Setting up Java
In order to set up Eclipse, you first have to install the Java Development Kit (JDK).  To run all Eclipse 4.6 (Neon) packages you will need JDK version 8, which can be downloaded from http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html . You can simply download the package corresponding to your system and follow the instructions on the website to install it. Linux users may wish to follow this guide https://docs.oracle.com/javase/8/docs/technotes/guides/install/linux_jdk.html .

### Installing Eclipse
Then you can download and install Eclipse Neon from https://www.eclipse.org/downloads/ . Click "Download" and follow the instructions to complete the installation.

### Importing the Project
The simplest way to import the project is to download the files on your computer, run Eclipse and then go to File -> Import -> General and select Existing Projects into Workspace. Then navigate to the directory where you downloaded your files and select it.
Eclipse also provides a neat way to import files directly from github - go to File -> Import -> Git and select Projects from Git. Click Next, select Clone URI and click Next. Then paste the github URL of this project (https://github.com/AIGamingCom/TexasHoldEm_Java) and click Next. The master branch is selected by default so click Next again. Finally, choose your preferred destination of the project and click Finish.

### Running the Demo Client
You can run the project by pressing Ctrl+F11. This should start the client's form where you can use your bot's details to login and start playing.

### Adding Logic to Your Bot
In order to define your own logic, you have to open the TexasHoldEm class. Once you open it, you will find a method called calculateMove. There you define how much you want to bet or whether you want to fold. There are various helper values and methods that allow you to determine your cards, the board cards and all other useful information that can help you build your logic.

### Final Remarks
Hopefully now you're in place to get picking through the demo code. This should help you figure out how the API works, and ultimately starting work on your army of intelligent bots! We look forward to seeing what you build!
