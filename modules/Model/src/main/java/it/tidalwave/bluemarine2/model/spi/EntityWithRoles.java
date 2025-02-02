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
package it.tidalwave.bluemarine2.model.spi;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import it.tidalwave.util.spi.PriorityAsSupport;
import lombok.experimental.Delegate;

/***********************************************************************************************************************
 *
 * A support class for entities that have roles.
 *
 * @stereotype  Datum
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class EntityWithRoles implements Entity
  {
    @Delegate @Nonnull
    private final PriorityAsSupport asSupport;

    @Nonnull // for toString() only
    private final Collection<Object> roles;

    public EntityWithRoles()
      {
        this(Collections.emptyList());
      }

    public EntityWithRoles (@Nonnull final Collection<Object> roles)
      {
        this.asSupport = new PriorityAsSupport(this, roles);
        this.roles = roles;
      }

    protected EntityWithRoles (@Nonnull final PriorityAsSupport.RoleProvider additionalRoleProvider,
                               @Nonnull final Collection<Object> roles)
      {
        this.asSupport = new PriorityAsSupport(this, additionalRoleProvider, roles);
        this.roles = roles;
      }

    @Override @Nonnull
    public String toString()
      {
        return String.format("%s(%s)", getClass().getSimpleName(), roles);
      }
  }
