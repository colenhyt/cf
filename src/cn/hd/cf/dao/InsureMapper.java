package cn.hd.cf.dao;

import cn.hd.cf.model.Insure;
import cn.hd.cf.model.InsureExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface InsureMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table insure
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int countByExample(InsureExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table insure
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int deleteByExample(InsureExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table insure
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table insure
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int insert(Insure record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table insure
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int insertSelective(Insure record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table insure
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    List<Insure> selectByExample(InsureExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table insure
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    Insure selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table insure
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int updateByExampleSelective(@Param("record") Insure record, @Param("example") InsureExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table insure
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int updateByExample(@Param("record") Insure record, @Param("example") InsureExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table insure
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int updateByPrimaryKeySelective(Insure record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table insure
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int updateByPrimaryKey(Insure record);
}