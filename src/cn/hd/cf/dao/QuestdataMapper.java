package cn.hd.cf.dao;

import cn.hd.cf.model.Questdata;
import cn.hd.cf.model.QuestdataExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface QuestdataMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table questdata
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int countByExample(QuestdataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table questdata
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int deleteByExample(QuestdataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table questdata
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int deleteByPrimaryKey(Integer questid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table questdata
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int insert(Questdata record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table questdata
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int insertSelective(Questdata record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table questdata
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    List<Questdata> selectByExampleWithBLOBs(QuestdataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table questdata
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    List<Questdata> selectByExample(QuestdataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table questdata
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    Questdata selectByPrimaryKey(Integer questid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table questdata
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int updateByExampleSelective(@Param("record") Questdata record, @Param("example") QuestdataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table questdata
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int updateByExampleWithBLOBs(@Param("record") Questdata record, @Param("example") QuestdataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table questdata
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int updateByExample(@Param("record") Questdata record, @Param("example") QuestdataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table questdata
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int updateByPrimaryKeySelective(Questdata record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table questdata
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int updateByPrimaryKeyWithBLOBs(Questdata record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table questdata
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int updateByPrimaryKey(Questdata record);
}