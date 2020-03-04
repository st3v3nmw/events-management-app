package com.aspinax.lanaevents;

public class Misc {
    public static class ResultStatus {
        private String val;
        private Boolean success;

        public String getVal() {
            return this.val;
        }

        public boolean getSuccess() {
            return this.success;
        }

        public void set(Boolean success, String data) {
            this.val = data;
            this.success = success;
        }
    }
}
