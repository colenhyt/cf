package cn.hd.cf.dao;

import cn.hd.cf.model.Saving;
import cn.hd.cf.model.SavingExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SavingMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table saving
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    int countByExample(SavingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table saving
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    int deleteByExample(SavingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table saving
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table saving
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    int insert(Saving record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table saving
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    int insertSelective(Saving record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table saving
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    List<Saving> selectByExample(SavingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table saving
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    Saving selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table saving
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    int updateByExampleSelective(@Param("record") Saving record, @Param("example") SavingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table saving
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    int updateByExample(@Param("record") Saving record, @Param("example") SavingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table saving
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    int updateByPrimaryKeySelective(Saving record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table saving
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    int updateByPrimaryKey(Saving record);
}