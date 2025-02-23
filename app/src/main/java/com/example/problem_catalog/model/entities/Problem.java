package com.example.problem_catalog.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity(tableName = "problems")
public class Problem {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String name;
    @ColumnInfo(name = "division_type")
    private Integer divisionType;
    private Boolean del;
}
