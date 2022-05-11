package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebFilter(filterName = "XSSFilter", urlPatterns = "/Bookstore")
public class XSSFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("X-XSS-Protection", "1; mode=block");

        filterChain.doFilter(servletRequest, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

}
