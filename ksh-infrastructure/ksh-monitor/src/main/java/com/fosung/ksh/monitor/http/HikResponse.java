package com.fosung.ksh.monitor.http;

import com.fosung.ksh.common.response.ResponseResult;
import lombok.Data;

/**
 * @author wangyihua
 * @date 2019-05-08 16:03
 */
@Data
public class HikResponse extends ResponseResult {
    private String msg;

    public boolean ok(){
        return "0".equals(this.getCode()) || "200".equals(this.getCode());
    }
}
