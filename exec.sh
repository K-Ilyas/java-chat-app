#!/bin/bash


## clean code
javac --module-path "/home/guetti/IHM/java-chat-app/lib/" --add-modules javafx.controls,javafx.fxml $*
java -Dprism.verbose=true  --module-path "/home/guetti/IHM/java-chat-app/lib/" --add-modules javafx.controls,javafx.fxml $*