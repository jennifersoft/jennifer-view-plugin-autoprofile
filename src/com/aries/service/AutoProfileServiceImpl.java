package com.aries.service;

import com.aries.data.viewdata.AutoProfileSettingViewData;
import com.aries.data.viewdata.ViewDataSerializer;
import com.aries.view.core.nio.DataServerCommandCaller;
import com.aries.view.core.nio.DataServerException;
import aries.net.CommandRequest;
import aries.net.CommandResponse;
import aries.nio.j5.CmdDef;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AutoProfileServiceImpl implements AutoProfileService {
	public List<AutoProfileSettingViewData> getAllSettings(short sid) throws DataServerException {
		CommandRequest commandRequest = new CommandRequest(CmdDef.AutoProfile, CmdDef.AutoProfile_getAllSettings, ViewDataSerializer.marshall(null));
		CommandResponse response = DataServerCommandCaller.call(sid, commandRequest, DataServerCommandCaller.TIMEOUT_FOR_NORMAL);
		return ViewDataSerializer.unmarshall(response.nullableBody);
	}

	public AutoProfileSettingViewData getSetting(short sid, int oid) throws DataServerException {
		CommandRequest commandRequest = new CommandRequest(CmdDef.AutoProfile, CmdDef.AutoProfile_getSetting, ViewDataSerializer.marshall(new Integer(oid)));
		CommandResponse response = DataServerCommandCaller.call(sid, commandRequest, DataServerCommandCaller.TIMEOUT_FOR_NORMAL);
		return ViewDataSerializer.unmarshall(response.nullableBody);
	}

	public boolean putSetting(short sid, AutoProfileSettingViewData params) throws DataServerException {
		CommandRequest commandRequest = new CommandRequest(CmdDef.AutoProfile, CmdDef.AutoProfile_putSetting, ViewDataSerializer.marshall(params));
		CommandResponse response = DataServerCommandCaller.call(sid, commandRequest, DataServerCommandCaller.TIMEOUT_FOR_NORMAL);
		return response != null;
	}

	public boolean removeSetting(short sid, int oid) throws DataServerException {
		CommandRequest commandRequest = new CommandRequest(CmdDef.AutoProfile, CmdDef.AutoProfile_removeSetting, ViewDataSerializer.marshall(new Integer(oid)));
		CommandResponse response = DataServerCommandCaller.call(sid, commandRequest, DataServerCommandCaller.TIMEOUT_FOR_NORMAL);
		return response != null;
	}

	public boolean restoreAllAutoProfiledClasses(short sid, int oid) throws DataServerException {
		CommandRequest commandRequest = new CommandRequest(CmdDef.AgentControl, CmdDef.AgentControl_restoreAllAutoProfiledClasses, ViewDataSerializer.marshall(new Integer(oid)));
		CommandResponse response = DataServerCommandCaller.call(sid, commandRequest, DataServerCommandCaller.TIMEOUT_FOR_NORMAL);
		return response != null;
	}
}

