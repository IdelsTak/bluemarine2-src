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
package it.tidalwave.bluemarine2.ui.audio.explorer.impl;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.net.URL;
import it.tidalwave.bluemarine2.model.role.EntityBrowser;
import it.tidalwave.bluemarine2.model.spi.Entity;

/***********************************************************************************************************************
 *
 * A SPI interface to {@link DefaultAudioExplorerPresentationControl} which is only exposed to local roles.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public interface AudioExplorerPresentationControlSpi
  {
    /*******************************************************************************************************************
     *
     * Selects a browser.
     *
     * @param   entityBrowser     the browser
     *
     ******************************************************************************************************************/
    public void selectBrowser (@Nonnull EntityBrowser entityBrowser);

    /*******************************************************************************************************************
     *
     * Navigates to a new folder, saving the current folder to the stack.
     *
     * @param   mediaFolder  the new folder
     *
     ******************************************************************************************************************/
    public void navigateTo (@Nonnull Entity mediaFolder);

    /*******************************************************************************************************************
     *
     * Clears the details area.
     *
     ******************************************************************************************************************/
    public void clearDetails();

    /*******************************************************************************************************************
     *
     * Renders a text in the details area.
     *
     ******************************************************************************************************************/
    public void renderDetails (@Nonnull String details);

    /*******************************************************************************************************************
     *
     * Requests to download some cover art.
     *
     ******************************************************************************************************************/
    public void requestCoverArt (@Nonnull Optional<URL> optionalCoverArtUri);

    /*******************************************************************************************************************
     *
     * Returns the list of media items in the current folder.
     *
     * FIXME: should be List<MediaItem>
     *
     ******************************************************************************************************************/
    @Nonnull
    public List<Entity> getMediaItems();
  }
