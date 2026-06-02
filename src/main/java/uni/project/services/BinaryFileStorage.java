package uni.project.services;

import uni.project.serviceContracts.IFileNameStrategy;
import uni.project.serviceContracts.IMapper;
import uni.project.serviceContracts.IStorage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class BinaryFileStorage<Model, DTO> implements IStorage<Model, DTO> {
    private final IMapper<Model, DTO> mapper;
    private final IFileNameStrategy<Model> nameStrategy;
    private final static String FILE_EXTENSION = ".bin";


    public BinaryFileStorage(IMapper<Model, DTO> mapper, IFileNameStrategy<Model> nameStrategy){
        if (mapper == null){
            throw new IllegalArgumentException("mapper cannot be null");
        }
        if (nameStrategy == null){
            throw new IllegalArgumentException("nameStrategy cannot be null");
        }
        this.nameStrategy = nameStrategy;
        this.mapper = mapper;
    }

    public  void save(Model object, String folderPath) throws IOException {
        if (object == null){
            throw new IllegalArgumentException("object cannot be null");
        }
        validate(folderPath);
        Files.createDirectories(Path.of(folderPath));

        Path path = Path.of(folderPath, nameStrategy.getFileName(object)  + FILE_EXTENSION);

        try (ObjectOutputStream out =
                     new ObjectOutputStream(Files.newOutputStream(path))) {
            out.writeObject(object);
        }
    }

    public DTO load(String filePath) throws IOException {
        validate(filePath);
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(filePath))) {

            Model object = (Model) in.readObject();
            return mapper.toDto(object);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void validate(String path){
        if (path == null || path.isBlank()) throw new IllegalArgumentException("path cannot be null or blank");
    }
}
