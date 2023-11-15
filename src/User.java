import java.util.*;
import javax.swing.*;

public class User extends Composite{

    private DefaultListModel<User> following;
    private ArrayList<Visitable> followers;
    private ArrayList<String> tweets;
    private DefaultListModel<String> feed;

    public User( String id ){
        super( id );
        following = new DefaultListModel<User>();
        followers = new ArrayList<Visitable>();
        
        tweets = new ArrayList<String>();
        feed = new DefaultListModel<String>();
       
        followers.add( this );
    }
    public void AddFollowing( User user ){
        following.addElement( user );
    }
    public DefaultListModel<User> GetFollowing(){
        return following;
    }
    public void AddFollowers( Visitable obs ){
        followers.add( obs );
    }
    public void Tweet( String tweet ){
        tweets.add( tweet );
        NotifyFollowers( tweet );
    }
    public ArrayList<String> GetMyTweets(){
        return tweets;
    }
    public void UpdateTweets( String tweet ){
        feed.addElement( tweet );
    }
    public void NotifyFollowers( String tweet ){
        for( Visitable user: followers ){
            user.UpdateTweets( tweet );
        }
    }
    public void AcceptMessages( Visitor visitor ){
        visitor.VisitMessages( this );
    }
    public DefaultListModel<String> GetTweets(){
        return feed;
    }
    public void AcceptPositiveMessages( Visitor visitor ){
        visitor.VisitPositiveMessages( this );
    }
}