package cn.hd.cf.model;

public class Stock {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock.id
     *
     * @mbggenerated Wed Dec 10 11:04:45 CST 2014
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock.name
     *
     * @mbggenerated Wed Dec 10 11:04:45 CST 2014
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock.desc
     *
     * @mbggenerated Wed Dec 10 11:04:45 CST 2014
     */
    private String desc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock.unit
     *
     * @mbggenerated Wed Dec 10 11:04:45 CST 2014
     */
    private Integer unit;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock.price
     *
     * @mbggenerated Wed Dec 10 11:04:45 CST 2014
     */
    private Float price;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column stock.per
     *
     * @mbggenerated Wed Dec 10 11:04:45 CST 2014
     */
    private Integer per;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock.id
     *
     * @return the value of stock.id
     *
     * @mbggenerated Wed Dec 10 11:04:45 CST 2014
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
     * @mbggenerated Wed Dec 10 11:04:45 CST 2014
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock.name
     *
     * @return the value of stock.name
     *
     * @mbggenerated Wed Dec 10 11:04:45 CST 2014
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
     * @mbggenerated Wed Dec 10 11:04:45 CST 2014
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock.desc
     *
     * @return the value of stock.desc
     *
     * @mbggenerated Wed Dec 10 11:04:45 CST 2014
     */
    public String getDesc() {
        return desc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stock.desc
     *
     * @param desc the value for stock.desc
     *
     * @mbggenerated Wed Dec 10 11:04:45 CST 2014
     */
    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock.unit
     *
     * @return the value of stock.unit
     *
     * @mbggenerated Wed Dec 10 11:04:45 CST 2014
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
     * @mbggenerated Wed Dec 10 11:04:45 CST 2014
     */
    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock.price
     *
     * @return the value of stock.price
     *
     * @mbggenerated Wed Dec 10 11:04:45 CST 2014
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
     * @mbggenerated Wed Dec 10 11:04:45 CST 2014
     */
    public void setPrice(Float price) {
        this.price = price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stock.per
     *
     * @return the value of stock.per
     *
     * @mbggenerated Wed Dec 10 11:04:45 CST 2014
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
     * @mbggenerated Wed Dec 10 11:04:45 CST 2014
     */
    public void setPer(Integer per) {
        this.per = per;
    }
}