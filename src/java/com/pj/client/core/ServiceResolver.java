/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pj.client.core;

import com.pj.utilities.ConvertUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 服务类，可以实现execute处理业务
 * 并返回数据ServiceResult
 * @author PENGJU
 * email:pengju114@163.com
 * 时间:2012-9-18 22:29:58
 */
public abstract class ServiceResolver {
    public static final String CONF_CLASS_PATTERN="pattern.service.resolver";
    
    private static final int DEFAULT_PAGESIZE = 10;
    
    public HttpServletRequest getRequest(){
        return ServiceInvoker.getRequest();
    }
    public HttpServletResponse getResponse(){
        return ServiceInvoker.getResponse();
    }
    
    /**
     * 获取客户端指定的每页多少个数据
     * @return 每页数据条数，未指定则返回10
     */
    public int getPageSize(){
        int pageSize = ConvertUtility.parseInt(getRequest().getParameter(ServiceInvoker.KEY_HEADER_PAGESIZE), DEFAULT_PAGESIZE);
        if (pageSize<1) {
            pageSize = DEFAULT_PAGESIZE;
        }
        return pageSize;
    }
    /**
     * 获取客户端指定的页
     * @return 指定页，未指定则返回1
     */
    public int getPageNumber(){
        int pageNumber = ConvertUtility.parseInt(getRequest().getParameter(ServiceInvoker.KEY_HEADER_PAGENUMBER), 1);
        if (pageNumber<1) {
            pageNumber = 1;
        }
        return pageNumber;
    }
    
    /**
     * 计算分页属性
     * @param total 总结果数
     * @param result 服务结果
     */
    public void caculatePageProperties(long total,ServiceResult result){
        result.setResultCount(total);
        int pn = getPageNumber();
        result.setPageNumber(pn);
        
        int pageSize = getPageSize();
        // 这里一般是没指定pageSize的情况
        if (result.getData().size()>pageSize) {
            pageSize = result.getData().size();
        }
        
        if (result.getResultCount()>0) {
            int pg = (int)(result.getResultCount()/pageSize);
            if (result.getResultCount()%pageSize>0) {
                pg++;
            }
            result.setPageCount(pg);
        }
    }
    
    /**
     * 在调用{@link #execute()}方法之前调用，此方法抛错将忽略
     */
    public void executePrepare(){}
    /**
     * 业务实现方法，Resolver要实现的功能在这个方法实现
     * @return 服务结果
     * @throws Exception 
     */
    public abstract ServiceResult execute() throws Exception;
    /**
     * 在调用{@link #execute() }方法完成后调用此方法
     * 当{@link #execute() }方法抛错时此方法依然调用
     * 方法抛错将忽略
     * @param result 
     */
    public void executeComplete(ServiceResult result) {}
}
