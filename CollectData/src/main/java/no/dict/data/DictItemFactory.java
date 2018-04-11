package no.dict.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.dict.utils.Constants;

public class DictItemFactory {
	private static final String BOKMÅL = "Bokmål ";
	private static final int BOKMÅL_LENGTH = BOKMÅL.length();
	private static final String ENGELSK = "Engelsk ";
	private static final String ENGELSK_OPPSLAGSORD = ENGELSK + "oppslagsord ";
	private static final int ENGELSK_OPPSLAGSORD_LENGTH = ENGELSK_OPPSLAGSORD.length();
	private static final String OPPSLAGSORD = BOKMÅL + "oppslagsord ";
	private static final int OPPSLAGSORD_LENGTH = OPPSLAGSORD.length();
	private static final String BØYNING = BOKMÅL + "bøyning ";
	private static final int BØYNING_LENGTH = BØYNING.length();
	private static final String FORKLARING = BOKMÅL + "forklaring ";
	private static final int FORKLARING_LENGTH = FORKLARING.length();
	private static final String ENGELSK_FORKLARING = ENGELSK + "forklaring ";
	private static final int ENGELSK_FORKLARING_LENGTH = ENGELSK_FORKLARING.length();
	private static final String EKSEMPEL = BOKMÅL + "eksempel ";
	private static final int EKSEMPEL_LENGTH = EKSEMPEL.length();
	private static final String SAMMENSETNING = BOKMÅL + "sammensetning ";
	private static final int SAMMENSETNING_LENGTH = SAMMENSETNING.length();
	private static final String ALTERNATIV = BOKMÅL + "alternativ ";
	private static final int ALTERNATIV_LENGTH = ALTERNATIV.length();
	private static final String KOMMENTAR = BOKMÅL + "kommentar ";
	private static final int KOMMENTAR_LENGTH = KOMMENTAR.length();
	private static final String ENGELSK_KOMMENTAR = ENGELSK + "kommentar ";
	private static final int ENGELSK_KOMMENTAR_LENGTH = ENGELSK_KOMMENTAR.length();
	private static final String TILLEGGSFORKLARING = BOKMÅL + "tilleggsforklaring ";
	private static final String ENGELSK_TILLEGGSFORKLARING = ENGELSK + "tilleggsforklaring ";
	private static final int ENGELSK_TILLEGGSFORKLARING_LENGTH = ENGELSK_TILLEGGSFORKLARING.length();
	private static final int TILLEGGSFORKLARING_LENGTH = TILLEGGSFORKLARING.length();
	private static final String GRAMMATIKK = BOKMÅL + "grammatikk ";
	private static final String ENGELSK_GRAMMATIKK = ENGELSK + "grammatikk ";
	private static final int GRAMMATIKK_LENGTH = GRAMMATIKK.length();
	private static final int ENGELSK_GRAMMATIKK_LENGTH = ENGELSK_GRAMMATIKK.length();
	private static final String UTTRYKK = BOKMÅL + "uttrykk ";
	private static final int UTTRYKK_LENGTH = UTTRYKK.length();
	private static final Pattern FIRST_MATCH = Pattern.compile("(.*)\\(\"(.*)");
//	private static final Pattern SECOND_MATCH = Pattern.compile("(.*)\"\\)?(.*)");
//	private static final Pattern END_TRIM_MATCHER = Pattern.compile("([^\\p{Alpha} åøæÅØÆ]*)(.*)");

	private static enum KEYWORD_INDEX {
		OPPSLAGSORD, ENGELSK_OPPSLAGSORD, BØYNING, ALTERNATIV, GRAMMATIKK, ENGELSK_GRAMMATIKK, KOMMENTAR, TILLEGGSFORKLARING, ENGELSK_TILLEGGSFORKLARING, FORKLARING, ENGELSK_FORKLARING, SAMMENSETNING, EKSEMPEL, UTTRYKK, ENGELSK_KOMMENTAR
	}

	private static final Pattern LINE = Pattern.compile("([\\p{Alpha}|åøæÅØÆ]+ [\\p{Alpha}|åøæÅØÆ]+ )[\\s\\S]*",
			Pattern.UNICODE_CHARACTER_CLASS);
	private static final Map<String, KEYWORD_INDEX> KEYWORDS = new HashMap<String, KEYWORD_INDEX>();
	static {
		KEYWORDS.put(OPPSLAGSORD, KEYWORD_INDEX.OPPSLAGSORD);
		KEYWORDS.put(ENGELSK_OPPSLAGSORD, KEYWORD_INDEX.ENGELSK_OPPSLAGSORD);
		KEYWORDS.put(BØYNING, KEYWORD_INDEX.BØYNING);
		KEYWORDS.put(ALTERNATIV, KEYWORD_INDEX.ALTERNATIV);
		KEYWORDS.put(GRAMMATIKK, KEYWORD_INDEX.GRAMMATIKK);
		KEYWORDS.put(ENGELSK_GRAMMATIKK, KEYWORD_INDEX.ENGELSK_GRAMMATIKK);
		KEYWORDS.put(KOMMENTAR, KEYWORD_INDEX.KOMMENTAR);
		KEYWORDS.put(ENGELSK_KOMMENTAR, KEYWORD_INDEX.ENGELSK_KOMMENTAR);
		KEYWORDS.put(TILLEGGSFORKLARING, KEYWORD_INDEX.TILLEGGSFORKLARING);
		KEYWORDS.put(ENGELSK_TILLEGGSFORKLARING, KEYWORD_INDEX.ENGELSK_TILLEGGSFORKLARING);
		KEYWORDS.put(FORKLARING, KEYWORD_INDEX.FORKLARING);
		KEYWORDS.put(ENGELSK_FORKLARING, KEYWORD_INDEX.ENGELSK_FORKLARING);
		KEYWORDS.put(SAMMENSETNING, KEYWORD_INDEX.SAMMENSETNING);
		KEYWORDS.put(EKSEMPEL, KEYWORD_INDEX.EKSEMPEL);
		KEYWORDS.put(UTTRYKK, KEYWORD_INDEX.UTTRYKK);
	}

	public static List<DictItem> getDictItem(List<String> item) {
		List<DictItem> results = new ArrayList<DictItem>();
		DictItem result = new DictItem();
		results.add(result);
		Index index = new Index(0);
		Matcher matcher = null;
		for (; index.value < item.size(); index.value++) {
			String item_attr = item.get(index.value);
			matcher = LINE.matcher(item_attr);
			String language_key = null;
			if (matcher.matches())
				language_key = matcher.group(1);
			KEYWORD_INDEX key_index = KEYWORDS.get(language_key);
			if (key_index == null) {
				String uformelt = handleMultiple(item, index, BOKMÅL + item_attr);
				if (language_key != null || !item_attr.equals("Bokmål uformelt")) {
					result.setExamples(uformelt);
				}
				continue;
			}
			switch (key_index) {
			case OPPSLAGSORD:
				String[] values = item_attr.substring(OPPSLAGSORD_LENGTH).split(Constants.LINE);
				result.setSyllabel(values[0]);
				result.setWord(values[0].replaceAll("\\|", "") + getEnglish(item, index));
				if (values.length != 3) {
					result.setExplain(BOKMÅL + values[1]);
				} else {
					result.setSound(values[1].replaceAll(" /", "/"));
					result.setClazz(values[2]);
				}
				break;
			case ENGELSK_OPPSLAGSORD:
				result.setWord(ENGELSK + item_attr.substring(ENGELSK_OPPSLAGSORD_LENGTH));
				break;
			// } else if (str.startsWith(BØYNING)) {
			case BØYNING:
				result.setFormat(handleMultiple(item, index, item_attr.substring(BØYNING_LENGTH)));
				break;
			// } else if (str.startsWith(ALTERNATIV)) {
			case ALTERNATIV:
				result.setAlternative(handleMultiple(item, index, BOKMÅL + item_attr.substring(ALTERNATIV_LENGTH)));
				break;
			// } else if (str.startsWith(GRAMMATIKK)) {
			case GRAMMATIKK:
				result.setGrammer(handleMultiple(item, index, BOKMÅL + item_attr.substring(GRAMMATIKK_LENGTH)));
				break;
			// } else if (str.startsWith(ENGELSK_GRAMMATIKK)) {
			case ENGELSK_GRAMMATIKK:
				result.setGrammer(ENGELSK + item_attr.substring(ENGELSK_GRAMMATIKK_LENGTH));
				break;
			// } else if (str.startsWith(KOMMENTAR)) {
			case KOMMENTAR:
				result.setComment(BOKMÅL + item_attr.substring(KOMMENTAR_LENGTH) + getEnglish(item, index));
				break;
			// } else if (str.startsWith(ENGELSK_KOMMENTAR)) {
			case ENGELSK_KOMMENTAR:
				result.setComment(ENGELSK + item_attr.substring(ENGELSK_KOMMENTAR_LENGTH) + getEnglish(item, index));
				break;
			// } else if (str.startsWith(TILLEGGSFORKLARING)) {
			case TILLEGGSFORKLARING:
				result.setExplain(BOKMÅL + item_attr.substring(TILLEGGSFORKLARING_LENGTH) + getEnglish(item, index));
				break;
			// } else if (str.startsWith(ENGELSK_TILLEGGSFORKLARING)) {
			case ENGELSK_TILLEGGSFORKLARING:
				result.setExplain(
						ENGELSK + item_attr.substring(ENGELSK_TILLEGGSFORKLARING_LENGTH) + getEnglish(item, index));
				break;
			// } else if (str.startsWith(FORKLARING)) {
			case FORKLARING:
				result.setExplain(BOKMÅL + item_attr.substring(FORKLARING_LENGTH) + getEnglish(item, index));
				break;
			// } else if (str.startsWith(FORKLARING)) {
			case ENGELSK_FORKLARING:
				result.setExplain(ENGELSK + item_attr.substring(ENGELSK_FORKLARING_LENGTH) + getEnglish(item, index));
				break;
			// } else if (str.startsWith(SAMMENSETNING)) {
			case SAMMENSETNING:
				result.setComposite(handleMultiple(item, index, BOKMÅL + item_attr.substring(SAMMENSETNING_LENGTH)));
				break;
			// } else if (str.startsWith(EKSEMPEL)) {
			case EKSEMPEL:
				result.setExamples(handleMultiple(item, index, BOKMÅL + item_attr.substring(EKSEMPEL_LENGTH)));
				break;
			// } else if (str.startsWith(UTTRYKK)) {
			case UTTRYKK:
				String phrasesString = handleMultiple(item, index, BOKMÅL + item_attr.substring(UTTRYKK_LENGTH));
				result.setPhrases(phrasesString);
				results.addAll(getPhrase(phrasesString));
				break;
			// } else {
			default:
				break;
			}
		}
		return results;
	}

	private static List<DictItem> getPhrase(String phrasesString) {
		List<DictItem> results = new ArrayList<DictItem>();
		try {
			String[] phrases = phrasesString.split(Constants.LINE);
			for (int iter = 0; iter < phrases.length;) {
				String phrase = "", explain = "", temp = phrases[iter];
				DictItem additional = null;
				Matcher matcher;
				if (temp.startsWith(BOKMÅL)) {
					additional = new DictItem();
					// temp = temp.substring(BOKMÅL_LENGTH);
					matcher = FIRST_MATCH.matcher(temp);
					if (matcher.matches()) {
						String first = matcher.group(1).trim(), second = matcher.group(2).trim();
						int second_index = second.indexOf("\"");
						if(second_index != -1) {
							int lenght = 1;
							if(second_index + 1 < second.length() && second.substring(second_index + 1, second_index + 2).equals(")"))
								lenght = 2;
							first += " " + second.substring(second_index + lenght).trim();
							second = second.substring(0, second_index).trim();
						}else {
							first = temp;
							second = "";
						}
						phrase += first;
						explain += second;
					} else {
						phrase += temp;
					}
				}
				phrase = phrase.trim();
				++iter;
				if (additional != null) {
					while (iter < phrases.length) {
						temp = phrases[iter];
						if (temp.startsWith(BOKMÅL)) {
							break;
						}
						matcher = FIRST_MATCH.matcher(temp);
						if (matcher.matches()) {
							String first = matcher.group(1);
							String second = matcher.group(2);
							int second_index = second.indexOf("\"");
							if(second_index != -1) {
								int lenght = 1;
								if(second_index + 1 < second.length() && second.substring(second_index + 1, second_index + 2).equals(")"))
									lenght = 2;
								first += second.substring(second_index + lenght);
								second = second.substring(0, second_index);
							}else {
								first = temp;
								second = "";
							}
							phrase += (phrase.isEmpty() ? "" : Constants.LINE) + first;
							temp = second;

						}
						explain += (explain.isEmpty() ? "" : Constants.LINE) + (temp.startsWith(ENGELSK) ? "" : ENGELSK) + temp;
						++iter;
					}
					

					String word = phrase.split(Constants.LINE)[0].trim();
					String replaced = word.replaceAll("\\([\\p{Alpha} åøæÅØÆ]+\\)", "");
					boolean isexample = replaced.length() != word.length();
					if (isexample) {
						additional.setExamples(phrase);
						word = phrase.replaceAll("\\([\\p{Alpha} åøæÅØÆ]+\\)", "").trim();
					} else
						word = phrase;
					if (word.contains("|")) {
						additional.setSyllabel(word.substring(BOKMÅL_LENGTH));
						word = word.replaceAll("\\|", "");
					}
					word = word.substring(BOKMÅL_LENGTH).trim();
					additional.setWord(word);
					explain.trim();
					additional.setExplain(explain.startsWith(ENGELSK) ? explain : BOKMÅL + explain);
					
					results.add(additional);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	private static String getEnglish(List<String> item, Index index) {
		if (index.value + 1 < item.size()) {
			String english = item.get(index.value + 1);
			if (english.startsWith(ENGELSK)) {
				++index.value;
				return Constants.LINE + english;
			}
		}
		return "";
	}

	private static boolean isMultiple(String next) {
		Matcher matcher = LINE.matcher(next);
		if (matcher.matches()) {
			String first_two_words = matcher.group(1);
			return !KEYWORDS.containsKey(first_two_words);
		}
		return true;
	}

	private static String handleMultiple(List<String> item, Index i, final String item_attr) {
		String result = item_attr + getEnglish(item, i);
		Index start = new Index(i.value + 1);
		for (; start.value < item.size(); ++start.value) {
			String next = item.get(start.value);
			if (isMultiple(next)) {
				result += Constants.LINE + next + getEnglish(item, start);
			} else {
				break;
			}
		}
		i.value = start.value - 1;
		return result;
	}

}
