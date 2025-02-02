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
package it.tidalwave.bluemarine2.ui.mainscreen;

import javax.annotation.Nonnull;
import java.util.Collection;
import it.tidalwave.role.ui.UserAction;

/***********************************************************************************************************************
 *
 * The Presentation for the main screen, which contains the main menu.
 * 
 * @stereotype  Presentation
 * 
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public interface MainScreenPresentation 
  {
    /*******************************************************************************************************************
     *
     * Binds the presentation to callbacks.
     * 
     * @param  mainMenuActions  the main menu actions
     * @param  powerOffAction   the action that powers off the system
     *
     ******************************************************************************************************************/
    public void bind (@Nonnull Collection<UserAction> mainMenuActions, @Nonnull UserAction powerOffAction);
    
    /*******************************************************************************************************************
     *
     * Shows this presentation on the screen.
     *
     ******************************************************************************************************************/
    public void showUp();
  }
