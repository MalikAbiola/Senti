
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Doctormaliko
 */
public class TwitterPull implements TwitterInterface {

    List<Status> twitterFeed;
    private String tweetDocument;
    private String queryString;
    private TwitterFactory twitterFactory;
    private Twitter twitter;

    public TwitterPull(String queryString) {
        this.queryString = queryString;
        init();
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public List<Status> getTwitterFeed() {
        return twitterFeed;
    }

    public void setTwitterFeed(List<Status> twitterFeed) {
        this.twitterFeed = twitterFeed;
    }

    public void setTweetDocument(String tweetDocument) {
        this.tweetDocument = tweetDocument;
    }

    public String getTweetDocument() {
        return tweetDocument;
    }

    private void appendTweetDocument(String string) {
        setTweetDocument(getTweetDocument() + string + "\n");
    }

    private void init() {
        this.twitterFactory = new TwitterFactory();
        this.twitter = this.twitterFactory.getInstance();
        this.twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        this.twitter.setOAuthAccessToken(new AccessToken(DEFAULT_ACCESS_TOKEN, DEFAULT_ACCESS_TOKEN_SECRET));
    }

    public void retrieveTweets() {
        try {
            Query query = new Query(this.queryString);
            query.setLang("en");
            query.setCount(100);
            QueryResult result;
            int i = 0;
            do {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
//                int i = 0;
                for (Status tweet : tweets) {
                    System.out.println(tweet.getText().replaceAll("\n","").replaceAll("\r", ""));
//                    appendTweetDocument(tweet.getText());
                }
                i++;
            } while ((query = result.nextQuery()) != null && i < 10);
//            setTwitterFeed(tweets);

//            System.exit(0);
        } catch (TwitterException te) {
            Logger.getLogger(TwitterPull.class.getName()).log(Level.SEVERE, null, te);
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
    }
    
    public static void main(String [] args) {
        try {
//            String q = URLEncoder.encode("\"Kim Kardashian\"", "UTF-8");
            String q = URLEncoder.encode("\"Tinubu\"", "UTF-8");
            new TwitterPull(q).retrieveTweets();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TwitterPull.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
