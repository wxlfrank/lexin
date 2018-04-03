/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.dict.threads;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.jsoup.nodes.Document;

import no.dict.data.DictItem;
import no.dict.data.Dictionary;
import no.dict.data.VisitHash;
import no.dict.services.SQLiteService;

/**
 * The main thread to collect dictionary data from http://lexin.udir.no/?mode=main-page&sub-mode=search&dict=nbo-maxi&ui-lang=nbo.
 * The website provides a bokmålsordbok.
 * @author wxlfr_000
 */
public class BuildDictionary extends AbstractThread {

    private final Dictionary dict = new Dictionary();
    /*
    Visit hash table used to record which words are visited and which words are not visited.
     */
    private final VisitHash hash = new VisitHash();
    private final Map<String, Object> config = new HashMap<String, Object>();
    private final int QUEUE_SIZE = 32;

    BlockingQueue<Document> downloadBuffer = new ArrayBlockingQueue<Document>(QUEUE_SIZE);
    /**
     * The thread to download html file from website
     */
    Downloader downloader;
    /**
     * The thread to extract words from the downloaded html files
     */
    Extractor extractor;

    /**
     * create children threads for downloading html files and extracting words from the downloaded files
     */
    public void createChildren(){
    	extractor = new Extractor(hash, dict, downloadBuffer);
        downloader = new Downloader(hash, downloadBuffer, extractor);
        downloader.start();
        extractor.start();
    }
    
    public Map<String, DictItem> getWords() {
		return dict.getWords();
	}
    
    /**
     * Add words from a text file into visit hash
     */
    public void initVisitHash(){
        try {
        	System.out.println("Loading words from words.txt");
            BufferedReader reader = new BufferedReader(new FileReader("words.txt"));
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                hash.addUnvisited(line.split(" ")[0]);
            }
            reader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * Loading data from database
     */
    public void loadData() {
    	SQLiteService.loadVisitHash(hash);
    	SQLiteService.loadConfig(config);
    	SQLiteService.loadWords(dict.getWords());
    }
    
    /**
	 * Register shutdown handler
	 */
    public void registerShutDownHandler() {
        Thread shutdown = new Thread("ShutDown") {
            public void run() {
                threadMessage("Preparing shutting!");
                BuildDictionary.this.stopChildren();
                threadMessage("Save to database");
                saveData();
				threadMessage("Save to database finished");
                threadMessage("Now, the crawler can be safely shut down!");
            }
        };

        Runtime.getRuntime().addShutdownHook(shutdown);
    }

    /**
     * The workflow of building dictionary
     */
    public void run(){
        threadMessage("starting");
    	initVisitHash();
    	loadData();
        createChildren();
        registerShutDownHandler();
    }
	
	/**
     * Wait for the children threads finishing
     */
	public void stopChildren() {
        downloader.interrupt();
        waitThreadFinish(downloader);
        waitThreadFinish(extractor);
    }

	/**
     * Store data into database
     */
    public void saveData() {
    	SQLiteService.saveVisitHash(hash);
    	SQLiteService.saveDictItem(dict.getWords());
    }
}
