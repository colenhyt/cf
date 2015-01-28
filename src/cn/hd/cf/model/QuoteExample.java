package cn.hd.cf.model;

import java.util.ArrayList;
import java.util.List;

public class QuoteExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table quote
     *
     * @mbggenerated Wed Jan 28 21:58:01 CST 2015
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table quote
     *
     * @mbggenerated Wed Jan 28 21:58:01 CST 2015
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table quote
     *
     * @mbggenerated Wed Jan 28 21:58:01 CST 2015
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quote
     *
     * @mbggenerated Wed Jan 28 21:58:01 CST 2015
     */
    public QuoteExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quote
     *
     * @mbggenerated Wed Jan 28 21:58:01 CST 2015
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quote
     *
     * @mbggenerated Wed Jan 28 21:58:01 CST 2015
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quote
     *
     * @mbggenerated Wed Jan 28 21:58:01 CST 2015
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quote
     *
     * @mbggenerated Wed Jan 28 21:58:01 CST 2015
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quote
     *
     * @mbggenerated Wed Jan 28 21:58:01 CST 2015
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quote
     *
     * @mbggenerated Wed Jan 28 21:58:01 CST 2015
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quote
     *
     * @mbggenerated Wed Jan 28 21:58:01 CST 2015
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quote
     *
     * @mbggenerated Wed Jan 28 21:58:01 CST 2015
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quote
     *
     * @mbggenerated Wed Jan 28 21:58:01 CST 2015
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quote
     *
     * @mbggenerated Wed Jan 28 21:58:01 CST 2015
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table quote
     *
     * @mbggenerated Wed Jan 28 21:58:01 CST 2015
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andStockidIsNull() {
            addCriterion("stockid is null");
            return (Criteria) this;
        }

        public Criteria andStockidIsNotNull() {
            addCriterion("stockid is not null");
            return (Criteria) this;
        }

        public Criteria andStockidEqualTo(Integer value) {
            addCriterion("stockid =", value, "stockid");
            return (Criteria) this;
        }

        public Criteria andStockidNotEqualTo(Integer value) {
            addCriterion("stockid <>", value, "stockid");
            return (Criteria) this;
        }

        public Criteria andStockidGreaterThan(Integer value) {
            addCriterion("stockid >", value, "stockid");
            return (Criteria) this;
        }

        public Criteria andStockidGreaterThanOrEqualTo(Integer value) {
            addCriterion("stockid >=", value, "stockid");
            return (Criteria) this;
        }

        public Criteria andStockidLessThan(Integer value) {
            addCriterion("stockid <", value, "stockid");
            return (Criteria) this;
        }

        public Criteria andStockidLessThanOrEqualTo(Integer value) {
            addCriterion("stockid <=", value, "stockid");
            return (Criteria) this;
        }

        public Criteria andStockidIn(List<Integer> values) {
            addCriterion("stockid in", values, "stockid");
            return (Criteria) this;
        }

        public Criteria andStockidNotIn(List<Integer> values) {
            addCriterion("stockid not in", values, "stockid");
            return (Criteria) this;
        }

        public Criteria andStockidBetween(Integer value1, Integer value2) {
            addCriterion("stockid between", value1, value2, "stockid");
            return (Criteria) this;
        }

        public Criteria andStockidNotBetween(Integer value1, Integer value2) {
            addCriterion("stockid not between", value1, value2, "stockid");
            return (Criteria) this;
        }

        public Criteria andDateIsNull() {
            addCriterion("date is null");
            return (Criteria) this;
        }

        public Criteria andDateIsNotNull() {
            addCriterion("date is not null");
            return (Criteria) this;
        }

        public Criteria andDateEqualTo(String value) {
            addCriterion("date =", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotEqualTo(String value) {
            addCriterion("date <>", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateGreaterThan(String value) {
            addCriterion("date >", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateGreaterThanOrEqualTo(String value) {
            addCriterion("date >=", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLessThan(String value) {
            addCriterion("date <", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLessThanOrEqualTo(String value) {
            addCriterion("date <=", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLike(String value) {
            addCriterion("date like", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotLike(String value) {
            addCriterion("date not like", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateIn(List<String> values) {
            addCriterion("date in", values, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotIn(List<String> values) {
            addCriterion("date not in", values, "date");
            return (Criteria) this;
        }

        public Criteria andDateBetween(String value1, String value2) {
            addCriterion("date between", value1, value2, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotBetween(String value1, String value2) {
            addCriterion("date not between", value1, value2, "date");
            return (Criteria) this;
        }

        public Criteria andUppriceIsNull() {
            addCriterion("upprice is null");
            return (Criteria) this;
        }

        public Criteria andUppriceIsNotNull() {
            addCriterion("upprice is not null");
            return (Criteria) this;
        }

        public Criteria andUppriceEqualTo(Float value) {
            addCriterion("upprice =", value, "upprice");
            return (Criteria) this;
        }

        public Criteria andUppriceNotEqualTo(Float value) {
            addCriterion("upprice <>", value, "upprice");
            return (Criteria) this;
        }

        public Criteria andUppriceGreaterThan(Float value) {
            addCriterion("upprice >", value, "upprice");
            return (Criteria) this;
        }

        public Criteria andUppriceGreaterThanOrEqualTo(Float value) {
            addCriterion("upprice >=", value, "upprice");
            return (Criteria) this;
        }

        public Criteria andUppriceLessThan(Float value) {
            addCriterion("upprice <", value, "upprice");
            return (Criteria) this;
        }

        public Criteria andUppriceLessThanOrEqualTo(Float value) {
            addCriterion("upprice <=", value, "upprice");
            return (Criteria) this;
        }

        public Criteria andUppriceIn(List<Float> values) {
            addCriterion("upprice in", values, "upprice");
            return (Criteria) this;
        }

        public Criteria andUppriceNotIn(List<Float> values) {
            addCriterion("upprice not in", values, "upprice");
            return (Criteria) this;
        }

        public Criteria andUppriceBetween(Float value1, Float value2) {
            addCriterion("upprice between", value1, value2, "upprice");
            return (Criteria) this;
        }

        public Criteria andUppriceNotBetween(Float value1, Float value2) {
            addCriterion("upprice not between", value1, value2, "upprice");
            return (Criteria) this;
        }

        public Criteria andPriceIsNull() {
            addCriterion("price is null");
            return (Criteria) this;
        }

        public Criteria andPriceIsNotNull() {
            addCriterion("price is not null");
            return (Criteria) this;
        }

        public Criteria andPriceEqualTo(Float value) {
            addCriterion("price =", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotEqualTo(Float value) {
            addCriterion("price <>", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThan(Float value) {
            addCriterion("price >", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThanOrEqualTo(Float value) {
            addCriterion("price >=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThan(Float value) {
            addCriterion("price <", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThanOrEqualTo(Float value) {
            addCriterion("price <=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceIn(List<Float> values) {
            addCriterion("price in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotIn(List<Float> values) {
            addCriterion("price not in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceBetween(Float value1, Float value2) {
            addCriterion("price between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotBetween(Float value1, Float value2) {
            addCriterion("price not between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andLowpriceIsNull() {
            addCriterion("lowprice is null");
            return (Criteria) this;
        }

        public Criteria andLowpriceIsNotNull() {
            addCriterion("lowprice is not null");
            return (Criteria) this;
        }

        public Criteria andLowpriceEqualTo(Float value) {
            addCriterion("lowprice =", value, "lowprice");
            return (Criteria) this;
        }

        public Criteria andLowpriceNotEqualTo(Float value) {
            addCriterion("lowprice <>", value, "lowprice");
            return (Criteria) this;
        }

        public Criteria andLowpriceGreaterThan(Float value) {
            addCriterion("lowprice >", value, "lowprice");
            return (Criteria) this;
        }

        public Criteria andLowpriceGreaterThanOrEqualTo(Float value) {
            addCriterion("lowprice >=", value, "lowprice");
            return (Criteria) this;
        }

        public Criteria andLowpriceLessThan(Float value) {
            addCriterion("lowprice <", value, "lowprice");
            return (Criteria) this;
        }

        public Criteria andLowpriceLessThanOrEqualTo(Float value) {
            addCriterion("lowprice <=", value, "lowprice");
            return (Criteria) this;
        }

        public Criteria andLowpriceIn(List<Float> values) {
            addCriterion("lowprice in", values, "lowprice");
            return (Criteria) this;
        }

        public Criteria andLowpriceNotIn(List<Float> values) {
            addCriterion("lowprice not in", values, "lowprice");
            return (Criteria) this;
        }

        public Criteria andLowpriceBetween(Float value1, Float value2) {
            addCriterion("lowprice between", value1, value2, "lowprice");
            return (Criteria) this;
        }

        public Criteria andLowpriceNotBetween(Float value1, Float value2) {
            addCriterion("lowprice not between", value1, value2, "lowprice");
            return (Criteria) this;
        }

        public Criteria andOpenpriceIsNull() {
            addCriterion("openprice is null");
            return (Criteria) this;
        }

        public Criteria andOpenpriceIsNotNull() {
            addCriterion("openprice is not null");
            return (Criteria) this;
        }

        public Criteria andOpenpriceEqualTo(Float value) {
            addCriterion("openprice =", value, "openprice");
            return (Criteria) this;
        }

        public Criteria andOpenpriceNotEqualTo(Float value) {
            addCriterion("openprice <>", value, "openprice");
            return (Criteria) this;
        }

        public Criteria andOpenpriceGreaterThan(Float value) {
            addCriterion("openprice >", value, "openprice");
            return (Criteria) this;
        }

        public Criteria andOpenpriceGreaterThanOrEqualTo(Float value) {
            addCriterion("openprice >=", value, "openprice");
            return (Criteria) this;
        }

        public Criteria andOpenpriceLessThan(Float value) {
            addCriterion("openprice <", value, "openprice");
            return (Criteria) this;
        }

        public Criteria andOpenpriceLessThanOrEqualTo(Float value) {
            addCriterion("openprice <=", value, "openprice");
            return (Criteria) this;
        }

        public Criteria andOpenpriceIn(List<Float> values) {
            addCriterion("openprice in", values, "openprice");
            return (Criteria) this;
        }

        public Criteria andOpenpriceNotIn(List<Float> values) {
            addCriterion("openprice not in", values, "openprice");
            return (Criteria) this;
        }

        public Criteria andOpenpriceBetween(Float value1, Float value2) {
            addCriterion("openprice between", value1, value2, "openprice");
            return (Criteria) this;
        }

        public Criteria andOpenpriceNotBetween(Float value1, Float value2) {
            addCriterion("openprice not between", value1, value2, "openprice");
            return (Criteria) this;
        }

        public Criteria andQtyIsNull() {
            addCriterion("qty is null");
            return (Criteria) this;
        }

        public Criteria andQtyIsNotNull() {
            addCriterion("qty is not null");
            return (Criteria) this;
        }

        public Criteria andQtyEqualTo(Integer value) {
            addCriterion("qty =", value, "qty");
            return (Criteria) this;
        }

        public Criteria andQtyNotEqualTo(Integer value) {
            addCriterion("qty <>", value, "qty");
            return (Criteria) this;
        }

        public Criteria andQtyGreaterThan(Integer value) {
            addCriterion("qty >", value, "qty");
            return (Criteria) this;
        }

        public Criteria andQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("qty >=", value, "qty");
            return (Criteria) this;
        }

        public Criteria andQtyLessThan(Integer value) {
            addCriterion("qty <", value, "qty");
            return (Criteria) this;
        }

        public Criteria andQtyLessThanOrEqualTo(Integer value) {
            addCriterion("qty <=", value, "qty");
            return (Criteria) this;
        }

        public Criteria andQtyIn(List<Integer> values) {
            addCriterion("qty in", values, "qty");
            return (Criteria) this;
        }

        public Criteria andQtyNotIn(List<Integer> values) {
            addCriterion("qty not in", values, "qty");
            return (Criteria) this;
        }

        public Criteria andQtyBetween(Integer value1, Integer value2) {
            addCriterion("qty between", value1, value2, "qty");
            return (Criteria) this;
        }

        public Criteria andQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("qty not between", value1, value2, "qty");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table quote
     *
     * @mbggenerated do_not_delete_during_merge Wed Jan 28 21:58:01 CST 2015
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table quote
     *
     * @mbggenerated Wed Jan 28 21:58:01 CST 2015
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}