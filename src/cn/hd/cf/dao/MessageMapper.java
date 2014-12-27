package cn.hd.cf.dao;

import cn.hd.cf.model.Message;
import cn.hd.cf.model.MessageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MessageMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbggenerated Sat Dec 13 11:21:15 CST 2014
     */
    int countByExample(MessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbggenerated Sat Dec 13 11:21:15 CST 2014
     */
    int deleteByExample(MessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbggenerated Sat Dec 13 11:21:15 CST 2014
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbggenerated Sat Dec 13 11:21:15 CST 2014
     */
    int insert(Message record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbggenerated Sat Dec 13 11:21:15 CST 2014
     */
    int insertSelective(Message record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbggenerated Sat Dec 13 11:21:15 CST 2014
     */
    List<Message> selectByExample(MessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbggenerated Sat Dec 13 11:21:15 CST 2014
     */
    Message selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbggenerated Sat Dec 13 11:21:15 CST 2014
     */
    int updateByExampleSelective(@Param("record") Message record, @Param("example") MessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbggenerated Sat Dec 13 11:21:15 CST 2014
     */
    int updateByExample(@Param("record") Message record, @Param("example") MessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbggenerated Sat Dec 13 11:21:15 CST 2014
     */
    int updateByPrimaryKeySelective(Message record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbggenerated Sat Dec 13 11:21:15 CST 2014
     */
    int updateByPrimaryKey(Message record);
}