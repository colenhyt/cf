package cn.hd.cf.tools;

import java.util.List;

import cn.hd.base.BaseService;
import cn.hd.cf.dao.EventdataMapper;
import cn.hd.cf.model.EventdataExample;
import cn.hd.cf.model.Eventdata;
import cn.hd.cf.model.EventdataExample.Criteria;

public class EventdataService extends BaseService {
	private EventdataMapper	eventdataMapper;
	
	public EventdataMapper getEventdataMapper() {
		return eventdataMapper;
	}

	public void setEventdataMapper(EventdataMapper eventdataMapper) {
		this.eventdataMapper = eventdataMapper;
	}

	public EventdataService()
	{
		initMapper("eventdataMapper");
	}
	
	public Eventdata findActive()
	{
		EventdataExample example = new EventdataExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(Byte.valueOf(DATA_STATUS_ACTIVE));
		List<Eventdata> datas = eventdataMapper.selectByExample(example);
		if (datas.size()>0)
			return datas.get(0);
		return null;
	}
	
	public boolean add(Eventdata record)
	{
		eventdataMapper.insert(record);
		DBCommit();
		return true;
	}
	
	public void resetInacvtive(Eventdata record)
	{
		record.setStatus(DATA_STATUS_INACTIVE);
		eventdataMapper.updateByPrimaryKey(record);
		DBCommit();
	}
}
