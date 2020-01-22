package ufam.gise.reconhecimento;


import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JLabel;

public class Home extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private File diretorioInput;
	private File diretorioOutput;
	private JLabel lblInput;
	private JLabel lblOutput;
	private JLabel lblGeraoDeAnlises;
	private boolean flagDiretorioInput = false;
	private boolean flagDiretorioOutput = false;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home frame = new Home();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Home() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 733, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnAnlises = new JButton("Gerar Análises");
		btnAnlises.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (flagDiretorioInput == true && flagDiretorioOutput == true) {
					GenerateAnalysis obj = new GenerateAnalysis();
					obj.generateAnalysis(diretorioInput, diretorioOutput);
				} else {
                    JOptionPane.showMessageDialog(null, "Selecione os diretórios de input e output!"); 
				}
			}
		});
		btnAnlises.setBounds(305, 211, 160, 25);
		contentPane.add(btnAnlises);
		
		JButton btnInputDiretorio = new JButton("Input diretorio");
		btnInputDiretorio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
                
                // restringe a amostra a diretorios apenas
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                
                int res = fc.showOpenDialog(null);
                
                if(res == JFileChooser.APPROVE_OPTION){
                    diretorioInput = fc.getSelectedFile();
                    JOptionPane.showMessageDialog(null, "Voce escolheu o diretório: " + diretorioInput.getName());
                    lblInput.setText(diretorioInput.getPath());
                    flagDiretorioInput = true;
                }
                else
                    JOptionPane.showMessageDialog(null, "Voce nao selecionou nenhum diretorio."); 
            }
			
		});
		btnInputDiretorio.setBounds(48, 68, 172, 25);
		contentPane.add(btnInputDiretorio);
		
		JButton btnOutputDiretorio = new JButton("Output diretorio");
		btnOutputDiretorio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
                
                // restringe a amostra a diretorios apenas
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                
                int res = fc.showOpenDialog(null);
                
                if(res == JFileChooser.APPROVE_OPTION){
                    diretorioOutput = fc.getSelectedFile();
                    JOptionPane.showMessageDialog(null, "Voce escolheu o diretório: " + diretorioOutput.getName());
                    flagDiretorioOutput = true;
                    lblOutput.setText(diretorioOutput.getPath());
                }
                else
                    JOptionPane.showMessageDialog(null, "Voce nao selecionou nenhum diretorio."); 
            }
			
		});

		btnOutputDiretorio.setBounds(48, 116, 172, 25);
		contentPane.add(btnOutputDiretorio);
		
		lblInput = new JLabel("Não selecionado");
		lblInput.setBounds(253, 73, 432, 15);
		contentPane.add(lblInput);
		
		lblOutput = new JLabel("Não selecionado");
		lblOutput.setBounds(253, 121, 422, 15);
		contentPane.add(lblOutput);
		
		lblGeraoDeAnlises = new JLabel("Geração de Análises");
		lblGeraoDeAnlises.setBounds(263, 12, 181, 15);
		contentPane.add(lblGeraoDeAnlises);
	}
}
