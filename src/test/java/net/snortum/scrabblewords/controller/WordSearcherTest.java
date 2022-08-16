package net.snortum.scrabblewords.controller;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ProgressBar;

import junit.framework.AssertionFailedError;

import net.snortum.scrabblewords.model.InputData;
import net.snortum.scrabblewords.model.ScrabbleWord;

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
		Set<ScrabbleWord> words = searcher.getWords();
		assertTrue(words.isEmpty());
	}
	
	@Test
	public void whenInputData_anm_ReturnListOfSixWords() {
		InputData data = new InputData.Builder("anm").build();
		Set<ScrabbleWord> expectedWords = new TreeSet<>();
		expectedWords.add(new ScrabbleWord("man", "man", false));
		expectedWords.add(new ScrabbleWord("nam", "nam", false));
		expectedWords.add(new ScrabbleWord("am", "am", false));
		expectedWords.add(new ScrabbleWord("ma", "ma", false));
		expectedWords.add(new ScrabbleWord("an", "an", false));
		expectedWords.add(new ScrabbleWord("na", "na", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<ScrabbleWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_an_AndContains_m_ReturnListOfFourWords() {
		InputData data = new InputData.Builder("an")
				.contains("m")
				.build();
		Set<ScrabbleWord> expectedWords = new TreeSet<>();
		expectedWords.add(new ScrabbleWord("man", "man", false));
		expectedWords.add(new ScrabbleWord("nam", "nam", false));
		expectedWords.add(new ScrabbleWord("am", "am", false));
		expectedWords.add(new ScrabbleWord("ma", "ma", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<ScrabbleWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_an_AndStartsWith_m_ReturnListOfTwoWords() {
		InputData data = new InputData.Builder("an")
				.startsWith("m")
				.build();
		Set<ScrabbleWord> expectedWords = new TreeSet<>();
		expectedWords.add(new ScrabbleWord("man", "man", false));
		expectedWords.add(new ScrabbleWord("ma", "ma", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<ScrabbleWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_an_AndEndsWith_m_ReturnListOfTwoWords() {
		InputData data = new InputData.Builder("an")
				.endsWith("m")
				.build();
		Set<ScrabbleWord> expectedWords = new TreeSet<>();
		expectedWords.add(new ScrabbleWord("nam", "nam", false));
		expectedWords.add(new ScrabbleWord("am", "am", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<ScrabbleWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_anm_AndContains_M_ReturnListOfFourWords() {
		InputData data = new InputData.Builder("anm")
				.contains("M")
				.build();
		Set<ScrabbleWord> expectedWords = new TreeSet<>();
		expectedWords.add(new ScrabbleWord("man", "man", false));
		expectedWords.add(new ScrabbleWord("nam", "nam", false));
		expectedWords.add(new ScrabbleWord("am", "am", false));
		expectedWords.add(new ScrabbleWord("ma", "ma", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<ScrabbleWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_as_AndContains_aM_ReturnListOfTwoWords() {
		InputData data = new InputData.Builder("as")
				.contains("mA")
				.build();
		Set<ScrabbleWord> expectedWords = new TreeSet<>();
		expectedWords.add(new ScrabbleWord("mas", "mas", false));
		expectedWords.add(new ScrabbleWord("ma", "ma", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<ScrabbleWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_anm_AndStartsWith_M_ReturnListOfTwoWords() {
		InputData data = new InputData.Builder("anm")
				.startsWith("M")
				.build();
		Set<ScrabbleWord> expectedWords = new TreeSet<>();
		expectedWords.add(new ScrabbleWord("man", "man", false));
		expectedWords.add(new ScrabbleWord("ma", "ma", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<ScrabbleWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_anm_AndEndsWith_M_ReturnListOfTwoWords() {
		InputData data = new InputData.Builder("anm")
				.endsWith("M")
				.build();
		Set<ScrabbleWord> expectedWords = new TreeSet<>();
		expectedWords.add(new ScrabbleWord("nam", "nam", false));
		expectedWords.add(new ScrabbleWord("am", "am", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<ScrabbleWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_a_AndStartsWith_m_AndEndsWith_n_ReturnListOfOneWord() {
		InputData data = new InputData.Builder("a")
				.startsWith("m")
				.endsWith("n")
				.build();
		Set<ScrabbleWord> expectedWords = new TreeSet<>();
		expectedWords.add(new ScrabbleWord("man", "man", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<ScrabbleWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_a_AndStartsWith_m_AndContains_a_ReturnListOfOneWord() {
		InputData data = new InputData.Builder("a")
				.startsWith("m")
				.contains("a")
				.build();
		Set<ScrabbleWord> expectedWords = new TreeSet<>();
		expectedWords.add(new ScrabbleWord("ma", "ma", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<ScrabbleWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_a_AndEndsWith_m_AndContains_a_ReturnListOfOneWord() {
		InputData data = new InputData.Builder("a")
				.endsWith("m")
				.contains("a")
				.build();
		Set<ScrabbleWord> expectedWords = new TreeSet<>();
		expectedWords.add(new ScrabbleWord("am", "am", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<ScrabbleWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_a_AndStartsWith_m_AndContains_n_AndEndsWith_a_ReturnListOfOneWord() {
		InputData data = new InputData.Builder("a")
				.startsWith("m")
				.endsWith("a")
				.contains("n")
				.build();
		Set<ScrabbleWord> expectedWords = new TreeSet<>();
		expectedWords.add(new ScrabbleWord("mana", "mana", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<ScrabbleWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_bdot_ReturnListOfFiveWords() {
		InputData data = new InputData.Builder("b.").build();
		Set<ScrabbleWord> expectedWords = new TreeSet<>();
		expectedWords.add(new ScrabbleWord("ba", "b", false));
		expectedWords.add(new ScrabbleWord("be", "b", false));
		expectedWords.add(new ScrabbleWord("bi", "b", false));
		expectedWords.add(new ScrabbleWord("bo", "b", false));
		expectedWords.add(new ScrabbleWord("by", "b", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<ScrabbleWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_bdot_AndEndsWith_y_ReturnListOfOneWord() {
		InputData data = new InputData.Builder("b.")
				.endsWith("z")
				.build();
		Set<ScrabbleWord> expectedWords = new TreeSet<>();
		expectedWords.add(new ScrabbleWord("biz", "bz", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<ScrabbleWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_anm_AndContains_dotadot_ReturnListOfThreeWords() {
		InputData data = new InputData.Builder("anm")
				.contains(".a.")
				.build();
		Set<ScrabbleWord> expectedWords = new TreeSet<>();
		expectedWords.add(new ScrabbleWord("mana", "mana", false));
		expectedWords.add(new ScrabbleWord("man", "man", false));
		expectedWords.add(new ScrabbleWord("nam", "nam", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<ScrabbleWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}

	@Test
	public void whenInputData_am_AndContains_abackslashb_ReturnListOfThreeWords() {
		InputData data = new InputData.Builder("am")
				.contains("a\\b")
				.build();
		Set<ScrabbleWord> expectedWords = new TreeSet<>();
		expectedWords.add(new ScrabbleWord("ama", "ama", false));
		expectedWords.add(new ScrabbleWord("ma", "ma", false));
		expectedWords.add(new ScrabbleWord("aa", "aa", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<ScrabbleWord> actualWords = searcher.getWords();
		assertSetsAreEqual(expectedWords, actualWords);
	}
	
	@Test
	public void whenInputData_managed_ReturnFirstInListIsBingo() {
		InputData data = new InputData.Builder("managed").build();
		ScrabbleWord bingoWord = new ScrabbleWord("managed", "managed", true);
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<ScrabbleWord> actualWords = searcher.getWords();
		assertTrue(actualWords.contains(bingoWord));
		Optional<ScrabbleWord> actualBingo = actualWords.stream()
				.filter(word -> word.equals(bingoWord))
				.findFirst();

		if (actualBingo.isPresent()) {
			assertEquals(actualBingo.get().getValue(), bingoWord.getValue());
		} else {
			fail();
		}
	}
	
	@Test
	public void whenInputData_anm_and_numOfWords_3_ReturnListOfTwoWords() {
		InputData data = new InputData.Builder("anm")
				.gameType(TypeOfGame.CROSSWORD)
				.numOfLetters("3")
				.build();
		Set<ScrabbleWord> expectedWords = new TreeSet<>();
		expectedWords.add(new ScrabbleWord("man", "man", false));
		expectedWords.add(new ScrabbleWord("nam", "nam", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<ScrabbleWord> actualWords = searcher.getWords();
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
		Set<ScrabbleWord> expectedWords = new TreeSet<>();
		expectedWords.add(new ScrabbleWord("biali", "biali", false));
		expectedWords.add(new ScrabbleWord("blank", "blank", false));
		expectedWords.add(new ScrabbleWord("blain", "blain", false));
		expectedWords.add(new ScrabbleWord("llano", "llano", false));
		WordSearcher searcher = new WordSearcher(data, progress);
		Set<ScrabbleWord> actualWords = searcher.getWords();
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
