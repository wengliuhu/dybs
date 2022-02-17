package com.artheia.usbcamera.ocr;

import java.io.Serializable;
import java.util.List;

/**
 * @author : wengliuhu
 * @version : 0.1
 * @since : 2022/2/15 15:53
 * Describe：
 * {
 *     "words_result":[
 *         {
 *             "words":"沈苏公司"
 *         },
 *         {
 *             "words":"(中国科学院所属企业)"
 *         },
 *         {
 *             "words":"员工手册"
 *         },
 *         {
 *             "words":"Employee Guide Book"
 *         },
 *         {
 *             "words":"(2020版)"
 *         }
 *     ],
 *     "words_result_num":5,
 *     "direction":0,
 *     "log_id":1493493297096570410
 * }
 */
public class OcrResult implements Serializable {

    private List<WordsResult> words_result;
    private Integer words_result_num;
    private Integer direction;
    private Long log_id;

    public List<WordsResult> getWords_result() {
        return words_result;
    }

    public void setWords_result(List<WordsResult> words_result) {
        this.words_result = words_result;
    }

    public Integer getWords_result_num() {
        return words_result_num;
    }

    public void setWords_result_num(Integer words_result_num) {
        this.words_result_num = words_result_num;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public Long getLog_id() {
        return log_id;
    }

    public void setLog_id(Long log_id) {
        this.log_id = log_id;
    }

    public static class WordsResult {
        private String words;

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }
    }
}
