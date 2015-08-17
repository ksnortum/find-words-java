package net.snortum.scrabble_words.view;

import javafx.scene.control.ProgressBar;

public class Progressor {
	private final ProgressBar progress;

	/**
	 * Show progress using a progress bar
	 * 
	 * @param progress
	 *            Progress bar
	 * @throws IllegalArgumentException
	 *             if progress bar is null
	 */
	public Progressor( ProgressBar progress ) {
		if ( progress == null ) {
			throw new IllegalArgumentException( "Progress bar cannot be null" );
		}

		this.progress = progress;
	}

	/**
	 * Show progress using System.out
	 */
	public Progressor() {
		this.progress = null;
	}

	/**
	 * Show progress percentage
	 * 
	 * @param percent
	 *            Fraction of one representing a percentage finished
	 */
	public void showProgress( double percent ) {
		if ( progress != null ) {
			progress.setProgress( percent );
		}
		else {
			System.out.printf( "Completed: %5.2f%n", percent * 100 );
		}
	}

}
