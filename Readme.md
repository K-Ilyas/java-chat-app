# Java Chat App

This is a simple chat application built using Java and JavaFX.

## Features

- User registration and login
- Real-time messaging
- Group chat functionality
- File sharing

## Technologies Used

- Java
- JavaFX
- Socket programming
- JavaFX Scene Builder

## Getting Started

1. Clone the repository.
2. Open the project in your preferred Java IDE.
3. Build and run the application.
<!-- 
## Screenshots

![Login Screen](screenshots/login.png)
![Chat Screen](screenshots/chat.png) -->


<!-- ## Contributing

Contributions are welcome! Please fork the repository and submit a pull request. -->
# Team

* Boussoualef Mohamed Amine
* Ahmed Guetti
* Kritet Ilyas
## Our application core idea
### General vision 

The main objectif of our application is to simplify the communication  between multiple users.
we have the intetion to create an effecente and "just work" platform, due to an instance connection to answer the collaboration needs.


#### First iteration V0

In this first phase, we made a simple text chat between multiple users on the terminal as a CLI, using the power of socket, we garentide a robust and fast performence, the connextion can be between two users or a broardcast to all connected users.

Moreover we had made a simple authontification precess, even if it still not secure enough

#### Second iteration V1

Poursuivant notre engagement envers l'amélioration continue, la deuxième itération introduira des fonctionnalités plus avancées. Nous créerons des salons(rooms) pour permettre aux utilisateurs de regrouper des discussions spécifiques. 

As a next iteration in our project, wew are going to add multiple new funtionnalaties, as creating rooms and let the users to chose the room to be in. Moreover, saving all the messages and information in a database (DAO)

#### Third iteration V2

We are going to add the posibilite to send a file, making the platforme more usable as a collaboration platform.

In this step we are going to add the graphical interface, that addapte to all the current functionality

### Fourth iteration V3
Add multiple functionnality to finalise the project, for exemple:
- Reaction to messages
- change profile information
- answer a specifique message


## Technologies Stack
Our choise of technologies was made to garante a fluid developing process and an optimised result, keeping in mind the end user expirience 

- Java: we chose Java for it's popularity and performance while maintaining a solid developing experince  

- JavaFX: As one of the most famous Desktop application framework. helping us to keep the UI as clean and straight forward as possible

- mysql: our database of choise, for our application 
# Interface

En tant qu'application desktop, notre interface sera conçue de manière à être à la fois fonctionnelle et esthétiquement plaisante. Des boutons intuitifs faciliteront la navigation, tandis que les zones de texte aux saisies du clavier, créant ainsi une expérience utilisateur immersive et agréable. Nous nous engageons à fournir une interface interactive.

Althought our application is a Desktop application, we are going to try making it 
functional as well as aesthetically plesent when it comes to the UI,


# class diagram
![uml_diagram](https://github.com/K-Ilyas/java-chat-app/assets/61426347/52cd08c3-837f-4a8e-82b8-4cd5a3050420)


# Use Case 

<img width="673" alt="image" src="https://github.com/K-Ilyas/java-chat-app/assets/124268899/1d81f5d8-cc94-44bd-8543-ede042173706">

# Technical implementation 

### ExecutorService

- ExecutorService create a pool of Threads
- send tasks (runnable or callable) to ExecutorService .
- get the result of the tasks in the form of Future.
###### Implementation 

 The Executors class allows you to create an ExecutorService with different
Thread Pool types.
 - Executors.newFixedThreadPool(int n)
   - Create an ExecutorService with a fixed number n of Threads and keep them always active
   - If they are all busy the tasks will be placed in a queue
 - Executors.newCachedThreadPool()
   - For a new task It uses an available Threads otherwise it creates a new one.
   - If a Thread remains unblemished for a period (one minute) it will be deleted
automatically.
 - Executors.newSingleThreadExecutor()
   - Creates a single Thread for sequential execution of several tasks.

![executor](https://github.com/K-Ilyas/java-chat-app/assets/61426347/37136685-5e66-4554-9caa-a43e98a690af)

# flowchart 
![flowchart](https://github.com/K-Ilyas/java-chat-app/assets/61426347/fde1d7df-2018-4128-bf17-8bd3de934d51)

# MCD
<img width="638" alt="mcd" src="https://github.com/K-Ilyas/java-chat-app/assets/124268899/21a63f1f-4d1b-48cc-98d5-41919c3b99c7">

# mockup

[wireframe.pdf](https://github.com/K-Ilyas/java-chat-app/files/14418175/wireframe.pdf)

## License

This project is licensed under the [MIT License](LICENSE).
