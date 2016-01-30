package cn.hd.cf.model;

import java.util.Date;

public class Player {
	private String createTimeStr;
	private String updateTimeStr;
	private Date questdonetime;
	private Date questassigntime;
	public Date getQuestassigntime() {
		return questassigntime;
	}

	public void setQuestassigntime(Date questassigntime) {
		this.questassigntime = questassigntime;
	}

	private int questdonecount = 0;
	public int getQuestdonecount() {
		return questdonecount;
	}

	public void setQuestdonecount(int questdonecount) {
		this.questdonecount = questdonecount;
	}

	private String questStr = null;
	
    public String getQuestStr() {
		return questStr;
	}

	public void setQuestStr(String questStr) {
		this.questStr = questStr;
	}

	public Date getQuestDoneTime() {
		return questdonetime;
	}

	public void setQuestDoneTime(Date questDoneTime) {
		this.questdonetime = questDoneTime;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getUpdateTimeStr() {
		return updateTimeStr;
	}

	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.playerid
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    private Integer playerid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.playername
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    private String playername;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.pwd
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    private String pwd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.accountid
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    private Integer accountid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.tel
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    private String tel;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.openid
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    private String openid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.sex
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    private Byte sex;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.openstock
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    private Byte openstock;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.money
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    private Float money;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.exp
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    private Integer exp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.job
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    private Byte job;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.zan
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    private Integer zan;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.createtime
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    private Date createtime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.lastlogin
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    private Date lastlogin;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.version
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    private Integer version;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.weektop
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    private Integer weektop;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.monthtop
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    private Integer monthtop;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.quotetime
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    private Float quotetime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.insure
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    private String insure;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.saving
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    private String saving;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.stock
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    private String stock;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.playerid
     *
     * @return the value of player.playerid
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public Integer getPlayerid() {
        return playerid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.playerid
     *
     * @param playerid the value for player.playerid
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public void setPlayerid(Integer playerid) {
        this.playerid = playerid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.playername
     *
     * @return the value of player.playername
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public String getPlayername() {
        return playername;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.playername
     *
     * @param playername the value for player.playername
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public void setPlayername(String playername) {
        this.playername = playername == null ? null : playername.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.pwd
     *
     * @return the value of player.pwd
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.pwd
     *
     * @param pwd the value for player.pwd
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.accountid
     *
     * @return the value of player.accountid
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public Integer getAccountid() {
        return accountid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.accountid
     *
     * @param accountid the value for player.accountid
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public void setAccountid(Integer accountid) {
        this.accountid = accountid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.tel
     *
     * @return the value of player.tel
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public String getTel() {
        return tel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.tel
     *
     * @param tel the value for player.tel
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.openid
     *
     * @return the value of player.openid
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.openid
     *
     * @param openid the value for player.openid
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.sex
     *
     * @return the value of player.sex
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public Byte getSex() {
        return sex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.sex
     *
     * @param sex the value for player.sex
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public void setSex(Byte sex) {
        this.sex = sex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.openstock
     *
     * @return the value of player.openstock
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public Byte getOpenstock() {
        return openstock;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.openstock
     *
     * @param openstock the value for player.openstock
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public void setOpenstock(Byte openstock) {
        this.openstock = openstock;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.money
     *
     * @return the value of player.money
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public Float getMoney() {
        return money;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.money
     *
     * @param money the value for player.money
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public void setMoney(Float money) {
        this.money = money;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.exp
     *
     * @return the value of player.exp
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public Integer getExp() {
        return exp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.exp
     *
     * @param exp the value for player.exp
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public void setExp(Integer exp) {
        this.exp = exp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.job
     *
     * @return the value of player.job
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public Byte getJob() {
        return job;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.job
     *
     * @param job the value for player.job
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public void setJob(Byte job) {
        this.job = job;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.zan
     *
     * @return the value of player.zan
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public Integer getZan() {
        return zan;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.zan
     *
     * @param zan the value for player.zan
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public void setZan(Integer zan) {
        this.zan = zan;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.createtime
     *
     * @return the value of player.createtime
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.createtime
     *
     * @param createtime the value for player.createtime
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.lastlogin
     *
     * @return the value of player.lastlogin
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public Date getLastlogin() {
        return lastlogin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.lastlogin
     *
     * @param lastlogin the value for player.lastlogin
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public void setLastlogin(Date lastlogin) {
        this.lastlogin = lastlogin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.version
     *
     * @return the value of player.version
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.version
     *
     * @param version the value for player.version
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.weektop
     *
     * @return the value of player.weektop
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public Integer getWeektop() {
        return weektop;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.weektop
     *
     * @param weektop the value for player.weektop
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public void setWeektop(Integer weektop) {
        this.weektop = weektop;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.monthtop
     *
     * @return the value of player.monthtop
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public Integer getMonthtop() {
        return monthtop;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.monthtop
     *
     * @param monthtop the value for player.monthtop
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public void setMonthtop(Integer monthtop) {
        this.monthtop = monthtop;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.quotetime
     *
     * @return the value of player.quotetime
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public Float getQuotetime() {
        return quotetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.quotetime
     *
     * @param quotetime the value for player.quotetime
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public void setQuotetime(Float quotetime) {
        this.quotetime = quotetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.insure
     *
     * @return the value of player.insure
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public String getInsure() {
        return insure;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.insure
     *
     * @param insure the value for player.insure
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public void setInsure(String insure) {
        this.insure = insure == null ? null : insure.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.saving
     *
     * @return the value of player.saving
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public String getSaving() {
        return saving;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.saving
     *
     * @param saving the value for player.saving
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public void setSaving(String saving) {
        this.saving = saving == null ? null : saving.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.stock
     *
     * @return the value of player.stock
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public String getStock() {
        return stock;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.stock
     *
     * @param stock the value for player.stock
     *
     * @mbggenerated Mon Dec 14 16:15:10 CST 2015
     */
    public void setStock(String stock) {
        this.stock = stock == null ? null : stock.trim();
    }
}