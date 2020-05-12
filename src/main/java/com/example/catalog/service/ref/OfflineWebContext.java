package com.example.catalog.service.ref;

import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.expression.ThymeleafEvaluationContext;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.*;

import static org.thymeleaf.spring5.expression.ThymeleafEvaluationContext.THYMELEAF_EVALUATION_CONTEXT_CONTEXT_VARIABLE_NAME;
import static org.thymeleaf.spring5.naming.SpringContextVariableNames.SPRING_REQUEST_CONTEXT;

/**
 * Adopted from the site below, added FakeHttpServletRequest to allow processing templates without a request eg. in quartz
 * Added FakeHttpResponse for Thymeleaf 3 dropped SpringWebContext, encodeURL does nothing...
 * <p>
 * Decorator for SpringWebContext performing all required initializations.
 * https://github.com/thymeleaf/thymeleafexamples-springmail/blob/2.1-master/src/main/java/thymeleafexamples/springmail/service/EmailService.java
 */
public class OfflineWebContext implements IWebContext {

    private final WebContext webContext;

    public OfflineWebContext(
            final ServletContext servletContext,
            final ApplicationContext applicationContext,
            final ConversionService conversionService) {
        HttpServletRequest request = new FakeHttpServletRequest();
        HttpServletResponse response = new FakeHttpServletResponse();
        // Create delegating WebContext
        final Locale locale = RequestContextUtils.getLocale(request);
        webContext = new WebContext(request, response, servletContext, locale);
        // Perform initializations
        initialize(conversionService, applicationContext);
    }

    public void setVariable(String name, Object object) {
        webContext.setVariable(name, object);
    }

    private void initialize(final ConversionService conversionService, ApplicationContext applicationContext) {
        createRequestContext();
        createEvaluationContext(conversionService, applicationContext);
    }

    private void createRequestContext() {
        RequestContext requestContext = new RequestContext(webContext.getRequest(), webContext.getResponse(), webContext.getServletContext(), new HashMap<>());
        webContext.setVariable(SPRING_REQUEST_CONTEXT, requestContext);
    }

    private void createEvaluationContext(final ConversionService conversionService, ApplicationContext applicationContext) {
        ThymeleafEvaluationContext evaluationContext = new ThymeleafEvaluationContext(applicationContext, conversionService);
        evaluationContext.setTypeLocator(new WhitelistTypeLocator());
        webContext.setVariable(THYMELEAF_EVALUATION_CONTEXT_CONTEXT_VARIABLE_NAME, evaluationContext);
    }

    @Override
    public HttpServletRequest getRequest() {
        return webContext.getRequest();
    }

    @Override
    public HttpServletResponse getResponse() {
        return webContext.getResponse();
    }

    @Override
    public HttpSession getSession() {
        return webContext.getSession();
    }

    @Override
    public ServletContext getServletContext() {
        return webContext.getServletContext();
    }

    @Override
    public Locale getLocale() {
        return webContext.getLocale();
    }

    @Override
    public boolean containsVariable(String name) {
        return webContext.containsVariable(name);
    }

    @Override
    public Set<String> getVariableNames() {
        return webContext.getVariableNames();
    }

    @Override
    public Object getVariable(String name) {
        return webContext.getVariable(name);
    }

    private class FakeHttpServletRequest implements HttpServletRequest {
        final HashMap<String, String[]> parameters = new HashMap<>();
        final HashMap<String, Object> attributes = new HashMap<>();

        @Override
        public String getAuthType() {
            return null;
        }

        @Override
        public Cookie[] getCookies() {
            return new Cookie[0];
        }

        @Override
        public long getDateHeader(String name) {
            return 0;
        }

        @Override
        public String getHeader(String name) {
            return null;
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            return null;
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            return null;
        }

        @Override
        public int getIntHeader(String name) {
            return 0;
        }

        @Override
        public String getMethod() {
            return null;
        }

        @Override
        public String getPathInfo() {
            return null;
        }

        @Override
        public String getPathTranslated() {
            return null;
        }

        @Override
        public String getContextPath() {
            return null;
        }

        @Override
        public String getQueryString() {
            return null;
        }

        @Override
        public String getRemoteUser() {
            return null;
        }

        @Override
        public boolean isUserInRole(String role) {
            return false;
        }

        @Override
        public Principal getUserPrincipal() {
            return null;
        }

        @Override
        public String getRequestedSessionId() {
            return null;
        }

        @Override
        public String getRequestURI() {
            return null;
        }

        @Override
        public StringBuffer getRequestURL() {
            return null;
        }

        @Override
        public String getServletPath() {
            return null;
        }

        @Override
        public HttpSession getSession(boolean create) {
            return null;
        }

        @Override
        public HttpSession getSession() {
            return null;
        }

        @Override
        public String changeSessionId() {
            return null;
        }

        @Override
        public boolean isRequestedSessionIdValid() {
            return false;
        }

        @Override
        public boolean isRequestedSessionIdFromCookie() {
            return false;
        }

        @Override
        public boolean isRequestedSessionIdFromURL() {
            return false;
        }

        @Override
        @Deprecated
        public boolean isRequestedSessionIdFromUrl() {
            return false;
        }

        @Override
        public boolean authenticate(HttpServletResponse response) {
            return false;
        }

        @Override
        public void login(String username, String password) {

        }

        @Override
        public void logout() {

        }

        @Override
        public Collection<Part> getParts() {
            return null;
        }

        @Override
        public Part getPart(String name) {
            return null;
        }

        @Override
        public <T extends HttpUpgradeHandler> T upgrade(Class<T> httpUpgradeHandlerClass) {
            return null;
        }

        @Override
        public Object getAttribute(String name) {
            return attributes.get(name);
        }

        @Override
        public Enumeration<String> getAttributeNames() {
            return new Vector<>(attributes.keySet()).elements();
        }

        @Override
        public String getCharacterEncoding() {
            return null;
        }

        @Override
        public void setCharacterEncoding(String env) {

        }

        @Override
        public int getContentLength() {
            return 0;
        }

        @Override
        public long getContentLengthLong() {
            return 0;
        }

        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public ServletInputStream getInputStream() {
            return null;
        }

        @Override
        public String getParameter(String name) {
            return parameters.get(name)[0];
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return new Vector<>(parameters.keySet()).elements();
        }

        @Override
        public String[] getParameterValues(String name) {
            return parameters.get(name);
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return parameters;
        }

        @Override
        public String getProtocol() {
            return null;
        }

        @Override
        public String getScheme() {
            return null;
        }

        @Override
        public String getServerName() {
            return null;
        }

        @Override
        public int getServerPort() {
            return 0;
        }

        @Override
        public BufferedReader getReader() {
            return null;
        }

        @Override
        public String getRemoteAddr() {
            return null;
        }

        @Override
        public String getRemoteHost() {
            return null;
        }

        @Override
        public void setAttribute(String name, Object o) {
            attributes.put(name, o);
        }

        @Override
        public void removeAttribute(String name) {
            attributes.remove(name);
        }

        @Override
        public Locale getLocale() {
            return Locale.CANADA;
        }

        @Override
        public Enumeration<Locale> getLocales() {
            return null;
        }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override
        public RequestDispatcher getRequestDispatcher(String path) {
            return null;
        }

        @Override
        @Deprecated
        public String getRealPath(String path) {
            return null;
        }

        @Override
        public int getRemotePort() {
            return 0;
        }

        @Override
        public String getLocalName() {
            return null;
        }

        @Override
        public String getLocalAddr() {
            return null;
        }

        @Override
        public int getLocalPort() {
            return 0;
        }

        @Override
        public ServletContext getServletContext() {
            return null;
        }

        @Override
        public AsyncContext startAsync() throws IllegalStateException {
            return null;
        }

        @Override
        public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
            return null;
        }

        @Override
        public boolean isAsyncStarted() {
            return false;
        }

        @Override
        public boolean isAsyncSupported() {
            return false;
        }

        @Override
        public AsyncContext getAsyncContext() {
            return null;
        }

        @Override
        public DispatcherType getDispatcherType() {
            return null;
        }
    }

    private class FakeHttpServletResponse implements HttpServletResponse {

        @Override
        public void addCookie(Cookie cookie) {

        }

        @Override
        public boolean containsHeader(String name) {
            return false;
        }

        @Override
        public String encodeURL(String url) {
            return url;
        }

        @Override
        public String encodeRedirectURL(String url) {
            return url;
        }

        @Override
        @Deprecated
        public String encodeUrl(String url) {
            return url;
        }

        @Override
        @Deprecated
        public String encodeRedirectUrl(String url) {
            return url;
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {

        }

        @Override
        public void sendError(int sc) throws IOException {

        }

        @Override
        public void sendRedirect(String location) throws IOException {

        }

        @Override
        public void setDateHeader(String name, long date) {

        }

        @Override
        public void addDateHeader(String name, long date) {

        }

        @Override
        public void setHeader(String name, String value) {

        }

        @Override
        public void addHeader(String name, String value) {

        }

        @Override
        public void setIntHeader(String name, int value) {

        }

        @Override
        public void addIntHeader(String name, int value) {

        }

        @Override
        public void setStatus(int sc) {

        }

        @Override
        @Deprecated
        public void setStatus(int sc, String sm) {

        }

        @Override
        public int getStatus() {
            return 0;
        }

        @Override
        public String getHeader(String name) {
            return "";
        }

        @Override
        public Collection<String> getHeaders(String name) {
            return new ArrayList<>();
        }

        @Override
        public Collection<String> getHeaderNames() {
            return new ArrayList<>();
        }

        @Override
        public String getCharacterEncoding() {
            return "";
        }

        @Override
        public String getContentType() {
            return "";
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return null;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return new PrintWriter(new ByteArrayOutputStream());
        }

        @Override
        public void setCharacterEncoding(String charset) {

        }

        @Override
        public void setContentLength(int len) {

        }

        @Override
        public void setContentLengthLong(long length) {

        }

        @Override
        public void setContentType(String type) {

        }

        @Override
        public void setBufferSize(int size) {

        }

        @Override
        public int getBufferSize() {
            return 0;
        }

        @Override
        public void flushBuffer() throws IOException {

        }

        @Override
        public void resetBuffer() {

        }

        @Override
        public boolean isCommitted() {
            return false;
        }

        @Override
        public void reset() {

        }

        @Override
        public void setLocale(Locale loc) {

        }

        @Override
        public Locale getLocale() {
            return Locale.CANADA;
        }
    }
}
