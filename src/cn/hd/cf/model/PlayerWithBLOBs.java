package cn.hd.cf.model;

public class PlayerWithBLOBs extends Player {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.base
     *
     * @mbggenerated Mon Dec 14 15:51:34 CST 2015
     */
    private byte[] base;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.event
     *
     * @mbggenerated Mon Dec 14 15:51:34 CST 2015
     */
    private byte[] event;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.feelings
     *
     * @mbggenerated Mon Dec 14 15:51:34 CST 2015
     */
    private byte[] feelings;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.base
     *
     * @return the value of player.base
     *
     * @mbggenerated Mon Dec 14 15:51:34 CST 2015
     */
    public byte[] getBase() {
        return base;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.base
     *
     * @param base the value for player.base
     *
     * @mbggenerated Mon Dec 14 15:51:34 CST 2015
     */
    public void setBase(byte[] base) {
        this.base = base;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.event
     *
     * @return the value of player.event
     *
     * @mbggenerated Mon Dec 14 15:51:34 CST 2015
     */
    public byte[] getEvent() {
        return event;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.event
     *
     * @param event the value for player.event
     *
     * @mbggenerated Mon Dec 14 15:51:34 CST 2015
     */
    public void setEvent(byte[] event) {
        this.event = event;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.feelings
     *
     * @return the value of player.feelings
     *
     * @mbggenerated Mon Dec 14 15:51:34 CST 2015
     */
    public byte[] getFeelings() {
        return feelings;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player.feelings
     *
     * @param feelings the value for player.feelings
     *
     * @mbggenerated Mon Dec 14 15:51:34 CST 2015
     */
    public void setFeelings(byte[] feelings) {
        this.feelings = feelings;
    }
}