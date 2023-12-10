import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.util.*;

public class AdminControlPanel implements ActionListener, Visitor{

    //Singleton Pattern
    private JFrame frame;
    private Group root;
    private JTree userList;
    private HashMap<String,Visitable> users;
    private Composite selectedUser;
    private static AdminControlPanel adminManager;
    private int messageTotal, userTotal, groupTotal, positivePercentage;
    private JPanel adminPanel, centerPanel, upperPanel, middleColumn, lowerPanel, topPanel;
    private JButton b1, b2, b3, b4, b6, b5, b7, b8, b9;
    private JTextField t1, t2;
    private DefaultTreeModel userListModel;
    // set positive words for percentage check
    private static final String[] positiveWords = { "cool", "great", "good", "amazing", "excellent", "fantastic"};

    //User Interface for Admin Control Panel
    private AdminControlPanel (){
        users = new HashMap<String,Visitable>();
        userTotal = 0;
        groupTotal = 1;

        // Create and set up the window
        frame = new JFrame("MiniTwitter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(500, 200);

        // admin panel with BorderLayout
        adminPanel = new JPanel(new BorderLayout());

        // Center panel with BorderLayout
        centerPanel = new JPanel(new BorderLayout());

        // Upper panel for top alignment
        upperPanel = new JPanel();
        upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));

        // Lower panel for bottom alignment
        lowerPanel = new JPanel();
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.X_AXIS));

        // Top panel with GridLayout for userid/groupid text and add user/group button
        topPanel = new JPanel(new GridLayout(2, 2, 0, 0));

        //UserID text/button
        t1 = createTextFieldWithPlaceholder( "Enter UserId" );
        b1 = new JButton("Add User");
        topPanel.add(t1);
        topPanel.add(b1);

        //GroupID text/button
        t2 = createTextFieldWithPlaceholder( "Enter GroupId" );
        b2 = new JButton("Add Group");
        topPanel.add(t2);
        topPanel.add(b2);

        // middle panel
        middleColumn = new JPanel();
        middleColumn.setLayout(new BoxLayout(middleColumn, BoxLayout.Y_AXIS));
        middleColumn.add(Box.createVerticalStrut(2)); // spacer for gap
        b3 = new JButton("Open User View");
        middleColumn.add(b3);
        b8 = new JButton("User/Group ID verification");
        middleColumn.add(b8);
        b9 = new JButton("Show Last Updated User");
        middleColumn.add(b9);

        // Add topPanel to upperPanel
        upperPanel.add(topPanel);

        // Add upperPanel to centerPanel
        centerPanel.add(upperPanel, BorderLayout.NORTH);

        // Add the middleColumn to the centerPanel
        centerPanel.add(middleColumn, BorderLayout.CENTER);

        // Set the preferred width to match the width of the centerPanel
        t1.setMaximumSize(new Dimension(Integer.MAX_VALUE, b2.getPreferredSize().height));
        t2.setMaximumSize(new Dimension(Integer.MAX_VALUE, b2.getPreferredSize().height));
        b1.setMaximumSize(new Dimension(Integer.MAX_VALUE, b2.getPreferredSize().height));
        b2.setMaximumSize(new Dimension(Integer.MAX_VALUE, b2.getPreferredSize().height));
        b3.setMaximumSize(new Dimension(Integer.MAX_VALUE, b2.getPreferredSize().height));
        b8.setMaximumSize(new Dimension(Integer.MAX_VALUE, b2.getPreferredSize().height));
        b9.setMaximumSize(new Dimension(Integer.MAX_VALUE, b2.getPreferredSize().height));

        // Bottom panel with GridLayout for User/Group/message total and positive percentage
        JPanel bottomPanel = new JPanel(new GridLayout(2, 2, 0, 0));
        b4 = new JButton("Show Total User");
        b5 = new JButton("Show Total Messages");
        b6 = new JButton("Show Total Group");
        b7 = new JButton("Show Positive Percentage");
        bottomPanel.add(b4);
        bottomPanel.add(b5);
        bottomPanel.add(b6);
        bottomPanel.add(b7);

        // Add bottomPanel to lowerPanel
        lowerPanel.add(bottomPanel);

        // Add lowerPanel to centerPanel
        centerPanel.add(lowerPanel, BorderLayout.SOUTH);

        b4.setMaximumSize(new Dimension(Integer.MAX_VALUE, b2.getPreferredSize().height));
        b5.setMaximumSize(new Dimension(Integer.MAX_VALUE, b2.getPreferredSize().height));
        b6.setMaximumSize(new Dimension(Integer.MAX_VALUE, b2.getPreferredSize().height));
        b7.setMaximumSize(new Dimension(Integer.MAX_VALUE, b2.getPreferredSize().height));

        // Add panels to admin panel
        adminPanel.add(centerPanel, BorderLayout.CENTER);

        // give buttons action
        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);
        b5.addActionListener(this);
        b6.addActionListener(this);
        b7.addActionListener(this);
        b8.addActionListener(this);
        b9.addActionListener(this);

        // Root for Treeview
        root = new Group("root");
        selectedUser = root;
        userList = new JTree(root);
        userListModel = (DefaultTreeModel)userList.getModel();
        userList.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        // Set the tree selection to the root node
        userList.setSelectionPath(new TreePath(root));

        // Left panel for Treeview
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(userList, BorderLayout.CENTER);

        // Set the preferred size of the JTree
        userList.setPreferredSize(new Dimension(300, 400));

        //add Tree to admin Panel
        adminPanel.add(leftPanel, BorderLayout.WEST);

        //Add all to the main UI and set size
        frame.add(adminPanel);
        frame.pack();
        frame.setSize(900, 600);
        frame.setVisible(true);

    }

    // Set a placeholder for a JTextField in Grey
    private JTextField createTextFieldWithPlaceholder(String placeholder) {
        JTextField textField = new JTextField(10);
        setPlaceholder(textField, placeholder);

        return textField;
    }

    // Set a placeholder for a JTextField in Grey
    private void setPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
    }

    public static AdminControlPanel Start(){
        if(adminManager == null){
            adminManager = new AdminControlPanel();
        }
        return adminManager;
    }

    public User GetUser(String id){
        return (User)users.get(id);
    }

    //Prints out # of Users.
    public void GetUserTotal(){
        JOptionPane.showMessageDialog(frame, "Total Users: " + userTotal);
    }

    //Prints out the # of groups
    public void GetGroupTotal(){
        JOptionPane.showMessageDialog(frame, "Total Groups: " + groupTotal);
    }

    //Prints out the # of messages
    public void GetMessagesTotal(){
        messageTotal = 0;
        for(Visitable user: users.values()){
            user.AcceptMessages( this );
        }
        JOptionPane.showMessageDialog(frame, "Total Messages: " + messageTotal);
    }

    //Prints out the positive percentage
    public void GetPositiveTotal(){
        positivePercentage = 0;
        messageTotal = 0;
        for( Visitable user: users.values() ){
            user.AcceptPositiveMessages(this);
        }
        if(messageTotal == 0){
            messageTotal = 1;
        }
        JOptionPane.showMessageDialog(frame, "Positive Percentage: " + ((double)positivePercentage/messageTotal * 100));
    }

    //Prints out the positive percentage
    public boolean GetVerification(){
        Set<String> names = users.keySet();
        for( String name: names ){
            if( name.indexOf( " " ) != -1 ){
                return false;
            }
        }
        return true;
    }

    public void VisitMessages(User user){
        messageTotal += user.GetMyTweets().size();
    }

    public void VisitPositiveMessages(User user){
        ArrayList<String> tweets = user.GetMyTweets();
        messageTotal += tweets.size();
        for(String tweet: tweets){
            for(String positive: positiveWords){
                if( tweet.toLowerCase().contains(positive) ){
                    positivePercentage++;
                    break;
                }
            }
        }
    }

    private void FindLastUpdatedUser() {
        User lastUpdatedUser = null;
        long maxUpdateTime = Long.MIN_VALUE;
    
        for (Visitable user : users.values()) {
            if (user instanceof User) {
                long updateTime = ((User) user).GetLastUpdateTime();
                if (updateTime > maxUpdateTime) {
                    maxUpdateTime = updateTime;
                    lastUpdatedUser = (User) user;
                }
            }
        }
    
        if (lastUpdatedUser != null) {
            JOptionPane.showMessageDialog(frame, "Last Updated User: " + lastUpdatedUser.GetId());
        } else {
            JOptionPane.showMessageDialog(frame, "No users found.");
        }
    }
    
    // action path for buttons
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == b1){
            String userId = t1.getText();
           
            if(users.containsKey(userId))
                return;
            User user = new User(userId);
            try{
                Group selectedUser = (Group)userList.getLastSelectedPathComponent();
                
                if(selectedUser == null)
                    return;
                selectedUser.add(user);
                users.put(user.GetId(), user);
                userListModel.reload(selectedUser);
                ++userTotal;
            } catch (Exception ex){}
        }
        else if(e.getSource() == b2){
            Group user = new Group(t2.getText());
            try{
                Group selectedUser = (Group)userList.getLastSelectedPathComponent();
                if(selectedUser == null)
                    return;
                selectedUser.add(user);
                users.put(user.GetId(), user);
                userListModel.reload(selectedUser);
                ++groupTotal;
            } catch (Exception ex){}
        }
        else if( e.getSource() == b3 ){
            try{
                User user = (User)userList.getLastSelectedPathComponent();
                UserView newUserview = new UserView(user, this);
            } catch (Exception ex){}
        }
        else if(e.getSource() == b4){
            GetUserTotal();
        }
        else if(e.getSource() == b5){
            GetMessagesTotal();
        }
        else if(e.getSource() == b6){
            GetGroupTotal();
        }
        else if(e.getSource() == b7){
            GetPositiveTotal();
        }
        else if (e.getSource() == b8) {
            if (GetVerification()){
                JOptionPane.showMessageDialog(frame, "Validation Successful");
            }
            else{
                JOptionPane.showMessageDialog(frame, "Invalidation found");
            }
        }
        else if(e.getSource() == b9){
            FindLastUpdatedUser();
        }
    }

}