package string;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static generators.StringGenerator.createRandomReadableString;
import static generators.StringGenerator.createRandomReadableStringBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KMPTest {
	@Test
	void testWithPatternInside_ShouldAlwaysReturnAMatch() {

		KMP kmp = new KMP();

		String pattern = "aacecaaa";
		String text = createTextWithPatternInside(pattern);

		int[] prefixArray = kmp.getPrefixArray(pattern);
		System.out.println(Arrays.toString(prefixArray));

		int startingPosition = text.indexOf(pattern);
		int actualResult = kmp.searchText(prefixArray, pattern, text);

		assertEquals(startingPosition + pattern.length() - 1, actualResult);
	}

	private String createTextWithPatternInside(String pattern) {
		StringBuilder sb = createRandomReadableStringBuilder(100);
		sb.append(pattern);
		sb.append(createRandomReadableStringBuilder(100));
		return sb.toString();
	}

	@Test
	void testWithRandomInputs_ShouldNotAlwaysReturnAMatch() {
		String pattern = createRandomReadableString(30);
		String text = createRandomReadableString(200);

		int startingPosition = text.indexOf(pattern);

		KMP kmp = new KMP();
		int[] prefixArray = kmp.getPrefixArray(pattern);
		int actualResult = kmp.searchText(prefixArray, pattern, text);

		assertEquals((startingPosition == -1 ? -1 : startingPosition + pattern.length() - 1),
		actualResult);
	}

	@Test
	void testWithRandomTextAndRandomlyChosenCharAsPettern_ShouldAlwaysReturnAMatch() {
		String text = createRandomReadableString(200);
		String letter = String.valueOf(text.charAt(new Random().nextInt(text.length())));
		System.out.println(letter);

		int startingPosition = text.indexOf(letter);
		System.out.println(startingPosition);

		KMP kmp = new KMP();
		int[] prefixArray = kmp.getPrefixArray(letter);
		int actualResult = kmp.searchText(prefixArray, letter, text);

		assertEquals((startingPosition == -1 ? -1 : startingPosition + letter.length() - 1),
		actualResult);
	}
}