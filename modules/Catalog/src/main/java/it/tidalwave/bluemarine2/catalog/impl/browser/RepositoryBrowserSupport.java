/*
 * #%L
 * *********************************************************************************************************************
 *
 * blueMarine2 - Semantic Media Center
 * http://bluemarine2.tidalwave.it - hg clone https://bitbucket.org/tidalwave/bluemarine2-src
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
package it.tidalwave.bluemarine2.catalog.impl.browser;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.function.Supplier;
import it.tidalwave.util.Finder8;
import it.tidalwave.role.SimpleComposite8;
import it.tidalwave.bluemarine2.catalog.Catalog;
import it.tidalwave.bluemarine2.catalog.impl.RepositoryCatalog;
import it.tidalwave.bluemarine2.model.Entity;
import it.tidalwave.bluemarine2.model.impl.EntityWithRoles;
import it.tidalwave.bluemarine2.model.role.EntityBrowser;
import it.tidalwave.bluemarine2.persistence.Persistence;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class RepositoryBrowserSupport extends EntityWithRoles implements EntityBrowser
  {
    @Inject 
    private Persistence persistence;
    
    @CheckForNull
    private Catalog catalog;
    
    @Nonnull
    protected SimpleComposite8<? extends Entity> compositeForRootEntity;
        
    @Override @Nonnull
    public Entity getRoot() 
      {
        return new EntityWithRoles(compositeForRootEntity);
      }
    
    protected final void setFinder (final @Nonnull Supplier<Finder8<? extends Entity>> finderSupplier)
      {
        // FIXME: drop cast
        compositeForRootEntity = () -> (Finder8<Entity>)finderSupplier.get();
      }   
    
    @Nonnull
    protected final synchronized Catalog getCatalog()
      {
        if (catalog == null)
          {
            catalog = new RepositoryCatalog(persistence.getRepository());
          }
        
        return catalog;
      }
  }
