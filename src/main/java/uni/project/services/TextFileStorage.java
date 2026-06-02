package uni.project.services;



import uni.project.serviceContracts.IFileNameStrategy;
import uni.project.serviceContracts.IStorage;
import uni.project.serviceContracts.ITextReader;
import uni.project.serviceContracts.ITextWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class TextFileStorage<Model, DTO> implements IStorage<Model, DTO> {
    private final ITextWriter<Model> textWriter;
    private final ITextReader<DTO> textReader;
    private final IFileNameStrategy<Model> nameStrategy;
    private static final  String FILE_EXTENSION = ".txt";

    public TextFileStorage(ITextWriter<Model> textWriter, ITextReader<DTO> textReader
            , IFileNameStrategy<Model> nameStrategy) {
        if (textWriter == null) {
            throw new IllegalArgumentException("Text Writer cannot be null.");
        }
        if (textReader == null) {
            throw new IllegalArgumentException("Text Reader cannot be null.");
        }
        if (nameStrategy == null) {
            throw new IllegalArgumentException("File path name service cannot be null.");
        }

        this.textWriter = textWriter;
        this.textReader = textReader;
        this.nameStrategy = nameStrategy;
    }

    @Override
    public void save(Model object, String folderPath) throws IOException {
        if (object == null){
            throw new IllegalArgumentException("object cant be null when saving to a file");
        }
        validatePath(folderPath);
        Files.createDirectories(Path.of(folderPath));

        Path path = Path.of(folderPath, nameStrategy.getFileName(object)  + FILE_EXTENSION);

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            textWriter.write(object, writer);
        }
    }



    @Override
    public DTO load(String filePath) throws IOException {
        validatePath(filePath);
        try (BufferedReader reader = Files.newBufferedReader(Path.of(filePath))) {

            return textReader.read(reader);
        }
    }

    private void validatePath(String path) {
        if (path == null){
            throw new IllegalArgumentException("folder path cant be null");
        }
    }
}
