package ufam.gise.reconhecimento;


import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class GenerateAnalysis {

    FileOutputStream arqAnalisys;
    File diretorioAnalisys;
    File diretorioOutput;
    File diretorioInput;
    int NQuestion = 20;
    
	public void generateAnalysis (File diretorioInput, File diretorioOutput){
		this.diretorioInput = diretorioInput;
		this.diretorioOutput = diretorioOutput;
		
		File arquivos[];
		//File diretorio = new File(diretorioInput.getPath());  
		arquivos = this.diretorioInput.listFiles();  
	  
		for(int i = 0; i < arquivos.length; i++){  
			//leia arquivos[i];
			generateFileToAnalysis(arquivos[i].getName());
			System.out.println(arquivos[i].getName());
			
			orderLiness(arquivos[i].getName());
			counterTimeQuestion(arquivos[i].getName());
			enthropy(arquivos[i].getName());
			measureDoubt(arquivos[i].getName());
			changeAnswer(arquivos[i].getName());
			
		} 
	}
	
	public void generateFileToAnalysis (String name) {
		
/*        File f = new File(Environment.getExternalStorageDirectory(), "Player_Quiz/Log_Analisys");
        
        if (!f.exists()) {
            f.mkdirs();
        }*/
        //Escrevendo o arquivo
        try {

            diretorioAnalisys = new File(diretorioOutput.getPath(), name);
            arqAnalisys = new FileOutputStream(diretorioAnalisys, true);

        }catch(Exception e){
        	e.printStackTrace();	
        }

	}
	
    public void orderLiness(String csvFile) {

        //abrindo o csv

        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        String orderLiness = "";
        String cabecalho="";
        try {

            br = new BufferedReader(new FileReader(diretorioInput.getPath()+"/"+csvFile));
            //Pegando o cabeçalho
            cabecalho = br.readLine() + "\n\n";
            br.readLine();

            //Lendo linha por linha
            while ((line = br.readLine()) != null) {
                String[] dados = line.split(csvSplitBy);
                if((dados[0].equals("Next")==false) &&(dados[0].equals("Back")==false))
                orderLiness = orderLiness + dados[2] + dados[0] + ", ";

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

                String insertOrder = "Ordem de execucao on APP:\n" + orderLiness;
                try {
                    arqAnalisys.write(cabecalho.getBytes());
                    arqAnalisys.flush();

                    arqAnalisys.write(insertOrder.getBytes());
                    arqAnalisys.flush();
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    public void counterTimeQuestion(String csvFile) {

        //abrindo o csv
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        List<Float> TimeQuestion = new ArrayList<>();

        int i;
        float z = 0;
        for (i=0; i < NQuestion; i++) {
            TimeQuestion.add(z);
        }

        i=0;

        String cabecalho = "\n\nTempo em cada questao on APP:\n";
        try {

            arqAnalisys.write(cabecalho.getBytes());
            arqAnalisys.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            br = new BufferedReader(new FileReader(diretorioInput.getPath()+"/"+csvFile));
            //Pegando o cabeçalho
            cabecalho = br.readLine() + "\n\n";
            br.readLine();
            Integer j;

            //Salvando o da primeira linha
            line = br.readLine();
            String[] dados = line.split(csvSplitBy);

            Float valorAnterior = Float.parseFloat(dados[1]);
            //Log.d("CounterView", "DADOS [1]" + dados[1]);
            TimeQuestion.set(0, valorAnterior);

            //Lendo linha por linha
            while ((line = br.readLine()) != null) {
                dados = line.split(csvSplitBy);
                j = Integer.parseInt(dados[2]);
                j--;

                Float valor = TimeQuestion.get(j) + (Float.parseFloat(dados[1]) - valorAnterior);
                TimeQuestion.set(j, valor);
             //   Log.d("CounterView", "valor: " + valor + " posicao: " + j);
                valorAnterior = Float.parseFloat(dados[1]);
                }

            try {
                String insertTimeQuestion = "";
                for (i=0; i < TimeQuestion.size(); i++) {
                    int q = i + 1;
                    insertTimeQuestion = insertTimeQuestion + "Q" + q + "," + TimeQuestion.get(i) + "\n";
                }

                arqAnalisys.write(insertTimeQuestion.getBytes());
                arqAnalisys.flush();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void changeAnswer(String csvFile) {

        //abrindo o csv

        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        List<String> answersUser = new ArrayList<>();

        int i;
        for (i=0; i < NQuestion; i++) {
            answersUser.add("0");
        }

        i=0;

        String cabecalho = "\n\nMudancas de respostas on APP:\n";
        try {

            arqAnalisys.write(cabecalho.getBytes());
            arqAnalisys.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            br = new BufferedReader(new FileReader(diretorioInput.getPath()+"/"+csvFile));
            //Pegando o cabeçalho
            cabecalho = br.readLine() + "\n\n";
            br.readLine();
            Integer j;
            //Lendo linha por linha
            while ((line = br.readLine()) != null) {
                String[] dados = line.split(csvSplitBy);
                j = Integer.parseInt(dados[2]);
                j--;

                if (dados[0].equals("A") ||dados[0].equals("B") || dados[0].equals("C")) {
                    if (answersUser.get(j).equals("0")){
                        answersUser.set(j, dados[0]);
                        //Log.d("ChangeAnswer", "Na posição " + j + " adicionou: " + dados[0]);
                    } else {
                        String changeAnswer = "Q" + dados[2] + ", change, " + answersUser.get(j) + ", to, " + dados[0] + "\n";
                       // Log.d("ChangeAnswer", "Na posição " + j + " setou: " + dados[0]);
                        answersUser.set(j, dados[0]);
                        try {
                            arqAnalisys.write(changeAnswer.getBytes());
                            arqAnalisys.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}

    public void enthropy(String csvFile) {
        //abrindo o csv
       
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        String orderLiness = "";
        String cabecalho="";
        double entropia = 0;
        List<Integer> questionNumber = new ArrayList<>();
        List<Integer> questionOrder = new ArrayList<>();

        try {

            br = new BufferedReader(new FileReader(diretorioInput.getPath()+"/"+csvFile));
            //Pegando o cabeçalho
            cabecalho = br.readLine() + "\n\n";
            br.readLine();

             //Lendo linha por linha
            while ((line = br.readLine()) != null) {
                String[] dados = line.split(csvSplitBy);
                if((dados[0].equals("Next")==false) &&(dados[0].equals("Back")==false)){
                    questionNumber.add(Integer.parseInt(dados[2]));
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
           for (int i=0; i<questionNumber.size(); i++) {
          //     Log.d("Elementos","posição" + i + ": " + questionNumber.get(i));
           }
            int q = questionNumber.size()-1;
            while(questionOrder.size()<=NQuestion && q>=0){
                if (questionOrder.contains(questionNumber.get(q))==false){
                    questionOrder.add(questionNumber.get(q));
        //            Log.d("QUESTION ORDER", "ADD" + questionNumber.get(q));
                }
               q--;
            }



            int p1 = 0;
            int p2 = 0;
            for(int i=questionOrder.size()-1;i>0;i--){
                int qAtual = questionOrder.get(i);

                int qAnt = questionOrder.get(i-1);
                if(qAtual<qAnt){
                    p1++;

                }else{
                    p2++;

                }
            }
            
            float sum = p1+p2;
            double np1 = p1/sum;
            double np2 = p2/sum;

            double logp1 = Math.log(np1);

            double logp2 = Math.log(np2);

            if(p1==0){
                entropia = -(np2*logp2);
            }else if(p2==0){
                entropia = -(np1*logp1);
            }else{
                entropia = -(np1*logp1)-(np2*logp2);
            }

            String insert = "\n\n" + "Indice de entropia: " + entropia;
            try {
                arqAnalisys.write(insert.getBytes());
                arqAnalisys.flush();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void measureDoubt(String csvFile) {
        //abrindo o csv

        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        String orderLiness = "";
        String cabecalho="";
        double duvida = 0;
        float sum=0;
        int[] questions = new int[NQuestion];
        for(int i=0;i<NQuestion;i++){
            questions[i]=0;
        }

        // for (int i=0; i < NQuestion; i++) {
        //      questionOrder.add(0);
        // }

        try {


           br = new BufferedReader(new FileReader(diretorioInput.getPath()+"/"+csvFile));
            //Pegando o cabeçalho
           cabecalho = br.readLine() + "\n\n";
           br.readLine();

            //Lendo linha por linha
            while ((line = br.readLine()) != null) {
                String[] dados = line.split(csvSplitBy);
                if((dados[0].equals("Next")==false) &&(dados[0].equals("Back")==false)){
                    int question = Integer.parseInt(dados[2]);
                    questions[question-1]++;
                }
            }
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for(int i =0;i<NQuestion;i++){
            	dataset.addValue(questions[i],"Changes",String.valueOf(i+1));
            }
            
            PlotOrientation orientation = PlotOrientation.VERTICAL;
			JFreeChart chart = ChartFactory.createBarChart( csvFile+"Histogram", "Questions", "", 
					dataset, orientation, true, true, false);
			chart.getPlot().setBackgroundPaint(Color.WHITE);
			try {
				ChartUtilities.saveChartAsPNG(new File(diretorioOutput,csvFile+"Histogram.PNG"), chart, 500, 300);
			} catch (IOException e) {}
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            for(int i=0;i<NQuestion;i++){
                sum = sum+questions[i];
            }
            double[] questionsN = new double[NQuestion];
            for (int i=0;i<NQuestion;i++){
                questionsN[i]=questions[i]/sum;
            }
            double[]questionsLog = new double[NQuestion];
            for(int i =0;i<NQuestion;i++){
                questionsLog[i]=questionsN[i]*Math.log(questionsN[i]);
            }
           
            for (int i =0;i<NQuestion;i++){
                duvida=duvida-questionsLog[i];
            }
           duvida = 1-(duvida/Math.log(NQuestion));
            String insert = "\n\n" + "Indice de duvida: " + duvida;
            try {
                arqAnalisys.write(insert.getBytes());
                arqAnalisys.flush();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }	
}
