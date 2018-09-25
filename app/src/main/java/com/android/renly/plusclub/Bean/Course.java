package com.android.renly.plusclub.Bean;

public class Course {
    /**
     * 课程信息
     */
    private String[] CourseInfo;
    /**
     * 课程表中的行
     */
    private int rows;
    /**
     * 课程表中的列
     */
    private int weekday;
    /**
     * 课程名
     */
    private String courseName;
    /**
     * 课程时间
     */
    private String courseTime;
    /**
     * 教师
     */
    private String teacher;
    /**
     * 教室
     */
    private String classRoom;
    /**
     * 课程节数
     */
    private int classNum;
    /**
     * 起始周数
     */
    private int startWeek;
    /**
     * 结束周数
     */
    private int endWeek;
    /**
     * 数据库primary key 在初始化的时候根据顺序分配 从0开始
     */
    private int cid;
    /**
     * course 存进数据库的时间
     */
    private String create_time;
    /**
     * 单双周
     * 单:1
     * 双:2
     * both:0
     */
    private int sd_week;

    public Course(){
        super();
    }

    public Course(String[] CourseInfo, int rows, int weekday, int cid) {
        this.CourseInfo = CourseInfo;
        this.rows = rows;
        this.weekday = weekday;
        this.cid = cid;
        this.courseName = CourseInfo[0];
        this.courseTime = CourseInfo[1];
        this.teacher = CourseInfo[2];
        this.classRoom = CourseInfo[3];
        if (courseTime.contains(","))
            classNum = 2;
        else
            classNum = 1;
        // 分词操作
        if (courseTime.contains("{") && courseTime.contains("}") && courseTime.contains("|")){
            String week = courseTime.substring(courseTime.indexOf("{"),courseTime.indexOf("}")).substring(1);
            String left = week.split("\\|")[0];

            String startWeek = left.split("-")[0];
            this.startWeek = Integer.parseInt(startWeek.substring(1,startWeek.length()));

            String endWeek = left.split("-")[1];
            this.endWeek = Integer.parseInt(endWeek.substring(0,endWeek.length()-1));

            String right = week.split("\\|")[1];
            if (right.equals("双周"))
                sd_week = 2;
            else
                sd_week = 1;

        }else if(courseTime.contains("{") && courseTime.contains("}")){
            String week = courseTime.substring(courseTime.indexOf("{"),courseTime.indexOf("}")).substring(1);

            String startWeek = week.split("-")[0];
            this.startWeek = Integer.parseInt(startWeek.substring(1,startWeek.length()));

            String endWeek = week.split("-")[1];
            this.endWeek = Integer.parseInt(endWeek.substring(0,endWeek.length()-1));
            sd_week = 0;
        }
    }

    public Course(int rows, int weekday, String courseName, String courseTime, String teacher, String classRoom, int startWeek, int endWeek, String create_time, int cid) {
        this.rows = rows;
        this.weekday = weekday;
        this.courseName = courseName;
        this.courseTime = courseTime;
        this.teacher = teacher;
        this.classRoom = classRoom;
        this.classNum = classNum;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.cid = cid;
        this.create_time = create_time;
    }

    public String[] getCourseInfo() {
        return CourseInfo;
    }

    public void setCourseInfo(String[] courseInfo) {
        CourseInfo = courseInfo;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    @Override
    public String toString() {
        return "Course[ name = " + courseName +
                ",teacher = " + teacher +
                ",startWeek = " + startWeek +
                ",endWeek = " + endWeek +
                " ]";
    }
}
