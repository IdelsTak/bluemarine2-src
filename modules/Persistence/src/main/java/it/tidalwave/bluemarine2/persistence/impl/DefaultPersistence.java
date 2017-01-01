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
package it.tidalwave.bluemarine2.persistence.impl;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.concurrent.CountDownLatch;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandler;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.n3.N3Writer;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.messagebus.annotation.ListensTo;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.bluemarine2.util.PowerOnNotification;
import it.tidalwave.bluemarine2.persistence.Persistence;
import it.tidalwave.bluemarine2.persistence.PersistencePropertyNames;
import it.tidalwave.bluemarine2.util.PersistenceInitializedNotification;
import it.tidalwave.messagebus.MessageBus;
import lombok.extern.slf4j.Slf4j;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.nio.charset.StandardCharsets.UTF_8;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@SimpleMessageSubscriber @Slf4j
public class DefaultPersistence implements Persistence
  {
    @Inject
    private Provider<MessageBus> messageBus;

    private final CountDownLatch initialized = new CountDownLatch(1);

    private Repository repository;

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public Repository getRepository()
      {
        waitForPowerOn();
        return repository;
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    /* VisibleForTesting */ void onPowerOnNotification (final @ListensTo @Nonnull PowerOnNotification notification)
      throws RepositoryException, IOException, RDFParseException
      {
        log.info("onPowerOnNotification({})", notification);
        repository = new SailRepository(new MemoryStore());
        repository.initialize();

        try
          {
            final Path repositoryPath = notification.getProperties().get(PersistencePropertyNames.REPOSITORY_PATH);

            if (Files.exists(repositoryPath))
              {
                try (final RepositoryConnection connection = repository.getConnection();
                     final Reader reader = Files.newBufferedReader(repositoryPath, UTF_8))
                  {
                    log.info("Importing repository from {} ...", repositoryPath);
                    connection.add(reader, repositoryPath.toUri().toString(), RDFFormat.N3);
                    connection.commit();
                  }
              }
          }
        catch (NotFoundException e)
          {
            log.warn("No repository path: operating in memory");
          }

        initialized.countDown();
        messageBus.get().publish(new PersistenceInitializedNotification());
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Override
    public void dump (final @Nonnull Path path)
      throws RDFHandlerException, IOException, RepositoryException
      {
        log.info("dump({})", path);
        Files.createDirectories(path.getParent());

        try (final PrintWriter pw = new PrintWriter(Files.newBufferedWriter(path, UTF_8));
             final RepositoryConnection connection = repository.getConnection())
          {
            final RDFHandler writer = new SortingRDFHandler(new N3Writer(pw));

            for (final Namespace namespace : connection.getNamespaces().asList())
              {
                writer.handleNamespace(namespace.getPrefix(), namespace.getName());
              }

            writer.handleNamespace("bio",   "http://purl.org/vocab/bio/0.1/");
            writer.handleNamespace("bmmo",  "http://bluemarine.tidalwave.it/2015/04/mo/");
            writer.handleNamespace("dc",    "http://purl.org/dc/elements/1.1/");
            writer.handleNamespace("foaf",  "http://xmlns.com/foaf/0.1/");
            writer.handleNamespace("owl",   "http://www.w3.org/2002/07/owl#");
            writer.handleNamespace("mo",    "http://purl.org/ontology/mo/");
            writer.handleNamespace("rdfs",  "http://www.w3.org/2000/01/rdf-schema#");
            writer.handleNamespace("rel",   "http://purl.org/vocab/relationship/");
            writer.handleNamespace("vocab", "http://dbtune.org/musicbrainz/resource/vocab/");
            writer.handleNamespace("xs",    "http://www.w3.org/2001/XMLSchema#");

            connection.export(writer);
          }
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Override
    public <E extends Exception> void runInTransaction (final @Nonnull TransactionalTask<E> task)
      throws E, RepositoryException
      {
        log.info("runInTransaction({})", task);
        waitForPowerOn();
        final long baseTime = System.nanoTime();

        try (final RepositoryConnection connection = repository.getConnection()) // TODO: pool?
          {
            task.run(connection);
            connection.commit();
            connection.close();
          }
        catch (Exception e)
          {
            log.error("Transaction failed: {}", e.toString());
          }

        if (log.isDebugEnabled())
          {
            log.debug(">>>> done in {} ms", (System.nanoTime() - baseTime) * 1E-6);
          }
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    private void waitForPowerOn()
      {
        try
          {
            if (!initialized.await(10, SECONDS))
              {
                throw new IllegalStateException("Did not receive PowerOnNotification");
              }
          }
        catch (InterruptedException ex)
          {
            throw new IllegalStateException("Interrupted while waiting for PowerOnNotification");
          }
      }
  }
