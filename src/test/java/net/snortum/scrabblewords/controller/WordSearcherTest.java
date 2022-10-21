package net.snortum.scrabblewords.controller;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ProgressBar;

import junit.framework.AssertionFailedError;

import net.snortum.scrabblewords.model.CustomWord;
import net.snortum.scrabblewords.model.InputData;

import net.snortum.scrabblewords.model.TypeOfGame;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

/**
 * @author Knute Snortum 
 * @version 2.7.1
 */
public class WordSearcherTest {
	
	ProgressBar progress;

	@Before
	public void setUp() {
		JFXPanel fxPanel = new JFXPanel(); // needed to initialize JavaFX
		progress = new ProgressBar();
	}

	@Test
	public void whenInputDataIsEmptyThenReturnEmptyList() {
		InputData data = new InputData.Builder("").build();
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<CustomWord> words = searcher.getWords();
		assertTrue(words.isEmpty());
	}
	
	@Test
	public void whenInputData_anm_ReturnListOfSixWords() {
		InputData data = new InputData.Builder("anm").build();
		Set<CustomWord> expectedWords = new TreeSet<>();
		expectedWords.add(new CustomWord("man", "man", false));
		expectedWords.add(new CustomWord("nam", "nam", false));
		expectedWords.add(new CustomWord("am", "am", false));
		expectedWords.add(new CustomWord("ma", "ma", false));
		expectedWords.add(new CustomWord("an", "an", false));
		expectedWords.add(new CustomWord("na", "na", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<CustomWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_an_AndContains_m_ReturnListOfFourWords() {
		InputData data = new InputData.Builder("an")
				.contains("m")
				.build();
		Set<CustomWord> expectedWords = new TreeSet<>();
		expectedWords.add(new CustomWord("man", "man", false));
		expectedWords.add(new CustomWord("nam", "nam", false));
		expectedWords.add(new CustomWord("am", "am", false));
		expectedWords.add(new CustomWord("ma", "ma", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<CustomWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_an_AndStartsWith_m_ReturnListOfTwoWords() {
		InputData data = new InputData.Builder("an")
				.startsWith("m")
				.build();
		Set<CustomWord> expectedWords = new TreeSet<>();
		expectedWords.add(new CustomWord("man", "man", false));
		expectedWords.add(new CustomWord("ma", "ma", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<CustomWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_an_AndEndsWith_m_ReturnListOfTwoWords() {
		InputData data = new InputData.Builder("an")
				.endsWith("m")
				.build();
		Set<CustomWord> expectedWords = new TreeSet<>();
		expectedWords.add(new CustomWord("nam", "nam", false));
		expectedWords.add(new CustomWord("am", "am", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<CustomWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_anm_AndContains_M_ReturnListOfFourWords() {
		InputData data = new InputData.Builder("anm")
				.contains("M")
				.build();
		Set<CustomWord> expectedWords = new TreeSet<>();
		expectedWords.add(new CustomWord("man", "man", false));
		expectedWords.add(new CustomWord("nam", "nam", false));
		expectedWords.add(new CustomWord("am", "am", false));
		expectedWords.add(new CustomWord("ma", "ma", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<CustomWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_as_AndContains_aM_ReturnListOfTwoWords() {
		InputData data = new InputData.Builder("as")
				.contains("mA")
				.build();
		Set<CustomWord> expectedWords = new TreeSet<>();
		expectedWords.add(new CustomWord("mas", "mas", false));
		expectedWords.add(new CustomWord("ma", "ma", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<CustomWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_anm_AndStartsWith_M_ReturnListOfTwoWords() {
		InputData data = new InputData.Builder("anm")
				.startsWith("M")
				.build();
		Set<CustomWord> expectedWords = new TreeSet<>();
		expectedWords.add(new CustomWord("man", "man", false));
		expectedWords.add(new CustomWord("ma", "ma", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<CustomWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_anm_AndEndsWith_M_ReturnListOfTwoWords() {
		InputData data = new InputData.Builder("anm")
				.endsWith("M")
				.build();
		Set<CustomWord> expectedWords = new TreeSet<>();
		expectedWords.add(new CustomWord("nam", "nam", false));
		expectedWords.add(new CustomWord("am", "am", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<CustomWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_a_AndStartsWith_m_AndEndsWith_n_ReturnListOfOneWord() {
		InputData data = new InputData.Builder("a")
				.startsWith("m")
				.endsWith("n")
				.build();
		Set<CustomWord> expectedWords = new TreeSet<>();
		expectedWords.add(new CustomWord("man", "man", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<CustomWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_a_AndStartsWith_m_AndContains_a_ReturnListOfOneWord() {
		InputData data = new InputData.Builder("a")
				.startsWith("m")
				.contains("a")
				.build();
		Set<CustomWord> expectedWords = new TreeSet<>();
		expectedWords.add(new CustomWord("ma", "ma", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<CustomWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_a_AndEndsWith_m_AndContains_a_ReturnListOfOneWord() {
		InputData data = new InputData.Builder("a")
				.endsWith("m")
				.contains("a")
				.build();
		Set<CustomWord> expectedWords = new TreeSet<>();
		expectedWords.add(new CustomWord("am", "am", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<CustomWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_a_AndStartsWith_m_AndContains_n_AndEndsWith_a_ReturnListOfOneWord() {
		InputData data = new InputData.Builder("a")
				.startsWith("m")
				.endsWith("a")
				.contains("n")
				.build();
		Set<CustomWord> expectedWords = new TreeSet<>();
		expectedWords.add(new CustomWord("mana", "mana", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<CustomWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_bdot_ReturnListOfFiveWords() {
		InputData data = new InputData.Builder("b.").build();
		Set<CustomWord> expectedWords = new TreeSet<>();
		expectedWords.add(new CustomWord("ba", "b", false));
		expectedWords.add(new CustomWord("be", "b", false));
		expectedWords.add(new CustomWord("bi", "b", false));
		expectedWords.add(new CustomWord("bo", "b", false));
		expectedWords.add(new CustomWord("by", "b", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<CustomWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_bdot_AndEndsWith_y_ReturnListOfOneWord() {
		InputData data = new InputData.Builder("b.")
				.endsWith("z")
				.build();
		Set<CustomWord> expectedWords = new TreeSet<>();
		expectedWords.add(new CustomWord("biz", "bz", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<CustomWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_anm_AndContains_dotadot_ReturnListOfThreeWords() {
		InputData data = new InputData.Builder("anm")
				.contains(".a.")
				.build();
		Set<CustomWord> expectedWords = new TreeSet<>();
		expectedWords.add(new CustomWord("mana", "mana", false));
		expectedWords.add(new CustomWord("man", "man", false));
		expectedWords.add(new CustomWord("nam", "nam", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<CustomWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}

	@Test
	public void whenInputData_am_AndContains_abackslashb_ReturnListOfThreeWords() {
		InputData data = new InputData.Builder("am")
				.contains("a\\b")
				.build();
		Set<CustomWord> expectedWords = new TreeSet<>();
		expectedWords.add(new CustomWord("ama", "ama", false));
		expectedWords.add(new CustomWord("ma", "ma", false));
		expectedWords.add(new CustomWord("aa", "aa", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<CustomWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_managed_ReturnFirstInListIsBingo() {
		InputData data = new InputData.Builder("managed").build();
		CustomWord bingoWord = new CustomWord("managed", "managed", true);
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<CustomWord> actualWords = searcher.getWords();
		assertTrue(actualWords.contains(bingoWord));
		Optional<CustomWord> actualBingo = actualWords.stream()
				.filter(word -> word.equals(bingoWord))
				.findFirst();

		if (actualBingo.isPresent()) {
			assertEquals(actualBingo.get().getValue(), bingoWord.getValue());
		} else {
			fail();
		}
	}
	
	@Test
	public void whenCrosswordInput_and_numOfWords_3_ReturnListOfFourWords() {
		InputData data = new InputData.Builder("")
				.gameType(TypeOfGame.CROSSWORD)
				.startsWith("m")
				.endsWith("n")
				.numOfLetters("3")
				.build();
		Set<CustomWord> expectedWords = new TreeSet<>();
		expectedWords.add(new CustomWord("man", "", false));
		expectedWords.add(new CustomWord("men", "", false));
		expectedWords.add(new CustomWord("mon", "", false));
		expectedWords.add(new CustomWord("mun", "", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<CustomWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}

	// can't have: qwertyasdfghcpvzjm, contains ^..a, expected: blank blain
	@Test
	public void whenWordleTestOne_ReturnListOfTwoWords() {
		InputData data = new InputData.Builder("qwertyasdfghcpvzjm")
				.gameType(TypeOfGame.WORDLE)
				.contains("^..a")
				.numOfLetters("5")
				.build();
		Set<CustomWord> expectedWords = new TreeSet<>();
		expectedWords.add(new CustomWord("biali", "biali", false));
		expectedWords.add(new CustomWord("blank", "blank", false));
		expectedWords.add(new CustomWord("blain", "blain", false));
		expectedWords.add(new CustomWord("llano", "llano", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<CustomWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	private <T> void assertSetsAreEqual(Set<T> expectedSet, Set<T> actualSet) {
		if (expectedSet == null || actualSet == null) {
			throw new AssertionFailedError("Neither expectedSet nor actualSet can be null");
		}

		if (expectedSet.size() != actualSet.size()) {
			System.out.println(actualSet);
			throw new AssertionFailedError("Sets are not the same size");
		}
		
		Iterator<T> it = expectedSet.iterator();
		
		while (it.hasNext()) {
			T element = it.next();
			it.remove();
			assertTrue("Actual word '" + element + "' not found", actualSet.remove(element));
		}
		
		assertTrue(expectedSet.isEmpty());
		assertTrue(actualSet.isEmpty());
	}

}
