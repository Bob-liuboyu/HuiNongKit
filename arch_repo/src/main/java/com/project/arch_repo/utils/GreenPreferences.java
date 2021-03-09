package com.project.arch_repo.utils;


/**
 * 应用SP存储
 * Created By junbinfan on 2017/10/29.
 */
public class GreenPreferences extends SharedPreferenceWrapper {

    private static volatile GreenPreferences _instance;

    public static GreenPreferences getInstance() {
        if (_instance == null) {
            synchronized (SharedPreferenceWrapper.class) {
                if (_instance == null) {
                    _instance = new GreenPreferences();
                }
            }
        }
        return _instance;
    }

    private GreenPreferences() {
        super("green_preferences_default");
    }


    /**
     * 公司搜索历史
     */
    private static final String COMPANY_SEARCH_HISTORY = "company_search_history";

    /**
     * 保存公司搜索历史
     * @param value
     */
    public void saveCompanySearchHistory(String value) {
        putString(COMPANY_SEARCH_HISTORY, value);
    }

    /**
     * 查询公司搜索历史
     * @return
     */
    public String getCompanySearchHistory() {
        return getString(COMPANY_SEARCH_HISTORY);
    }

    /**
     * 清空公司搜索历史
     * @return
     */
    public void clearCompanySearchHistory() {
        putString(COMPANY_SEARCH_HISTORY, "");
        removeKey(COMPANY_SEARCH_HISTORY);
    }


}
