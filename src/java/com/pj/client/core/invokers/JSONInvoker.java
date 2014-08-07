/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pj.client.core.invokers;

import com.pj.client.core.ServiceInvoker;
import com.pj.utilities.StringUtility;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author PENGJU
 * email:pengju114@163.com
 * 时间:2012-9-20 0:38:18
 */
public class JSONInvoker extends ServiceInvoker{
    private String resolverClassName;
    
    public JSONInvoker(){
        resolverClassName="com.pj.client.core.resolvers.Resolver";
        HttpServletRequest request=getRequest();
        resolverClassName+=request.getParameter(KEY_HEADER_SERVICE);
    }

    @Override
    public String getResolverClassName() {
        String configName = super.getResolverClassName();
        if (StringUtility.isEmpty(configName)) {
            return resolverClassName;
        }
        return configName;
    }
}
