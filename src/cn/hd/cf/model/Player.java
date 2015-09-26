package cn.hd.cf.model;

import java.util.Date;

public class Player {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.playerid
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private Integer playerid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.playername
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private String playername;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.pwd
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private String pwd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.accountid
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private Integer accountid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.tel
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private String tel;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.sex
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private Byte sex;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.openstock
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private Byte openstock;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.money
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private Float money;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.exp
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private Integer exp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.job
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private Byte job;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.saving
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private String saving;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.insure
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private String insure;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.finan
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private String finan;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.stock
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private String stock;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.quest
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private String quest;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.zan
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private Integer zan;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.createtime
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private Date createtime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.lastlogin
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private Date lastlogin;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.version
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private Integer version;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.versions
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private String versions;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.stockdata
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private String stockdata;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.weektop
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private Integer weektop;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.monthtop
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private Integer monthtop;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.quotetime
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private Float quotetime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.playerid
     *
     * @return the value of player.playerid
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.sex
     *
     * @return the value of player.sex
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    public void setJob(Byte job) {
        this.job = job;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.saving
     *
     * @return the value of player.saving
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    public void setSaving(String saving) {
        this.saving = saving == null ? null : saving.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.insure
     *
     * @return the value of player.insure
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    public void setInsure(String insure) {
        this.insure = insure == null ? null : insure.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.finan
     *
     * @return the value of player.finan
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    public String getFinan() {
        return finan;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.finan
     *
     * @param finan the value for player.finan
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    public void setFinan(String finan) {
        this.finan = finan == null ? null : finan.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.stock
     *
     * @return the value of player.stock
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    public void setStock(String stock) {
        this.stock = stock == null ? null : stock.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.quest
     *
     * @return the value of player.quest
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    public String getQuest() {
        return quest;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.quest
     *
     * @param quest the value for player.quest
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    public void setQuest(String quest) {
        this.quest = quest == null ? null : quest.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.zan
     *
     * @return the value of player.zan
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    public void setVersions(String versions) {
        this.versions = versions == null ? null : versions.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.stockdata
     *
     * @return the value of player.stockdata
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    public String getStockdata() {
        return stockdata;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.stockdata
     *
     * @param stockdata the value for player.stockdata
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    public void setStockdata(String stockdata) {
        this.stockdata = stockdata == null ? null : stockdata.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.weektop
     *
     * @return the value of player.weektop
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    public void setQuotetime(Float quotetime) {
        this.quotetime = quotetime;
    }
}