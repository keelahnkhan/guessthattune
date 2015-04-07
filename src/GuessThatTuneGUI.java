/**
 * Implementation of the main frame of the application.
 * Combines view and controllers methods of the app.
 * Also Ryan is a loser.
 * 	@author: Keelan Tucker
 * 
 */


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

import javazoom.jl.decoder.JavaLayerException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GuessThatTuneGUI extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	/** Declaration of player booleans */
	private final static int NOTSTARTED = 0;
    private final static int PLAYING = 1;
    private final static int PAUSED = 2;
    
    private int playerStatus = NOTSTARTED;

	
    /** Selected index **/
    private int indx;
    
	/** Declaration of player */
	MusicPlayer player;
	
	/** Declarations of JButtons */
	JButton load, artist, title, source, portrait, play;
	
	/** Declaration of Labels */
	static JLabel artistLabel, sourceLabel, titleLabel;
	
	/** List component */
	JList<String> songList;
	
	/** File chooser */
	JFileChooser fc;
	
	/** Current collection of files */
	MusicFile[] files;
	
	/**
	 * IS IT AN MP3 FILE!??!?!?!?
	 */
	boolean isMp3(File f) {
		return f.getName().toLowerCase().endsWith(".mp3");
    }
	
	/**
	 * Reads files from the selected directory and
	 * 	places them in the top-level array only if they are 
	 * 	MP3 files.
	 * @param dir The opened directory path.
	 */
	private void getFiles(File dir){
		File[] initFiles = dir.listFiles();
		
		ArrayList<MusicFile> fileList = new ArrayList<MusicFile>();
		int size = initFiles.length;
		for(int i = 0; i < size; i++){
			if(isMp3(initFiles[i])){
				MusicFile file = new MusicFile(initFiles[i]);
				fileList.add(file);
			}else { }//do nothing 
		}
		this.files = new MusicFile[fileList.size()];
		fileList.toArray(this.files);
	}
	
	/**
	 * Updates the list with the current song file list.
	 */
	private void populateList(){
		int len = this.files.length;
		ArrayList<String> fileNames = new ArrayList<String>();
		for(int i = 0; i < len; i++){
			String name = this.files[i].getSource();
			name += " - " + this.files[i].getSongTitle();
			name += " - " + this.files[i].getArtist();
			fileNames.add(name);
		}
		String[] names =  new String[len];
		fileNames.toArray(names);
		songList.setListData(names);
	}
	
	/**
	 * Get the selected file from the list and return it as a MusicFile
	 */
	private MusicFile getSelectedFile() throws IOException{
		indx = getSelectedIndex();
		MusicFile f2 = files[indx];
		return f2;
	}
	
	/**
	 * Get the selected index
	 */
	private int getSelectedIndex(){
		return songList.getSelectedIndex();
	}
	
	/**
	 * Plays the given file, reconstructing the music player
	 */
	private void playFile(){
		artistLabel.setText("Artist: ???");
		sourceLabel.setText("Source: ???");
		titleLabel.setText("Title: ???");
		try {
			FileInputStream input;
			input = new FileInputStream(getSelectedFile().getPathname());
			player = new MusicPlayer(input);
			playerStatus = PLAYING;
			player.play();
		} catch (IOException | JavaLayerException e1) {
			e1.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e){
		String command = e.getActionCommand();
		if("load".equals(command)){
			int returnVal = fc.showDialog(this, "Open");
			
			if(returnVal == JFileChooser.APPROVE_OPTION){
				File dir = fc.getSelectedFile();
				getFiles(dir);
				populateList();
			} else{} //do nothing
			
		} else if("artist".equals(command)){
			try{
				if(getSelectedIndex() != indx){
					artistLabel.setText("Artist: ???");
					sourceLabel.setText("Source: ???");
					titleLabel.setText("Title: ???");
				}
				String artistStr = getSelectedFile().getArtist();
				artistLabel.setText("Artist: " + artistStr);
				System.out.println(artistStr);
			} catch(IOException e1){
				e1.printStackTrace();
			}
		} else if("source".equals(command)){
			try{
				if(getSelectedIndex() != indx){
					artistLabel.setText("Artist: ???");
					sourceLabel.setText("Source: ???");
					titleLabel.setText("Title: ???");
				}
				String sourceStr = getSelectedFile().getSource();	
				sourceLabel.setText("Source: " + sourceStr);
				System.out.println(sourceStr);
			} catch(IOException e1){
				e1.printStackTrace();
			}
		} else if("title".equals(command)){
			try{
				if(getSelectedIndex() != indx){
					artistLabel.setText("Artist: ???");
					sourceLabel.setText("Source: ???");
					titleLabel.setText("Title: ???");
				}
				String titleStr = getSelectedFile().getSongTitle();
				titleLabel.setText("Title: " + titleStr);
				System.out.println(titleStr);
			} catch(IOException e1){
				e1.printStackTrace();
			}
		} else if("play".equals(command)){
			switch(playerStatus){
				case NOTSTARTED:
					playFile();
					break;
				case PLAYING:
					if(getSelectedIndex() == indx){
						player.pause();
						playerStatus = PAUSED;
					}
					else{
						player.pause();
						player.close();
						playFile();
					}
					break;
				case PAUSED:
					try {
						if(getSelectedIndex() == indx){
							playerStatus = PLAYING;
							player.play();
						}
						else{
							player.close();
							playFile();
						}
					} catch (JavaLayerException e1) {						
						e1.printStackTrace();
					}				
			}
		}
	}
	
	static class MusicPanel extends JPanel{
		
		private static final long serialVersionUID = 1L;

		public MusicPanel(){
			super(new GridLayout(3, 1, 0, -20));
			
			artistLabel = new JLabel("Artist: ???");
			sourceLabel = new JLabel("Source: ???");
			titleLabel = new JLabel("Title: ???");
			
			artistLabel.setHorizontalAlignment(JLabel.CENTER);
			sourceLabel.setHorizontalAlignment(JLabel.CENTER);
			titleLabel.setHorizontalAlignment(JLabel.CENTER);
			
			artistLabel.setFont(new Font("Serif", Font.BOLD,30));
			sourceLabel.setFont(new Font("Serif", Font.BOLD,30));
			titleLabel.setFont(new Font("Serif", Font.BOLD,30));

			add(sourceLabel);
			add(titleLabel);
			add(artistLabel);
		}
		
	}
	
	public GuessThatTuneGUI(){
		super(new BorderLayout());
		
		songList = new JList<String>();
		JScrollPane scrollPanel = new JScrollPane(songList,
							JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
							JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,
										BoxLayout.PAGE_AXIS));
		
		InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
		
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "play");
		
		JButton load = new JButton("Load");
		load.addActionListener(this);
		load.setActionCommand("load");
		
		JButton artist = new JButton("Artist");
		artist.addActionListener(this);
		artist.setActionCommand("artist");
		
		JButton source = new JButton("Source");
		source.addActionListener(this);
		source.setActionCommand("source");
		
		JButton title = new JButton("Title");
		title.addActionListener(this);
		title.setActionCommand("title");
		
		JButton play = new JButton("Play");
		play.addActionListener(this);
		play.setActionCommand("play");
		
		buttonPanel.add(play);
		buttonPanel.add(Box.createRigidArea(new Dimension(0,50)));
		buttonPanel.add(title);
		buttonPanel.add(Box.createRigidArea(new Dimension(0,20)));
		buttonPanel.add(artist);
		buttonPanel.add(Box.createRigidArea(new Dimension(0,20)));
		buttonPanel.add(source);
		buttonPanel.add(Box.createRigidArea(new Dimension(0,50)));
		buttonPanel.add(load);
		
		add(scrollPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.LINE_END);
		
		
	}
	
	/**public static void main(String[] args) {
        //Create and set up the window.
        JFrame frame1 = new JFrame("Guess That Tune - Love, Keelan~~~");
        frame1.setResizable(false);
		frame1.setBackground(SystemColor.control);
		frame1.setSize(600, 500);
		frame1.setLocation(100, 100);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        frame1.add(new GuessThatTuneGUI());
 
        frame1.setVisible(true);
    }*/
	
	public static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame1 = new JFrame("Guess That Tune - Love, Keelan~~~");
        
        frame1.setResizable(false);
		frame1.setBackground(SystemColor.control);
		frame1.setSize(800, 500);
		frame1.setLocation(500, 100);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        JFrame frame2 = new JFrame("Music Player");
        frame2.setResizable(true);
        frame2.setBackground(SystemColor.control);
        frame2.setSize(500, 500);
        frame2.setLocation(600, 200);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame1.add(new GuessThatTuneGUI());
        frame2.add(new MusicPanel());
 
        frame1.setVisible(true);
        frame2.setVisible(true);
    }
 
   public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }
}
