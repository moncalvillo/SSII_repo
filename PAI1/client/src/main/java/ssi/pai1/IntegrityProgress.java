package ssi.pai1;

import java.util.ArrayList;
import java.util.List;

public class IntegrityProgress {

    Double nextProgress;
    Double step;
    List<IntegrityResult> results;
    Integer totalFiles;

    public IntegrityProgress(Integer totalFiles, Double step) {
        this.results = new ArrayList<>();
        this.totalFiles = totalFiles;
        this.step = step;
        this.nextProgress = 0.0;
    }

    public Double getCurrentProgress() {
        Integer filesChecked = 0;

        for (IntegrityResult result : results) {

            Node node = result.getNode();
            if (!node.isLeaf() && result.isOk) {
                filesChecked += node.getNumberOfChildren();
            }

            if (node.isLeaf()) {
                filesChecked++;
            }

        }
        return ((double) filesChecked) / totalFiles;
    }

    public Double getNextStep() {
        return nextProgress;
    }

    public void setNextStep(Double nextProgress) {
        this.nextProgress = nextProgress;
    }

    public List<IntegrityResult> getResults() {
        return results;
    }

    public Integer getTotalFiles() {
        return totalFiles;
    }

    public void incrementStep() {
        while (isNewProgress()) {
            this.nextProgress += this.step / 100;
        }
    }

    public Boolean isNewProgress() {
        return this.getCurrentProgress() >= this.nextProgress;
    }

    public void showProgress() {
        Integer totalSteps = getTotalSteps();
        Integer step = getStepNumber();

        String progress = "[";

        for (int i = 0; i < step; i++) {
            progress += "#";
        }

        for (int i = step + 1; i <= totalSteps; i++) {
            progress += ".";
        }

        progress += "]";

        incrementStep();

        System.out.println(progress);
    }

    public void showResults() {
        Integer numberOfOkFiles = 0;
        Integer numberOfNotOkFiles = 0;
        String notOkFiles = "";
        for (IntegrityResult result : results) {
            Node node = result.getNode();
            if (!node.isLeaf()) {
                if (result.getIsOk()) {
                    numberOfOkFiles += node.getNumberOfChildren();
                }
            } else {
                if (result.getIsOk()) {
                    numberOfOkFiles++;

                } else {
                    numberOfNotOkFiles++;
                    notOkFiles += "ERROR - " + result.getNode().getId() + "\n";
                }
            }

        }

        Integer total = numberOfNotOkFiles + numberOfOkFiles;
        Double percentageOk = ((double) numberOfOkFiles * 100) / total;
        Double percentageNotOk = ((double) numberOfNotOkFiles * 100) / total;

        System.out.println("\nAnálisis de integridad terminado, estos son los resultados: \n");
        System.out.println(notOkFiles);

        System.out.println("Ficheros analizados: " + total);
        
        String fileschanges = String.format("\nFicheros corruptos: %s - %.0f%%", numberOfNotOkFiles, percentageNotOk);
        System.out.println(fileschanges);
        System.out.println(String.format("Ficheros sanos: %s - %.0f%%", numberOfOkFiles, percentageOk));
        
        if(numberOfNotOkFiles>0) {
        	String message = "<br>Se ha detectado una intrusion en el sistema de archivos<br>";
        	message+=fileschanges;
        	message+="<br>Los ficheros que han sido modificados son:<br>"+notOkFiles;
        	
        	//Meter aqui los tres parametros que faltan (los correos deben ser gmail).
        	//Para la contrase�a es posible que haya que generar una contrase�a de aplicacion en https://myaccount.google.com/security?hl=es
        	//La contrase�a generada se copia en el parametro password
        	SendEmail.sendEmail("destino@gmail.com", "origen@gmail.com", message, "Ficheros corruptos!!!", "claveCorreoOrigen");
        	
        }
    }

    public Integer getStepNumber() {
        return (int) Math.round(this.getCurrentProgress() * this.getTotalSteps());
    }

    public Integer getTotalSteps() {
        return (int) Math.round(100 / this.step);
    }

    @Override
    public String toString() {
        return "IntegrityProgress [nextProgress=" + nextProgress + ", currentProgress=" + getCurrentProgress()
                + ", stepNumber=" + getStepNumber()
                + ", totalSteps=" + getTotalSteps() + ", currentFiles=" + results.size() + ", totalFiles="
                + this.totalFiles + "]";
    }

}
