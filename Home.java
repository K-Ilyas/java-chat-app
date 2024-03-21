import java.sql.Date;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;


/*
 * 
 * 
 * 
 * https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm
 * 
 * 
 * 
*/
public class Home extends Application {

    private UserInformation LoggedInUser;
    private VBox chatArea;
    private TextArea messageArea;
    private TextField messageInput;

    // SocketClient client = new SocketClient();

    private MessageToDAO DB_messages = new MessageToDAO(MySQLConnectSingleton.getInstance());
    private RoomDAO DB_room = new RoomDAO(MySQLConnectSingleton.getInstance());
    private MessageRoomDAO DB_messages_room = new MessageRoomDAO(MySQLConnectSingleton.getInstance());
    private UserDAO user_orm = new UserDAO(MySQLConnectSingleton.getInstance());


    private Thread recive = null;
    private Thread send = null;


    ListView<UserInformation> contactList;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chat Application");

        if(Session.isLoggedIn()) { 
            LoggedInUser = Session.getLoggedInUser();
            System.out.println(LoggedInUser.getPseudo());

            
        } 
        
       
        // Label helloWorldLabel = new Label("Hello world!");
        // helloWorldLabel.setAlignment(Pos.CENTER);
        // Scene primaryScene = new Scene(helloWorldLabel);
        // primaryStage.setScene(primaryScene);

        BorderPane root = new BorderPane();

        // Contact Panel

        BorderPane sidePanel = new BorderPane();
        sidePanel.setPrefWidth(270);
        sidePanel.setPadding(new Insets(10));
        sidePanel.setStyle("-fx-background-color: #2C2F33");


        VBox leftPanel = new VBox();
        leftPanel.setPrefWidth(8);
        leftPanel.setPadding(new Insets(2));
        leftPanel.setStyle("-fx-background-color: #2C2F33");
        leftPanel.setSpacing(10);


        // Adding some UI element to the left panel
        // add Setting button
        Button settingButton = new Button("");
        settingButton.setStyle("-fx-background-color: #2C2F33; -fx-text-fill: white;");
        Image settingImage = new Image(getClass().getResourceAsStream("./Images/settings2.png"));
        ImageView settingImageView = new ImageView(settingImage);
        settingImageView.setFitHeight(24);
        settingImageView.setFitHeight(24);
        settingImageView.setPreserveRatio(true);
        settingImageView.setOpacity(1);

        settingButton.setGraphic(settingImageView);

        settingButton.setOnAction(e -> {
            Setting settingUI = new Setting();
            settingUI.start(new Stage());
        });

        leftPanel.setAlignment(Pos.BOTTOM_CENTER);
        leftPanel.setSpacing(10);
        leftPanel.getChildren().addAll(settingButton);
        LinkedList<UserInformation> contactsListT = user_orm.getContacts(); 

   
        // ListView<UserInformation> contactsList = new ListView<>();
        // for (UserInformation user : contactsListT){
        //     System.out.println(user);
        //     contactsList.getItems().add(user);
        // }
        // https://examples.javacodegeeks.com/java-development/desktop-java/javafx/listview-javafx/javafx-listview-example/
        ObservableList<UserInformation> users = FXCollections.<UserInformation>observableArrayList(contactsListT);
        ListView<UserInformation> contactList = new ListView<>(users);

        contactList.setPrefHeight(500);
        contactList.setStyle("-fx-control-inner-background: #2C2F33; -fx-background-insets: 0;");



        // contactList.setPrefHeight(500);

        sidePanel.setLeft(leftPanel);
        sidePanel.setCenter(contactList);

        ScrollPane scroll = new ScrollPane();
        scroll.setStyle("-fx-background-color: #23272A");

        chatArea = new VBox();
        chatArea.setPadding(new Insets(10));
        chatArea.setSpacing(10);
        chatArea.setStyle("-fx-background-color: #23272A");


        HBox topBar = new HBox();
        topBar.setPadding(new Insets(10));
        topBar.setSpacing(10);
        topBar.setStyle("-fx-background-color: #23272A");

        ToggleButton ContactsButton = new ToggleButton("Contacts");
        ToggleButton RoomsButton = new ToggleButton("Rooms");
        ContactsButton.setStyle("-fx-background-color: #7289DA; -fx-text-fill: white;");
        RoomsButton.setStyle("-fx-background-color: #7289DA; -fx-text-fill: white;");
        Button newContact = new Button("+");
        
        newContact.setStyle("-fx-background-color: #a1a4ad; -fx-text-fill: white;");
        newContact.setPrefWidth(30);
        newContact.setPrefHeight(26);
        newContact.setPadding(new Insets(0));
        newContact.setOpacity(1);
        newContact.setFont(new Font("Arial", 20));

        Label nameLabel = new Label(LoggedInUser.getPseudo());
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 17px; -fx-font-weight: bold;");
        nameLabel.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(nameLabel, Priority.ALWAYS); // Give Label any extra space

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label messageCount = new Label("Friends: " + contactList.getItems().size());
        // Label messageCount = new Label("Friends: " + 7);
        messageCount.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        topBar.getChildren().addAll(ContactsButton, RoomsButton, newContact,messageCount, spacer,nameLabel);
        topBar.setSpacing(10);



        newContact.setOnAction(e->{
            NewContact.display();
        });

        ContactsButton.setOnAction(e->{
            if(ContactsButton.isSelected()){
                contactList.setPrefHeight(500);
                contactList.setStyle("-fx-control-inner-background: #2C2F33; -fx-background-insets: 0;");
                    // https://stackoverflow.com/questions/29697800/javafx-listview-getting-the-text-value-of-a-cell-in-the-list
                    contactList.setCellFactory(lv -> new ListCell<UserInformation>() {
                        @Override
                        public void updateItem(UserInformation user, boolean empty) {
                            super.updateItem(user, empty);
                            if (empty || user == null) {
                                setText(null);
                            } else {
                                setText(user.getPseudo());
                                setStyle("-fx-background-color: #f0f2f5; -fx-padding: 5;");
                            }
                            setStyle("-fx-text-fill: white;");
                        }
                    });
                sidePanel.setCenter(contactList);
            }
        });





        ObjectProperty<UserInformation> selectedUserProperty = new SimpleObjectProperty<>();
        ObjectProperty<MessageTo> selectedMessageProperty = new SimpleObjectProperty<>();
        // List<MessageTo> messages = DB_messages.findAll(selectedUserProperty.get().getUuid(), LoggedInUser.getUuid());
        
        List<MessageTo> messages = new ArrayList<MessageTo>();

        messages.add(new MessageTo("07cd0cd2-a63c-4935-974b-bd0f2dc92f8b", "7758ad13-eb98-49d5-8e86-3d43a06488f5", "Message 1", new Date(System.currentTimeMillis()), false));
        messages.add(new MessageTo("07cd0cd2-a63c-4935-974b-bd0f2dc92f8b", "7758ad13-eb98-49d5-8e86-3d43a06488f5", "Message 2", new Date(System.currentTimeMillis()), false));
        messages.add(new MessageTo("07cd0cd2-a63c-4935-974b-bd0f2dc92f8b", "7758ad13-eb98-49d5-8e86-3d43a06488f5", "Message 3", new Date(System.currentTimeMillis()), false));
        messages.add(new MessageTo("07cd0cd2-a63c-4935-974b-bd0f2dc92f8b", "eaac235b-4988-4a0b-8a3f-b6bd3e365dee", "Message 4", new Date(System.currentTimeMillis()), false));
        messages.add(new MessageTo("eaac235b-4988-4a0b-8a3f-b6bd3e365dee", "07cd0cd2-a63c-4935-974b-bd0f2dc92f8b", "Message 5", new Date(System.currentTimeMillis()), false));
        messages.add(new MessageTo("eaac235b-4988-4a0b-8a3f-b6bd3e365dee", "07cd0cd2-a63c-4935-974b-bd0f2dc92f8b", "Message 6", new Date(System.currentTimeMillis()), false));
        messages.add(new MessageTo("7758ad13-eb98-49d5-8e86-3d43a06488f5", "eaac235b-4988-4a0b-8a3f-b6bd3e365dee", "Message 7", new Date(System.currentTimeMillis()), false));
        messages.add(new MessageTo("7758ad13-eb98-49d5-8e86-3d43a06488f5", "eaac235b-4988-4a0b-8a3f-b6bd3e365dee", "Message 8", new Date(System.currentTimeMillis()), false));
        messages.add(new MessageTo("eaac235b-4988-4a0b-8a3f-b6bd3e365dee", "357d30b4-1938-4322-9606-b774e079ae15", "Message 9", new Date(System.currentTimeMillis()), false));


        
        contactList.setCellFactory(lv -> new ListCell<UserInformation>() {
            @Override
            protected void updateItem(UserInformation user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(user.getPseudo());
                    setStyle("-fx-background-color: #f0f2f5; -fx-padding: 5;");
                    setOnMouseClicked(event -> {
                        selectedUserProperty.set(user);
                        System.out.println(user.getPseudo());
                       

                        messageCount.setText("Message Count: " + messages.size());
                        messages.sort(Comparator.comparing(MessageTo::getMessage_date));

                        System.out.println(messages.size());
                        
                        
                        chatArea.getChildren().clear();
                        for(MessageTo message : messages){

                            Label messageLabel;
                            if (message.getUuid_sender() == LoggedInUser.getUuid()) {

                                messageLabel = new Label( "You: "+ message.getMessage());
                                messageLabel.setStyle("-fx-background-color: #484b54; -fx-text-fill: white; -fx-background-radius: 10px;");
                                messageLabel.setPadding(new Insets(10));
                                messageLabel.setWrapText(true);
                                messageLabel.setMaxWidth(300);
                                messageLabel.setMinWidth(300);
                                messageLabel.setPrefWidth(300);
                                
                                chatArea.getChildren().add(messageLabel);
                               
                            } else {
                                messageLabel = new Label( message.getMessage());

                                messageLabel.setStyle("-fx-background-color: #7289DA; -fx-text-fill: white; -fx-background-radius: 10px;");
                                messageLabel.setPadding(new Insets(10));
                                messageLabel.setWrapText(true);
                                messageLabel.setMaxWidth(300);
                                messageLabel.setMinWidth(300);
                                messageLabel.setPrefWidth(300);



                                chatArea.getChildren().add(messageLabel);
                            }


                            

                            messageLabel.setOnMouseClicked(mouseevent -> {
                                selectedMessageProperty.set(message);
                                displayMessageOptions(selectedMessageProperty.get());
                            });
                           
                        }
                    });
                }
                setStyle("-fx-text-fill: white;");
            }
        });





        ObjectProperty<Room> selectedRoomProperty = new SimpleObjectProperty<>();


        // List<Room> RoomsListT = DB_room.findAllRoom();
        // ObservableList<Room> observableList = FXCollections.observableArrayList(RoomsListT);
        // ListView<Room> RoomsList = new ListView<>(observableList);

        ListView<Room> RoomsList = new ListView<>();

        RoomsList.getItems().add(new Room("uuid1", "Room 1", "image1.jpg"));
        RoomsList.getItems().add(new Room("uuid2", "Room 2", "image2.jpg"));
        RoomsList.getItems().add(new Room("uuid3", "Room 3", "image3.jpg"));
        RoomsList.getItems().add(new Room("uuid4", "Room 4", "image4.jpg"));
        RoomsList.getItems().add(new Room("uuid5", "Room 5", "image5.jpg"));
        RoomsList.getItems().add(new Room("uuid6", "Room 6", "image6.jpg"));
        RoomsList.getItems().add(new Room("uuid7", "Room 7", "image7.jpg"));
        RoomsList.getItems().add(new Room("uuid8", "Room 8", "image8.jpg"));
        RoomsList.getItems().add(new Room("uuid9", "Room 9", "image9.jpg"));

        System.out.println(RoomsList.getItems().size());

        RoomsButton.setOnAction(event -> {
            if (RoomsButton.isSelected()) {
                
                RoomsList.setPrefHeight(500);
                RoomsList.setStyle("-fx-control-inner-background: #2C2F33; -fx-background-insets: 0;");


                RoomsList.setCellFactory(lv -> new ListCell<Room>() {
                    @Override
                    protected void updateItem(Room Room, boolean empty) {
                        super.updateItem(Room, empty);
                        if (empty || Room == null) {
                            setText(null);
                        } else {
                            setText(Room.getRoomname());
                            setStyle("-fx-background-color: #f0f2f5; -fx-padding: 5;");
                        }
                        setStyle("-fx-text-fill: white;");
                    }
                });
                sidePanel.setCenter(RoomsList);
            }
        });

        RoomsList.setOnMouseClicked(event -> {
            selectedUserProperty.set(null);
            Room selectedRoom = RoomsList.getSelectionModel().getSelectedItem();
            if (selectedRoom != null) {
                selectedRoomProperty.set(selectedRoom);
                System.out.println(selectedRoom.getRoomname());
        
                List<Messageroom> messagesRoom = DB_messages_room.findAllMessageInRoom(selectedRoomProperty.get().getUuid_room());
                
                messageCount.setText("Message Count: " + messages.size());

                messagesRoom.sort(Comparator.comparing(Messageroom::getDate));


                chatArea.getChildren().clear();
                for (Messageroom message : messagesRoom) {
                    if (message.getUuid_user() == LoggedInUser.getUuid()) {
                        Label messageLabel = new Label("You: " + message.getMessage());
                        messageLabel.setStyle("-fx-background-color: #484b54; -fx-text-fill: white; -fx-background-radius: 10px;");
                        messageLabel.setPadding(new Insets(10));
                        messageLabel.setWrapText(true);
                        messageLabel.setMaxWidth(300);
                        messageLabel.setMinWidth(300);
                        messageLabel.setPrefWidth(300);
        
                        chatArea.getChildren().add(messageLabel);
                    } else {
                        Label messageLabel = new Label(message.getUuid_user() + ": " + message.getMessage());
                        messageLabel.setStyle("-fx-background-color: #7289DA; -fx-text-fill: white; -fx-background-radius: 10px;");
                        messageLabel.setPadding(new Insets(10));
                        messageLabel.setWrapText(true);
                        messageLabel.setMaxWidth(300);
                        messageLabel.setMinWidth(300);
                        messageLabel.setPrefWidth(300);
        
                        chatArea.getChildren().add(messageLabel);
                    }
                }
            }
        });
        
    
        
        // Create a VBox for the chat area
        chatArea.setPadding(new Insets(10));
        chatArea.setSpacing(30);
        chatArea.setStyle("-fx-background-color: #23272A");

        // Create a TextArea for displaying the messages
        messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setStyle("-fx-control-inner-background: #2C2F33; -fx-text-fill: white;");

        // Create a HBox for the bottom bar (message input and send button)
        HBox bottomBar = new HBox();
        bottomBar.setSpacing(0);
        bottomBar.setAlignment(Pos.BOTTOM_LEFT);
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(0, 0, 10, 0));

        

        messageInput = new TextField();

        messageInput.setStyle("-fx-control-inner-background: #2C2F33; -fx-text-fill: white;");

        HBox.setHgrow(messageInput, Priority.ALWAYS);

        Button sendButton = new Button("Send");

        sendButton.setOnAction(e -> {
            if (selectedUserProperty.get() != null) {
                // sendContactMessage(selectedUserProperty);
                System.out.println("Sending message to user");
            } else if (selectedRoomProperty.get() != null) {
                // sendRoomMessage(selectedRoomProperty);
                // send Message
                System.out.println("Sending message to Room");
            }else{
                System.out.println("No user or Room selected");
            }
        });
        

        sendButton.setStyle("-fx-background-color: #7289DA; -fx-text-fill: white;");


        // Add the message area and bottom bar to the chat box
        chatArea.getChildren().addAll(messageArea, bottomBar);
        bottomBar.getChildren().addAll(messageInput, sendButton);

        //set bottombar to the bottom of the chatbox
        // VBox.setVgrow(chatArea, Priority.ALWAYS);
        scroll.setContent(chatArea); 

        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);

        BorderPane wholeChat = new BorderPane();
        wholeChat.setCenter(scroll);
        wholeChat.setBottom(bottomBar);
   

        root.setLeft(sidePanel);
        root.setCenter(wholeChat);
        root.setTop(topBar);



        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    
    }





    public static void main(String[] args) {
        Application.launch(args);
    }

























    // private void sendContactMessage(ObjectProperty<UserInformation> selectedUserProperty) {
    //     String messageContent = messageInput.getText();
    //     UserInformation Recipient =  selectedUserProperty.get(); 

    //     // Create a unique ID for the message, by finding the highest ID in the Message collection and incrementing it by one
    //     MongoClient mongoClient = new MongoClient("localhost", 27017);
    //     MongoDatabase database = mongoClient.getDatabase("ChatApp");
    //     MongoCollection<Document> messageCollection = database.getCollection("Message");
        
    //     // Find the highest ID in the Message collection and increment it by one
    //     int messageId = 1;
    //     FindIterable<Document> results = messageCollection.find().sort(Sorts.descending("message_id")).limit(1);
        
    //     if (results.first() != null) {
    //         messageId = results.first().getInteger("message_id") + 1;
    //     }

    //     MessageTo newMessage = new MessageTo(LoggedInUser.getUuid(), Recipient.getUuid(), messageContent, LocalDateTime.now().toString(), false);
    //     System.out.println(newMessage.toString());

        

    //     storeMessageInDatabase(newMessage);
    
    //     // Clear the input field after sending the message
    //     messageInput.clear();

    //     // Retrieve the updated list of messages
    //     List<MessageTo> updatedMessages = MessageList.getContactMessages(selectedUserProperty.get());
    //     // Sort the updated messages
    //     updatedMessages.sort(Comparator.comparing(MessageTo::getMessage_date));
    //     // Clear the message area
    //     chatArea.getChildren().clear();
        



    //     String pseudo = "", name = "";
    //     Scanner sc = new Scanner(System.in);
    //     try {
    //         do {
    //             pseudo = sc.nextLine();
    //             out.writeUTF(pseudo.toString());
    //             if (!pseudo.toUpperCase().equals("N")) {
    //                 System.out.println("Enter your message ===>> ");
    //                 name = sc.nextLine();
    //                 out.writeUTF(name.toString());
    //             }
    //             out.flush();
    //             bos.writeObject(user);
    //             bos.flush();
    //             bos.flush();

    //             API.printMessage("\n Enter the user pseudo or nothing to brodcast ===>> ");


    //         } while (!pseudo.toUpperCase().equals("N"));

    //         sc.close();

    //     } catch (FileNotFoundException e) {
    //         e.printStackTrace();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }



    //     for (MessageTo message : updatedMessages) {
    //         Label messageLabel;
    //         if (message.getUuid_sender() == LoggedInUser.getUuid()) {
    //             messageLabel = new Label("You: " + message.getMessage());
    //             messageLabel.setStyle("-fx-background-color: #484b54; -fx-text-fill: white; -fx-background-radius: 10px;");
    //         } else {
    //             messageLabel = new Label(message.getMessage());
    //             messageLabel.setStyle("-fx-background-color: #7289DA; -fx-text-fill: white; -fx-background-radius: 10px;");
    //         }
    //         messageLabel.setPadding(new Insets(10));
    //         messageLabel.setWrapText(true);
    //         messageLabel.setMaxWidth(300);
    //         messageLabel.setMinWidth(300);
    //         messageLabel.setPrefWidth(300);
    //         chatArea.getChildren().add(messageLabel);
    //     }

    // }

    // private void sendGroupMessage(ObjectProperty<Group> selectedGroupProperty) {
    //     // Get the content of the message from your input field
    //     String messageContent = messageInput.getText();
        
    //     Group group = selectedGroupProperty.get();
        
    //     // Create a unique ID for the message, by finding the highest ID in the Message collection and incrementing it by one
    //     int messageId = 1;
    //     MongoClient mongoClient = new MongoClient("localhost", 27017);
    //     MongoDatabase database = mongoClient.getDatabase("ChatApp");
    //     MongoCollection<Document> messageCollection = database.getCollection("Message");
        
    //     FindIterable<Document> results = messageCollection.find().sort(Sorts.descending("message_id")).limit(1);
        
    //     if (results.first() != null) {
    //         messageId = results.first().getInteger("message_id") + 1;
    //     }
        
    //     // Create a new instance of the Message class with the necessary information
    //     Message newMessage = new Message(messageId, loggedInUser.getUserId(), group.getGroupId(), "group", messageContent, LocalDateTime.now().toString());
        
    //     // Store the new message in the Message collection (replace this with your MongoDB logic)
    //     storeMessageInDatabase(newMessage);
        
    //     // Clear the input field after sending the message
    //     messageInput.clear();
        
    //     // Retrieve the updated list of messages
    //     List<Message> updatedMessages = UserGroupMessagesList.getGroupMessages(selectedGroupProperty.get());
    //     // Sort the updated messages
    //     updatedMessages.sort(Comparator.comparing(Message::getMessageId));
        
    //     // Clear the message area
    //     chatBox.getChildren().clear();
        
    //     // Iterate over the updated messages and display them
    //     for (Message message : updatedMessages) {
    //         Label messageLabel;
    //         if (message.getSenderId() == loggedInUser.getUserId()) {
    //             messageLabel = new Label("You: " + message.getContent());
    //             messageLabel.setStyle("-fx-background-color: #484b54; -fx-text-fill: white; -fx-background-radius: 10px;");
    //         } else {
    //             messageLabel = new Label(message.getSenderId() + ": " + message.getContent());
    //             messageLabel.setStyle("-fx-background-color: #7289DA; -fx-text-fill: white; -fx-background-radius: 10px;");
    //         }
    //         messageLabel.setPadding(new Insets(10));
    //         messageLabel.setWrapText(true);
    //         messageLabel.setMaxWidth(300);
    //         messageLabel.setMinWidth(300);
    //         messageLabel.setPrefWidth(300);
    //         chatBox.getChildren().add(messageLabel);
    //     }
    // }






    private void displayMessageOptions(MessageTo selectedMessage) {
        if (selectedMessage == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Message Options");
        alert.setHeaderText("Choose an option for the message:");
        alert.setContentText(selectedMessage.getMessage());

    
        ButtonType editButton = new ButtonType("Edit");
        ButtonType deleteButton = new ButtonType("Delete");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    
        alert.getButtonTypes().setAll(editButton, deleteButton, cancelButton);
    
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == editButton) {
                // Handle the edit option
                // editMessage(selectedMessage);
            } else if (result.get() == deleteButton) {
                // Handle the delete option
                // deleteMessage(selectedMessage);
            }
        }
    }



    // private void editMessage(MessageTo message) {
    //     // Implement the logic to allow the user to edit the message
    
    //     TextInputDialog dialog = new TextInputDialog(message.getMessage());
    //     dialog.setTitle("Edit Message");
    //     dialog.setHeaderText(null);
    //     dialog.setContentText("Enter the new message:");
    //     // input field?
    // }
    
    // private void deleteMessage(Message message) {
        
       


    // }


    











}