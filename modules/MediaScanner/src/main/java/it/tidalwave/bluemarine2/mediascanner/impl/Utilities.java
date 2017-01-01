/*
 * #%L
 * *********************************************************************************************************************
 *
 * blueMarine2 - Semantic Media Center
 * http://bluemarine2.tidalwave.it - git clone https://tidalwave@bitbucket.org/tidalwave/bluemarine2-src.git
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
package it.tidalwave.bluemarine2.mediascanner.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Instant;
import java.text.Normalizer;
import java.util.Date;
import java.util.Optional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.net.MalformedURLException;
import java.net.URL;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.helpers.RDFHandlerBase;
import org.eclipse.rdf4j.rio.n3.N3ParserFactory;
import it.tidalwave.util.Id;
import it.tidalwave.bluemarine2.downloader.DownloadComplete;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.text.Normalizer.Form.NFC;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class Utilities
  {
    private final static ValueFactory FACTORY = SimpleValueFactory.getInstance(); // FIXME

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Model parseModel (final @Nonnull DownloadComplete message)
        throws RDFHandlerException, RDFParseException, IOException
      {
        final N3ParserFactory n3ParserFactory = new N3ParserFactory();
        final RDFParser parser = n3ParserFactory.getParser();
        final Model model = new LinkedHashModel();

        parser.setRDFHandler(new RDFHandlerBase()
          {
            @Override
            public void handleStatement (final @Nonnull Statement statement)
              {
                model.add(statement);
              }
          });

        // FIXME
        final byte[] bytes = new String(message.getBytes(), UTF_8).replaceAll(" = ", "owl:sameAs")
                                                                  .replaceAll("/ASIN/ *>", "/ASIN>")
                                                                  .getBytes(UTF_8);
        final String uri = message.getUrl().toString();
        parser.parse(new ByteArrayInputStream(bytes), uri);

        return model;
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Value literalFor (final Path path)
      {
        return FACTORY.createLiteral(Normalizer.normalize(path.toString(), NFC));
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Value literalFor (final String string)
      {
        return FACTORY.createLiteral(string);
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Optional<Value> literalFor (final Optional<String> optionalString)
      {
        return optionalString.map(s -> literalFor(s));
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Value literalFor (final Id id)
      {
        return FACTORY.createLiteral(id.stringValue());
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Value literalFor (final int value)
      {
        return FACTORY.createLiteral(value);
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Value literalFor (final long value)
      {
        return FACTORY.createLiteral(value);
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Value literalFor (final short value)
      {
        return FACTORY.createLiteral(value);
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Value literalFor (final float value)
      {
        return FACTORY.createLiteral(value);
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Value literalFor (final @Nonnull Instant instant)
      {
        return FACTORY.createLiteral(new Date(instant.toEpochMilli()));
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public static IRI uriFor (final @Nonnull Id id)
      {
        return uriFor(id.stringValue());
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public static IRI uriFor (final @Nonnull String id)
      {
        return FACTORY.createIRI(id);
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public static IRI uriFor (final @Nonnull URL url)
      {
        return FACTORY.createIRI(url.toString());
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public static URL urlFor (final @Nonnull IRI uri)
      throws MalformedURLException
      {
        return new URL(uri.toString());
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public static String emptyWhenNull (final @Nullable String string)
      {
        return (string != null) ? string : "";
      }
  }
