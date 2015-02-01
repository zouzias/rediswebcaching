package org.zouzias.rediscachingfilter.filters;

import org.zouzias.rediscachingfilter.aux.MultiWriteHttpServletResponse;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/** Filter that allows to call response.getWriter() twice or more. 
 * 
 *  @author zouzias
 */
public class MultiWriteResponseFilter implements Filter {
    private final FilterConfig filterConfig = null;
    
    @Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse httpResp = (HttpServletResponse) response;
		MultiWriteHttpServletResponse multiResp = new MultiWriteHttpServletResponse(httpResp);        
		chain.doFilter(request, multiResp);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
    
}
