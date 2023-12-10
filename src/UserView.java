import javax.swing.*;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.*;

public class UserView implements ActionListener{

    private User user;
    private AdminControlPanel manager;
    private JTextField t1, t2;
    private JButton b1, b2;
    private JPanel userPanel, topPanel, middlePanel, bottomPanelContainer;
    private JList<String> newsfeed;
    private JList<User> currentFollowing;
    
    //User Interface for user Panel
    public UserView( User user, AdminControlPanel manager ){
        
        this.user = user;
        this.manager = manager;
        
        JFrame userFrame = new JFrame("MiniTwitter -" + user.GetId() );
        userFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        userFrame.setLocation(800, 200);
        
        // user panel with GridBagLayout
        userPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Top panel with GridLayout for UserID text and add Follow button
        topPanel = new JPanel(new GridLayout(1,2,0,0));
        t1 = createTextFieldWithPlaceholder("Enter User ID");
        b1 = new JButton("Follow User");
        topPanel.add( t1 );
        topPanel.add( b1 );

        // current following list
        currentFollowing = new JList<User> (user.GetFollowing());

        //set positions of top half of ui
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        userPanel.add(topPanel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        userPanel.add(currentFollowing, gbc);
        
        // Middle panel for user input and tweet button
        middlePanel = new JPanel(new GridLayout(1, 2, 0, 0));
        t2 = createTextFieldWithPlaceholder("Enter Tweet");
        b2 = new JButton("Post Tweet");
        middlePanel.add(t2);
        middlePanel.add(b2);
        
        // give buttons action
        b1.addActionListener(this);
        b2.addActionListener(this);

        // list of tweets        
        newsfeed = new JList<String>(user.GetTweets());
       
        // Add components to the bottomPanelContainer with GridBagLayout
        bottomPanelContainer = new JPanel(new GridBagLayout());
        GridBagConstraints bottomGBC = new GridBagConstraints();

        bottomGBC.fill = GridBagConstraints.HORIZONTAL;
        bottomGBC.weightx = 1.0;
        bottomGBC.anchor = GridBagConstraints.NORTH;
        bottomPanelContainer.add(middlePanel, bottomGBC);

        bottomGBC.gridy = 1;
        bottomGBC.weighty = 1.0;
        bottomGBC.fill = GridBagConstraints.BOTH;
        bottomPanelContainer.add(newsfeed, bottomGBC);

        gbc.gridy = 2;
        gbc.weighty = 1.0;
        userPanel.add(bottomPanelContainer, gbc);

        // Add the userPanel to the frame
        userFrame.add(userPanel);

        userFrame.pack();
        userFrame.setSize(300, 600);
        userFrame.setVisible(true);

        // Print creation time in the console
        System.out.println("Creation Time: " + user.GetCreationTime());
        System.out.println("Last Update Time: " + user.GetLastUpdateTime());
    }
    
    // action path for buttons
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == b1){
            String followId = t1.getText();
            User dude = manager.GetUser(followId);
            
            if( dude != null && !user.equals(dude) && !user.GetFollowing().contains(dude)){
                user.AddFollowing(dude);
                dude.AddFollowers(user);
            } else {
                if (user.equals(dude)) {
                    System.out.println("You cannot follow yourself.");
                } else if (dude == null) {
                    System.out.println("User not found");
                } else {
                    System.out.println("User is already being followed.");
                } 
            }
        }
        if(e.getSource() == b2){
            String tweet = user.GetId() + ": " + t2.getText();
            user.Tweet(tweet);
        }
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
}