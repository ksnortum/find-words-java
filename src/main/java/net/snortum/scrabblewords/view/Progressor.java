package net.snortum.scrabblewords.view;

import javafx.scene.control.ProgressBar;

/**
 * Show progress of a task. Uses either a {@link ProgressBar} or messages sent to the
 * system output.
 * 
 * @author Knute Snortum
 * @version 0.1
 */
public class Progressor {
	public static final int FINISHED = 1;
	
	private final ProgressBar progress;

	/**
	 * Show progress using a progress bar
	 * 
	 * @param progress
	 *            the progress bar
	 * @throws IllegalArgumentException
	 *             if progress bar is null
	 */
	public Progressor(ProgressBar progress) {
		if (progress == null) {
			throw new IllegalArgumentException("Progress bar cannot be null");
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
	public void showProgress(double percent) {
		if (progress != null) {
			if (percent == FINISHED) {
				progress.setVisible(false);
			} else {
				progress.setVisible(true);
				progress.setProgress(percent);
			}
		} else {
			System.out.printf("Completed: %5.2f%n", percent * 100);
		}
	}

}
