package cn.hd.cf.model;

public class Insure {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column insure.id
     *
     * @mbggenerated Sun Dec 07 15:41:57 CST 2014
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column insure.name
     *
     * @mbggenerated Sun Dec 07 15:41:57 CST 2014
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column insure.prize
     *
     * @mbggenerated Sun Dec 07 15:41:57 CST 2014
     */
    private Float prize;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column insure.profit
     *
     * @mbggenerated Sun Dec 07 15:41:57 CST 2014
     */
    private Float profit;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column insure.period
     *
     * @mbggenerated Sun Dec 07 15:41:57 CST 2014
     */
    private Integer period;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column insure.id
     *
     * @return the value of insure.id
     *
     * @mbggenerated Sun Dec 07 15:41:57 CST 2014
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column insure.id
     *
     * @param id the value for insure.id
     *
     * @mbggenerated Sun Dec 07 15:41:57 CST 2014
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column insure.name
     *
     * @return the value of insure.name
     *
     * @mbggenerated Sun Dec 07 15:41:57 CST 2014
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column insure.name
     *
     * @param name the value for insure.name
     *
     * @mbggenerated Sun Dec 07 15:41:57 CST 2014
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column insure.prize
     *
     * @return the value of insure.prize
     *
     * @mbggenerated Sun Dec 07 15:41:57 CST 2014
     */
    public Float getPrize() {
        return prize;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column insure.prize
     *
     * @param prize the value for insure.prize
     *
     * @mbggenerated Sun Dec 07 15:41:57 CST 2014
     */
    public void setPrize(Float prize) {
        this.prize = prize;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column insure.profit
     *
     * @return the value of insure.profit
     *
     * @mbggenerated Sun Dec 07 15:41:57 CST 2014
     */
    public Float getProfit() {
        return profit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column insure.profit
     *
     * @param profit the value for insure.profit
     *
     * @mbggenerated Sun Dec 07 15:41:57 CST 2014
     */
    public void setProfit(Float profit) {
        this.profit = profit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column insure.period
     *
     * @return the value of insure.period
     *
     * @mbggenerated Sun Dec 07 15:41:57 CST 2014
     */
    public Integer getPeriod() {
        return period;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column insure.period
     *
     * @param period the value for insure.period
     *
     * @mbggenerated Sun Dec 07 15:41:57 CST 2014
     */
    public void setPeriod(Integer period) {
        this.period = period;
    }
}