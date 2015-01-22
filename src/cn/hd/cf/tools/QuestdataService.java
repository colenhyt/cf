package cn.hd.cf.tools;

import java.util.List;

import cn.hd.base.BaseService;
import cn.hd.cf.dao.QuestdataMapper;
import cn.hd.cf.model.QuestdataExample;
import cn.hd.cf.model.Questdata;
import cn.hd.cf.model.QuestdataExample.Criteria;

public class QuestdataService extends BaseService {
	private QuestdataMapper	questdataMapper;
	
	public QuestdataMapper getQuestdataMapper() {
		return questdataMapper;
	}

	public void setQuestdataMapper(QuestdataMapper questdataMapper) {
		this.questdataMapper = questdataMapper;
	}

	public QuestdataService()
	{
		initMapper("questdataMapper");
	}
	
	public Questdata findActive()
	{
		QuestdataExample example = new QuestdataExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(Byte.valueOf(DATA_STATUS_ACTIVE));
		List<Questdata> datas = questdataMapper.selectByExampleWithBLOBs(example);
		if (datas.size()>0)
			return datas.get(0);
		return null;
	}
	
	public boolean add(Questdata record)
	{
		questdataMapper.insert(record);
		DBCommit();
		return true;
	}
	
	public void resetInacvtive(Questdata record)
	{
		record.setStatus(DATA_STATUS_INACTIVE);
		questdataMapper.updateByPrimaryKey(record);
		DBCommit();
	}
}
