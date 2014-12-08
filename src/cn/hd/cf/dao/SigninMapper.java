package cn.hd.cf.dao;

import cn.hd.cf.model.Signin;
import cn.hd.cf.model.SigninExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SigninMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table signin
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int countByExample(SigninExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table signin
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int deleteByExample(SigninExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table signin
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table signin
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int insert(Signin record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table signin
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int insertSelective(Signin record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table signin
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    List<Signin> selectByExample(SigninExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table signin
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    Signin selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table signin
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int updateByExampleSelective(@Param("record") Signin record, @Param("example") SigninExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table signin
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int updateByExample(@Param("record") Signin record, @Param("example") SigninExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table signin
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int updateByPrimaryKeySelective(Signin record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table signin
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    int updateByPrimaryKey(Signin record);
}