package com.aries.ctrl;

import com.aries.data.viewdata.AutoProfileSettingViewData;
import com.aries.view.core.nio.DataServerException;
import com.aries.view.domain.Agent;
import com.aries.view.service.DomainService;
import com.aries.view.service.mng.AgentService;
import com.aries.view.service.perf.TextDataService;
import com.aries.view.service.perf.XViewService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import com.aries.service.AutoProfileService;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = { "/plugin" })
public class AutoProfileController {
    @Autowired
    private ServletContext servletContext;
    @Autowired
    TextDataService textDataService;
    @Autowired
    XViewService xviewService;
    @Autowired
    DomainService domainService;
    @Autowired
    AutoProfileService autoProfileService;
    @Autowired
    AgentService agentService;

    @RequestMapping(value = { "/autoprofile" }, method = RequestMethod.GET)
    public ModelAndView mainPage(WebRequest request) throws JSONException
    {
        ModelAndView modelAndView = new ModelAndView();
        ModelMap map = modelAndView.getModelMap();
        map.put("domains", domainService.connectedList());

        return modelAndView;
    }

    @RequestMapping(value = { "/autoprofile/put" }, method = RequestMethod.GET)
    @ResponseBody
    public boolean putAutoProfileSetting(@RequestParam short sid, @RequestParam int oid,
        @RequestParam(required = false, defaultValue = "false") boolean enable,
        @RequestParam(required = false, defaultValue = "8000") long baselineTransactionElapsedTimeForProfile,
        @RequestParam(required = false, defaultValue = "100") long baselineMethodElapsedTimeForProfile,
        @RequestParam(required = false, defaultValue = "0") long baselineIgnorableMethodElapsedTimeForSendToDataServer,
        @RequestParam(required = false, defaultValue = "10000") int profileQueueSize,
        @RequestParam(required = false, defaultValue = "86400000") long profileClearInterval,
        @RequestParam(required = false, defaultValue = "1000") int checkIntervalForSelectTransactionToProfile,
        @RequestParam(required = false, defaultValue = "100") int methodSamplingIntervalDuringProfile,
        @RequestParam(required = false, defaultValue = "100") int methodSamplingStackDepthLimit,
        @RequestParam(required = false, defaultValue = "10000") int maxProfileTargetCount
                                         ) throws DataServerException {

        AutoProfileSettingViewData params = new AutoProfileSettingViewData(
            oid,
            enable,
            baselineTransactionElapsedTimeForProfile,
            baselineMethodElapsedTimeForProfile,
            baselineIgnorableMethodElapsedTimeForSendToDataServer,
            profileQueueSize,
            profileClearInterval,
            checkIntervalForSelectTransactionToProfile,
            methodSamplingIntervalDuringProfile,
            methodSamplingStackDepthLimit,
            maxProfileTargetCount
        );

        return autoProfileService.putSetting(sid, params);
    }

    @RequestMapping(value = { "/autoprofile/get" }, method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAutoProfileSetting(@RequestParam short sid, @RequestParam int oid) throws DataServerException {
        return convertViewDataToMap(sid, autoProfileService.getSetting(sid, oid));
    }

    @RequestMapping(value = { "/autoprofile/remove" }, method = RequestMethod.GET)
    @ResponseBody
    public boolean removeAutoProfileSetting(@RequestParam short sid, @RequestParam int oid) throws DataServerException {
        return autoProfileService.removeSetting(sid, oid);
    }

    @RequestMapping(value = { "/autoprofile/restore" }, method = RequestMethod.GET)
    @ResponseBody
    public boolean restoreAllAutoProfiledClasses(@RequestParam short sid, @RequestParam int oid) throws DataServerException {
        return autoProfileService.restoreAllAutoProfiledClasses(sid, oid);
    }

    @RequestMapping(value = { "/autoprofile/list" }, method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getAutoProfileSettings(@RequestParam short sid) throws DataServerException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<AutoProfileSettingViewData> datas = autoProfileService.getAllSettings(sid);

        for(AutoProfileSettingViewData data : datas) {
            list.add(convertViewDataToMap(sid, data));
        }

        return list;
    }

    private Map<String, Object> convertViewDataToMap(short sid, AutoProfileSettingViewData data) throws DataServerException {
        Map<String, Object> result = new HashMap<String, Object>();
        Agent agent = agentService.findAgent(sid, data.instanceOid);

        result.put("enable", data.enable);
        result.put("baselineTransactionElapsedTimeForProfile", data.baselineTransactionElapsedTimeForProfile);
        result.put("baselineMethodElapsedTimeForProfile", data.baselineMethodElapsedTimeForProfile);
        result.put("baselineIgnorableMethodElapsedTimeForSendToDataServer", data.baselineIgnorableMethodElapsedTimeForSendToDataServer);
        result.put("profileQueueSize", data.profileQueueSize);
        result.put("profileClearInterval", data.profileClearInterval);
        result.put("checkIntervalForSelectTransactionToProfile", data.checkIntervalForSelectTransactionToProfile);
        result.put("methodSamplingIntervalDuringProfile", data.methodSamplingIntervalDuringProfile);
        result.put("methodSamplingStackDepthLimit", data.methodSamplingStackDepthLimit);
        result.put("maxProfileTargetCount", data.maxProfileTargetCount);
        result.put("oid", data.instanceOid);
        result.put("name", agent.getShortName());

        return result;
    }
}