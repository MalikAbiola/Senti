/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Interfaces.TwitterInterface;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import java.util.List;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

/**
 *
 * @author Doctormaliko
 */
public class TwitterPull implements TwitterInterface {

    private TwitterFactory twitterFactory;
    private Twitter twitter;
    private List<Status> twitterFeed;
//    private String tweetDocument;
    private String queryString;
    private ArrayList<String> retrievedTweets;

    public TwitterPull(String queryString) throws UnsupportedEncodingException {
        this.queryString = URLEncoder.encode("\"" + queryString + "\"", "UTF-8");
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

//    public void setTweetDocument(String tweetDocument) {
//        this.tweetDocument = tweetDocument;
//    }
//    private void appendTweetDocument(String string) {
//        setTweetDocument(getTweetDocument() + string + "\n");
//    }
    public Document getRetrievedTweets() {
        return new Document(retrievedTweets.toArray(new String[retrievedTweets.size()]));
    }

    private void init() {
        this.twitterFactory = new TwitterFactory();
        this.twitter = this.twitterFactory.getInstance();
        this.twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        this.twitter.setOAuthAccessToken(new AccessToken(DEFAULT_ACCESS_TOKEN, DEFAULT_ACCESS_TOKEN_SECRET));
        retrievedTweets = new ArrayList<>();
    }

    public void retrieveTweets() throws TwitterException {
        Query query = new Query("\"" + this.queryString + "\"");
        query.setLang("en");
        query.setCount(100);
        QueryResult result;
        int i = 0;
        do {
            result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            for (Status tweet : tweets) {
                String t = tweet.getText().replaceAll("\n", "").replaceAll("\r", "");
//                appendTweetDocument(t);
                retrievedTweets.add(t);
            }
            i++;
        } while ((query = result.nextQuery()) != null && i < 50);

    }
}
