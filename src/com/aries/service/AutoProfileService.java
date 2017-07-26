package com.aries.service;

import com.aries.data.viewdata.AutoProfileSettingViewData;
import com.aries.view.core.nio.DataServerException;

import java.util.List;

/**
 *
 *@author david
 *@since 2013. 7. 30.
 */

public interface AutoProfileService {
	public List<AutoProfileSettingViewData> getAllSettings(short sid) throws DataServerException;
	public AutoProfileSettingViewData getSetting(short sid, int oid) throws DataServerException;
	public boolean putSetting(short sid, AutoProfileSettingViewData params) throws DataServerException;
	public boolean removeSetting(short sid, int oid) throws DataServerException;
	public boolean restoreAllAutoProfiledClasses(short sid, int oid) throws DataServerException;
}

