package no.dict.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.dict.utils.Constants;

public class DictItemFactory {
	private static final String BOKMÅL = "Bokmål ";
	private static final String OPPSLAGSORD = BOKMÅL + "oppslagsord ";
	private static final int OPPSLAGSORD_LENGTH = OPPSLAGSORD.length();
	private static final String ENGELSK = "Engelsk ";
	private static final String BØYNING = BOKMÅL + "bøyning ";
	private static final int BØYNING_LENGTH = BØYNING.length();
	private static final String FORKLARING = BOKMÅL + "forklaring ";
	private static final int FORKLARING_LENGTH = FORKLARING.length();
	private static final String EKSEMPEL = BOKMÅL + "eksempel ";
	private static final int EKSEMPEL_LENGTH = EKSEMPEL.length();
	private static final int BOKMÅL_LENGTH = BOKMÅL.length();
	private static final String SAMMENSETNING = BOKMÅL + "sammensetning ";
	private static final int SAMMENSETNING_LENGTH = SAMMENSETNING.length();
	private static final String ALTERNATIV = BOKMÅL + "alternativ ";
	private static final int ALTERNATIV_LENGTH = ALTERNATIV.length();
	private static final String KOMMENTAR = BOKMÅL + "kommentar ";
	private static final int KOMMENTAR_LENGTH = KOMMENTAR.length();
	private static final String TILLEGGSFORKLARING = BOKMÅL + "tilleggsforklaring ";
	private static final int TILLEGGSFORKLARING_LENGTH = TILLEGGSFORKLARING.length();
	private static final String GRAMMATIKK = BOKMÅL + "grammatikk ";
	private static final String ENGELSK_GRAMMATIKK = ENGELSK + "grammatikk ";
	private static final int GRAMMATIKK_LENGTH = GRAMMATIKK.length();
	private static final int ENGELSK_GRAMMATIKK_LENGTH = ENGELSK_GRAMMATIKK.length();
	private static final String UTTRYKK = BOKMÅL + "uttrykk ";
	private static final int UTTRYKK_LENGTH = UTTRYKK.length();

	private static enum KEYWORD_INDEX {
		OPPSLAGSORD, BØYNING, ALTERNATIV, GRAMMATIKK, ENGELSK_GRAMMATIKK, KOMMENTAR, TILLEGGSFORKLARING, FORKLARING, SAMMENSETNING, EKSEMPEL, UTTRYKK
	}

	private static final Pattern LINE = Pattern.compile("([\\p{Alpha}åøæÅØÆ]+ [\\p{Alpha}åøæÅØÆ]+ )[\\s\\S]*",
			Pattern.UNICODE_CHARACTER_CLASS);
	private static final Map<String, KEYWORD_INDEX> KEYWORDS = new HashMap<String, KEYWORD_INDEX>();
	static {
		KEYWORDS.put(OPPSLAGSORD, KEYWORD_INDEX.OPPSLAGSORD);
		KEYWORDS.put(BØYNING, KEYWORD_INDEX.BØYNING);
		KEYWORDS.put(ALTERNATIV, KEYWORD_INDEX.ALTERNATIV);
		KEYWORDS.put(GRAMMATIKK, KEYWORD_INDEX.GRAMMATIKK);
		KEYWORDS.put(ENGELSK_GRAMMATIKK, KEYWORD_INDEX.ENGELSK_GRAMMATIKK);
		KEYWORDS.put(KOMMENTAR, KEYWORD_INDEX.KOMMENTAR);
		KEYWORDS.put(TILLEGGSFORKLARING, KEYWORD_INDEX.TILLEGGSFORKLARING);
		KEYWORDS.put(FORKLARING, KEYWORD_INDEX.FORKLARING);
		KEYWORDS.put(SAMMENSETNING, KEYWORD_INDEX.SAMMENSETNING);
		KEYWORDS.put(EKSEMPEL, KEYWORD_INDEX.EKSEMPEL);
		KEYWORDS.put(UTTRYKK, KEYWORD_INDEX.UTTRYKK);
	}

	public static DictItem getDictItem(List<String> item) {
		DictItem result = new DictItem();
		Index index = new Index(0);
		for (; index.value < item.size(); index.value++) {
			String str = item.get(index.value);
			Matcher matcher = LINE.matcher(str);
			String first_two_words = null;
			if (matcher.matches())
				first_two_words = matcher.group(1);
			KEYWORD_INDEX keyword_index = KEYWORDS.get(first_two_words);
			if (keyword_index == null) {
				// System.out.println("------Error: " + str);
				result.setExamples(result.getExamples() + handleMultiple(item, index, str.substring(BOKMÅL_LENGTH)));
				// System.out.println(result.examples);
				continue;
			}
			switch (keyword_index) {
			case OPPSLAGSORD:
				String[] fields = str.substring(OPPSLAGSORD_LENGTH).split(Constants.LINE);
				result.setSyllabel(fields[0]);
				result.setWord(fields[0].replaceAll("\\|", "") + getEnglish(item, index));
				if (fields.length != 3) {
					result.setExplain(fields[1]);
				} else {
					result.setSound(fields[1]);
					result.setClazz(fields[2]);
				}
				break;
			// } else if (str.startsWith(BØYNING)) {
			case BØYNING:
				result.setFormat(str.substring(BØYNING_LENGTH));
				break;
			// } else if (str.startsWith(ALTERNATIV)) {
			case ALTERNATIV:
				result.setAlternative(handleMultiple(item, index, str.substring(ALTERNATIV_LENGTH)));
				break;
			// } else if (str.startsWith(GRAMMATIKK)) {
			case GRAMMATIKK:
				result.setGrammer(handleMultiple(item, index, str.substring(GRAMMATIKK_LENGTH)));
				break;
			// } else if (str.startsWith(ENGELSK_GRAMMATIKK)) {
			case ENGELSK_GRAMMATIKK:
				result.setGrammer(result.getGrammer() + ENGELSK + str.substring(ENGELSK_GRAMMATIKK_LENGTH));
				break;
			// } else if (str.startsWith(KOMMENTAR)) {
			case KOMMENTAR:
				result.setComment(str.substring(KOMMENTAR_LENGTH) + getEnglish(item, index));
				break;
			// } else if (str.startsWith(TILLEGGSFORKLARING)) {
			case TILLEGGSFORKLARING:
				result.setExplain(
						result.getExplain() + str.substring(TILLEGGSFORKLARING_LENGTH) + getEnglish(item, index));
				break;
			// } else if (str.startsWith(FORKLARING)) {
			case FORKLARING:
				result.setExplain(result.getExplain() + str.substring(FORKLARING_LENGTH) + getEnglish(item, index));
				break;
			// } else if (str.startsWith(SAMMENSETNING)) {
			case SAMMENSETNING:
				result.setComposite(handleMultiple(item, index, str.substring(SAMMENSETNING_LENGTH)));
				break;
			// } else if (str.startsWith(EKSEMPEL)) {
			case EKSEMPEL:
				result.setExamples(result.getExamples() + handleMultiple(item, index, str.substring(EKSEMPEL_LENGTH)));
				break;
			// } else if (str.startsWith(UTTRYKK)) {
			case UTTRYKK:
				result.setPhrases(handleMultiple(item, index, str.substring(UTTRYKK_LENGTH)));
				break;
			// } else {
			default:

				break;
			}
		}
		return result;
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
				result += Constants.LINE + next.substring(BOKMÅL_LENGTH) + getEnglish(item, start);
			} else {
				break;
			}
		}
		i.value = start.value - 1;
		return result;
	}

}
