package com.zdp.pojo;

import javax.persistence.Id;

public class Stu {
    /**
     * id 主键
     */
    @Id
    private Integer id;

    /**
     * name
     */
    private String name;

    /**
     * age
     */
    private Integer age;

    /**
     * 获取id 主键
     *
     * @return id - id 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置id 主键
     *
     * @param id id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取name
     *
     * @return name - name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置name
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取age
     *
     * @return age - age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * 设置age
     *
     * @param age age
     */
    public void setAge(Integer age) {
        this.age = age;
    }
}