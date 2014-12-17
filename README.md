CS263WorldChat
==============

##CS 263 Project

Available [here](http://www.worldchat263.appspot.com)

This project is meant to connect people from around the world for the purposes of cultural exchange and language learning.  

This project is a google app engine app that is deployed using Apache Maven.  The Api for this project depends on a gson jar located at target/WorldChat-0.0.1-SNAPSHOT/WEB-INF/lib.  

#Javadocs

Javadocs may are stored in the doc folder, they contain documentation for the WorldChat Project as well as it's ApiTester Client.  [They may be viewed here.](http://cs.ucsb.edu/~shayon/CS263/doc/)

#Selenium Tests

There are Selenium traces to test the UI located in the Selenium Tests Folder.  To run them get the Selenium extension on Firefox and select File->Open then navigate to the file named "test suite" located in the Selenium Tests folder and select it.  It should be noted that some Tests involve uploading files, these will need to be modified if run on your own machine, as they contain filepaths specific to my computer.

##API Tester

  There is an [Api Client](https://github.com/Shayon/CS263ApiTester) for this app.  The Api Tester project files must be in the same eclipse workspace as CS263WorldChat as it uses the gson jar that is in the lib of target.  The Tester also uses Apache Commons IO.
  The tester makes use of the exposed post and get at '/interact' and allows a user to choose either to do a get or post on the command line.  For each GET and POST the user performs an HTTP Response Status is printed to System.err.  