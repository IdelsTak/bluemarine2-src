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
package it.tidalwave.bluemarine2.model;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.time.Duration;
import it.tidalwave.util.Finder8;

/***********************************************************************************************************************
 *
 * Represents an audio file. Maps the homonymous concept from the Music Ontology.
 * 
 * @stereotype  Datum
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface AudioFile extends MediaItem // FIXME: MediaItem should not be statically Parentable
  {
    /*******************************************************************************************************************
     *
     * Returns the label of this audio file.
     * 
     * @return  the label
     *
     ******************************************************************************************************************/
    @Nonnull
    public Optional<String> getLabel();
    
    /*******************************************************************************************************************
     *
     * Returns the duration of this audio file.
     * 
     * @return  the duration
     *
     ******************************************************************************************************************/
    @Nonnull
    public Optional<Duration> getDuration();
    
    /*******************************************************************************************************************
     *
     * Returns the makers of this audio file.
     * 
     * FIXME: shouldn't be here - should be in getTrack().findMakers().
     * 
     * @return  the makers
     *
     ******************************************************************************************************************/
    // FIXME: and it should be Optional<MusicArtist>
    @Nonnull
    public Finder8<? extends Entity> findMakers();

    /*******************************************************************************************************************
     *
     * Returns the composers of the musical expression related to this audio file.
     * 
     * FIXME: shouldn't be here - should be in getSignal().findMakers() or such
     * 
     * @return  the composer
     *
     ******************************************************************************************************************/
    // FIXME: and it should be Optional<MusicArtist>
    @Nonnull
    public Finder8<? extends Entity> findComposers();

    /*******************************************************************************************************************
     *
     * Returns the record related to this audio file.
     * 
     * FIXME: shouldn't be here - should be in getTrack().getRecord() or such
     * 
     * @return  the composer
     *
     ******************************************************************************************************************/
    // FIXME: and it should be Optional<Record>
    public Optional<? extends Entity> getRecord();
  }
