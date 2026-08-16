package guru.springframework.springairagexpert.bootstrap;

import guru.springframework.springairagexpert.config.VectorStoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class LoadVectorStore implements CommandLineRunner {

    @Autowired
    VectorStore vectorStore;

    @Autowired
    VectorStoreProperties vectorStoreProperties;

    @Override
    public void run(String... args) throws Exception {
        log.debug("Loading vector store");
        if (vectorStore.similaritySearch("Sportsman").isEmpty()) {
            log.debug("Loading documents");
            vectorStoreProperties.getDocumentsToLoad().forEach(document ->
            {
                log.debug("Loading document " + document.getFilename());
                TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(document);
                List<Document> documents = tikaDocumentReader.get();
                TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
                List<Document> splitDocuments = tokenTextSplitter.apply(documents);
                vectorStore.add(splitDocuments);
            });
        }
        log.debug("Vector store loaded");
    }
}
