package Repository;
import Domain.Inchiriere;
import Domain.Masina;
import Exceptions.RepoException;

import java.io.*;
public class TextFileRepositoryInchiriere extends MemoryRepo<Inchiriere> {
    private final String fileName;

    public TextFileRepositoryInchiriere(String fileName) {
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

    private void readFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String linie;
            while ((linie = br.readLine()) != null) {
                Inchiriere entitate = convertReadLineToEntity(linie);
                elems.add(entitate);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void writeInFile() throws IOException {
        // FIXME try with resources
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Inchiriere inchiriere : elems) {
                String linie = convertEntityInLine(inchiriere);
                bw.write(linie);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String convertEntityInLine(Inchiriere inchiriere) {
        int idInchiriere = inchiriere.getId();
        int idMasina = inchiriere.getCar().getId();
        String marca = inchiriere.getCar().getMarca();
        String model = inchiriere.getCar().getModel();
        String dataInceput = inchiriere.getDateIn();
        String dataSfarsit = inchiriere.getDateOut();
        return idInchiriere + "," + idMasina + "," + marca + "," + model + "," + dataInceput + "," + dataSfarsit;
    }

    private Inchiriere convertReadLineToEntity(String linie){
        String[] parts = linie.split(",");
        Inchiriere masinaInchiriata;
        int idInchiriere = Integer.parseInt(parts[0]);
        Masina masina;
        int idMasina = Integer.parseInt(parts[1]);
        String marca = parts[2];
        String model = parts[3];
        masina = new Masina(idMasina, marca, model);

        String dataInceput = parts[4];
        String dataFinal = parts[5];

        masinaInchiriata = new Inchiriere(idInchiriere, masina,
                dataInceput, dataFinal);
        return masinaInchiriata;
    }
}
