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
# L'équipe

* Ahmed Guetti
* Kritet Ilyas
* Boussoualef Mohamed Amine
## L'idée de l'application

### Vision générale

L'objectif principal de notre application est de faciliter la communication entre utilisateurs, en créant une plateforme conviviale et efficace. Nous visons à établir des connexions instantanées pour répondre aux besoins de collaboration dans un monde toujours plus connecté.
#### Première itération

Dans cette première phase, nous mettons en œuvre un chat simple entre plusieurs utilisateurs. L'utilisation de textes bruts, associée à la puissance des sockets, garantira une connexion robuste et rapide. Les échanges entre utilisateurs seront ainsi instantanés.

#### Deuxième itération

Poursuivant notre engagement envers l'amélioration continue, la deuxième itération introduira des fonctionnalités plus avancées. Nous créerons des salons(rooms) pour permettre aux utilisateurs de regrouper des discussions spécifiques. 
#### Troisième itération

Nous intégrerons la possibilité d'envoyer des fichiers, rendant la plateforme plus polyvalente et adaptée à une variété de besoins de communication ( media repository ).

### Quatrième itération 

Ajouter une nouvelle fonctionnalité qui permet de creer des stories et répondre sur un message
## Technologies

Notre choix de technologies reflète notre engagement envers des solutions robustes et évolutives pour offrir une expérience utilisateur optimale.

- Java servira de base solide pour la logique applicative.

- JavaFX sera utilisé pour créer une interface utilisateur attrayante et conviviale.

- Les sockets garantiront une connectivité fiable, essentielle pour des échanges en temps réel.
# Type d'interface

En tant qu'application desktop, notre interface sera conçue de manière à être à la fois fonctionnelle et esthétiquement plaisante. Des boutons intuitifs faciliteront la navigation, tandis que les zones de texte aux saisies du clavier, créant ainsi une expérience utilisateur immersive et agréable. Nous nous engageons à fournir une interface interactive.
# class diagram

![JAVA_CHAT_CLASS_UML drawio](https://github.com/K-Ilyas/java-chat-app/assets/61426347/94c3bd5f-b678-4483-802a-e755005852f6)

# Use Case 

<img width="476" alt="image" src="https://github.com/K-Ilyas/java-chat-app/assets/61426347/07055bf7-abed-46db-b03f-f98ec8e9c25e">

# Technical implementation 

### ExecutorService

- ExecutorService crees un ensemble de Threads
- Envoyer des taches (runnable ou callable) à l’ExecutorService .
- Recuperer les resultats des tâches sous forme de Future.
###### Implementation 

 La classe Executors permet créer un ExecutorService avec differents 
types de Thread Pool.
 - Executors.newFixedThreadPool(int n)
   - Créer un ExecutorService avec un nombre fixe n de Threads et les garde toujour actives
   - S'ils sont tous occupés les tâches seront placé dans une file d’attente
 - Executors.newCachedThreadPool()
   - Pour une nouvelle tâche Il utilise un Threads disponible sinon il crée un nouveau.
   - Si un Thread reste sans tache pour une période(une minute) il sera supprimé 
automatiquement.
 - Executors.newSingleThreadExecutor()
   - Crée un seul Thread pour une exécution séquentielle de plusieur tâches.
![executor](https://github.com/K-Ilyas/java-chat-app/assets/61426347/37136685-5e66-4554-9caa-a43e98a690af)

# mockup

[wireframe.pdf](https://github.com/K-Ilyas/java-chat-app/files/14418175/wireframe.pdf)

## License

This project is licensed under the [MIT License](LICENSE).
