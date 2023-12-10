import javax.swing.tree.DefaultMutableTreeNode;

public class Composite extends DefaultMutableTreeNode implements Visitable {
    
    protected String userId;
    protected long creationTime;

    public Composite(String id){
        setUserObject(id);
        this.userId = id;
        this.creationTime = System.currentTimeMillis();// Set creation time
    }
    public String GetId(){
        return userId;
    }
    public long GetCreationTime() {
        return creationTime;
    }
    public void AddFollowers(Visitable obs){  
    }
    public void AcceptMessages(Visitor visitor){  
    }
    public void UpdateTweets(String tweet){
    }
    public void NotifyFollowers(String tweet){   
    }
    public void AcceptPositiveMessages(Visitor visitor){  
    }

}