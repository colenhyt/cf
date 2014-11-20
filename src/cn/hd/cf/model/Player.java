package cn.hd.cf.model;

import java.util.Date;

public class Player {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.playerid
     *
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
     */
    private Integer playerid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.playername
     *
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
     */
    private String playername;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.pwd
     *
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
     */
    private String pwd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.accountid
     *
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
     */
    private Integer accountid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.level
     *
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
     */
    private Byte level;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.job
     *
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
     */
    private Byte job;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.exp
     *
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
     */
    private Integer exp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.cash
     *
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
     */
    private Integer cash;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.createtime
     *
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
     */
    private Date createtime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.version
     *
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
     */
    private Integer version;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.versions
     *
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
     */
    private String versions;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.playerid
     *
     * @return the value of player.playerid
     *
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
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
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
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
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
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
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
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
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
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
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
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
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
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
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
     */
    public void setAccountid(Integer accountid) {
        this.accountid = accountid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.level
     *
     * @return the value of player.level
     *
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
     */
    public Byte getLevel() {
        return level;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.level
     *
     * @param level the value for player.level
     *
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
     */
    public void setLevel(Byte level) {
        this.level = level;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.job
     *
     * @return the value of player.job
     *
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
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
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
     */
    public void setJob(Byte job) {
        this.job = job;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.exp
     *
     * @return the value of player.exp
     *
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
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
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
     */
    public void setExp(Integer exp) {
        this.exp = exp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.cash
     *
     * @return the value of player.cash
     *
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
     */
    public Integer getCash() {
        return cash;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.cash
     *
     * @param cash the value for player.cash
     *
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
     */
    public void setCash(Integer cash) {
        this.cash = cash;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.createtime
     *
     * @return the value of player.createtime
     *
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
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
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.version
     *
     * @return the value of player.version
     *
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
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
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.versions
     *
     * @return the value of player.versions
     *
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
     */
    public String getVersions() {
        return versions;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.versions
     *
     * @param versions the value for player.versions
     *
     * @mbggenerated Thu Nov 20 17:13:16 CST 2014
     */
    public void setVersions(String versions) {
        this.versions = versions == null ? null : versions.trim();
    }
}