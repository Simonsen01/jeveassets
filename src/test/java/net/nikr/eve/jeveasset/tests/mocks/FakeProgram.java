/*
 * Copyright 2009-2013 Contributors (see credits.txt)
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

package net.nikr.eve.jeveasset.tests.mocks;

import ca.odell.glazedlists.EventList;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.List;
import net.nikr.eve.jeveasset.Program;
import net.nikr.eve.jeveasset.data.Account;
import net.nikr.eve.jeveasset.data.AccountBalance;
import net.nikr.eve.jeveasset.data.ProfileManager;
import net.nikr.eve.jeveasset.gui.dialogs.settings.UserNameSettingsPanel;
import net.nikr.eve.jeveasset.gui.dialogs.settings.UserPriceSettingsPanel;
import net.nikr.eve.jeveasset.gui.frame.MainWindow;
import net.nikr.eve.jeveasset.gui.frame.StatusPanel;
import net.nikr.eve.jeveasset.gui.shared.components.JMainTab;
import net.nikr.eve.jeveasset.gui.tabs.assets.Asset;
import net.nikr.eve.jeveasset.gui.tabs.assets.AssetsTab;
import net.nikr.eve.jeveasset.gui.tabs.contracts.ContractItem;
import net.nikr.eve.jeveasset.gui.tabs.jobs.IndustryJob;
import net.nikr.eve.jeveasset.gui.tabs.journal.Journal;
import net.nikr.eve.jeveasset.gui.tabs.orders.MarketOrder;
import net.nikr.eve.jeveasset.gui.tabs.overview.OverviewTab;
import net.nikr.eve.jeveasset.gui.tabs.reprocessed.ReprocessedTab;
import net.nikr.eve.jeveasset.gui.tabs.routing.RoutingTab;
import net.nikr.eve.jeveasset.gui.tabs.stockpile.StockpileTab;
import net.nikr.eve.jeveasset.gui.tabs.transaction.Transaction;
import net.nikr.eve.jeveasset.io.online.PriceDataGetter;

/**
 * any method called will throw an exception. extend and override only the ones that are needed to perform the tests.
 * @author Candle
 */
public abstract class FakeProgram extends Program {

	public FakeProgram() {
		super(false);
	}

	@Override
	public AssetsTab getAssetsTab() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void checkForProgramUpdates(final Window parent) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void exit() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public EventList<Asset> getAssetEventList() {
		throw new UnsupportedOperationException("Not implemented");
	}


	@Override
	public MainWindow getMainWindow() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public String getProgramDataVersion() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public StatusPanel getStatusPanel() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void saveSettingsAndProfile() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void showAbout() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void showSettings() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void addMainTab(final JMainTab jMainTab) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public StockpileTab getStockpileTool() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public UserNameSettingsPanel getUserNameSettingsPanel() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public UserPriceSettingsPanel getUserPriceSettingsPanel() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void overviewGroupsChanged() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void tabChanged() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void updateTableMenu() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public OverviewTab getOverviewTab() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void createTrackerDataPoint() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public ReprocessedTab getReprocessedTab() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public EventList<ContractItem> getContractItemEventList() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public EventList<IndustryJob> getIndustryJobsEventList() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public EventList<MarketOrder> getMarketOrdersEventList() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public List<String> getOwners(boolean all) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public List<Account> getAccounts() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public ProfileManager getProfileManager() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public PriceDataGetter getPriceDataGetter() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public EventList<AccountBalance> getAccountBalanceEventList() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public EventList<Transaction> getTransactionsEventList() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public RoutingTab getRoutingTab() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public EventList<Journal> getJournalEventList() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void updateTags() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void saveSettings(String msg) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void saveExit() {
		throw new UnsupportedOperationException("Not implemented");
	}
}
