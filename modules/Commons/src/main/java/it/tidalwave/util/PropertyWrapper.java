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
package it.tidalwave.util;

import javax.annotation.Nonnull;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import it.tidalwave.role.ui.BoundProperty;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class PropertyWrapper
  {
    /*******************************************************************************************************************
     *
     * Returns a JavaFX wrapping property which is kept in sync with the given {@link BoundProperty}.
     * FIXME: this is temporary until we fix the JavaFX property issue: either they are supported by UserAction or
     * everything is replaced by BoundProperty.
     * FIXME: this is for sure prone to leaks, as listeners are not weak.
     *
     ******************************************************************************************************************/
    @Nonnull
    public static BooleanProperty wrap (@Nonnull final BoundProperty<Boolean> property)
      {
        final BooleanProperty jfxProperty = new SimpleBooleanProperty(property.get());
        property.addPropertyChangeListener(event ->
           {
             if (!jfxProperty.isBound()) jfxProperty.setValue((Boolean)event.getNewValue());
           });
        jfxProperty.addListener((observable, oldValue, newValue) -> property.set(newValue));
        return jfxProperty;
      }
  }
