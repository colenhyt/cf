package cn.hd.cf.model;

import java.util.Date;

public class Stock {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock.id
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock.playerid
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    private Integer playerid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock.itemid
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    private Integer itemid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock.name
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock.descs
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    private String descs;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock.price
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    private Float price;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock.profit
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    private Float profit;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock.amount
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    private Float amount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock.unit
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    private Integer unit;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock.qty
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    private Integer qty;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock.createtime
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    private Date createtime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock.updatetime
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    private Date updatetime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock.status
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    private Byte status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock.per
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    private Integer per;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock.id
     *
     * @return the value of stock.id
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock.id
     *
     * @param id the value for stock.id
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock.playerid
     *
     * @return the value of stock.playerid
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public Integer getPlayerid() {
        return playerid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock.playerid
     *
     * @param playerid the value for stock.playerid
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public void setPlayerid(Integer playerid) {
        this.playerid = playerid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock.itemid
     *
     * @return the value of stock.itemid
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public Integer getItemid() {
        return itemid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock.itemid
     *
     * @param itemid the value for stock.itemid
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock.name
     *
     * @return the value of stock.name
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock.name
     *
     * @param name the value for stock.name
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock.descs
     *
     * @return the value of stock.descs
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public String getDescs() {
        return descs;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock.descs
     *
     * @param descs the value for stock.descs
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public void setDescs(String descs) {
        this.descs = descs == null ? null : descs.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock.price
     *
     * @return the value of stock.price
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public Float getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock.price
     *
     * @param price the value for stock.price
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public void setPrice(Float price) {
        this.price = price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock.profit
     *
     * @return the value of stock.profit
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public Float getProfit() {
        return profit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock.profit
     *
     * @param profit the value for stock.profit
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public void setProfit(Float profit) {
        this.profit = profit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock.amount
     *
     * @return the value of stock.amount
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public Float getAmount() {
        return amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock.amount
     *
     * @param amount the value for stock.amount
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public void setAmount(Float amount) {
        this.amount = amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock.unit
     *
     * @return the value of stock.unit
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public Integer getUnit() {
        return unit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock.unit
     *
     * @param unit the value for stock.unit
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock.qty
     *
     * @return the value of stock.qty
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public Integer getQty() {
        return qty;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock.qty
     *
     * @param qty the value for stock.qty
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public void setQty(Integer qty) {
        this.qty = qty;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock.createtime
     *
     * @return the value of stock.createtime
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock.createtime
     *
     * @param createtime the value for stock.createtime
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock.updatetime
     *
     * @return the value of stock.updatetime
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock.updatetime
     *
     * @param updatetime the value for stock.updatetime
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock.status
     *
     * @return the value of stock.status
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock.status
     *
     * @param status the value for stock.status
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock.per
     *
     * @return the value of stock.per
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public Integer getPer() {
        return per;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock.per
     *
     * @param per the value for stock.per
     *
     * @mbggenerated Mon Dec 29 08:15:07 CST 2014
     */
    public void setPer(Integer per) {
        this.per = per;
    }
}