public interface Visitable{

    public void AddFollowers(Visitable obs);

    public void AcceptMessages(Visitor visitor);

    public void UpdateTweets(String tweet);

    public void NotifyFollowers(String tweet);

    public void AcceptPositiveMessages(Visitor visitor);

}