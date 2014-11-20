package cn.hd.cf.dao;

import cn.hd.cf.model.Toplist;
import cn.hd.cf.model.ToplistExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ToplistMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table toplist
     *
     * @mbggenerated Thu Nov 06 18:21:16 CST 2014
     */
    int countByExample(ToplistExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table toplist
     *
     * @mbggenerated Thu Nov 06 18:21:16 CST 2014
     */
    int deleteByExample(ToplistExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table toplist
     *
     * @mbggenerated Thu Nov 06 18:21:16 CST 2014
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table toplist
     *
     * @mbggenerated Thu Nov 06 18:21:16 CST 2014
     */
    int insert(Toplist record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table toplist
     *
     * @mbggenerated Thu Nov 06 18:21:16 CST 2014
     */
    int insertSelective(Toplist record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table toplist
     *
     * @mbggenerated Thu Nov 06 18:21:16 CST 2014
     */
    List<Toplist> selectByExample(ToplistExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table toplist
     *
     * @mbggenerated Thu Nov 06 18:21:16 CST 2014
     */
    Toplist selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table toplist
     *
     * @mbggenerated Thu Nov 06 18:21:16 CST 2014
     */
    int updateByExampleSelective(@Param("record") Toplist record, @Param("example") ToplistExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table toplist
     *
     * @mbggenerated Thu Nov 06 18:21:16 CST 2014
     */
    int updateByExample(@Param("record") Toplist record, @Param("example") ToplistExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table toplist
     *
     * @mbggenerated Thu Nov 06 18:21:16 CST 2014
     */
    int updateByPrimaryKeySelective(Toplist record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table toplist
     *
     * @mbggenerated Thu Nov 06 18:21:16 CST 2014
     */
    int updateByPrimaryKey(Toplist record);
}