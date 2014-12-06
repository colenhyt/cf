package cn.hd.cf.dao;

import cn.hd.cf.model.Titledata;
import cn.hd.cf.model.TitledataExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TitledataMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table titledata
     *
     * @mbggenerated Sat Dec 06 10:36:00 CST 2014
     */
    int countByExample(TitledataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table titledata
     *
     * @mbggenerated Sat Dec 06 10:36:00 CST 2014
     */
    int deleteByExample(TitledataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table titledata
     *
     * @mbggenerated Sat Dec 06 10:36:00 CST 2014
     */
    int deleteByPrimaryKey(Integer titleid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table titledata
     *
     * @mbggenerated Sat Dec 06 10:36:00 CST 2014
     */
    int insert(Titledata record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table titledata
     *
     * @mbggenerated Sat Dec 06 10:36:00 CST 2014
     */
    int insertSelective(Titledata record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table titledata
     *
     * @mbggenerated Sat Dec 06 10:36:00 CST 2014
     */
    List<Titledata> selectByExampleWithBLOBs(TitledataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table titledata
     *
     * @mbggenerated Sat Dec 06 10:36:00 CST 2014
     */
    List<Titledata> selectByExample(TitledataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table titledata
     *
     * @mbggenerated Sat Dec 06 10:36:00 CST 2014
     */
    Titledata selectByPrimaryKey(Integer titleid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table titledata
     *
     * @mbggenerated Sat Dec 06 10:36:00 CST 2014
     */
    int updateByExampleSelective(@Param("record") Titledata record, @Param("example") TitledataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table titledata
     *
     * @mbggenerated Sat Dec 06 10:36:00 CST 2014
     */
    int updateByExampleWithBLOBs(@Param("record") Titledata record, @Param("example") TitledataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table titledata
     *
     * @mbggenerated Sat Dec 06 10:36:00 CST 2014
     */
    int updateByExample(@Param("record") Titledata record, @Param("example") TitledataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table titledata
     *
     * @mbggenerated Sat Dec 06 10:36:00 CST 2014
     */
    int updateByPrimaryKeySelective(Titledata record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table titledata
     *
     * @mbggenerated Sat Dec 06 10:36:00 CST 2014
     */
    int updateByPrimaryKeyWithBLOBs(Titledata record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table titledata
     *
     * @mbggenerated Sat Dec 06 10:36:00 CST 2014
     */
    int updateByPrimaryKey(Titledata record);
}