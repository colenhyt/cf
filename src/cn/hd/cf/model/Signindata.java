package cn.hd.cf.model;

import java.util.Date;

public class Signindata {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column signindata.signinid
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    private Integer signinid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column signindata.type
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    private Byte type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column signindata.createtime
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    private Date createtime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column signindata.status
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    private Byte status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column signindata.version
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    private Float version;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column signindata.data
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    private byte[] data;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column signindata.signinid
     *
     * @return the value of signindata.signinid
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    public Integer getSigninid() {
        return signinid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column signindata.signinid
     *
     * @param signinid the value for signindata.signinid
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    public void setSigninid(Integer signinid) {
        this.signinid = signinid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column signindata.type
     *
     * @return the value of signindata.type
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    public Byte getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column signindata.type
     *
     * @param type the value for signindata.type
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column signindata.createtime
     *
     * @return the value of signindata.createtime
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column signindata.createtime
     *
     * @param createtime the value for signindata.createtime
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column signindata.status
     *
     * @return the value of signindata.status
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column signindata.status
     *
     * @param status the value for signindata.status
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column signindata.version
     *
     * @return the value of signindata.version
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    public Float getVersion() {
        return version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column signindata.version
     *
     * @param version the value for signindata.version
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    public void setVersion(Float version) {
        this.version = version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column signindata.data
     *
     * @return the value of signindata.data
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    public byte[] getData() {
        return data;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column signindata.data
     *
     * @param data the value for signindata.data
     *
     * @mbggenerated Mon Dec 08 17:48:02 CST 2014
     */
    public void setData(byte[] data) {
        this.data = data;
    }
}