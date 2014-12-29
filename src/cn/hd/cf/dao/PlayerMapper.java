package cn.hd.cf.dao;

import cn.hd.cf.model.Player;
import cn.hd.cf.model.PlayerExample;
import cn.hd.cf.model.PlayerWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PlayerMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Mon Dec 29 09:00:05 CST 2014
     */
    int countByExample(PlayerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Mon Dec 29 09:00:05 CST 2014
     */
    int deleteByExample(PlayerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Mon Dec 29 09:00:05 CST 2014
     */
    int deleteByPrimaryKey(Integer playerid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Mon Dec 29 09:00:05 CST 2014
     */
    int insert(PlayerWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Mon Dec 29 09:00:05 CST 2014
     */
    int insertSelective(PlayerWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Mon Dec 29 09:00:05 CST 2014
     */
    List<PlayerWithBLOBs> selectByExampleWithBLOBs(PlayerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Mon Dec 29 09:00:05 CST 2014
     */
    List<Player> selectByExample(PlayerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Mon Dec 29 09:00:05 CST 2014
     */
    PlayerWithBLOBs selectByPrimaryKey(Integer playerid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Mon Dec 29 09:00:05 CST 2014
     */
    int updateByExampleSelective(@Param("record") PlayerWithBLOBs record, @Param("example") PlayerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Mon Dec 29 09:00:05 CST 2014
     */
    int updateByExampleWithBLOBs(@Param("record") PlayerWithBLOBs record, @Param("example") PlayerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Mon Dec 29 09:00:05 CST 2014
     */
    int updateByExample(@Param("record") Player record, @Param("example") PlayerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Mon Dec 29 09:00:05 CST 2014
     */
    int updateByPrimaryKeySelective(PlayerWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Mon Dec 29 09:00:05 CST 2014
     */
    int updateByPrimaryKeyWithBLOBs(PlayerWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Mon Dec 29 09:00:05 CST 2014
     */
    int updateByPrimaryKey(Player record);
}