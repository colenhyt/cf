package cn.hd.cf.model;

public class PlayerWithBLOBs extends Player {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.base
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private byte[] base;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.event
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private byte[] event;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player.feelings
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    private byte[] feelings;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player.base
     *
     * @return the value of player.base
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    public void setFeelings(byte[] feelings) {
        this.feelings = feelings;
    }
}