/*
 * Copyright 2009, 2010 Contributors (see credits.txt)
 *
 * This file is part of jEveAssets.
 *
 * jEveAssets is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * jEveAssets is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jEveAssets; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package net.nikr.eve.jeveasset.gui.tabs.jobs;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.ListSelection;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import net.nikr.eve.jeveasset.Program;
import net.nikr.eve.jeveasset.data.Account;
import net.nikr.eve.jeveasset.data.Human;
import net.nikr.eve.jeveasset.data.IndustryJob;
import net.nikr.eve.jeveasset.gui.images.Images;
import net.nikr.eve.jeveasset.gui.shared.JMainTab;
import net.nikr.eve.jeveasset.gui.shared.JAutoColumnTable;
import net.nikr.eve.jeveasset.gui.shared.JMenuAssetFilter;
import net.nikr.eve.jeveasset.gui.shared.JMenuCopy;
import net.nikr.eve.jeveasset.gui.shared.JMenuLookup;
import net.nikr.eve.jeveasset.i18n.TabsJobs;
import net.nikr.eve.jeveasset.io.shared.ApiConverter;


public class IndustryJobsTab extends JMainTab implements ActionListener {

	private final static String ACTION_SELECTED = "ACTION_SELECTED";

	private JComboBox jCharacters;
	private JComboBox jState;
	private JComboBox jActivity;
	private JAutoColumnTable jTable;

	private EventList<IndustryJob> jobsEventList;
	private EventTableModel<IndustryJob> jobsTableModel;

	private List<IndustryJob> all;
	private Map<String, List<IndustryJob>> jobs;
	private Vector<String> characters;

	public IndustryJobsTab(Program program) {
		super(program, TabsJobs.get().industry(), Images.ICON_TOOL_INDUSTRY_JOBS, true);

		jCharacters = new JComboBox();
		jCharacters.setActionCommand(ACTION_SELECTED);
		jCharacters.addActionListener(this);

		jState = new JComboBox();
		jState.setActionCommand(ACTION_SELECTED);
		jState.addActionListener(this);

		jActivity = new JComboBox();
		jActivity.setActionCommand(ACTION_SELECTED);
		jActivity.addActionListener(this);

		JLabel jCharactersLabel = new JLabel(TabsJobs.get().character());
		JLabel jStateLabel = new JLabel(TabsJobs.get().state());
		JLabel jActivityLabel = new JLabel(TabsJobs.get().activity());

		//Table format
		IndustryJobTableFormat industryJobsTableFormat = new IndustryJobTableFormat();
		//Backend
		jobsEventList = new BasicEventList<IndustryJob>();
		//For soring the table
		SortedList<IndustryJob> jobsSortedList = new SortedList<IndustryJob>(jobsEventList);
		//Table Model
		jobsTableModel = new EventTableModel<IndustryJob>(jobsSortedList, industryJobsTableFormat);
		//Tables
		jTable = new JAutoColumnTable(jobsTableModel);
		//Table Selection
		EventSelectionModel<IndustryJob> selectionModel = new EventSelectionModel<IndustryJob>(jobsEventList);
		selectionModel.setSelectionMode(ListSelection.MULTIPLE_INTERVAL_SELECTION_DEFENSIVE);
		jTable.setSelectionModel(selectionModel);
		//Listeners
		installTableMenu(jTable);
		//Sorters
		TableComparatorChooser.install(jTable, jobsSortedList, TableComparatorChooser.MULTIPLE_COLUMN_MOUSE, industryJobsTableFormat);
		//Scroll Panels
		JScrollPane jJobsScrollPanel = jTable.getScrollPanel();

		layout.setHorizontalGroup(
			layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
					.addComponent(jCharactersLabel)
					.addComponent(jCharacters, 200, 200, 200)
					.addComponent(jActivityLabel)
					.addComponent(jActivity, 200, 200, 200)
					.addComponent(jStateLabel)
					.addComponent(jState, 200, 200, 200)
				)
				.addComponent(jJobsScrollPanel, 700, 700, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
					.addComponent(jCharactersLabel, Program.BUTTONS_HEIGHT, Program.BUTTONS_HEIGHT, Program.BUTTONS_HEIGHT)
					.addComponent(jCharacters, Program.BUTTONS_HEIGHT, Program.BUTTONS_HEIGHT, Program.BUTTONS_HEIGHT)
					.addComponent(jStateLabel, Program.BUTTONS_HEIGHT, Program.BUTTONS_HEIGHT, Program.BUTTONS_HEIGHT)
					.addComponent(jState, Program.BUTTONS_HEIGHT, Program.BUTTONS_HEIGHT, Program.BUTTONS_HEIGHT)
					.addComponent(jActivityLabel, Program.BUTTONS_HEIGHT, Program.BUTTONS_HEIGHT, Program.BUTTONS_HEIGHT)
					.addComponent(jActivity, Program.BUTTONS_HEIGHT, Program.BUTTONS_HEIGHT, Program.BUTTONS_HEIGHT)
				)
				.addComponent(jJobsScrollPanel, 100, 400, Short.MAX_VALUE)
		);
	}

	@Override
	protected void showTablePopupMenu(MouseEvent e) {
		JPopupMenu jTablePopupMenu = new JPopupMenu();

		//Select Single Row
		jTable.setRowSelectionInterval(jTable.rowAtPoint(e.getPoint()), jTable.rowAtPoint(e.getPoint()));
		jTable.setColumnSelectionInterval(0, jTable.getColumnCount()-1);

		updateTableMenu(jTablePopupMenu);

		jTablePopupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	@Override
	public void updateTableMenu(JComponent jComponent){
		jComponent.removeAll();
		jComponent.setEnabled(true);

		

		boolean isSingleRow = jTable.getSelectedRows().length == 1;
		boolean isSelected = (jTable.getSelectedRows().length > 0 && jTable.getSelectedColumns().length > 0);

		IndustryJob industryJob = isSingleRow ? jobsTableModel.getElementAt(jTable.getSelectedRow()) : null;
	//COPY
		if (isSelected && jComponent instanceof JPopupMenu){
			jComponent.add(new JMenuCopy(jTable));
			addSeparator(jComponent);
		}
		jComponent.add(new JMenuAssetFilter(program, industryJob));
		jComponent.add(new JMenuLookup(program, industryJob));
	}

	@Override
	public void updateData() {
		characters = new Vector<String>();
		//characters.add("All");
		jobs = new HashMap<String, List<IndustryJob>>();
		all = new ArrayList<IndustryJob>();
		List<Account> accounts = program.getSettings().getAccounts();
		for (int a = 0; a < accounts.size(); a++){
			List<Human> tempHumans = accounts.get(a).getHumans();
			for (int b = 0; b < tempHumans.size(); b++){
				Human human = tempHumans.get(b);
				if (human.isShowAssets()){
					characters.add(human.getName());
					List<IndustryJob> characterIndustryJobs = ApiConverter.apiIndustryJobsToIndustryJobs(human.getIndustryJobs(), human.getName(), program.getSettings());
					jobs.put(human.getName(), characterIndustryJobs);
					all.addAll(characterIndustryJobs);
					if (human.isUpdateCorporationAssets()){
						String corpKey = TabsJobs.get().whitespace(human.getCorporation());
						if (!characters.contains(corpKey)){
							characters.add(corpKey);
							jobs.put(corpKey, new ArrayList<IndustryJob>());
						}
						List<IndustryJob> corporationIndustryJobs = ApiConverter.apiIndustryJobsToIndustryJobs(human.getIndustryJobsCorporation(), human.getCorporation(), program.getSettings());
						jobs.get(corpKey).addAll(corporationIndustryJobs);
						all.addAll(corporationIndustryJobs);
					}
				}
			}
		}
		if (!characters.isEmpty()){
			jCharacters.setEnabled(true);
			jTable.setEnabled(true);
			jActivity.setEnabled(true);
			jState.setEnabled(true);
			Collections.sort(characters);
			characters.add(0, TabsJobs.get().all());
			jCharacters.setModel( new DefaultComboBoxModel(characters));
			jActivity.setModel( new DefaultComboBoxModel(IndustryJob.IndustryActivity.values()));
			jState.setModel( new DefaultComboBoxModel(IndustryJob.IndustryJobState.values()));
			jCharacters.setSelectedIndex(0);
			jActivity.setSelectedIndex(0);
			jState.setSelectedIndex(0);
		} else {
			jCharacters.setEnabled(false);
			jTable.setEnabled(false);
			jActivity.setEnabled(false);
			jState.setEnabled(false);
			jCharacters.setModel( new DefaultComboBoxModel());
			jCharacters.getModel().setSelectedItem(TabsJobs.get().no());
			jActivity.setModel( new DefaultComboBoxModel());
			jActivity.getModel().setSelectedItem(TabsJobs.get().no());
			jState.setModel( new DefaultComboBoxModel());
			jState.getModel().setSelectedItem(TabsJobs.get().no());
			jobsEventList.clear();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (ACTION_SELECTED.equals(e.getActionCommand())) {
			String selected = (String) jCharacters.getSelectedItem();
			if (characters.size() > 1){
				List<IndustryJob> industryJobsInput;
				List<IndustryJob> industryJobsOutput = new ArrayList<IndustryJob>();
				//Characters
				if (selected.equals(TabsJobs.get().all())){
					industryJobsInput = all;
				} else {
					industryJobsInput = jobs.get(selected);
				}
				//State
				IndustryJob.IndustryJobState sState = (IndustryJob.IndustryJobState) jState.getSelectedItem();
				//Activity
				Object activity = jActivity.getSelectedItem();
				for (int a = 0; a < industryJobsInput.size(); a++){
					IndustryJob industryJob = industryJobsInput.get(a);
					boolean bState = (industryJob.getState().equals(sState) || sState.equals(IndustryJob.IndustryJobState.STATE_ALL));
					boolean bActivity = (industryJob.getActivity().equals(activity) || activity.equals(IndustryJob.IndustryActivity.ACTIVITY_ALL));
					if (bState && bActivity){
						industryJobsOutput.add(industryJob);
					}
				}
				try {
					jobsEventList.getReadWriteLock().writeLock().lock();
					jobsEventList.clear();
					jobsEventList.addAll( industryJobsOutput );
				} finally {
					jobsEventList.getReadWriteLock().writeLock().unlock();
				}
				
			}
		}
	}
}
