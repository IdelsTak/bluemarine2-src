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
package it.tidalwave.bluemarine2.model.impl.catalog.factory;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.net.URL;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.repository.Repository;
import it.tidalwave.bluemarine2.model.audio.AudioFile;
import it.tidalwave.bluemarine2.model.audio.MusicArtist;
import it.tidalwave.bluemarine2.model.audio.MusicPerformer;
import it.tidalwave.bluemarine2.model.audio.Performance;
import it.tidalwave.bluemarine2.model.audio.Record;
import it.tidalwave.bluemarine2.model.audio.Track;
import it.tidalwave.bluemarine2.model.impl.catalog.RepositoryAudioFile;
import it.tidalwave.bluemarine2.model.impl.catalog.RepositoryMusicArtist;
import it.tidalwave.bluemarine2.model.impl.catalog.RepositoryMusicPerformer;
import it.tidalwave.bluemarine2.model.impl.catalog.RepositoryPerformance;
import it.tidalwave.bluemarine2.model.impl.catalog.RepositoryRecord;
import it.tidalwave.bluemarine2.model.impl.catalog.RepositoryTrack;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class DefaultRepositoryEntityFactory implements RepositoryEntityFactory
  {
    private final Map<Class<?>, EntityFactoryFunction<?>> factoryMapByType = new HashMap<>();

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public <E> E createEntity (@Nonnull final Repository repository,
                               @Nonnull final Class<E> entityClass,
                               @Nonnull final BindingSet bindingSet)
      {
        return entityClass.cast(factoryMapByType.getOrDefault(entityClass, notFound(entityClass))
                                                .apply(repository, bindingSet));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Nonnull
    private <E> EntityFactoryFunction<E> notFound (@Nonnull final Class<E> entityClass)
      {
        return (repository, bindingSet) ->
          {
            throw new RuntimeException("Unknown type: " + entityClass + "; registered: " + factoryMapByType.keySet());
          };
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @PostConstruct
    private void initialize()
      {
        // FIXME: use discovery. Either plain class discovery, or Spring bean discovery
        factoryMapByType.put(String.class, new StringFactory());
        factoryMapByType.put(URL.class, new UrlFactory());
        factoryMapByType.put(Record.class, RepositoryRecord::new);
        factoryMapByType.put(MusicArtist.class, RepositoryMusicArtist::new);
        factoryMapByType.put(MusicPerformer.class, RepositoryMusicPerformer::new);
        factoryMapByType.put(Track.class, RepositoryTrack::new);
        factoryMapByType.put(Performance.class, RepositoryPerformance::new);
        factoryMapByType.put(AudioFile.class, RepositoryAudioFile::new);
      }
  }
