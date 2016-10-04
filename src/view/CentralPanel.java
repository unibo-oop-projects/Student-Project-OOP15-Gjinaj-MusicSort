package view;

import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import controller.MainController;
import model.IPlaylist;
import model.PlaylistModelTable;
import model.Song;
import model.Playlist;

/**
 * 
 * @author rrok
 *
 */
public class CentralPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable showSongJTable;
	private PlaylistModelTable myTableModel;
	private MainController controller;
	
	public CentralPanel(MainController controller) {
		this.controller = controller;
		JScrollPane scrollPaneShowSongs = new JScrollPane();
		this.add(scrollPaneShowSongs, "name_9428167759486");

		myTableModel = new PlaylistModelTable();
		
		
		showSongJTable = new JTable();
		
		Vector<String> columnNameS = new Vector<String>();
        columnNameS.add("Artist");
        columnNameS.add("Title");
        columnNameS.add("Album");
        
        myTableModel.setColumnNames(columnNameS);
        
		showSongJTable.setModel(myTableModel);
		showSongJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPaneShowSongs.setViewportView(showSongJTable);
		showSongJTable.addMouseListener(new MouseAdapter(){
			
			private int rowSelected;

			public void mousePressed(MouseEvent e){
		        if (e.isPopupTrigger()){
		            doPop(e);
		            
		            JTable source = (JTable)e.getSource();
		            int row = source.rowAtPoint( e.getPoint() );
		            int column = source.columnAtPoint( e.getPoint() );

		            if (! source.isRowSelected(row))
		                source.changeSelection(row, column, false, false);
		            System.out.println("riga numero "+row+" "+"colonna numero:"+ column);
		        }
		    }

		    public void mouseReleased(MouseEvent e){
		        if (e.isPopupTrigger()){
		            doPop(e);
		        }
		    }

		    private void doPop(MouseEvent e){
		        PopUpDemo menu = new PopUpDemo(controller ,rowSelected);
		        menu.show(e.getComponent(), e.getX(), e.getY());
		    }

		    @Override
		    public void mouseClicked(MouseEvent e) {
		     // TODO Auto-generated method stub
		     super.mouseClicked(e);
		     JTable source = (JTable)e.getSource();
	            int row = source.rowAtPoint( e.getPoint() );
	            int column = source.columnAtPoint( e.getPoint() );
		     
	            if(e.getClickCount()==2){
		      
		            controller.onSongSelected(row);
		            controller.startBarThread();
		            if (! source.isRowSelected(row))
		                source.changeSelection(row, column, false, false);
		            //System.out.println("doppio click in riga numero "+row+" "+"colonna numero:"+ column);
		            controller.setPauseIcon();
		            if(controller.getProgressBar()!=null) {
						controller.getProgressBar();
						controller.getProgressBar().cleanBarData();
					} else
		            	controller.startBarThread();
		     }
		     else {
				rowSelected = row;
			}
		    }
		});
		this.setLayout(new GridBagLayout());
	}
	
	public void addSong(Song song){
		myTableModel.addSong(song);
	}
	
	public void changeModel(IPlaylist playlist){
		myTableModel.refresh(playlist);
	}
}