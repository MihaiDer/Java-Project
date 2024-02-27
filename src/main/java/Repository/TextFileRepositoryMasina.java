package Repository;
import Domain.Masina;
import Exceptions.RepoException;

import java.io.*;


public class TextFileRepositoryMasina extends MemoryRepo<Masina> {
    private final String fileName;

    public TextFileRepositoryMasina(String fileName) {
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
    public Masina getById(int id) {
        return super.getById(id);
    }

    @Override
    public void modifyEntity(int id, Masina entitate) throws RepoException {
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
    public void addEntity(Masina o) throws RepoException {
        super.addEntity(o);
        try {
            writeInFile();
        } catch (IOException e) {
            throw new RepoException("Error saving file " + e.getMessage());
        }
    }

    private void readFromFile() {

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String linie;
            while ((linie = br.readLine()) != null) {
                Masina masina = convertReadLineToEntity(linie);
                elems.add(masina);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void writeInFile() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Masina elem : elems) {
                String linie = convertEntityInLine(elem);
                bw.write(linie);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Masina convertReadLineToEntity(String linie) {
        String[] parts = linie.split(",");
        int idMasina = Integer.parseInt(parts[0]);
        String marca = parts[1];
        String model = parts[2];
        return new Masina(idMasina, marca, model);
    }

    private String convertEntityInLine(Masina entitate) {
        return entitate.getId() + "," + entitate.getMarca() + "," + entitate.getModel();
    }

}