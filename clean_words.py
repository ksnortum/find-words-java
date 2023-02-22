#
# Clean words in dictionary (like Linux /usr/share/dict/words) for use with Scrabble/Wordle/Crosswords
#

# Read all lines into a list
path = '/usr/share/dict/words'
words = []
with open(path, 'r', encoding='utf-8') as f_dictionary:
    for line in f_dictionary.readlines():
        words.append(line)

count = 0
new_words = words.copy()
for word in words:

    # Print progress
    if count % 1000 == 0:
        print("Processing " + word, end="")
    count += 1

    # Start by removing this word.  It will be added back if it passes the tests
    try:
        new_words.remove(word)
    except ValueError:  # Word may have been removed previously
        continue

    # No single letter words or apostrophes
    if len(word) == 1 or word.find("'") > -1:
        continue

    # We don't want two words that differ only by a capital letter
    while word.capitalize() in new_words:
        new_words.remove(word.capitalize())

    while word.lower() in new_words:
        new_words.remove(word.lower())

    # Word passes tests, so add it back
    new_words.append(word.lower())

print("Writing new file...")
print("Original size:", len(words))
print("New size:", len(new_words))

path = '../resources/new_words.txt'
with open(path, 'w', encoding='utf-8') as f_dictionary:
    f_dictionary.writelines(new_words)

print("fin")
