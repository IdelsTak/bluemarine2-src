/*
 * *********************************************************************************************************************
 *
 * blueMarine II: Semantic Media Centre
 * http://tidalwave.it/projects/bluemarine2
 *
 * Copyright (C) 2015 - 2021 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 * *********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * *********************************************************************************************************************
 *
 * git clone https://bitbucket.org/tidalwave/bluemarine2-src
 * git clone https://github.com/tidalwave-it/bluemarine2-src
 *
 * *********************************************************************************************************************
 */
package it.tidalwave.bluemarine2.rest.impl.server;

import javax.annotation.Nonnull;
import java.util.Enumeration;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Slf4j
public class LoggingFilter implements Filter
  {

    @Override
    public void init (@Nonnull final FilterConfig filterConfig)
      {
      }

    @Override
    public void doFilter (@Nonnull final ServletRequest req,
                          @Nonnull final ServletResponse res,
                          @Nonnull final FilterChain chain)
      throws IOException, ServletException
      {
        final HttpServletRequest request = (HttpServletRequest)req;
        final HttpServletResponse response = (HttpServletResponse)res;

        try
          {
            log.debug(">>>> request: {} {} {}", request.getMethod(), request.getRequestURI(), request.getProtocol());

            for (final Enumeration<String> names = request.getHeaderNames(); names.hasMoreElements(); )
              {
                final String headerName = names.nextElement();
                log.debug(">>>> request header: {} = {}", headerName, request.getHeader(headerName));
              }

            chain.doFilter(req, res);
          }
        finally
          {
            final int status = response.getStatus();
            log.debug(">>>> response {} {}", status, HttpStatus.valueOf(status).getReasonPhrase());

            for (final String headerName : response.getHeaderNames())
              {
                log.debug(">>>> response header: {} = {}", headerName, response.getHeaders(headerName));
              }
          }
      }

    @Override
    public void destroy()
      {
      }
  }
