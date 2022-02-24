package com.baidu.aip.asrwakeup3.core.inputstream;

import java.io.Serializable;

/**
 * @author : wengliuhu
 * @version : 0.1
 * @since : 2022/2/24 11:18
 * Describe：
 */
public class WakeUpResult implements Serializable {

    private String errorDesc;
    private Integer errorCode;
    private String word;

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
