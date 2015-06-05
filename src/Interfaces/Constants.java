/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

/**
 *
 * @author Doctormaliko
 */
public interface Constants {

    public static String[] negations = {
        "aint", "can't", "cannot", "couldn't", "didn't", "didnt", "don't", "dont", "dnt", "doesn't", "hardly", "hadn't", "haven't", "hasn't", "isnt", "isn't",
        "no", "nobody","n't", "non", "none", "nt", "not", "nothing", "rather", "wasn't", "weren't", "won't", "wont", "wouldn't", "shouldn't", "yet"
    };

    public static String[] stopWords = {
        "about", "across", "again", "all", "alone", "already", "although", "am", "among", "and", "any", "anyone", "anywhere",
        "area", "around", "ask", "asking", "at", "b", "backed", "backs", "became", "become", "been", "began", "being", "between", "both", "by", "came",
        "cases", "could", "did", "different", "do", "done", "down", "downing", "during", "each", "either", "ended", "ends",
        "even", "ever", "everybody", "everything", "f", "faces", "facts", "felt", "find", "first", "four", "full", "g", "general", "get",
        "give", "gives", "going", "goods", "grouped", "groups", "has", "had", "have", "he", "here", "himself", "how", "i", "is",
        "into", "it", "it's", "its", "itself", "just", "keep", "kind", "know", "knows", "last", "latest", "lets", "likely", ",", ".",
        "longer", "m", "make", "man", "may", "member", "men", "more", "mostly", "mrs", "must", "myself", "needed", "needs", "new", "newer", "next",
        "now", "number", "o", "of", "oldest", "once", "only", "opened", "opens", "order", "ordering", "other", "our", "part", "parting", "per",
        "place", "point", "pointing", "possible", "presented", "presents", "problems", "puts", "quite", "rather", "right", "room", "s", "same", "say", "second", "see",
        "she", "show", "showing", "side", "since", "smaller", "so", "somebody", "something", "state", "still", "such", "t", "taken", "that", "the",
        "their", "then", "therefore", "they", "things", "thinks", "those", "thought", "three", "thus", "today", "too", "toward", "turned", "turns", "u", "until", "upon",
        "use", "uses", "very", "want", "wanting", "was", "ways", "well", "went", "what", "where", "which", "who", "whose", "why", "will", "within", "work", "working", "would", "y",
        "years", "you", "younger", "your", "z"
    };
//    public static String[] stopWords = {
//        "about", "across", "again", "all", "alone", "already", "although", "am", "among", "and", "any", "anyone", "anywhere",
//        "area", "around", "ask", "asking", "at", "b", "backed", "backs", "became", "become", "been", "began", "being", "between", "both", "by", "came",
//        "cases", "could", "did", "different", "do", "done", "down", "downing", "during", "each", "either", "ended", "ends",
//        "even", "ever", "everybody", "everything", "f", "faces", "facts", "felt", "find", "first", "four", "full", "further", "furthering", "g", "general", "get",
//        "give", "gives", "going", "goods", "grouped", "groups", "has", "had", "have", "he", "here", "high", "highest", "himself", "how", "i", "is",
//        "into", "it", "it's", "its", "itself", "just", "keep", "kind", "know", "knows", "last", "latest", "lets", "likely", ",", ".",
//        "longer", "m", "make", "man", "may", "member", "men", "more", "mostly", "mrs", "must", "myself", "necessary", "needed", "needs", "new", "newer", "next",
//        "now", "number", "o", "of", "oldest", "once", "only", "opened", "opens", "order", "ordering", "other", "our", "part", "parting", "per",
//        "place", "point", "pointing", "possible", "presented", "presents", "problems", "puts", "quite", "rather", "right", "room", "s", "same", "say", "second", "see",
//        "she", "show", "showing", "side", "since", "smaller", "so", "somebody", "something", "state", "still", "such", "t", "taken", "that", "the",
//        "their", "then", "therefore", "they", "things", "thinks", "those", "thought", "three", "thus", "today", "too", "toward", "turned", "turns", "u", "until", "upon",
//        "use", "uses", "very", "want", "wanting", "was", "ways", "well", "went", "what", "where", "which", "who", "whose", "why", "will", "within", "work", "working", "would", "y",
//        "years", "you", "younger", "your", "z"
//    };

    public static String [] sarcasmIndicators = {
      "yeah right", "#sarcasm", "#lying", "#notreally", "#sarcastic"  
    };
    public static String CAT_ADVERB = "r";
    public static String CAT_ADJECTIVE = "a";
    public static String CAT_NOUN = "n";
    public static String CAT_VERB = "v";
    public static final int ALLOWED_CHAR_REPITION = 2;

}
