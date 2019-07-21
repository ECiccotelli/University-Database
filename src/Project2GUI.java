import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;


import java.sql.*;


public class Project2GUI extends Application
{
    int currentlySelected = 0;  // 1- Instructor,   2 - Department,     3 - New Record

    private Button instructorButton;
    private Button departmentButton;
    private Button insertNewButton;
    private Button executeButton;

    private Label idLabel;
    private Label departmentLabel;
    private Label nameLabel;

    public Text currentlySelectedText;

    private TextField idTextField;
    private TextField departmentTextField;
    private TextField nameTextField;

    private Text output;

    private Text usernameText;
    private Text passwordText;

    private TextField username;
    private PasswordField password;

    private Button loginButton;
    private Text loginStatus;

    private Button logout;


    public void start(Stage primaryStage) throws SQLException, ClassNotFoundException
    {
        //Overall border pane to hold other panes
        BorderPane pane = new BorderPane();
        //pane.setLeft(getVBox());
        //pane.setMinSize(200, 100);
        usernameText = new Text("Enter username: ");
        username = new TextField();

        passwordText = new Text("Enter password: ");
        password = new PasswordField();

        loginButton = new Button("Login");
        loginStatus = new Text();

        logout = new Button("4. Logout");


        //output = new Text("Output: ");
        output = new Text();


        instructorButton = new Button("1. Instructor");
        departmentButton = new Button("2. Department");
        insertNewButton = new Button("3. Insert New");

        executeButton = new Button("Execute");


        idLabel = new Label("ID: ");
        departmentLabel = new Label("Department: ");
        nameLabel = new Label("Instructor Name: ");


        idTextField = new TextField();
        departmentTextField = new TextField();
        nameTextField = new TextField();

        currentlySelectedText = new Text("Currently Selected: None");

        //Creates left vBox
        VBox vBox = new VBox(5);
        vBox.setPadding(new Insets(15, 5, 5, 5));
        vBox.getChildren().add(instructorButton);
        vBox.getChildren().add(departmentButton);
        vBox.getChildren().add(insertNewButton);
        vBox.getChildren().add(logout);
        vBox.getChildren().add(currentlySelectedText);

        //Sets left vBox
        pane.setLeft(vBox);
        pane.setBottom(output);
        //pane.setPadding(new Insets(15, 5, 5, 5));

        //Creates right grid pane
        GridPane g = new GridPane();
        g.setPadding(new Insets(15, 5, 5, 5));

        //Row 0
        g.add(idLabel, 0, 0);
        g.add(idTextField, 1, 0);

        //Row 1
        g.add(departmentLabel, 0, 1);
        g.add(departmentTextField, 1, 1);

        //Row 2
        g.add(nameLabel, 0, 2);
        g.add(nameTextField, 1, 2);

        //Row 3: Button
        g.add(executeButton, 1, 3);
        g.setVgap(5);
        //g.setAlignment(Pos.CENTER_RIGHT);
        //g.getColumnConstraints().add(new ColumnConstraints(0, HPos.RIGHT));

        //Sets right grid pane
        pane.setRight(g);

        primaryStage.setMinHeight(350);
        primaryStage.setMinWidth(500);

        //output.setTextAlignment(TextAlignment.CENTER);


        GridPane loginGrid = new GridPane();
        //loginGrid.setPadding(new Insets(15, 5, 5, 5));

        //Row 0
        loginGrid.add(usernameText, 0, 0);
        loginGrid.add(username, 1, 0);

        //Row 1
        loginGrid.add(passwordText, 0, 1);
        loginGrid.add(password, 1, 1);

        //Row 2
        loginGrid.add(loginButton, 1, 2);

        //Row 3
        loginGrid.add(loginStatus, 1, 3);

        loginGrid.setVgap(5);

        BorderPane wrapperBorder = new BorderPane();
        wrapperBorder.setPadding(new Insets(75, 125, 75, 125));

        wrapperBorder.setCenter(loginGrid);
        //wrapperBorder.setBottom(new ImageView(new Image("File:MC_logo.png")));

        wrapperBorder.setStyle("-fx-background-color: #9de29c;");
        Scene loginScene = new Scene(wrapperBorder);
        primaryStage.setScene(loginScene);

        pane.setStyle("-fx-background-color: #9de29c;");
        Scene scene = new Scene(pane);
        primaryStage.setTitle("Manhattan College Database V2");
        primaryStage.getIcons().add(new Image("MC_logo.png"));

        //primaryStage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        loginScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        primaryStage.show();


        //LAMBDA EXPRESSIONS FOR BUTTONS ON LEFT OF GUI
        instructorButton.setOnAction(e ->{
            currentlySelectedText.setText("Currently Selected: Instructor");
            currentlySelected = 1;

            //Clear all text fields
            clearTextFields();

            //Enable editing ID text field, Disable Department and Instructor Name fields
            idTextField.setEditable(true);
            departmentTextField.setEditable(false);
            nameTextField.setEditable(false);
        });

        departmentButton.setOnAction(e ->{
            currentlySelectedText.setText("Currently Selected: Department");
            currentlySelected = 2;

            //Clear all text fields
            clearTextFields();

            //Enable editing department text field, Disable ID and Instructor Name Fields
            departmentTextField.setEditable(true);
            idTextField.setEditable(false);
            nameTextField.setEditable(false);

        });

        insertNewButton.setOnAction(e ->{
            currentlySelectedText.setText("Currently Selected: Insert New");
            currentlySelected = 3;

            //Clear all text fields
            clearTextFields();

            //Set all editable
            departmentTextField.setEditable(true);
            idTextField.setEditable(true);
            nameTextField.setEditable(true);

        });

        //System.out.println("Testing start fcn");
        Statement s = declareStatements();
        executeButton.setOnAction(e ->{
            try
            {
                executeQuery(s);
            }catch(Exception ex)
            {
                System.out.println("Exception occurred on executeQuery" + ex);
            }


        });

        loginButton.setOnAction(e ->{
            String user = username.getText();
            String pass = password.getText();
            try{
                if(loggedIn(s, user, pass))
                    primaryStage.setScene(scene);
                else
                {
                    loginStatus.setText("Username or password incorrect \nPlease try again");
                    loginStatus.setStyle("-fx-fill: red;");
                }


            }
            catch(Exception ex)
            {
                System.out.println("SQL Exception " + ex);
            }

        });
        logout.setOnAction(e ->{
            loginStatus.setText("");
            primaryStage.setScene(loginScene);
            clearTextFields();
        });


    }

    public boolean loggedIn(Statement s, String username, String password) throws SQLException, ClassNotFoundException
    {
        String query = "select username, password from login where username = '" + username + "' " +
                "and password = '" + password + "';";

        //System.out.print(query);

        if(username == "" || password == "")
            return false;

        ResultSet rs = s.executeQuery(query);

        if(rs.next() == false)
            return false;
        else
            return true;

    }

    public void clearTextFields()
    {
        departmentTextField.clear();
        idTextField.clear();
        nameTextField.clear();
        password.clear();
        username.clear();
        output.setText("");
    }

    public void updateStatusText()
    {
        String s = "Currently Selected: ";
        switch(currentlySelected)
        {
            case 0:
                break;
            case 1:
                currentlySelectedText.setText(s + " Instructor");
                break;
            case 2:
                currentlySelectedText.setText(s + " Department");
                break;
            case 3:
                currentlySelectedText.setText(s + " Insert New");
                break;

        }

    }

    public void executeQuery(Statement s) throws SQLException, ClassNotFoundException
    {
        if(currentlySelected == 1)
        {
            //Get instructor ID from text field
            String ID = idTextField.getText();
            //System.out.println("Text is: " + ID);
            String o = instructorID(s, ID);
            output.setText(o);
        }
        else if(currentlySelected == 2)
        {
            //Get department name from text field
            String deptName = departmentTextField.getText();
            String o = departmentName(s, deptName);
            output.setText(o);
        }
        else if(currentlySelected == 3)
        {
            //Statement s, String ID, String name, String dept_name
            //Get info from text fields
            String ID = idTextField.getText();
            String name = nameTextField.getText();
            String deptName = departmentTextField.getText();
            String o = insertNew(s, ID, name, deptName);
            output.setText(o);

        }



    }

    public Statement declareStatements() throws SQLException, ClassNotFoundException
    {
        //Load the JDBC driver
        Class.forName("com.mysql.jdbc.Driver");
        System.out.println("Driver loaded");

        //Connect to a database
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/?allowPublicKeyRetrieval=true&useSSL=false", "root", "root");
        System.out.println("Database connected");

        //Create a statement
        Statement s = con.createStatement();

        //Insert statements into database
        insertStatements(s);
        return s;
    }




    public static void main(String [] args) throws SQLException, ClassNotFoundException
    {
        //Opens GUI
        launch(args);
    }

    public static void insertStatements(Statement s) throws SQLException, ClassNotFoundException
    {
        //Execute the statement
        try{
            s.executeUpdate("create database university");
        }catch(SQLException sq)
        {
            s.executeUpdate("drop database university");
            s.executeUpdate("create database university");
        }
        s.executeUpdate("use university");
        s.executeUpdate("create table department(dept_name varchar(30), location varchar(5), budget varchar(10), primary key (dept_name))");
        s.executeUpdate("create table instructor(ID varchar(5), name varchar(20), dept_name varchar(30), primary key (ID), foreign key (dept_name) references department (dept_name))");
        s.executeUpdate("create table login(username varchar(30), password varchar(30), primary key(username))");

        //Department insert statements
        s.executeUpdate("insert into department values ('CMPT', 'RLC', '75000')");
        s.executeUpdate("insert into department values ('MATH', 'RLC', '55000')");
        s.executeUpdate("insert into department values ('BIOL', 'LEO', '60000')");

        //Instructor insert statements
        s.executeUpdate("insert into instructor values ('1001', 'Robert Smith', 'CMPT')");
        s.executeUpdate("insert into instructor values ('1002', 'Natasha Anderson', 'MATH')");
        s.executeUpdate("insert into instructor values ('1003', 'James Nassimi', 'BIOL')");
        s.executeUpdate("insert into instructor values ('1004', 'Guiling Wei', 'CMPT')");
        s.executeUpdate("insert into instructor values ('1005', 'Mary Harnett', 'BIOL')");
        s.executeUpdate("insert into instructor values ('1006', 'David Ochs', 'CMPT')");

        s.executeUpdate("insert into login values ('admin', 'admin')");
        s.executeUpdate("insert into login values ('eric123', 'pass')");

    }

    public static String instructorID(Statement s, String ID) throws SQLException, ClassNotFoundException
    {
        String out = "";
        if(ID.isEmpty())
            return "Please fill in the ID field";
        String query = "select I.name, D.dept_name, D.location from instructor I, department D \n" +
                "where I.dept_name = D.dept_name and I.ID ='" + ID + "';";

        ResultSet rs = s.executeQuery(query);

        if(rs.next() == false)
        {
            out = "The ID doesn't appear in the database";
        }
        else
        {
            do{
                out = rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3);
            }while(rs.next());
        }
        return out;

    }

    public static String departmentName(Statement s, String dept_name) throws SQLException, ClassNotFoundException
    {
        String out = "";
        if(dept_name.isEmpty())
            return "Please fill in the department name field";
        String query = "select D.location, D.budget, I.name from instructor I, department D where D.dept_name ='" + dept_name + "' and I.dept_name = D.dept_name;";

        ResultSet rs = s.executeQuery(query);


        if(rs.next() == false)
        {
            out = "The department does not appear in the database";
        }
        else
        {

            out += "Location: " + rs.getString(1) + "\n";
            out += "Budget: " + rs.getString(2) + "\n";
            do{
                //System.out.println(rs.getString(3));
                out += rs.getString(3) + "\n";
            }while(rs.next());

        }
        return out;
    }

    public static String insertNew(Statement s, String ID, String name, String dept_name) throws SQLException
    {
        String out = "";
        if(ID.isEmpty() || name.isEmpty() || dept_name.isEmpty())
        {
            return "Please fill in all the fields";
        }
        try
        {
            if(!deptExists(s, dept_name))
            {
                out = "The department does not exist and hence the instructor record cannot be added";

                return out;

            }
            if(idExists(s, ID))
            {
                System.out.println("The ID already exists in the database and hence the instructor record cannot be added");
                out = "The ID already exists in the database and hence the instructor record cannot be added to the database";
                return out;
            }


            String insertQuery = "insert into instructor values('" + ID + "','" + name + "','" + dept_name + "');";

            s.executeUpdate(insertQuery);
            out = "Instructor successfully added to database!";
            return out;
        }
        catch(SQLException e)
        {
            System.out.println("SQL Exception thrown" + e);
        }
        return out;

    }

    public static boolean deptExists(Statement s, String dept_name) throws SQLException
    {
        String query = "select dept_name from department where dept_name = '" + dept_name + "';";

        ResultSet rs = s.executeQuery(query);

        if(rs.next() == false)
            return false;   //Department name does not exist in database
        else
            return true;    //Department name exists in database


    }

    public static boolean idExists(Statement s, String ID) throws SQLException
    {
        String query = "select ID from instructor where ID = '" + ID + "';";

        ResultSet rs = s.executeQuery(query);
        if(rs.next() == false)
            return false;   //ID does not exist in database
        else
            return true;    //ID exists in database


    }




}





