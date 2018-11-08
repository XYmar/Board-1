package com.rengu.project.board.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

/**
 * @program: board
 * @author: hanch
 * @create: 2018-11-08 14:44
 **/

@Data
@Entity
public class LayoutDetailEntity {

    @Id
    private String id = UUID.randomUUID().toString();
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime = new Date();
    private int x;
    private int y;
    private int w;
    private int h;
    private String i;
    private String component;
}
