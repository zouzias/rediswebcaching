/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ibm.zurich.rediscachingfilter.filters;

import com.ibm.zurich.rediscachingfilter.core.MultiReadHttpServletRequest;
import com.ibm.zurich.rediscachingfilter.core.MultiWriteHttpServletResponse;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author azo
 */
public class MultiReadWriteFilter implements Filter {
    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private final FilterConfig filterConfig = null;
    
    @Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpResp = (HttpServletResponse) response;

		MultiReadHttpServletRequest multiReq = new MultiReadHttpServletRequest(httpReq);
		MultiWriteHttpServletResponse multiResp = new MultiWriteHttpServletResponse(httpResp);

        
		chain.doFilter(multiReq, multiResp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	@Override
	public void destroy() {
	}
    
}
