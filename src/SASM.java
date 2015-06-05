/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Doctormaliko
 */
public class SASM {
    
    public static void main(String [] args) {
        TwitterPull twitterPull = new TwitterPull("Z10");
        twitterPull.retrieveTweets();
        System.out.println(twitterPull.getTweetDocument());
    }
}
