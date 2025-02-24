package com.example.problem_catalog.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "problems")
public class Problem {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String name;
    @ColumnInfo(name = "division_type")
    private Integer divisionType;
    private Boolean del;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getDivisionType() {
        return divisionType;
    }
    public void setDivisionType(Integer divisionType) {
        this.divisionType = divisionType;
    }
    public Boolean getDel() {
        return del;
    }
    public void setDel(Boolean del) {
        this.del = del;
    }
}
