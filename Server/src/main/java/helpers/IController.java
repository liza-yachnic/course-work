package helpers;

import java.io.IOException;

public interface IController {

    void saveDate(String msg) throws IOException, ClassNotFoundException;

    void editDate(String msg) throws IOException, ClassNotFoundException;

    void deleteData(String msg) throws IOException, ClassNotFoundException;

    void getDate(String msg) throws IOException, ClassNotFoundException;

    void start();
}
