package cn.hd.cf.model;

import cn.hd.base.Bean;

public class Event extends Bean{
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column event.id
     *
     * @mbggenerated Wed Dec 10 17:59:51 CST 2014
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column event.name
     *
     * @mbggenerated Wed Dec 10 17:59:51 CST 2014
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column event.desc
     *
     * @mbggenerated Wed Dec 10 17:59:51 CST 2014
     */
    private String desc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column event.type
     *
     * @mbggenerated Wed Dec 10 17:59:51 CST 2014
     */
    private Byte type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column event.prize
     *
     * @mbggenerated Wed Dec 10 17:59:51 CST 2014
     */
    private byte[] prize;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column event.id
     *
     * @return the value of event.id
     *
     * @mbggenerated Wed Dec 10 17:59:51 CST 2014
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column event.id
     *
     * @param id the value for event.id
     *
     * @mbggenerated Wed Dec 10 17:59:51 CST 2014
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column event.name
     *
     * @return the value of event.name
     *
     * @mbggenerated Wed Dec 10 17:59:51 CST 2014
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column event.name
     *
     * @param name the value for event.name
     *
     * @mbggenerated Wed Dec 10 17:59:51 CST 2014
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column event.desc
     *
     * @return the value of event.desc
     *
     * @mbggenerated Wed Dec 10 17:59:51 CST 2014
     */
    public String getDesc() {
        return desc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column event.desc
     *
     * @param desc the value for event.desc
     *
     * @mbggenerated Wed Dec 10 17:59:51 CST 2014
     */
    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column event.type
     *
     * @return the value of event.type
     *
     * @mbggenerated Wed Dec 10 17:59:51 CST 2014
     */
    public Byte getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column event.type
     *
     * @param type the value for event.type
     *
     * @mbggenerated Wed Dec 10 17:59:51 CST 2014
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column event.prize
     *
     * @return the value of event.prize
     *
     * @mbggenerated Wed Dec 10 17:59:51 CST 2014
     */
    public byte[] getPrize() {
        return prize;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column event.prize
     *
     * @param prize the value for event.prize
     *
     * @mbggenerated Wed Dec 10 17:59:51 CST 2014
     */
    public void setPrize(byte[] prize) {
        this.prize = prize;
    }
}