package com.rengu.project.board.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @program: board
 * @author: hanch
 * @create: 2018-11-08 14:42
 **/

@Data
@Entity
public class LayoutEntity implements Serializable {

    @OneToMany(cascade = CascadeType.ALL)
    List<LayoutDetailEntity> layoutDetailEntities;
    @Id
    private String id = UUID.randomUUID().toString();
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime = new Date();
    @OneToOne
    private BoardEntity boardEntity;
}
