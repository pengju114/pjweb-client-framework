package com.pj.client.ui;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

/**
 *
 * @author pengju 2011-9-24 15:55
 */
public class Paging {

    private int pageSize;           //每页显示的记录数
    private int totalResults;       //记录总数
    private int currentPage;        //当前页
    private String link;            //点击指定页要连接到的目标页的URL,相对于当前页
    private int linkSize;           //指定要显示页码按钮的个数
    private boolean useSelectList;  //是否使用下拉列表
    private String parameterName;   //就是附加到URL后面的参数名,可通过request.getParameter(parameterName)取得当前页
    private String inputForm = "";    //如果设置了此项则在点击链接的时候会提交制定名字的表单而不是跳到指定页,参数比较多时推荐使用
    private String buttonAttibute=" ";     //给每个按钮（即链接）添加属性,如：id="current" 则 <a id="current" ...>...</a>

    public Paging() {
        pageSize = 10;
        totalResults = 0;
        currentPage = 1;
        link = "javascript:void(0)";
        linkSize = 9;
        useSelectList = false;
        parameterName = "page";
    }

    /**
     *
     * @return 附加到URL后面的参数名
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     * 设置附加到URL后面的参数名
     * @param parameterName
     */
    public Paging setParameterName(String parameterName) {
        this.parameterName = parameterName;
        return this;
    }

    /**
     * 设置是否使用下拉列表
     * @param useSelectList
     */
    public Paging setUseSelectList(boolean useSelectList) {
        this.useSelectList = useSelectList;
        return this;
    }

    /**
     *
     * @return 判断是否使用下拉列表
     */
    public boolean isUseSelectList() {
        return this.useSelectList;
    }

    private String createSelectList() {
        String s = "";
        if (inputForm.isEmpty()) {
            s = "<select name=\"pageList\" id=\"pageList\" onchange=\"location.href='" + getURL() + "'+this.options[this.selectedIndex].value\">";
        } else {
            s = "<select name=\"pageList\" id=\"pageList\" onchange=\"pagingFormDispatcher(this.options[this.selectedIndex].value)\">";
        }
        int size = this.getPageCount();
        String sel = " ";
        for (int i = 1; i <= size; i++) {
            if (i == currentPage) {
                sel = " selected ";
            }
            s += "<option" + sel + "value=\"" + String.valueOf(i) + "\">第" + String.valueOf(i) + "页</option>";
            sel = " ";
        }
        s += "</select>";
        return s;
    }

    private String getLinkString(int page) {
        return getLinkString(page, String.valueOf(page));
    }

    private String getLinkString(int page, String text) {
        if (!inputForm.isEmpty()) {
            return "<a href=\"javascript:void(0);\""+buttonAttibute+"target=\"_self\" onclick=\"pagingFormDispatcher('" + String.valueOf(page) + "')\">" + text + "</a>";
        }
        return "<a target=\"_self\""+buttonAttibute+"href=\"" + getURL() + String.valueOf(page) + "\">" + text + "</a>";
    }

    /**
     *
     * @return 每页显示的记录数
     */
    public int getPageSize() {
        return this.pageSize;
    }

    /**
     * 设置每页显示的记录数
     * @param pageSize
     */
    public Paging setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    /**
     *
     * @return 总记录数
     */
    public int getTotalResults() {
        return this.totalResults;
    }

    /**
     * 设置总记录数
     * @param totalResults
     */
    public Paging setTotalResults(int totalResults) {
        this.totalResults = totalResults;
        return this;
    }

    /**
     * 获取总页数
     * @return 总页数
     */
    public int getPageCount() {
        int c = totalResults / pageSize;
        if (totalResults % pageSize != 0) {
            c += 1;
        }
        return c;
    }

    /**
     * 获取当前页
     * @return 当前页
     */
    public int getCurrentPage() {
        return this.currentPage;
    }

    /**
     * 设置当前页
     * @param currentPage
     */
    public Paging setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        return this;
    }
    
    /**
     * 设置当前页，默认为第1页
     * @param currentPage 
     */
    public Paging setCurrentPage(String currentPage){
        if(currentPage==null||currentPage.isEmpty()||currentPage.matches("\\d*\\D\\d*")){
            this.currentPage=1;
        }else{
            this.currentPage=Integer.valueOf(currentPage.trim());
        }
        return this;
    }

    /**
     * 获取要跳转的相对链接地址
     * @return 要跳转的相对链接地址
     */
    public String getLink() {
        return this.link;
    }

    /**
     * 设置要跳转的相对链接地址
     * @param link
     */
    public Paging setLink(String link) {
        this.link = link;
        return  this;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(wrapWithTag("第 " + String.valueOf(currentPage) + "/" + String.valueOf(getPageCount()) + " 页 共" + totalResults + "条记录", "td"));
        for (String s : getRawHTMLAsArray()) {
            b.append(wrapWithTag(s, "td"));
        }
        return "<table border=\"0\"><tr>" + b.toString() + "</tr></table><script type=\"text/javascript\">" + getFunction() + "</script>";
    }

    /**
     * 取生成的连接按钮,如上一页、1、2...下一页等的连接
     * @return HTML字符串
     */
    public String getRawHTML() {
        StringBuilder b = new StringBuilder();
        for (String s : getRawHTMLAsArray()) {
            b.append(s);
        }
        return b.toString();
    }

    /**
     * 取包含生成的连接按钮的字符串数组,如上一页、1、2...下一页等的连接
     * @return 包含生成的连接按钮的字符串数组
     */
    public String[] getRawHTMLAsArray() {
        int size = getPageCount();

        ArrayList<String> arr = new ArrayList<String>();
        if (currentPage > 1) {
            arr.add(getLinkString(currentPage - 1, "上一页"));
        }

        int page = 1;
        if (currentPage >= linkSize) {
            page = (currentPage - linkSize / 2);
            if (page <= 0) {
                page = 1;
            }
        }
        for (int i = 0; (i < linkSize) && (page <= size); i++, page++) {
            arr.add(getLinkString(page));
        }

        if (currentPage < size) {
            arr.add(getLinkString(currentPage + 1, "下一页"));
        }

        if (useSelectList) {
            arr.add(createSelectList());
        }
        return arr.toArray(new String[]{});
    }

    private String wrapWithTag(String str, String tag) {
        return "<" + tag + ">" + str + "</" + tag + ">";
    }

    public int getLinkSize() {
        return this.linkSize;
    }

    public void setLinkSize(int linkSize) {
        this.linkSize = linkSize;
    }

    /**
     * 以当前页为基准返回应跳过的记录个数,getSkipCount()就是当前页开始记录的序号[在总记录中的下标(从0开始)]
     * @return 应跳过的记录数
     */
    public int getSkipCount() {
        return (currentPage - 1) * pageSize;
    }

    /**
     * 通过此方法可以取最终的URL，比如你设置的link为view.jsp并且参数名为page,此方法就会返回view.jsp?page=
     * @return 附带参数的URL
     */
    public String getURL() {
        String regexp = "&?" + parameterName + "=\\d+";
        if (Pattern.compile(regexp).matcher(link).find()) {//如果已存在参数则替换掉它
            link = link.replaceAll(regexp, "");
        }
        if (link.indexOf("?") < 0) {
            return link + "?" + parameterName + "=";
        } else if (link.contains("?")) {
            if (link.endsWith("&") || link.endsWith("?")) {
                return link + parameterName + "=";
            }
            return link + "&" + parameterName + "=";
        } else {
            return link + "?" + parameterName + "=";
        }

    }

    /**
     * @return the inputForm
     */
    public String getInputForm() {
        return inputForm;
    }

    /**
     * @param inputForm the inputForm to set
     */
    public void setInputForm(String inputForm) {
        this.inputForm = inputForm;
    }

    /**
     * 当使用inputForm时会生成一个函数，用来设置参数和提交表单,接收一个参数，要跳到的页码
     * @return 函数字符串
     */
    public String getFunction() {
        StringBuilder sb = new StringBuilder();
        if (!inputForm.isEmpty()) {
            sb.append("function pagingFormDispatcher(targetPage){");
            sb.append("var form=document.forms[\"").append(inputForm).append("\"];").append("if(form){");
            sb.append("var elems=form.elements;").append("if(elems[\"").append(parameterName).append("\"]){elems[\"").append(parameterName).append("\"].value=targetPage;");
            sb.append("}else{var hid=document.createElement(\"input\");hid.setAttribute(\"type\",\"hidden\");hid.setAttribute(\"name\",\"").append(parameterName).append("\");hid.value=targetPage;form.insertBefore(hid,form.firstChild);}");
            sb.append("form.submit();}}");
        }
        return sb.toString();
    }

    /**
     * @return the buttonAttibute
     */
    public String getButtonAttibute() {
        return buttonAttibute;
    }

    /**
     * @param buttonAttibute the buttonAttibute to set
     */
    public Paging setButtonAttibute(Map buttonAttibute) {
        if(buttonAttibute!=null){
            StringBuilder sb=new StringBuilder(' ');
            for(Entry e:(Set<Entry>)buttonAttibute.entrySet()){
                sb.append(e.getKey()).append('=').append(e.getValue()).append(' ');
            }
            this.buttonAttibute=sb.toString();
        }
        return this;
    }
    
    public Paging setButtonAttibute(String buttonAttibute) {
        this.buttonAttibute=" "+buttonAttibute+" ";
        return this;
    }
}