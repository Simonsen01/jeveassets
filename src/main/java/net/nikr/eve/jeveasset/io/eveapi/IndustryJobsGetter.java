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

package net.nikr.eve.jeveasset.io.eveapi;

import com.beimin.eveapi.shared.industryjobs.ApiIndustryJob;
import com.beimin.eveapi.shared.industryjobs.IndustryJobsResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import net.nikr.eve.jeveasset.data.Account;
import net.nikr.eve.jeveasset.data.Human;
import net.nikr.eve.jeveasset.gui.shared.UpdateTask;
import net.nikr.eve.jeveasset.io.shared.AbstractApiGetter;
import org.xml.sax.SAXException;


public class IndustryJobsGetter extends AbstractApiGetter<IndustryJobsResponse> {

	public IndustryJobsGetter() {
		super("Industry Jobs", true, false);
	}

	@Override
	public void load(UpdateTask updateTask, boolean forceUpdate, List<Account> accounts) {
		super.load(updateTask, forceUpdate, accounts);
	}

	@Override
	protected IndustryJobsResponse getResponse(boolean bCorp) throws IOException, SAXException {
		if (bCorp){
			return com.beimin.eveapi.corporation.industryjobs.IndustryJobsParser.getInstance().getIndustryJobsResponse(Human.getApiAuthorization(getHuman()));
		} else {
			return com.beimin.eveapi.character.industryjobs.IndustryJobsParser.getInstance().getIndustryJobsResponse(Human.getApiAuthorization(getHuman()));
		}
	}

	@Override
	protected Date getNextUpdate() {
		return getHuman().getIndustryJobsNextUpdate();
	}

	@Override
	protected void setNextUpdate(Date nextUpdate) {
		getHuman().setIndustryJobsNextUpdate(nextUpdate);
	}

	@Override
	protected void setData(IndustryJobsResponse response, boolean bCorp) {
		List<ApiIndustryJob> industryJobs = new Vector<ApiIndustryJob>(response.getIndustryJobs());
		if (bCorp){
			getHuman().setIndustryJobsCorporation(industryJobs);
		} else {
			getHuman().setIndustryJobs(industryJobs);
		}
	}
}
