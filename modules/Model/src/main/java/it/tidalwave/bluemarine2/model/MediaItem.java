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
import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.nio.file.Path;
import it.tidalwave.util.As;
import it.tidalwave.util.Key;

/***********************************************************************************************************************
 *
 * Represents a media item. It is usually associated with one or more files on a filesystem.
 * 
 * @stereotype  Datum
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface MediaItem extends As
  {
    /*******************************************************************************************************************
     *
     * A container of metadata objects for a {@link MediaItem}.
     *
     ******************************************************************************************************************/
    public interface Metadata
      {
        public static final Key<Duration> DURATION = new Key<>("mp3.duration");
        public static final Key<Integer> BIT_RATE = new Key<>("mp3.bitRate");
        public static final Key<Integer> SAMPLE_RATE = new Key<>("mp3.sampleRate");
        public static final Key<String> ARTIST = new Key<>("mp3.artist");
        public static final Key<String> COMPOSER = new Key<>("mp3.composer");
        public static final Key<String> PUBLISHER = new Key<>("mp3.publisher");
        public static final Key<String> TITLE = new Key<>("mp3.title");
        public static final Key<Integer> YEAR = new Key<>("mp3.year");
        public static final Key<String> ALBUM = new Key<>("mp3.album");
        public static final Key<String> TRACK = new Key<>("mp3.track"); // FIXME: should be a Track class
        public static final Key<String> COMMENT = new Key<>("mp3.comment");
        
        /***************************************************************************************************************
         *
         * 
         *
         **************************************************************************************************************/
        @Nonnull
        public <T> Optional<T> get (@Nonnull Key<T> key);

        /***************************************************************************************************************
         *
         * 
         *
         **************************************************************************************************************/
        public boolean containsKey (@Nonnull Key<?> key);

        /***************************************************************************************************************
         *
         * 
         *
         **************************************************************************************************************/
        @Nonnull
        public Set<Key<?>> getKeys();
    }
    
    /*******************************************************************************************************************
     *
     * Returns the {@link Path} associated with this object.
     * 
     * @return  the path
     *
     ******************************************************************************************************************/
    @Nonnull
    public Path getPath();
    
    /*******************************************************************************************************************
     *
     * Returns the {@link Metadata} associated with this object.
     * 
     * @return  the metadata
     *
     ******************************************************************************************************************/
    @Nonnull
    public Metadata getMetadata();
  }
