package cn.hd.cf.model;

public class Message {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.id
     *
     * @mbggenerated Sat Dec 13 11:21:15 CST 2014
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.desc
     *
     * @mbggenerated Sat Dec 13 11:21:15 CST 2014
     */
    private String desc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.code
     *
     * @mbggenerated Sat Dec 13 11:21:15 CST 2014
     */
    private Integer code;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.id
     *
     * @return the value of message.id
     *
     * @mbggenerated Sat Dec 13 11:21:15 CST 2014
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.id
     *
     * @param id the value for message.id
     *
     * @mbggenerated Sat Dec 13 11:21:15 CST 2014
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.desc
     *
     * @return the value of message.desc
     *
     * @mbggenerated Sat Dec 13 11:21:15 CST 2014
     */
    public String getDesc() {
        return desc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.desc
     *
     * @param desc the value for message.desc
     *
     * @mbggenerated Sat Dec 13 11:21:15 CST 2014
     */
    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.code
     *
     * @return the value of message.code
     *
     * @mbggenerated Sat Dec 13 11:21:15 CST 2014
     */
    public Integer getCode() {
        return code;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.code
     *
     * @param code the value for message.code
     *
     * @mbggenerated Sat Dec 13 11:21:15 CST 2014
     */
    public void setCode(Integer code) {
        this.code = code;
    }
}