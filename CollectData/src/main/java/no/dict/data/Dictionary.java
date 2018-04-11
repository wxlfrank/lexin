package no.dict.data;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import no.dict.services.LogService;
/**
 * Created by wxlfr_000 on 2016/4/12.
 */
public class Dictionary {
    private Map<String, Set<DictItem>> errors;
    /**
     For each dictionary item, the key is its word and its explanation.
     */
    private Map<String, DictItem> words;

    public Dictionary(){
        words = new ConcurrentHashMap<String, DictItem>();
        errors = new ConcurrentHashMap<String, Set<DictItem>>();
    }
    public Map<String, DictItem> getWords(){
    	return words;
    }

    public synchronized  void putIntoError(DictItem item){
    	LogService.log.warning(item.toString());
        String key = item.getWord() + "|" + item.getExplain();
        Set<DictItem> value = errors.get(key);
        if(value == null)
            value = new HashSet<DictItem>();
        if(!value.contains(item))
            value.add(item);
    }
    
//    private int id = 0;
//    public synchronized void setId(int id){
//    	this.id = id;
//    }
//    public synchronized int getId(){
//    	return id;
//    }
    public synchronized  void putIntoWords(DictItem item){
//    	item.setId(id++);
        String key = item.getWord() + "|" + item.getExplain();
        if(words.get(key) == null) {
            words.put(key, item);
            if (!item.getError().isEmpty())
                putIntoError(item);
        }
    }
}
