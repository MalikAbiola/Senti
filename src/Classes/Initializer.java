/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Interfaces.Constants;
import gate.util.GateException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Doctormaliko
 */
public class Initializer implements Constants {

    public POSTagger appPOSTagger;
    public SentiWordNet appSentiWordNet;
    public UrbanRetriever appUrbanRetriever;
    public EmotiSentiWordNet appEmotiSentiWordNet;
    public SpamDetector appSpamDetector;

    

    public Initializer() {
    }

    public void init() {
        Thread posThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    appPOSTagger = new POSTagger();
                } catch (GateException | IOException ex) {
                    Logger.getLogger(Initializer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        Thread sentiThread = new Thread(new Runnable() {
            @Override
            public void run() {
                appSentiWordNet = new SentiWordNet();
            }
        });

        Thread emoticonSentiThread = new Thread(new Runnable() {
            @Override
            public void run() {
                appEmotiSentiWordNet = new EmotiSentiWordNet();
            }
        });
        Thread urbanThread = new Thread(new Runnable() {
            @Override
            public void run() {
                appUrbanRetriever = new UrbanRetriever();
            }
        });
        Thread spamDetectionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    appSpamDetector = new SpamDetector();
                } catch (IOException ex) {
                    Logger.getLogger(Initializer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        posThread.start();
        sentiThread.start();
        emoticonSentiThread.start();
        spamDetectionThread.start();
        urbanThread.start();

    }

    public void initPOSTagger() {
        Thread posThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    appPOSTagger = new POSTagger();
                } catch (GateException | IOException ex) {
                    Logger.getLogger(Initializer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        posThread.start();
    }

    public void initSentiWordNet() {
        Thread sentiThread = new Thread(new Runnable() {
            @Override
            public void run() {
                appSentiWordNet = new SentiWordNet();
            }
        });
        sentiThread.start();
    }

    public void initUrbanRetriever() {
        Thread urbanThread = new Thread(new Runnable() {
            @Override
            public void run() {
                appUrbanRetriever = new UrbanRetriever();
            }
        });
        urbanThread.start();
    }
    
    public void initSpamDetector() {
        Thread spamDetectionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    appSpamDetector = new SpamDetector();
                } catch (IOException ex) {
                    Logger.getLogger(Initializer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        spamDetectionThread.start();
    }
    
    public void initEmotiSentiWordNet() {
        Thread emotiSentiWordNetThread = new Thread(new Runnable() {
            @Override
            public void run() {
                appEmotiSentiWordNet = new EmotiSentiWordNet();
            }
        });
        emotiSentiWordNetThread.start();
    }
}
