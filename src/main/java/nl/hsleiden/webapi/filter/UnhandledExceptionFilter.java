/*
 * Copyright 2014 Hogeschool Leiden.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.hsleiden.webapi.filter;

import com.google.common.base.Preconditions;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

public class UnhandledExceptionFilter implements Filter {

    public interface ErrorHandler {

        void handle(HttpServletRequest request, HttpServletResponse response, Throwable throwable);
    }
    private ErrorHandler errorHandler;

    public UnhandledExceptionFilter() {
    }

    public UnhandledExceptionFilter(ErrorHandler errorHandler) {
        Preconditions.checkNotNull(errorHandler);
        this.errorHandler = errorHandler;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO: add init code to lookup errorHandler
    }

    // suppress calls to sendError() and just setStatus() instead
    private static class StatusCodeCaptureWrapper extends HttpServletResponseWrapper {

        public StatusCodeCaptureWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public void sendError(int sc) throws IOException {
            // do NOT use sendError() otherwise per servlet spec the container will send an html error page
            this.setStatus(sc);
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {
            // do NOT use sendError() otherwise per servlet spec the container will send an html error page
            this.setStatus(sc, msg);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        StatusCodeCaptureWrapper responseWrapper = new StatusCodeCaptureWrapper(response);
        Throwable throwable = null;

        try {
            chain.doFilter(request, responseWrapper);
        } catch (ServletException e) {
            throwable = e.getRootCause();
        } catch (Throwable e) {
            throwable = e;
        }

        if (throwable != null) {
            errorHandler.handle(request, response, throwable);
        }
        // flush to prevent servletcontainer to add anymore headers or content
        response.flushBuffer();
    }

    @Override
    public void destroy() {
// noop
    }
}