package com.rengu.project.board.Service;

import com.rengu.project.board.Entity.ResultEntity;

public class ResultService {

    public static ResultEntity<Object> build(Object data) {
        ResultEntity<Object> resultEntity = new ResultEntity<>();
        resultEntity.setData(data);
        return resultEntity;
    }
}
