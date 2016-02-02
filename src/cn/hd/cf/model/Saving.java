package cn.hd.cf.model;

import java.util.Date;

public class Saving {
	private float liveamount = 0;
    public float getLiveamount() {
		return liveamount;
	}

	public void setLiveamount(float liveamount) {
		this.liveamount = liveamount;
	}

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column saving.id
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column saving.playerid
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    private Integer playerid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column saving.itemid
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    private Integer itemid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column saving.name
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column saving.type
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    private Byte type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column saving.price
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    private Float price;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column saving.amount
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    private Float amount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column saving.profit
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    private Float profit;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column saving.qty
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    private Integer qty;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column saving.rate
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    private Float rate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column saving.createtime
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    private Date createtime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column saving.updatetime
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    private Date updatetime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column saving.period
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    private Float period;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column saving.status
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    private Byte status;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column saving.id
     *
     * @return the value of saving.id
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column saving.id
     *
     * @param id the value for saving.id
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column saving.playerid
     *
     * @return the value of saving.playerid
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public Integer getPlayerid() {
        return playerid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column saving.playerid
     *
     * @param playerid the value for saving.playerid
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public void setPlayerid(Integer playerid) {
        this.playerid = playerid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column saving.itemid
     *
     * @return the value of saving.itemid
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public Integer getItemid() {
        return itemid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column saving.itemid
     *
     * @param itemid the value for saving.itemid
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column saving.name
     *
     * @return the value of saving.name
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column saving.name
     *
     * @param name the value for saving.name
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column saving.type
     *
     * @return the value of saving.type
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public Byte getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column saving.type
     *
     * @param type the value for saving.type
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column saving.price
     *
     * @return the value of saving.price
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public Float getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column saving.price
     *
     * @param price the value for saving.price
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public void setPrice(Float price) {
        this.price = price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column saving.amount
     *
     * @return the value of saving.amount
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public Float getAmount() {
        return amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column saving.amount
     *
     * @param amount the value for saving.amount
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public void setAmount(Float amount) {
        this.amount = amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column saving.profit
     *
     * @return the value of saving.profit
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public Float getProfit() {
        return profit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column saving.profit
     *
     * @param profit the value for saving.profit
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public void setProfit(Float profit) {
        this.profit = profit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column saving.qty
     *
     * @return the value of saving.qty
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public Integer getQty() {
        return qty;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column saving.qty
     *
     * @param qty the value for saving.qty
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public void setQty(Integer qty) {
        this.qty = qty;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column saving.rate
     *
     * @return the value of saving.rate
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public Float getRate() {
        return rate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column saving.rate
     *
     * @param rate the value for saving.rate
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public void setRate(Float rate) {
        this.rate = rate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column saving.createtime
     *
     * @return the value of saving.createtime
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column saving.createtime
     *
     * @param createtime the value for saving.createtime
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column saving.updatetime
     *
     * @return the value of saving.updatetime
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column saving.updatetime
     *
     * @param updatetime the value for saving.updatetime
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column saving.period
     *
     * @return the value of saving.period
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public Float getPeriod() {
        return period;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column saving.period
     *
     * @param period the value for saving.period
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public void setPeriod(Float period) {
        this.period = period;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column saving.status
     *
     * @return the value of saving.status
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column saving.status
     *
     * @param status the value for saving.status
     *
     * @mbggenerated Mon Feb 02 21:45:47 CST 2015
     */
    public void setStatus(Byte status) {
        this.status = status;
    }
}