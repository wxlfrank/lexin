/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.dict.tests;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

//import junit.framework.TestCase;
import no.dict.data.DictItem;
import no.dict.services.SQLiteService;
import no.dict.threads.BuildDictionary;

/**
 *
 * @author wxlfr_000
 */
public class BuildDictionaryTest{

	@Test
	public void testDB(){
		BuildDictionary builder = new BuildDictionary();
		builder.initVisitHash();
		builder.loadData();
		builder.createChildren();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		builder.stopChildren();
		builder.saveData();
		Map<String, DictItem> newDict = builder.getWords();
		Map<String, DictItem> another = new HashMap<String, DictItem>();
		SQLiteService.loadWords(another);
		for(Entry<String, DictItem> iter : newDict.entrySet()){
			Assert.assertNotNull(another.get(iter.getKey()));
			another.remove(iter.getKey());
		}
		Assert.assertTrue(another.isEmpty());
	}
}
