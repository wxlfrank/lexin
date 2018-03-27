package no.dict.tests;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import no.dict.data.DictItem;
import no.dict.data.DictItemFactory;
import no.dict.services.HttpService;
import no.dict.services.JSoupService;

public class WorkflowTest {

	@Test
	public void testWorkFlow() {
		String[] words = {"annen", "meg", "jeg", "pluss"};
		for(String word : words){
			testWord(word);
		}
	}
	
	public void testWord(String word){

		int from = 0;
		int count = 10;
		String url = null;
		int searched = 0;
		int expected = 0;
		int page = 0;
		do {
			url = HttpService.getURL(word, from, count);

			Document document = HttpService.getDocument(url);
			expected = JSoupService.getSearched(document);
			Assert.assertNotNull(document);
			List<List<String>> items = JSoupService.getItems(document);
			List<DictItem> results = new ArrayList<DictItem>();
			for (List<String> item : items) {
				results.add(DictItemFactory.getDictItem(item));
			}
			searched += items.size();
			if (JSoupService.hasNext(document))
				from += count;
			else
				break;
			System.out.println("-----------" + page ++);
		} while (true);
//		System.out.println(expected);
//		System.out.println(searched);
		Assert.assertTrue(expected >= searched);
	
	}
}
