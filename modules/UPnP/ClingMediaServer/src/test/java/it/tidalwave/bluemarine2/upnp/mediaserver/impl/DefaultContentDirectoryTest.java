/*
 * #%L
 * *********************************************************************************************************************
 *
 * blueMarine2 - Semantic Media Center
 * http://bluemarine2.tidalwave.it - git clone https://tidalwave@bitbucket.org/tidalwave/bluemarine2-src.git
 * %%
 * Copyright (C) 2015 - 2016 Tidalwave s.a.s. (http://tidalwave.it)
 * %%
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
 * $Id$
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.bluemarine2.upnp.mediaserver.impl;

import it.tidalwave.bluemarine2.upnp.mediaserver.impl.device.DefaultDeviceFactory;
import it.tidalwave.util.spi.AsDelegateProvider;
import it.tidalwave.util.spi.EmptyAsDelegateProvider;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.types.UDN;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class DefaultContentDirectoryTest
  {
    private UpnpService upnpService;

    @BeforeClass
    public void setup()
      {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        AsDelegateProvider.Locator.set(new EmptyAsDelegateProvider()); // FIXME: use Spring
        upnpService = new UpnpServiceImpl();
      }

    @AfterClass
    public void shutdown()
      {
        log.info("Shutting down...");
        upnpService.shutdown();
      }

    @Test
    public void registerDevice()
      throws Exception
      {
        final DefaultDeviceFactory<ContentDirectoryClingAdapter> underTest =
                new DefaultDeviceFactory<>(upnpService, ContentDirectoryClingAdapter.class);
        underTest.setUdn(UDN.uniqueSystemIdentifier("1"));
        underTest.setFriendlyName("blueMarine II");
//        underTest.setIcons(Arrays.asList(createDefaultDeviceIcon()));
        underTest.registerDevice();

        log.info("Completed device registration");
        Thread.sleep(60000);
      }
  }
