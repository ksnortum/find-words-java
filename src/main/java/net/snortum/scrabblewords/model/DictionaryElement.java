package net.snortum.scrabblewords.model;

import java.util.Objects;

/**
 * Immutable class that holds one @{link Dictionary} element comprised of
 * a word and a definition.
 *
 * @author Knute Snortum
 * @version 2.6.0
 */
public class DictionaryElement {
    private final String word;
    private final String definition;

    public DictionaryElement(String word, String definition) {
        this.word = Objects.requireNonNull(word);
        this.definition = definition;
    }

    public String getWord() {
        return word;
    }

    public String getDefinition() {
        return definition;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(word);

        if (definition != null && !definition.isEmpty()) {
            sb.append(", ");
            sb.append(definition);
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DictionaryElement element = (DictionaryElement) o;
        return word.equals(element.word) &&
                Objects.equals(definition, element.definition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, definition);
    }
}
