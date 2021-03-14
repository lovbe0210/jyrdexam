package com.jyrd.exam.base.vo;

/**
 * 前端统一参数接收Vo
 */
public class ParamVo {
    /* 分页信息 */
    private int page = 1;
    private int rows = 10;

    /* 查询参数 */
    private String one;
    private String two;
    private String three;  //题干
    private String four;   // 试卷状态：已配置、尚未配置
    private String five;   // 类型： 单选、多选
    private String six;    // 分类： 安规、运规
    private String seven;  // 后端考试状态：尚未发布、进行中、已结束
    private String eight;  // 前端考试状态：只查已发布的考试，state=1或2，默认-1表示两者都查
    private String nine;
    private String ten;

    public int getCurrentPage() {
        return page;
    }

    public int getPageSize() {
        if (rows > 100) {
            rows = 100;
        }
        return rows;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public String getThree() {
        return three;
    }

    public void setThree(String three) {
        this.three = three;
    }

    public String getFour() {
        return four;
    }

    public void setFour(String four) {
        this.four = four;
    }

    public String getFive() {
        return five;
    }

    public void setFive(String five) {
        this.five = five;
    }

    public String getSix() {
        return six;
    }

    public void setSix(String six) {
        this.six = six;
    }

    public String getSeven() {
        return seven;
    }

    public void setSeven(String seven) {
        this.seven = seven;
    }

    public String getEight() {
        return eight;
    }

    public void setEight(String eight) {
        this.eight = eight;
    }

    public String getNine() {
        return nine;
    }

    public void setNine(String nine) {
        this.nine = nine;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }
}
