package uni.project.serviceContracts;

import java.io.BufferedWriter;
import java.io.IOException;

public interface ITextWriter<T> {
    void write(T object, BufferedWriter writer) throws IOException;
}
