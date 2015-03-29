/*
 * #%L
 * *********************************************************************************************************************
 *
 * blueMarine2 - lightweight MediaCenter
 * http://bluemarine.tidalwave.it - hg clone https://bitbucket.org/tidalwave/bluemarine2-src
 * %%
 * Copyright (C) 2015 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.bluemarine2.ui.mainscreen.impl;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.function.Supplier;
import it.tidalwave.role.ui.UserAction;
import it.tidalwave.role.ui.spi.UserActionRunnable;
import it.tidalwave.messagebus.MessageBus;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import static it.tidalwave.bluemarine2.ui.util.BundleUtilities.*;

// TODO: if approved, move to TheseFoolishThings
@RequiredArgsConstructor
class SupplierFromClass<T> implements Supplier<T>
  {
    @Nonnull
    private final Class<T> factoryClass;

    @Override
    public T get() 
      {
        try
          {
            return factoryClass.newInstance();
          } 
        catch (InstantiationException | IllegalAccessException e) 
          {
            throw new RuntimeException(e);
          }
      } 
  }

/***********************************************************************************************************************
 *
 * A proritized container of {@link UserAction}s to be placed on a menu.
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class MainMenuItem 
  {
    @Getter @Nonnull
    private final int priority;
    
    @Inject
    private MessageBus messageBus;
    
    @Getter @Nonnull
    private final UserAction action;
    
    public MainMenuItem (final @Nonnull String displayNameKey, 
                         final @Nonnull String requestClassName,
                         final @Nonnull int priority)
      throws ClassNotFoundException
      {
        this.priority = priority;
        final Class<?> requestClass = Thread.currentThread().getContextClassLoader().loadClass(requestClassName);
        final SupplierFromClass<?> supplier = new SupplierFromClass<>(requestClass);
        // FIXME: use MessageSendingUserAction?
        this.action  = new UserActionRunnable(displayableFromBundle(getClass(), displayNameKey), 
                                              () -> messageBus.publish(supplier.get()));
      }
  }
