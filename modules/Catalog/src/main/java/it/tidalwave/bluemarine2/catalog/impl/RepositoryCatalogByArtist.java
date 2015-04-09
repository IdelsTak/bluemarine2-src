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
package it.tidalwave.bluemarine2.catalog.impl;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import it.tidalwave.util.spi.AsSupport;
import it.tidalwave.role.SimpleComposite8;
import it.tidalwave.bluemarine2.catalog.Catalog;
import it.tidalwave.bluemarine2.catalog.MusicArtist;
import it.tidalwave.bluemarine2.model.Entity;
import it.tidalwave.bluemarine2.model.EntitySupplier;
import it.tidalwave.bluemarine2.persistence.Persistence;
import lombok.Delegate;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class RepositoryCatalogByArtist implements EntitySupplier
  {
    @Inject 
    private Persistence persistence;
    
    @CheckForNull
    private Catalog catalog;
    
    @Delegate
    private final AsSupport asSupport = new AsSupport(this);
    
    private final SimpleComposite8<MusicArtist> composite = () -> getCatalog().findArtists();
        
    @Override @Nonnull
    public Entity get() 
      {
        return new Entity() 
          {
            @Delegate
            private final AsSupport asSupport = new AsSupport(this, composite);
          };
      }
    
    @Nonnull
    private synchronized Catalog getCatalog()
      {
        if (catalog == null)
          {
            catalog = new RepositoryCatalog(persistence.getRepository());
          }
        
        return catalog;
      }
  }
