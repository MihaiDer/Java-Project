package Repository;
import Domain.Inchiriere;
import Exceptions.RepoException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BinaryFileRepositoryInchiriere extends MemoryRepo<Inchiriere> {
    private final String fileName;

    public BinaryFileRepositoryInchiriere(String fileName) {
        this.fileName = fileName;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        readFromFile();
    }

    @Override
    public boolean findById(int id) {
        return super.findById(id);
    }

    @Override
    public Inchiriere getById(int id) {
        return super.getById(id);
    }
    @Override
    public void modifyEntity(int id, Inchiriere entitate) throws RepoException {
        try {
            super.modifyEntity(id, entitate);
            writeInFile();
        } catch (RepoException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException e) {
            throw new RepoException("Error saving file " + e.getMessage());
        }
    }

    @Override
    public void deleteEntity(int id) throws RepoException {
        super.deleteEntity(id);
        try {
            writeInFile();
        } catch (IOException e) {
            throw new RepoException("Error saving file " + e.getMessage());
        }
    }

    @Override
    public void addEntity(Inchiriere o) throws RepoException {
        super.addEntity(o);
        try {
            writeInFile();
        } catch (IOException e) {
            throw new RepoException("Error saving file " + e.getMessage());
        }
    }

    private void readFromFile()
    {

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            Object obj = ois.readObject();

            if (obj instanceof List<?> deserializedList) {

                if (!deserializedList.isEmpty() && deserializedList.get(0) instanceof Inchiriere) {
                    this.elems = (List<Inchiriere>) obj;
                } else {
                    System.err.println("Unexpected object type in the file.");
                }
            }
        } catch (EOFException ignored) {
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
    private void writeInFile() throws IOException {
        try {
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
                outputStream.writeObject(new ArrayList<>(elems.stream().toList()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}