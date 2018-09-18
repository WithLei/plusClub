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
    private int cols;
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

    public Course(String[] CourseInfo, int rows, int cols) {
        this.CourseInfo = CourseInfo;
        this.rows = rows;
        this.cols = cols;
        this.courseName = CourseInfo[0];
        this.courseTime = CourseInfo[1];
        this.teacher = CourseInfo[2];
        this.classRoom = CourseInfo[3];
        if (courseTime.contains(","))
            classNum = 2;
        else
            classNum = 1;
        // 需要分词操作
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

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
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

    @Override
    public String toString() {
        return super.toString();
    }
}
