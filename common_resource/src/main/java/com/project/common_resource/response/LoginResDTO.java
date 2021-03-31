package com.project.common_resource.response;

import java.io.Serializable;
import java.util.List;

public class LoginResDTO implements Serializable {
    private SettingsBean settings;
    private UserinfoBean userinfo;
    private String token;

    public SettingsBean getSettings() {
        return settings;
    }

    public void setSettings(SettingsBean settings) {
        this.settings = settings;
    }

    public UserinfoBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserinfoBean userinfo) {
        this.userinfo = userinfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class SettingsBean {
        private String phone_support;
        private boolean needResetPwd;
        private String version;
        /**
         * claimName : 肥母猪
         * claimId : 123
         * measure_ways : [{"details":[{"name":"猪身","column":"siteType","iconUrl":"icon图片url"},{"name":"正脸","column":"siteType","iconUrl":"icon图片url"},{"name":"耳朵","column":"siteType","iconUrl":"icon图片url"},{"name":"环境","column":"siteType","iconUrl":"icon图片url"}],"measureName":"测长","measureType":"0"}]
         */

        private List<CategoryBean> category;

        public String getPhone_support() {
            return phone_support;
        }

        public void setPhone_support(String phone_support) {
            this.phone_support = phone_support;
        }

        public boolean isNeedResetPwd() {
            return needResetPwd;
        }

        public void setNeedResetPwd(boolean needResetPwd) {
            this.needResetPwd = needResetPwd;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public List<CategoryBean> getCategory() {
            return category;
        }

        public void setCategory(List<CategoryBean> category) {
            this.category = category;
        }

        public static class CategoryBean {
            private String claimName;
            private String claimId;
            /**
             * details : [{"name":"猪身","column":"siteType","iconUrl":"icon图片url"},{"name":"正脸","column":"siteType","iconUrl":"icon图片url"},{"name":"耳朵","column":"siteType","iconUrl":"icon图片url"},{"name":"环境","column":"siteType","iconUrl":"icon图片url"}]
             * measureName : 测长
             * measureType : 0
             */

            private List<MeasureWaysBean> measure_ways;

            public String getClaimName() {
                return claimName;
            }

            public void setClaimName(String claimName) {
                this.claimName = claimName;
            }

            public String getClaimId() {
                return claimId;
            }

            public void setClaimId(String claimId) {
                this.claimId = claimId;
            }

            public List<MeasureWaysBean> getMeasure_ways() {
                return measure_ways;
            }

            public void setMeasure_ways(List<MeasureWaysBean> measure_ways) {
                this.measure_ways = measure_ways;
            }

            public static class MeasureWaysBean {
                private String measureName;
                private String measureType;
                /**
                 * name : 猪身
                 * column : siteType
                 * iconUrl : icon图片url
                 */

                private List<DetailsBean> details;

                public String getMeasureName() {
                    return measureName;
                }

                public void setMeasureName(String measureName) {
                    this.measureName = measureName;
                }

                public String getMeasureType() {
                    return measureType;
                }

                public void setMeasureType(String measureType) {
                    this.measureType = measureType;
                }

                public List<DetailsBean> getDetails() {
                    return details;
                }

                public void setDetails(List<DetailsBean> details) {
                    this.details = details;
                }

                public static class DetailsBean {
                    private String name;
                    private String column;
                    private String iconUrl;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getColumn() {
                        return column;
                    }

                    public void setColumn(String column) {
                        this.column = column;
                    }

                    public String getIconUrl() {
                        return iconUrl;
                    }

                    public void setIconUrl(String iconUrl) {
                        this.iconUrl = iconUrl;
                    }
                }
            }
        }
    }

    public static class UserinfoBean {
        private String orgName;
        private String companyName;
        private String userName;
        private String userId;
        private String userRoleName;

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserRoleName() {
            return userRoleName;
        }

        public void setUserRoleName(String userRoleName) {
            this.userRoleName = userRoleName;
        }
    }
}
