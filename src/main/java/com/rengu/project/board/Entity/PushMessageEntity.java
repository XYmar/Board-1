package com.rengu.project.board.Entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PushMessageEntity implements Serializable {

    private String direction;
    private String font;
    private double duration;
    private int speed;
    private String content;
}
