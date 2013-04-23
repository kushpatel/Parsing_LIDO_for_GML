REM Compile all the .java files in JSONHarvester folder
javac JSONHarvester\*.java

REM Package the project into an executable jar file
jar cvfm JsonHarvester.jar manifest.mf JSONHarvester\*.*