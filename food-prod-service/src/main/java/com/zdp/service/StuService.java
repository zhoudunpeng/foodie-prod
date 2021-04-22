package com.zdp.service;

import com.zdp.pojo.Stu;


/**
 * @author sesshomaru
 * @date 2021/4/22 20:57
 */
public interface StuService {

    public Stu getStuInfo(int id);

    public void saveStu();

    public void updateStu(int id);

    public void deleteStu(int id);
}
