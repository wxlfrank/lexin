package no.dict.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.dict.services.LogService;
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
	private static final Pattern SEARCHEDMATCHER = Pattern.compile("(.*) \\(\"(.*)");
	private static final Pattern END_TRIM_MATCHER = Pattern.compile("([^\\p{Alpha} åøæÅØÆ]*)(.*)");
	private static final Pattern EXAMPLE_PHRASE_MATCHER = Pattern.compile("\\(.*\\)([\\s\\S]*)");

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
		for (; index.value < item.size(); index.value++) {
			String str = item.get(index.value);
			Matcher matcher = LINE.matcher(str);
			String first_two_words = null;
			if (matcher.matches())
				first_two_words = matcher.group(1);
			for (String key : KEYWORDS.keySet()) {
				LogService.log.warning(key);
			}
			KEYWORD_INDEX keyword_index = KEYWORDS.get(first_two_words);
			if (keyword_index == null) {
				String uformelt = handleMultiple(item, index, str);
				if (first_two_words != null || !str.equals("Bokmål uformelt")) {
					result.setExamples(result.getExamples() + BOKMÅL + uformelt);
				}
				continue;
			}
			switch (keyword_index) {
			case OPPSLAGSORD:
				String[] fields = str.substring(OPPSLAGSORD_LENGTH).split(Constants.LINE);
				result.setSyllabel(fields[0]);
				result.setWord(BOKMÅL + fields[0].replaceAll("\\|", "") + getEnglish(item, index));
				if (fields.length != 3) {
					result.setExplain(result.getExplain() + BOKMÅL + fields[1]);
				} else {
					result.setSound(fields[1].replaceAll(" /", "/"));
					result.setClazz(fields[2]);
				}
				break;
			case ENGELSK_OPPSLAGSORD:
				result.setWord(result.getWord() + ENGELSK + str.substring(ENGELSK_OPPSLAGSORD_LENGTH));
				break;
			// } else if (str.startsWith(BØYNING)) {
			case BØYNING:
				result.setFormat(handleMultiple(item, index, str.substring(BØYNING_LENGTH)));
				break;
			// } else if (str.startsWith(ALTERNATIV)) {
			case ALTERNATIV:
				result.setAlternative(handleMultiple(item, index, BOKMÅL + str.substring(ALTERNATIV_LENGTH)));
				break;
			// } else if (str.startsWith(GRAMMATIKK)) {
			case GRAMMATIKK:
				result.setGrammer(
						result.getGrammer() + handleMultiple(item, index, BOKMÅL + str.substring(GRAMMATIKK_LENGTH)));
				break;
			// } else if (str.startsWith(ENGELSK_GRAMMATIKK)) {
			case ENGELSK_GRAMMATIKK:
				result.setGrammer(result.getGrammer() + ENGELSK + str.substring(ENGELSK_GRAMMATIKK_LENGTH));
				break;
			// } else if (str.startsWith(KOMMENTAR)) {
			case KOMMENTAR:
				result.setComment(
						result.getComment() + BOKMÅL + str.substring(KOMMENTAR_LENGTH) + getEnglish(item, index));
				break;
			// } else if (str.startsWith(ENGELSK_KOMMENTAR)) {
			case ENGELSK_KOMMENTAR:
				result.setComment(result.getComment() + ENGELSK + str.substring(ENGELSK_KOMMENTAR_LENGTH)
						+ getEnglish(item, index));
				break;
			// } else if (str.startsWith(TILLEGGSFORKLARING)) {
			case TILLEGGSFORKLARING:
				result.setExplain(result.getExplain() + BOKMÅL + str.substring(TILLEGGSFORKLARING_LENGTH)
						+ getEnglish(item, index));
				break;
			// } else if (str.startsWith(ENGELSK_TILLEGGSFORKLARING)) {
			case ENGELSK_TILLEGGSFORKLARING:
				result.setExplain(result.getExplain() + ENGELSK + str.substring(ENGELSK_TILLEGGSFORKLARING_LENGTH)
						+ getEnglish(item, index));
				break;
			// } else if (str.startsWith(FORKLARING)) {
			case FORKLARING:
				result.setExplain(
						result.getExplain() + BOKMÅL + str.substring(FORKLARING_LENGTH) + getEnglish(item, index));
				break;
			// } else if (str.startsWith(FORKLARING)) {
			case ENGELSK_FORKLARING:
				result.setExplain(result.getExplain() + ENGELSK + str.substring(ENGELSK_FORKLARING_LENGTH)
						+ getEnglish(item, index));
				break;
			// } else if (str.startsWith(SAMMENSETNING)) {
			case SAMMENSETNING:
				result.setComposite(handleMultiple(item, index, BOKMÅL + str.substring(SAMMENSETNING_LENGTH)));
				break;
			// } else if (str.startsWith(EKSEMPEL)) {
			case EKSEMPEL:
				result.setExamples(
						result.getExamples() + handleMultiple(item, index, BOKMÅL + str.substring(EKSEMPEL_LENGTH)));
				break;
			// } else if (str.startsWith(UTTRYKK)) {
			case UTTRYKK:
				String phrasesString = handleMultiple(item, index, BOKMÅL + str.substring(UTTRYKK_LENGTH));
				try {
					String[] phrases = phrasesString.split(Constants.LINE);
					for (int iter = 0; iter < phrases.length;) {
						String phrase = "", explain = "", temp = phrases[iter];
						DictItem additional = null;
						if (temp.startsWith(BOKMÅL)) {
							additional = new DictItem();
							// temp = temp.substring(BOKMÅL_LENGTH);
							matcher = SEARCHEDMATCHER.matcher(temp);
							if (matcher.matches()) {
								phrase += matcher.group(1);
								String sec = matcher.group(2);
								matcher = END_TRIM_MATCHER.matcher(new StringBuilder(sec).reverse().toString());
								if (matcher.matches()) {
									sec = new StringBuilder(matcher.group(2)).reverse().toString();
								}
								explain += sec;
							} else {
								phrase += temp;
							}
						}
						++iter;
						if (additional != null) {
							while (iter < phrases.length) {
								temp = phrases[iter];
								if (temp.startsWith(BOKMÅL)) {
									break;
								}
								matcher = SEARCHEDMATCHER.matcher(temp);
								if (matcher.matches()) {
									phrase += Constants.LINE + matcher.group(1);
									temp = matcher.group(2);
									matcher = END_TRIM_MATCHER.matcher(new StringBuilder(temp).reverse().toString());
									if (matcher.matches()) {
										temp = new StringBuilder(matcher.group(2)).reverse().toString();
									}

								}
								explain += Constants.LINE + (temp.startsWith(ENGELSK) ? "" : ENGELSK) + temp;
								++iter;
							}

							String word = phrase.split(Constants.LINE)[0];
							String replaced = word.replaceAll(" ?\\([\\p{Alpha} åøæÅØÆ]+\\) ?", " ").trim();
							if (replaced.length() != word.length()) {
								additional.setExamples(phrase);
								word = phrase.replaceAll(" ?\\([\\p{Alpha} åøæÅØÆ]+\\) ?", " ").trim();
							} else
								word = phrase;
							if (word.contains("|")) {
								additional.setSyllabel(word);
								word = word.replaceAll("\\|", "");
							}
							additional.setWord(word);
							additional.setExplain(BOKMÅL + explain.replaceAll(" ?\\([\\p{Alpha} åøæÅØÆ]+\\) ?", " ").trim());
							System.out.println(additional);
							results.add(additional);
						}
					}
					result.setPhrases(phrasesString);
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;
			// } else {
			default:

				break;
			}
		}
		System.out.println(result);
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

	private static String handleMultiple(List<String> item, Index i, String first) {
		String result = first + getEnglish(item, i);
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
