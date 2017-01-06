/*
 * #%L
 * *********************************************************************************************************************
 *
 * blueMarine2 - Semantic Media Center
 * http://bluemarine2.tidalwave.it - git clone https://bitbucket.org/tidalwave/bluemarine2-src.git
 * %%
 * Copyright (C) 2015 - 2017 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.bluemarine2.model.impl;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.nio.file.Path;
import it.tidalwave.util.Finder8;
import it.tidalwave.util.MappingFilter;
import it.tidalwave.role.SimpleComposite8;
import it.tidalwave.bluemarine2.model.MediaFolder;
import it.tidalwave.bluemarine2.model.role.Entity;
import it.tidalwave.bluemarine2.model.role.PathAwareEntity;
import it.tidalwave.bluemarine2.model.finder.EntityFinder;
import it.tidalwave.bluemarine2.model.spi.VirtualMediaFolder;
import static it.tidalwave.bluemarine2.model.impl.PathAwareEntityDecorator.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class PathAwareMediaFolderDecorator extends PathAwareEntityDecorator implements MediaFolder
  {
    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public PathAwareMediaFolderDecorator (final @Nonnull Entity delegate,
                                          final @Nonnull PathAwareEntity parent,
                                          final @Nonnull Path pathSegment,
                                          final @Nonnull Object ... roles)
      {
        super(delegate, parent, pathSegment, roles);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public EntityFinder findChildren()
      {
        final SimpleComposite8<Entity> composite = delegate.as(SimpleComposite8.class);
        return wrappedFinder(this, composite.findChildren());
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public String toString()
      {
        return String.format("%s(path=%s, parent=%s)", getClass().getSimpleName(), getPath(),
                (parent instanceof VirtualMediaFolder) ? Optional.of(parent) : "Optional[Folder()]");
//        return String.format("%s(path=%s, delegate=%s, parent=%s)", getClass().getSimpleName(), getPath(), delegate, parent);
      }

    /*******************************************************************************************************************
     *
     * Creates a wrapped finder, that wraps all entities in its result.
     *
     * @param   parent          the parent
     * @param   finder          the source finder
     * @return                  the wrapped finder
     *
     ******************************************************************************************************************/
    @Nonnull
    private static EntityFinder wrappedFinder (final @Nonnull MediaFolder parent,
                                               final @Nonnull Finder8<? extends Entity> finder)
      {
        if (finder instanceof PathAwareEntityFinderDelegate)
          {
            return (EntityFinder)finder;
          }

        return new PathAwareEntityFinderDelegate(parent,
                                                 (Finder8)new MappingFilter<>((Finder8)finder,
                                                                              child -> wrappedEntity(parent, (Entity)child)));
      }
  }
