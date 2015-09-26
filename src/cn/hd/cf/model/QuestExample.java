package cn.hd.cf.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuestExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table quest
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table quest
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table quest
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quest
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    public QuestExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quest
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quest
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quest
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quest
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quest
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quest
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quest
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quest
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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
     * This method corresponds to the database table quest
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quest
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table quest
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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

        public Criteria andPlayeridIsNull() {
            addCriterion("playerid is null");
            return (Criteria) this;
        }

        public Criteria andPlayeridIsNotNull() {
            addCriterion("playerid is not null");
            return (Criteria) this;
        }

        public Criteria andPlayeridEqualTo(Integer value) {
            addCriterion("playerid =", value, "playerid");
            return (Criteria) this;
        }

        public Criteria andPlayeridNotEqualTo(Integer value) {
            addCriterion("playerid <>", value, "playerid");
            return (Criteria) this;
        }

        public Criteria andPlayeridGreaterThan(Integer value) {
            addCriterion("playerid >", value, "playerid");
            return (Criteria) this;
        }

        public Criteria andPlayeridGreaterThanOrEqualTo(Integer value) {
            addCriterion("playerid >=", value, "playerid");
            return (Criteria) this;
        }

        public Criteria andPlayeridLessThan(Integer value) {
            addCriterion("playerid <", value, "playerid");
            return (Criteria) this;
        }

        public Criteria andPlayeridLessThanOrEqualTo(Integer value) {
            addCriterion("playerid <=", value, "playerid");
            return (Criteria) this;
        }

        public Criteria andPlayeridIn(List<Integer> values) {
            addCriterion("playerid in", values, "playerid");
            return (Criteria) this;
        }

        public Criteria andPlayeridNotIn(List<Integer> values) {
            addCriterion("playerid not in", values, "playerid");
            return (Criteria) this;
        }

        public Criteria andPlayeridBetween(Integer value1, Integer value2) {
            addCriterion("playerid between", value1, value2, "playerid");
            return (Criteria) this;
        }

        public Criteria andPlayeridNotBetween(Integer value1, Integer value2) {
            addCriterion("playerid not between", value1, value2, "playerid");
            return (Criteria) this;
        }

        public Criteria andTelIsNull() {
            addCriterion("tel is null");
            return (Criteria) this;
        }

        public Criteria andTelIsNotNull() {
            addCriterion("tel is not null");
            return (Criteria) this;
        }

        public Criteria andTelEqualTo(String value) {
            addCriterion("tel =", value, "tel");
            return (Criteria) this;
        }

        public Criteria andTelNotEqualTo(String value) {
            addCriterion("tel <>", value, "tel");
            return (Criteria) this;
        }

        public Criteria andTelGreaterThan(String value) {
            addCriterion("tel >", value, "tel");
            return (Criteria) this;
        }

        public Criteria andTelGreaterThanOrEqualTo(String value) {
            addCriterion("tel >=", value, "tel");
            return (Criteria) this;
        }

        public Criteria andTelLessThan(String value) {
            addCriterion("tel <", value, "tel");
            return (Criteria) this;
        }

        public Criteria andTelLessThanOrEqualTo(String value) {
            addCriterion("tel <=", value, "tel");
            return (Criteria) this;
        }

        public Criteria andTelLike(String value) {
            addCriterion("tel like", value, "tel");
            return (Criteria) this;
        }

        public Criteria andTelNotLike(String value) {
            addCriterion("tel not like", value, "tel");
            return (Criteria) this;
        }

        public Criteria andTelIn(List<String> values) {
            addCriterion("tel in", values, "tel");
            return (Criteria) this;
        }

        public Criteria andTelNotIn(List<String> values) {
            addCriterion("tel not in", values, "tel");
            return (Criteria) this;
        }

        public Criteria andTelBetween(String value1, String value2) {
            addCriterion("tel between", value1, value2, "tel");
            return (Criteria) this;
        }

        public Criteria andTelNotBetween(String value1, String value2) {
            addCriterion("tel not between", value1, value2, "tel");
            return (Criteria) this;
        }

        public Criteria andCrdateIsNull() {
            addCriterion("crdate is null");
            return (Criteria) this;
        }

        public Criteria andCrdateIsNotNull() {
            addCriterion("crdate is not null");
            return (Criteria) this;
        }

        public Criteria andCrdateEqualTo(Date value) {
            addCriterion("crdate =", value, "crdate");
            return (Criteria) this;
        }

        public Criteria andCrdateNotEqualTo(Date value) {
            addCriterion("crdate <>", value, "crdate");
            return (Criteria) this;
        }

        public Criteria andCrdateGreaterThan(Date value) {
            addCriterion("crdate >", value, "crdate");
            return (Criteria) this;
        }

        public Criteria andCrdateGreaterThanOrEqualTo(Date value) {
            addCriterion("crdate >=", value, "crdate");
            return (Criteria) this;
        }

        public Criteria andCrdateLessThan(Date value) {
            addCriterion("crdate <", value, "crdate");
            return (Criteria) this;
        }

        public Criteria andCrdateLessThanOrEqualTo(Date value) {
            addCriterion("crdate <=", value, "crdate");
            return (Criteria) this;
        }

        public Criteria andCrdateIn(List<Date> values) {
            addCriterion("crdate in", values, "crdate");
            return (Criteria) this;
        }

        public Criteria andCrdateNotIn(List<Date> values) {
            addCriterion("crdate not in", values, "crdate");
            return (Criteria) this;
        }

        public Criteria andCrdateBetween(Date value1, Date value2) {
            addCriterion("crdate between", value1, value2, "crdate");
            return (Criteria) this;
        }

        public Criteria andCrdateNotBetween(Date value1, Date value2) {
            addCriterion("crdate not between", value1, value2, "crdate");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table quest
     *
     * @mbggenerated do_not_delete_during_merge Sat Sep 26 09:48:14 CST 2015
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table quest
     *
     * @mbggenerated Sat Sep 26 09:48:14 CST 2015
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