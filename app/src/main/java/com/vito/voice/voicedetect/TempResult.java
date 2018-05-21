package com.vito.voice.voicedetect;

import java.util.List;

public class TempResult {


    /**
     * results_recognition : [" "]
     * origin_result : {"corpus_no":6537928513702468535,"err_no":0,"result":{"uncertain_word":["常"],"word":[" "]},"sn":"6dbf2ba5-31e1-4478-83e4-86e36e1f4688"}
     * error : 0
     * best_result :
     * result_type : partial_result
     */

    private OriginResultBean origin_result;
    private int error;
    private String best_result;
    private String result_type;
    private List<String> results_recognition;
    private String desc;
    private int sub_error;

    public OriginResultBean getOrigin_result() {
        return origin_result;
    }

    public void setOrigin_result(OriginResultBean origin_result) {
        this.origin_result = origin_result;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getBest_result() {
        return best_result;
    }

    public void setBest_result(String best_result) {
        this.best_result = best_result;
    }

    public String getResult_type() {
        return result_type;
    }

    public void setResult_type(String result_type) {
        this.result_type = result_type;
    }

    public List<String> getResults_recognition() {
        return results_recognition;
    }

    public void setResults_recognition(List<String> results_recognition) {
        this.results_recognition = results_recognition;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSub_error() {
        return sub_error;
    }

    public void setSub_error(int sub_error) {
        this.sub_error = sub_error;
    }

    public static class OriginResultBean {
        /**
         * corpus_no : 6537928513702468535
         * err_no : 0
         * result : {"uncertain_word":["常"],"word":[" "]}
         * sn : 6dbf2ba5-31e1-4478-83e4-86e36e1f4688
         */

        private long corpus_no;
        private int err_no;
        private ResultBean result;
        private String sn;

        public long getCorpus_no() {
            return corpus_no;
        }

        public void setCorpus_no(long corpus_no) {
            this.corpus_no = corpus_no;
        }

        public int getErr_no() {
            return err_no;
        }

        public void setErr_no(int err_no) {
            this.err_no = err_no;
        }

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public static class ResultBean {
            private List<String> uncertain_word;
            private List<String> word;

            public List<String> getUncertain_word() {
                return uncertain_word;
            }

            public void setUncertain_word(List<String> uncertain_word) {
                this.uncertain_word = uncertain_word;
            }

            public List<String> getWord() {
                return word;
            }

            public void setWord(List<String> word) {
                this.word = word;
            }
        }
    }
}
