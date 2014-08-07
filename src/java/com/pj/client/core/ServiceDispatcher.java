/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pj.client.core;

import com.pj.utilities.StringUtility;
import com.pj.web.res.Config;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 * 服务分发器,每一个客户端请求经过这个转发器转发
 * @author PENGJU
 * email:pengju114@163.com
 * 时间:2012-9-18 22:42:41
 */
public class ServiceDispatcher {
    private static final Logger LOGGER=Logger.getLogger(ServiceDispatcher.class);
    
    public static void dispatch(HttpServletRequest request,HttpServletResponse response){
        ServiceInvoker.setRequest(request);
        ServiceInvoker.setResponse(response);
        
        //在这里根据参数module判断要调用哪个 Invoker
        String type=StringUtility.ensureAsString(request.getParameter(ServiceInvoker.KEY_HEADER_DATA_TYPE));
        if (StringUtility.isEmpty(type)) {
            type="JSON";
        }
        
        String clazz = null;
        
        String invokerPattern = Config.getConfig(ServiceInvoker.CONF_CLASS_PATTERN, null);
        if (StringUtility.isEmpty(invokerPattern)) {
            String pkg=ServiceDispatcher.class.getPackage().getName();
            clazz=pkg+".invokers."+type+"Invoker";
        }else{
            // 配置文件设置了模式
            clazz = String.format(invokerPattern, type);
        }
        
        
        try {
            Class<ServiceInvoker> invokerClass=(Class<ServiceInvoker>) Class.forName(clazz);
            ServiceInvoker invoker=invokerClass.newInstance();
            invoker.invoke();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        
        ServiceInvoker.clear();
    }
}
