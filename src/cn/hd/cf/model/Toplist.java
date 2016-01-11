package cn.hd.cf.model;

import java.math.BigDecimal;
import java.util.Date;

public class Toplist implements Comparable {
	private String updateTimeStr;
	public String getUpdateTimeStr() {
		return updateTimeStr;
	}

	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}

	private String openid;
    public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column toplist.id
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column toplist.type
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    private Integer type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column toplist.week
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    private Integer week;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column toplist.playerid
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    private Integer playerid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column toplist.playername
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    private String playername;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column toplist.money
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    private BigDecimal money;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column toplist.top
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    private Integer top;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column toplist.zan
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    private Integer zan;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column toplist.createtime
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    private Date createtime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column toplist.updatetime
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    private Date updatetime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column toplist.id
     *
     * @return the value of toplist.id
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column toplist.id
     *
     * @param id the value for toplist.id
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column toplist.type
     *
     * @return the value of toplist.type
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column toplist.type
     *
     * @param type the value for toplist.type
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column toplist.week
     *
     * @return the value of toplist.week
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    public Integer getWeek() {
        return week;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column toplist.week
     *
     * @param week the value for toplist.week
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    public void setWeek(Integer week) {
        this.week = week;
    }

    @Override
    public int compareTo(Object o)
    {

    	Toplist sdto = (Toplist)o;

    	BigDecimal otherMoney = sdto.getMoney();

          return otherMoney.compareTo(this.getMoney());
    }
    
    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column toplist.playerid
     *
     * @return the value of toplist.playerid
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    public Integer getPlayerid() {
        return playerid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column toplist.playerid
     *
     * @param playerid the value for toplist.playerid
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    public void setPlayerid(Integer playerid) {
        this.playerid = playerid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column toplist.playername
     *
     * @return the value of toplist.playername
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    public String getPlayername() {
        return playername;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column toplist.playername
     *
     * @param playername the value for toplist.playername
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    public void setPlayername(String playername) {
        this.playername = playername == null ? null : playername.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column toplist.money
     *
     * @return the value of toplist.money
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column toplist.money
     *
     * @param money the value for toplist.money
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column toplist.top
     *
     * @return the value of toplist.top
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    public Integer getTop() {
        return top;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column toplist.top
     *
     * @param top the value for toplist.top
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    public void setTop(Integer top) {
        this.top = top;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column toplist.zan
     *
     * @return the value of toplist.zan
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    public Integer getZan() {
        return zan;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column toplist.zan
     *
     * @param zan the value for toplist.zan
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    public void setZan(Integer zan) {
        this.zan = zan;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column toplist.createtime
     *
     * @return the value of toplist.createtime
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column toplist.createtime
     *
     * @param createtime the value for toplist.createtime
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column toplist.updatetime
     *
     * @return the value of toplist.updatetime
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column toplist.updatetime
     *
     * @param updatetime the value for toplist.updatetime
     *
     * @mbggenerated Mon Jan 19 09:51:04 CST 2015
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}