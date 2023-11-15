import javax.swing.tree.DefaultMutableTreeNode;

public class Composite extends DefaultMutableTreeNode implements Visitable {
    
    protected String userId;

    public Composite( String id ){
        setUserObject( id );
        this.userId = id;
    }
    public String GetId(){
        return userId;
    }
    public void AddFollowers( Visitable obs ){  
    }
    public void AcceptMessages( Visitor visitor ){  
    }
    public void UpdateTweets( String tweet ){
    }
    public void NotifyFollowers( String tweet ){   
    }
    public void AcceptPositiveMessages( Visitor visitor ){  
    }

}