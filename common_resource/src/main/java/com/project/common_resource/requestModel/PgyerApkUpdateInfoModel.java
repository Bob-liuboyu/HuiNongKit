package com.project.common_resource.requestModel;

import java.io.Serializable;

/**
 * Created by liuboyu on 2019/5/8.
 */
public class PgyerApkUpdateInfoModel implements Serializable {


    /**
     * code : 0
     * message :
     * data : {"buildBuildVersion":"11","forceUpdateVersion":"","forceUpdateVersionNo":"","downloadURL":"https://www.pgyer.com/app/installUpdate/cd64d2533190b74a78b114c343d07a8f?sig=22iZV%2FtFbtHtmzRoXfSn4xhuj0Fvw4kyRFjHZI2rt2X5DJD37Gax%2FREyLRl5o7dd","buildHaveNewVersion":true,"buildVersionNo":"2030","buildVersion":"2.0.3","buildShortcutUrl":"https://www.pgyer.com/tokentm_test","buildUpdateDescription":""}
     */

    private int code;
    private String message;
    private DataEntity data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PgyerApkUpdateInfoModel{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataEntity {
        /**
         * buildBuildVersion : 11
         * forceUpdateVersion :
         * forceUpdateVersionNo :
         * downloadURL : https://www.pgyer.com/app/installUpdate/cd64d2533190b74a78b114c343d07a8f?sig=22iZV%2FtFbtHtmzRoXfSn4xhuj0Fvw4kyRFjHZI2rt2X5DJD37Gax%2FREyLRl5o7dd
         * buildHaveNewVersion : true
         * buildVersionNo : 2030
         * buildVersion : 2.0.3
         * buildShortcutUrl : https://www.pgyer.com/tokentm_test
         * buildUpdateDescription :
         */

        private String buildBuildVersion;
        private String forceUpdateVersion;
        private String forceUpdateVersionNo;
        private String downloadURL;
        private boolean buildHaveNewVersion;
        private String buildVersionNo;
        private String buildVersion;
        private String buildShortcutUrl;
        private String buildUpdateDescription;

        public String getBuildBuildVersion() {
            return buildBuildVersion;
        }

        public void setBuildBuildVersion(String buildBuildVersion) {
            this.buildBuildVersion = buildBuildVersion;
        }

        public String getForceUpdateVersion() {
            return forceUpdateVersion;
        }

        public void setForceUpdateVersion(String forceUpdateVersion) {
            this.forceUpdateVersion = forceUpdateVersion;
        }

        public String getForceUpdateVersionNo() {
            return forceUpdateVersionNo;
        }

        public void setForceUpdateVersionNo(String forceUpdateVersionNo) {
            this.forceUpdateVersionNo = forceUpdateVersionNo;
        }

        public String getDownloadURL() {
            return downloadURL;
        }

        public void setDownloadURL(String downloadURL) {
            this.downloadURL = downloadURL;
        }

        public boolean isBuildHaveNewVersion() {
            return buildHaveNewVersion;
        }

        public void setBuildHaveNewVersion(boolean buildHaveNewVersion) {
            this.buildHaveNewVersion = buildHaveNewVersion;
        }

        public String getBuildVersionNo() {
            return buildVersionNo;
        }

        public void setBuildVersionNo(String buildVersionNo) {
            this.buildVersionNo = buildVersionNo;
        }

        public String getBuildVersion() {
            return buildVersion;
        }

        public void setBuildVersion(String buildVersion) {
            this.buildVersion = buildVersion;
        }

        public String getBuildShortcutUrl() {
            return buildShortcutUrl;
        }

        public void setBuildShortcutUrl(String buildShortcutUrl) {
            this.buildShortcutUrl = buildShortcutUrl;
        }

        public String getBuildUpdateDescription() {
            return buildUpdateDescription;
        }

        public void setBuildUpdateDescription(String buildUpdateDescription) {
            this.buildUpdateDescription = buildUpdateDescription;
        }

        @Override
        public String toString() {
            return "DataEntity{" +
                    "buildBuildVersion='" + buildBuildVersion + '\'' +
                    ", forceUpdateVersion='" + forceUpdateVersion + '\'' +
                    ", forceUpdateVersionNo='" + forceUpdateVersionNo + '\'' +
                    ", downloadURL='" + downloadURL + '\'' +
                    ", buildHaveNewVersion=" + buildHaveNewVersion +
                    ", buildVersionNo='" + buildVersionNo + '\'' +
                    ", buildVersion='" + buildVersion + '\'' +
                    ", buildShortcutUrl='" + buildShortcutUrl + '\'' +
                    ", buildUpdateDescription='" + buildUpdateDescription + '\'' +
                    '}';
        }
    }
}
