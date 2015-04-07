/**
 * Class to describe the music files.
 * 	Keeps relevant information bundled for ease of lookup.
 * 	Also Ryan is a buttface.
 * @author Keelahn Khan
 *
 */


import java.io.File;
import java.io.IOException;

import com.beaglebuddy.mp3.MP3;

public class MusicFile {

	/** File path */
	private String pathname;
	/** Title of song */
	private String songTitle;
	/** Artist of song */
	private String artist;
	/** Album of song (for this app, the anime source) */
	private String source;
	
	//Constructor
	public MusicFile(File file){
		try {
			pathname = file.getAbsolutePath();
			MP3 mp3 = new MP3(file);
			songTitle = mp3.getTitle();
			artist = mp3.getBand();
			source = mp3.getAlbum();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the pathname
	 */
	public String getPathname() {
		return pathname;
	}

	/**
	 * @return the songTitle
	 */
	public String getSongTitle() {
		return songTitle;
	}

	/**
	 * @return the artist
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	
}
